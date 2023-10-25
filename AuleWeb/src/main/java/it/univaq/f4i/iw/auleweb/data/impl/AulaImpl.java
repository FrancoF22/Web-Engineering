/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.framework.data.DataItemImpl;
import java.util.*;

/**
 *
 * @author franc
 */
public class AulaImpl extends DataItemImpl<Integer> implements Aula{
    
    private Integer capienza, preseElettriche, preseRete;
    private String nome, luogo, nota, edificio, piano;
    private ArrayList<String> attrezzatura;
    private Professore professore;
    
    public AulaImpl(){
        super();
        this.capienza = 0;
        this.preseElettriche = 0;
        this.preseRete = 0;
        this.nome = "";
        this.nota = "";
        this.edificio = "";
        this.piano = "";
        this.attrezzatura = new ArrayList<>();
        this.professore = null;
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
    public List<String> getAttrezzature() {
        
        List<String> la = new ArrayList<>();
        
        for(String s: attrezzatura){
            if(s!=null) la.add(s);
        }
        
        return la;
        
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
    public Professore getProfessore() {
        return professore;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public void setCapienza(Integer capienza) {
        this.capienza = capienza;
    }

    @Override
    public void setPreseElettriche(Integer preseE) {
        this.preseElettriche = preseE;
    }

    @Override
    public void setPreseRete(Integer preseR) {
        this.preseRete = preseR;
    }

    @Override
    public void setAttrezzature(ArrayList<String> a) {
        this.attrezzatura = a;
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
    public void setProfessore(Professore resp) {
        this.professore = resp;
    }

}
