/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author Syd
 */
public class CorsoImpl extends DataItemImpl<Integer> implements Corso {
    private String nome, descrizione;
    private Gruppo dipartimento;

    public CorsoImpl(){
        super();
        this.nome = "";
        this.descrizione = "";
        this.dipartimento = null;
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
    public Gruppo getGruppo() {
        return dipartimento;
    }
    
    @Override
    public void setNome(String n) {
        this.nome = n;
    }

    @Override
    public void setDescrizione(String d) {
        this.descrizione = d;
    }
    
    @Override
    public void setGruppo(Gruppo g) {
        this.dipartimento = g;
    }
    
}
