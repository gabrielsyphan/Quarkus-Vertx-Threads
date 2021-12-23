package com.syphan.agendador;

import java.time.LocalDateTime;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {

    public static void main(String[] args) {
        Quarkus.run(MyApp.class, args);
    }

    public static class MyApp implements QuarkusApplication {


        @Override
        public int run(String... args) throws Exception {

            try (CloseableHttpClient client =  HttpClients.createDefault()) {
                HttpGet request = new HttpGet("http://localhost:8080/agendamento/iniciarAgendamentos");
                client.execute(request);
            } catch (Exception e) {
                e.printStackTrace();
            }

            Quarkus.waitForExit();
            return 0;
        }
    }
}