/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Eventi;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.auleweb.data.model.Responsabile;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;

/**
 *
 * @author franc
 */
public class EventiImpl implements Eventi {
    
    private Integer id;
    private String nome, descrizione;
    private Tipologia tipo;
    private Responsabile responasabile;
    private Gruppo corso;
    
    public EventiImpl(){
        super();
        this.id = null;
        this.nome = "";
        this.descrizione = "";
        this.tipo = null;
        this.responasabile = null;
        this.corso = null;
    }

    @Override
    public Integer getId() {
        return id;
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
    public Responsabile getResponsabile() {
        return responasabile;
    }

    @Override
    public Gruppo getCorso() {
        return corso;
    }

    @Override
    public void setId(int id) {
        this.id = id;
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
    public void setResponsabile(Responsabile r) {
        this.responasabile  = r;
    }

    @Override
    public void setGruppo(Gruppo g) {
        this.corso = g;
    }
    
}
