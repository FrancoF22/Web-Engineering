/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.auleweb.data.model.TipoGruppo;

/**
 *
 * @author franc
 */
public class GruppoImpl implements Gruppo {
    private String nome, descrizione;
    private TipoGruppo tipoGruppo;
    
    public GruppoImpl(){
        super();
        this.nome = "";
        this.descrizione = "";
        this.tipoGruppo = null;
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
    public TipoGruppo getTipoGruppo() {
        return tipoGruppo;
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
    public void setTipoGruppo(TipoGruppo tg) {
        this.tipoGruppo = tg;
    }
    
}
