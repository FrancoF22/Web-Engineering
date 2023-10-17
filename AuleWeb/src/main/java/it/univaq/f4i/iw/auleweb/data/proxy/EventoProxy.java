package it.univaq.f4i.iw.auleweb.data.proxy;

import it.univaq.f4i.iw.auleweb.data.dao.CorsoDAO;
import it.univaq.f4i.iw.auleweb.data.impl.EventoImpl;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.auleweb.data.model.Responsabile;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author franc
 */
public class EventoProxy extends EventoImpl implements DataItemProxy{

    protected boolean modified;
    protected DataLayer dataLayer;
    protected int corso_key, responsabile_key;
    
    public EventoProxy(DataLayer d){
        super();
        this.modified = false;
        this.corso_key = 0;
        this.responsabile_key = 0;
    }
    
    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }
    
    @Override
    public void setCorso(Corso corso) {
        super.setCorso(corso);
        if(corso != null)this.corso_key = corso.getKey();
        this.modified = true;
    }

    @Override
    public void setNome(String nome) {
        super.setNome(nome);
        this.modified = true;
    }

    @Override
    public void setDescrizione(String descrizione) {
        super.setDescrizione(descrizione);
        this.modified = true;
    }

   @Override
    public void setResponsabile(Responsabile resp) {
        super.setResponsabile(resp);
        this.responsabile_key = resp.getKey();
        this.modified = true;
    }

    @Override
    public void setTipo(Tipologia t) {
        super.setTipo(t);
        this.modified = true;
    }

    @Override
    public Corso getCorso() {
        if (super.getCorso() == null && corso_key > 0) {
            try {
                super.setCorso(((CorsoDAO) dataLayer.getDAO(Gruppo.class)).getCorsoById(corso_key));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return super.getCorso();
    }
    /*
    @Override
    public Responsabile getResponsabile() {
       if (super.getResponsabile() == null && !email_responsabile.isEmpty()) {
            try {
                super.setResponsabile(((ResponsabileDAO) dataLayer.getDAO(Responsabile.class)).getResponsabile(email_responsabile));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return super.getResponsabile();
    }
    */
    
    public void setCorsoKey(int corso_key) {
        this.corso_key = corso_key;
        super.setCorso(null);
    }

    public void setResponsabileKey(int responsabile_key) {
        this.responsabile_key = responsabile_key;
        super.setResponsabile(null);
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