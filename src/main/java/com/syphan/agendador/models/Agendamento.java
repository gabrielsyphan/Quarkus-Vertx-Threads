package com.syphan.agendador.models;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity()
public class Agendamento extends PanacheEntity {

    private String destino;

    private int periodo;
    
    private Timestamp termino;

    public Agendamento() {
        super();
    }

    public Agendamento(Long id, String destino, int periodo, String termino) {
        super();
        this.id = id;
        this.destino = destino;
        this.periodo = periodo;
        this.termino = Timestamp.valueOf(termino);
    }

    public Long getId() {
        return this.id;
    }
  
    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getDestino() {
        return destino;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public LocalDateTime getTermino() {
        return termino.toLocalDateTime();
    }

    public void setTermino(LocalDateTime termino) {
        this.termino = Timestamp.valueOf(termino);
    }

}
