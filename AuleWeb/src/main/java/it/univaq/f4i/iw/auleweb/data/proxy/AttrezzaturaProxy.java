/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.proxy;

import it.univaq.f4i.iw.auleweb.data.impl.AttrezzaturaImpl;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author franc
 */
public class AttrezzaturaProxy extends AttrezzaturaImpl {

    protected boolean modified;
    protected DataLayer dataLayer;

    public AttrezzaturaProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;
    }
    
    @Override
    public void setNome(String nome) {
        super.setNome(nome); //nome attrezzatura
        this.modified = true;
    }

    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

    public boolean isModified() {
        return modified;
    }
}
