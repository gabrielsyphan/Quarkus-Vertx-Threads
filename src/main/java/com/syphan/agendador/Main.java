package com.syphan.agendador;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.syphan.agendador.models.Agendamento;
import com.syphan.agendador.repositories.AgendamentoRepository;
import com.syphan.agendador.services.verticles.DisparoVerticle;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.smallrye.mutiny.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

@QuarkusMain
public class Main {

    public static void main(String[] args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {

        @Inject
        AgendamentoRepository agendamentoRepository;

        //private final List<String> verticlesMap = new ArrayList<>();

        @Override
        public int run(String... args) throws Exception {

            DeploymentOptions options = new DeploymentOptions();
            options.setWorker(true);
            Vertx vertx = Vertx.vertx();

            List<Agendamento> agendamentos = this.listarTodos();

            for (Agendamento agendamento : agendamentos) {
                DisparoVerticle disparoVerticle = new DisparoVerticle(agendamento);
                vertx.deployVerticle(disparoVerticle, options);
                //vertx.deployVerticle(disparoVerticle, options, deploymentId -> deployVerticleHandler(deploymentId));
            }

            vertx.eventBus().<String>consumer("DESTRUIR VERTICLE", m -> {
                vertx.undeploy(m.body());
                System.out.println("Verticle " + m.body() + " removido");
            });

            Quarkus.waitForExit();
            return 0;
        }

        public List<Agendamento> listarTodos() {
            List<Agendamento> agendamentos = new ArrayList<>();
            agendamentos.add(new Agendamento(1L, "https://maceio.orditi.com/source/services/testGov.php", 1, "2021-12-23 05:17:00"));
            agendamentos.add(new Agendamento(2L, "https://maceio.orditi.com/source/services/testGov.php", 1, "2021-12-22 14:28:00"));
            agendamentos.add(new Agendamento(3L, "https://maceio.orditi.com/source/services/testGov.php", 2, "2021-12-23 05:17:00"));
            agendamentos.add(new Agendamento(4L, "https://maceio.orditi.com/source/services/testGov.php", 2, "2021-12-23 05:17:00"));

            return agendamentos;
        }

        // private void deployVerticleHandler(AsyncResult<String> deploymentId) {
        //     if (deploymentId.succeeded()) {
        //         this.verticlesMap.add(deploymentId.result());
        //     }
        // }

        // private void stopVerticle(String verticleId) {
        //     vertx.undeploy(verticleId);
        // }
    }
}