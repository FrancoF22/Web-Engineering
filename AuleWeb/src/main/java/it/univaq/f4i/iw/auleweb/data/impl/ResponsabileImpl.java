/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Responsabile;
import it.univaq.f4i.iw.auleweb.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author franc
 */
public class ResponsabileImpl extends DataItemImpl<Integer> implements Responsabile {

    private String nome, cognome, email;
    private Utente utente;

    public ResponsabileImpl() {
        super();
        this.nome = "";
        this.cognome = "";
        this.email = "";
        this.utente = null;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getCognome() {
        return cognome;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public Utente getUtente() {
        return utente;
    }

    @Override
    public void setNome(String n) {
        this.nome = n;
    }

    @Override
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setUtente(Utente utente) {
        this.utente = utente;
    }

}
