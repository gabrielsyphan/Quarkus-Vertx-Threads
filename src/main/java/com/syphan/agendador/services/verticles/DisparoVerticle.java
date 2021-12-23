package com.syphan.agendador.services.verticles;

import java.time.LocalDateTime;

import javax.inject.Inject;

import com.syphan.agendador.models.Agendamento;
import com.syphan.agendador.models.Disparo;

import io.smallrye.mutiny.vertx.core.AbstractVerticle;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class DisparoVerticle extends AbstractVerticle {

    @Inject
    private Agendamento agendamento;

    public DisparoVerticle(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    @Override
    public void start() throws Exception {
        super.start();
        vertx.setTimer(this.agendamento.getPeriodo() * 1000, this::disparoHttp);
    }
    
    private void disparoHttp(long timerId) {

        System.out.println(this.agendamento.getId() + " - Realizou o disparo: "+ LocalDateTime.now());

        if (LocalDateTime.now().isBefore(this.agendamento.getTermino())) {
            try (CloseableHttpClient client =  HttpClients.createDefault()) {
                HttpPost request = new HttpPost(this.agendamento.getDestino());
                CloseableHttpResponse httpResponse = client.execute(request);
                HttpEntity httpEntity = httpResponse.getEntity();

                Disparo disparo = new Disparo(
                    this.agendamento.getId(), 
                    httpEntity == null ? null : EntityUtils.toString(httpResponse.getEntity()),
                    String.valueOf(httpResponse.getStatusLine().getStatusCode())
                );

                request = new HttpPost("http://localhost:8080/agendamento/disparo");
                request.setHeader("Content-Type", "application/json");
                StringEntity params = new StringEntity(disparo.toString());
                request.setEntity(params);
                client.execute(request);

                System.out.println(this.agendamento.getId() + " - Recebeu um retorno do servidor: "+ LocalDateTime.now());

                if (LocalDateTime.now().isBefore(this.agendamento.getTermino())) {
                    vertx.setTimer(this.agendamento.getPeriodo() * 60000, this::disparoHttp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(this.agendamento.getId() + " - Chamou a finalização");
            vertx.eventBus().publish("DESTRUIR VERTICLE", this.deploymentID());
        }
    }
}
