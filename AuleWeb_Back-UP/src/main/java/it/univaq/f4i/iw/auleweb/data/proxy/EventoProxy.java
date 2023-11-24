package it.univaq.f4i.iw.auleweb.data.proxy;

import it.univaq.f4i.iw.auleweb.data.dao.CorsoDAO;
import it.univaq.f4i.iw.auleweb.data.dao.ProfessoreDAO;
import it.univaq.f4i.iw.auleweb.data.impl.EventoImpl;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
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
    protected int corso_key = 0;
    protected int professore_key = 0;
    
    public EventoProxy(DataLayer d){
        super();
        this.modified = false;
        this.corso_key = 0;
        this.professore_key = 0;
        this.dataLayer = d;
    }

    @Override
    public void setKey(Integer key) {
        super.setKey(key);
        this.modified = true;
    }

    @Override
    public Professore getProfessore() {
        if (super.getProfessore() == null && professore_key > 0) {
            try {
                super.setProfessore(((ProfessoreDAO) dataLayer.getDAO(Professore.class)).getProfessoreById(professore_key));
            } catch (DataException ex) {
                Logger.getLogger(ProfessoreProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        return super.getProfessore();
    }
    
    @Override
    public Corso getCorso() {
        if (super.getCorso()== null && corso_key > 0) {
            try {
                super.setCorso(((CorsoDAO) dataLayer.getDAO(Corso.class)).getCorsoById(corso_key));
            } catch (DataException ex) {
                Logger.getLogger(CorsoProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        return super.getCorso();
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
    public void setTipo(Tipologia t) {
        super.setTipo(t);
        this.modified = true;
    }
    
    @Override
    public void setProfessore(Professore p) {
        super.setProfessore(p);
        this.professore_key = p.getKey();
        this.modified = true;
    }
    
    @Override
    public void setCorso(Corso c) {
        super.setCorso(c);
        this.professore_key = c.getKey();
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
        super.setProfessore(null);
    }
    
    public void setCorsoKey(int corso_key) {
        this.corso_key = corso_key;
        super.setCorso(null);
    }
}