package com.syphan.agendador.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.syphan.agendador.models.Agendamento;
import com.syphan.agendador.models.Disparo;
import com.syphan.agendador.repositories.AgendamentoRepository;
import com.syphan.agendador.repositories.DisparoRepository;
import com.syphan.agendador.services.verticles.DisparoVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

@Path("/agendamento")
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class AgendamentoService {

    @Inject
    AgendamentoRepository agendamentoRepository;

    @Inject
    Vertx vertx;

    @Inject
    DisparoRepository disparoRepository;

    @GET
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.listAll();
    }

    @POST
    @Transactional
    public String salvar(Agendamento agendamento) {
        agendamentoRepository.persist(agendamento);
        if (agendamento.isPersistent()) {
            if (LocalDateTime.now().isBefore(agendamento.getTermino())) {
                DeploymentOptions options = new DeploymentOptions();
                options.setWorker(true);
                this.vertx.deployVerticle(new DisparoVerticle(agendamento), options);
            }

            return "Salvou";
        }

        return "Não salvou";
    }

    @GET
    @Path("/iniciarAgendamentos")
    public void iniciarAgendamentos() {

        DeploymentOptions options = new DeploymentOptions();
        options.setWorker(true);
        List<Agendamento> agendamentos = this.agendamentoRepository.listAll();

        for (Agendamento agendamento : agendamentos) {
            if (LocalDateTime.now().isBefore(agendamento.getTermino())) {
                this.vertx.deployVerticle(new DisparoVerticle(agendamento), options);
            }
        }

        this.vertx.eventBus().<String>consumer("DESTRUIR VERTICLE", m -> {
            vertx.undeploy(m.body());
            System.out.println("Verticle " + m.body() + " removido");
        });
    }

    @POST
    @Path("/disparo")
    @Transactional
    public void salvarDisparo(Disparo disparo) {
        this.disparoRepository.persist(disparo);
    }
}
