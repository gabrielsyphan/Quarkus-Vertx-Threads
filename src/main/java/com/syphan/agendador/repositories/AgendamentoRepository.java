package com.syphan.agendador.repositories;

import javax.enterprise.context.ApplicationScoped;

import com.syphan.agendador.models.Agendamento;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class AgendamentoRepository implements PanacheRepository<Agendamento> {
        
}
