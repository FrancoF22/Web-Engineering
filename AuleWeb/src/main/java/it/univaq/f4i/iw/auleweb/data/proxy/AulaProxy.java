package it.univaq.f4i.iw.auleweb.data.proxy;

import it.univaq.f4i.iw.auleweb.data.impl.AulaImpl;
import it.univaq.f4i.iw.auleweb.data.model.Attrezzatura;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.util.*;

/**
 *
 * @author franc
 */
public class AulaProxy extends AulaImpl implements DataItemProxy {

    protected boolean modified;
    protected int professore_key;
    protected DataLayer dataLayer;

    public AulaProxy(DataLayer d) {
        this.modified = false;
        this.professore_key = 0;
        this.dataLayer = d;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    /*
    @Override
    public Responsabile getResponsabile() {
       if (super.getResponsabile() == null && !email.isEmpty()) {
            try {
                super.setResponsabile(((ResponsabileDAO) dataLayer.getDAO(Responsabile.class)).getResponsabile(email));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return super.getResponsabile();
    }
     */
    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setLuogo(String luogo) {
        super.setLuogo(luogo);
        this.modified = true;
    }

    @Override
    public void setEdificio(String Edificio) {
        super.setEdificio(Edificio);
        this.modified = true;
    }

    @Override
    public void setPiano(String piano) {
        super.setPiano(piano);
        this.modified = true;
    }

    @Override
    public void setCapienza(Integer capienza) {
        super.setCapienza(capienza);
        this.modified = true;
    }

    @Override
    public void setProfessore(Professore resp) {
        super.setProfessore(resp);
        this.professore_key = resp.getKey();
        this.modified = true;
    }

    @Override
    public void setPreseElettriche(Integer preseE) {
        super.setPreseElettriche(preseE);
        this.modified = true;
    }

    @Override
    public void setPreseRete(Integer preseR) {
        super.setPreseRete(preseR);
        this.modified = true;
    }

    @Override
    public void setAttrezzature(Set<Attrezzatura> attrezzatura) {
        super.setAttrezzature(attrezzatura);
        this.modified = true;
    }

    @Override
    public void setNota(String Note) {
        super.setNota(Note);
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

    public void setProfessoreKey(int prof_key) {
        this.professore_key = prof_key;
        //resettiamo la cache 
        super.setProfessore(null);
    }

    public void setProfKey(int aInt) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
