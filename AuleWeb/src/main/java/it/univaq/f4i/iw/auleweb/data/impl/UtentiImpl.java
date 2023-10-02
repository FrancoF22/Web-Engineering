/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Utenti;

/**
 *
 * @author franc
 */
public class UtentiImpl implements Utenti {
    
    private Integer id;
    private String nome, cognome, email, password;
    
    public UtentiImpl(){
        this.id = null;
        this.nome = "";
        this.cognome = "";
        this.email = "";
        this.password = "";
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
    public String getCognome() {
        return cognome;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
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
    public void setCognome(String c) {
        this.cognome = c;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }
    
}
