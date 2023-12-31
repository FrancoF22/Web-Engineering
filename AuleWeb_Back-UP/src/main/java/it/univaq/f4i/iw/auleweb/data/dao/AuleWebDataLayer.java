/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.auleweb.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.SQLException;
import javax.sql.DataSource;

/**
 *
 * @author franc
 */
public class AuleWebDataLayer extends DataLayer {

    public AuleWebDataLayer(DataSource datasource) throws SQLException {
        super(datasource);
    }

    @Override
    public void init() throws DataException {
        registerDAO(Utente.class, new UtenteDAO_MySQL(this));
        registerDAO(Professore.class, new ProfessoreDAO_MySQL(this));
        registerDAO(Corso.class, new CorsoDAO_MySQL(this));
        registerDAO(Gruppo.class, new GruppoDAO_MySQL(this));
        registerDAO(Evento.class, new EventoDAO_MySQL(this));
        registerDAO(Aula.class, new AulaDAO_MySQL(this));
        registerDAO(Calendario.class, new CalendarioDAO_MySQL(this));
    }

    //helpers
    public EventoDAO getEventoDAO() {
        return (EventoDAO) getDAO(Evento.class);
    }

    public AulaDAO getAulaDAO() {
        return (AulaDAO) getDAO(Aula.class);
    }

    public GruppoDAO getGruppoDAO() {
        return (GruppoDAO) getDAO(Gruppo.class);
    }

    public UtenteDAO getUtenteDAO() {
        return (UtenteDAO) getDAO(Utente.class);
    }

    public CorsoDAO getCorsoDAO() {
        return (CorsoDAO) getDAO(Corso.class);
    }
    
    public ProfessoreDAO getProfessoreDAO() {
        return (ProfessoreDAO) getDAO(Professore.class);
    }
    
    public CalendarioDAO getCalendarioDAO() {
        return (CalendarioDAO) getDAO(Calendario.class);
    }
    
}
