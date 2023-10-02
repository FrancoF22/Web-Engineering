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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setNome(String nome) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setCapienza(int capienza) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setPreseElettriche(int preseE) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setPreseRete(int preseR) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setAttrezzature(Set<Attrezzatura> attrezzature) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setNota(String nome) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setLuogo(String luogo) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setEdificio(String edificio) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setPiano(String piano) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void setIdResponsabile(Responsabile resp) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
