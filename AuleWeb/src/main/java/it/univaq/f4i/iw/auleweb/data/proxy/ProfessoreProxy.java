/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.proxy;

import it.univaq.f4i.iw.auleweb.data.impl.ProfessoreImpl;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author franc
 */
public class ProfessoreProxy extends ProfessoreImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;

    public ProfessoreProxy(DataLayer d) {
        super();
        this.dataLayer = d;
        this.modified = false;
    }
    
    @Override
    public void setNome(String n) {
        super.setNome(n);
        this.modified = true;
    }

    @Override
    public void setCognome(String c) {
        super.setCognome(c);
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
