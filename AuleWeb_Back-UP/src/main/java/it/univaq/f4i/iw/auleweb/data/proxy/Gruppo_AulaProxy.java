package it.univaq.f4i.iw.auleweb.data.proxy;

import it.univaq.f4i.iw.auleweb.data.dao.AulaDAO;
import it.univaq.f4i.iw.auleweb.data.dao.GruppoDAO;
import it.univaq.f4i.iw.auleweb.data.impl.Gruppo_AulaImpl;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author franc
 */
public class Gruppo_AulaProxy extends Gruppo_AulaImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected int gruppo_key, aula_key;

    public Gruppo_AulaProxy(DataLayer d) {
        super();
        //dependency injection
        this.dataLayer = d;
        this.modified = false;
        this.gruppo_key = 0;
        this.aula_key = 0;
    }

    @Override
    public Gruppo getGruppo() {
        if (super.getGruppo() == null && gruppo_key > 0) {
            try {
                super.setGruppo(((GruppoDAO) dataLayer.getDAO(Gruppo.class)).getGruppoById(gruppo_key));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return super.getGruppo();
    }

    @Override
    public Aula getAula() {
        if (super.getAula() == null && aula_key > 0) {
            try {
                super.setAula(((AulaDAO) dataLayer.getDAO(Aula.class)).getAula(aula_key));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return super.getAula();
    }
    
    @Override
    public void setGruppo(Gruppo gruppo) {
        super.setGruppo(gruppo);
        this.gruppo_key = gruppo.getKey();
        this.modified = true;
    }

    @Override
    public void setAula(Aula aula) {
        super.setAula(aula);
        this.aula_key = aula.getKey();
        this.modified = true;
    }

    public void setAulaKey(int aula_key) {
        this.aula_key = aula_key;
        super.setAula(null);
    }
    
    public void setGruppoKey(int gruppo_key) {
        this.gruppo_key = gruppo_key;
        super.setGruppo(null);
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
