package it.univaq.f4i.iw.auleweb.data.proxy;

import it.univaq.f4i.iw.auleweb.data.dao.AulaDAO;
import it.univaq.f4i.iw.auleweb.data.dao.EventoDAO;
import it.univaq.f4i.iw.auleweb.data.impl.CalendarioImpl;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.util.Date;
import java.time.LocalTime;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author franc
 */
public class CalendarioProxy extends CalendarioImpl implements DataItemProxy{
    
    protected boolean modified;
    protected DataLayer dataLayer;
    protected int aula_key, evento_key;
    
    public CalendarioProxy(DataLayer d){
        super();
        this.dataLayer = d;
        this.modified = false;
        this.aula_key = 0;
        this.evento_key = 0;
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
    public Evento getEvento() {
        if (super.getEvento() == null && evento_key > 0) {
            try {
                super.setEvento(((EventoDAO) dataLayer.getDAO(Evento.class)).getEvento(evento_key));
            } catch (DataException ex) {
                Logger.getLogger(AulaProxy.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       return super.getEvento();
    }
    
    @Override
    public void setAula(Aula aula) {
        super.setAula(aula);
        this.aula_key = aula.getKey();
        this.modified = true;
    }

    @Override
    public void setEvento(Evento evento) {
        super.setEvento(evento);
        this.evento_key = evento.getKey();
        this.modified = true;
    }

    @Override
    public void setGiorno(Date giorno) {
        super.setGiorno(giorno);
        this.modified = true;
    }

    @Override
    public void setOraInizio(LocalTime oraInizio) {
        super.setOraInizio(oraInizio);
        this.modified = true;
    }

    @Override
    public void setOraFine(LocalTime oraFine) {
       super.setOraFine(oraFine);
        this.modified = true;
    }

    public void setAulaKey(int aula_key) {
        this.aula_key = aula_key;
        super.setAula(null);
    }

    public void setEventoKey(int evento_key) {
        this.evento_key = evento_key;
        super.setEvento(null);
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
