/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.proxy;

import it.univaq.f4i.iw.auleweb.data.impl.UtenteImpl;
import it.univaq.f4i.iw.auleweb.data.model.Ruolo;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author franc
 */
public class UtentiProxy extends UtenteImpl implements DataItemProxy{

    protected boolean modified;
    protected DataLayer dataLayer;

    public UtentiProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }

    @Override
    public void setNome(String name) {
        super.setNome(name);
        this.modified = true;
    }

    @Override
    public void setPassword(String surname) {
        super.setPassword(surname);
        this.modified = true;
    }

    @Override
    public void setRuolo(Ruolo r) {
        super.setRuolo(r);
        this.modified = true;
    }
    
    @Override
    public boolean isModified() {
        return modified;
    }

    @Override
    public void setModified(boolean dirty) {
        this.modified = dirty;
    }

}
