/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import java.sql.Time;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author franc
 */
public class CalendarioImpl extends DataItemImpl<Integer> implements Calendario {
    
    private Aula aula;
    private Evento eventi;
    private Date giorno;
    private Time oraInizio, oraFine;

    public CalendarioImpl() {
       super();
       this.aula = null;
       this.eventi = null;
       this.giorno = Date.valueOf(LocalDate.now());
       this.oraInizio = Time.valueOf(LocalTime.now());
       this.oraFine = Time.valueOf(LocalTime.now());
    }

    @Override
    public Aula getAula() {
        return aula;
    }

    @Override
    public Evento getEvento() {
        return eventi;
    }

    @Override
    public Date getGiorno() {
        return giorno;
    }

    @Override
    public Time getOraInizio() {
        return oraInizio;
    }

    @Override
    public Time getOraFine() {
        return oraFine;
    }

    @Override
    public void setAula(Aula aula) {
        this.aula = aula;
    }

    @Override
    public void setEvento(Evento eventi) {
        this.eventi = eventi;
    }

    @Override
    public void setGiorno(Date giorno) {
        this.giorno = giorno;
    }

    @Override
    public void setOraInizio(Time oraInizio) {
        this.oraInizio = oraInizio;
    }

    @Override
    public void setOraFine(Time oraFine) {
        this.oraFine = oraFine;
    }
}
