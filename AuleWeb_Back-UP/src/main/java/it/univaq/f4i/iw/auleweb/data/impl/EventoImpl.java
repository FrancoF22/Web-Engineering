/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author franc
 */
public class EventoImpl extends DataItemImpl<Integer> implements Evento {
    
    private String nome, descrizione;
    private Tipologia tipo;
    private Professore professore;
    private Corso corso;
    private Calendario calendario;
    
    public EventoImpl(){
        super();
        this.nome = "";
        this.descrizione = "";
        this.tipo = null;
        this.professore = null;
        this.corso = null;
        this.calendario = null;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getDescrizione() {
        return descrizione;
    }

    @Override
    public Tipologia getTipo() {
        return tipo;
    }

    @Override
    public Professore getProfessore() {
        return professore;
    }

    @Override
    public Tipologia getTipologia() {
        return tipo;
    }
    
    @Override
    public Corso getCorso() {
        return corso;
    }

    @Override
    public Calendario getCalendario() {
        return calendario;
    }
    
    @Override
    public void setNome(String n) {
        this.nome = n;
    }

    @Override
    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public void setTipo(Tipologia t) {
        this.tipo = t;
    }

    @Override
    public void setProfessore(Professore r) {
        this.professore  = r;
    }
    
    @Override
    public void setCorso(Corso c) {
        this.corso = c;
    }
    
    @Override
    public void setCalendario(Calendario calendario) {
        this.calendario = calendario;
    }
    
}
