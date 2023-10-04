/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo_Aula;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author franc
 */
public class Gruppo_AulaImpl extends DataItemImpl<Integer> implements Gruppo_Aula{
    
    private Gruppo gruppo;
    private Aula aula;
    
    public Gruppo_AulaImpl(){
        super();
        this.gruppo = null;
        this.aula = null;
    }

    @Override
    public Gruppo getGruppo() {
        return gruppo;
    }

    @Override
    public Aula getAula() {
        return aula;
    }

    @Override
    public void setGruppo(Gruppo g) {
        this.gruppo = g;
    }

    @Override
    public void setAula(Aula a) {
        this.aula = a;
    }
    
}
