package com.syphan.agendador.models;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Disparo extends PanacheEntity {

    private Long id_agendamento;

    private String retorno;

    private String status;

    public Disparo() {
    }

    public Disparo(Long id_agendamento, String retorno, String status) {
        this.id_agendamento = id_agendamento;
        this.retorno = retorno;
        this.status = status;
    }

    public Long getId_agendamento() {
        return id_agendamento;
    }

    public void setId_agendamento(Long id_agendamento) {
        this.id_agendamento = id_agendamento;
    }

    public String getRetorno() {
        return retorno;
    }

    public void setRetorno(String retorno) {
        this.retorno = retorno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
}
