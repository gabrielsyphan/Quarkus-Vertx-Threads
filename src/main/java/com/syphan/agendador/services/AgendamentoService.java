package com.syphan.agendador.services;

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
import com.syphan.agendador.repositories.AgendamentoRepository;
import com.syphan.agendador.services.verticles.DisparoVerticle;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

@Path("/agendamento")
@Transactional
@Produces(value = MediaType.APPLICATION_JSON)
@Consumes(value = MediaType.APPLICATION_JSON)
public class AgendamentoService {

    @Inject
    AgendamentoRepository agendamentoRepository;

    @GET
    public List<Agendamento> listarTodos() {
        return agendamentoRepository.listAll();
    }

    @POST
    public String salvar(Agendamento agendamento) {
        agendamentoRepository.persist(agendamento);

        if (agendamento.isPersistent()) {

            DeploymentOptions options = new DeploymentOptions();
            options.setWorker(true);
            Vertx vertx = Vertx.vertx();
            vertx.deployVerticle(new DisparoVerticle(agendamento), options);

            return "Salvou";
        }

        return "Não salvou";
    }

}
