/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Ricorrenza;
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
    private Ricorrenza ricorrenza;
    private Date giorno, giornoFine;
    private Time oraInizio, oraFine;
    private int ripetizioni;

    public CalendarioImpl() {
       super();
       this.aula = null;
       this.eventi = null;
       this.giorno = Date.valueOf(LocalDate.now());
       this.giornoFine = Date.valueOf(LocalDate.now());
       this.oraInizio = Time.valueOf(LocalTime.now());
       this.oraFine = Time.valueOf(LocalTime.now());
       this.ricorrenza = Ricorrenza.giornaliera;
       this.ripetizioni=1;
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
    public Ricorrenza getRicorrenza() {
        return ricorrenza;
    }

    @Override
    public Date getGiorno() {
        return giorno;
    }

    @Override
    public Date getGiornoFine() {
        return giornoFine;
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
    public int getRipetizioni() {
        return ripetizioni;
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
    public void setRicorrenza(Ricorrenza ricorrenza) {
        this.ricorrenza = ricorrenza;
    }

    @Override
    public void setGiorno(Date giorno) {
        this.giorno = giorno;
    }

    @Override
    public void setGiornoFine(Date giorno) {
        this.giornoFine = giorno;
    }

    @Override
    public void setOraInizio(Time oraInizio) {
        this.oraInizio = oraInizio;
    }

    @Override
    public void setOraFine(Time oraFine) {
        this.oraFine = oraFine;
    }

    @Override
    public void setRipetizioni(int ripetizioni) {
        this.ripetizioni = ripetizioni;
    }
}
