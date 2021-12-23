package com.syphan.agendador.repositories;

import javax.enterprise.context.ApplicationScoped;

import com.syphan.agendador.models.Disparo;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class DisparoRepository implements PanacheRepository<Disparo> {
    
}
