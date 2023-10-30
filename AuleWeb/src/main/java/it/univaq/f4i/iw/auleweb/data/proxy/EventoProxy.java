package it.univaq.f4i.iw.auleweb.data.proxy;

import it.univaq.f4i.iw.auleweb.data.dao.CorsoDAO;
import it.univaq.f4i.iw.auleweb.data.impl.EventoImpl;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;
import it.univaq.f4i.iw.auleweb.data.model.Utente;
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
    protected int corso_key, professore_key;
    
    public EventoProxy(DataLayer d){
        super();
        this.modified = false;
        this.corso_key = 0;
        this.professore_key = 0;
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
    public void setProfessore(Professore prof) {
        super.setProfessore(prof);
        this.professore_key = prof.getKey();
        this.modified = true;
    }

    @Override
    public void setTipo(String t) {
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
    public Responsabile getUtente() {
       if (super.getUtente() == null && !email_responsabile.isEmpty()) {
            try {
                super.setUtente(((ResponsabileDAO) dataLayer.getDAO(Responsabile.class)).getUtente(email_responsabile));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return super.getUtente();
    }
    */
    
    public void setCorsoKey(int corso_key) {
        this.corso_key = corso_key;
        super.setCorso(null);
    }

    public void setProfessoreKey(int professore_key) {
        this.professore_key = professore_key;
        super.setProfessore(null);
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