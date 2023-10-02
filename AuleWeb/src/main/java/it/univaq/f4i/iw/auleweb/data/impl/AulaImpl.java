/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Attrezzatura;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Responsabile;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author franc
 */
public class AulaImpl implements Aula{
    
    private Integer Id, capienza, preseElettriche, preseRete;
    private String nome, luogo, nota, edificio, piano;
    private Set<Attrezzatura> attrezzatura;
    private Responsabile responsabile;
    
    public AulaImpl(){
        super();
        this.Id = null;
        this.capienza = 0;
        this.preseElettriche = 0;
        this.preseRete = 0;
        this.nome = "";
        this.nota = "";
        this.edificio = "";
        this.piano = "";
        this.attrezzatura = new HashSet<>();
        this.responsabile = null;
    }

    @Override
    public Integer getId() {
        return Id;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public Integer getCapienza() {
        return capienza;
    }

    @Override
    public Integer getPreseElettriche() {
        return preseElettriche;
    }

    @Override
    public Integer getPreseRete() {
        return preseRete;
    }

    @Override
    public List<Attrezzatura> getAttrezzature() {
        
    List<Attrezzatura> ListaAttrezzature = new ArrayList<>();
        
        for(Attrezzatura a: attrezzatura){
            if(a!=null) ListaAttrezzature.add(a);
        }
        
        return ListaAttrezzature;
    }

    @Override
    public String getNota() {
        return nota;
    }

    @Override
    public String getLuogo() {
        return luogo;
    }

    @Override
    public String getEdificio() {
        return edificio;
    }

    @Override
    public String getPiano() {
        return piano;
    }

    @Override
    public Responsabile getIdResponsabile() {
        return responsabile;
    }

    @Override
    public void setId(int id) {
        this.Id = id;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setCapienza(int capienza) {
        this.capienza = capienza;
    }

    @Override
    public void setPreseElettriche(int preseE) {
        this.preseElettriche = preseE;
    }

    @Override
    public void setPreseRete(int preseR) {
        this.preseRete = preseR;
    }

    @Override
    public void setAttrezzature(Set<Attrezzatura> attrezzature) {
        this.attrezzatura = attrezzature;
    }

    @Override
    public void setNota(String nota) {
        this.nota = nota;
    }

    @Override
    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    @Override
    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    @Override
    public void setPiano(String piano) {
        this.piano = piano;
    }

    @Override
    public void setIdResponsabile(Responsabile resp) {
        this.responsabile = resp;
    }
    
}
