package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;
import it.univaq.f4i.iw.auleweb.data.proxy.EventoProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Syd
 */
public class EventoDAO_MySQL extends DAO implements EventoDAO {

    private PreparedStatement sEventoById, sEventoByNome, sEventiByTipo;
    private PreparedStatement sEventoByResponsabile;
    private PreparedStatement iEvento, uEvento, dEvento;

    public EventoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            sEventoById = connection.prepareStatement("SELECT * FROM evento WHERE Id=?");
            sEventoByNome = connection.prepareStatement("SELECT id FROM evento WHERE nome=?");
            sEventiByTipo = connection.prepareStatement("SELECT id FROM evento WHERE tipologia=?");
            sEventoByResponsabile = connection.prepareStatement("SELECT id FROM evento WHERE id_responsabile=?");

            iEvento = connection.prepareStatement("INSERT INTO utente (nome,descrizione,tipologia,id_corso,id_responsabile) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uEvento = connection.prepareStatement("UPDATE evento SET nome=?,descrizione=?,tipologia=?,id_corso=?,id_responsabile=? WHERE ID=?");
            dEvento = connection.prepareStatement("DELETE FROM evento WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing aula web data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //chiusura dei preprared statement
        try {
            sEventoById.close();
            sEventoByNome.close();
            sEventiByTipo.close();
            sEventoByResponsabile.close();

            iEvento.close();
            uEvento.close();
            dEvento.close();
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Evento createEvento() {
        return new EventoProxy(getDataLayer());
    }

    //helper
    private EventoProxy createEvento(ResultSet rs) throws DataException {
        EventoProxy e = (EventoProxy) createEvento();
        try {
            e.setKey(rs.getInt("id"));
            e.setNome(rs.getString("nome"));
            e.setDescrizione(rs.getString("descrizione"));
            e.setTipo((Tipologia) rs.getObject("tipologia"));
            e.setCorsoKey(rs.getInt("id_corso"));
            e.setResponsabileKey(rs.getInt("id_responsabile"));

        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return e;
    }

    @Override
    public Evento getEvento(Integer event_key) throws DataException {
        Evento e = null;
        if (dataLayer.getCache().has(Evento.class, event_key)) {
            e = dataLayer.getCache().get(Evento.class, event_key);
        } else {
            try {
                sEventoById.setInt(1, event_key);
                try (ResultSet rs = sEventoById.executeQuery()) {
                    if (rs.next()) {
                        e = createEvento(rs);

                        dataLayer.getCache().add(Evento.class, e);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load evento by ID", ex);
            }
        }
        return e;
    }

    public Evento getEvento(String nome_evento) throws DataException {
        Evento e = null;
        if (dataLayer.getCache().has(Evento.class, nome_evento)) {
            e = dataLayer.getCache().get(Evento.class, nome_evento);
        } else {
            try {
                sEventoByNome.setString(1, nome_evento);
                try (ResultSet rs = sEventoByNome.executeQuery()) {
                    if (rs.next()) {
                        e = createEvento(rs);

                        dataLayer.getCache().add(Evento.class, e);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load evento by Name", ex);
            }
        }
        return e;
    }
    
    public List<Evento> getAllEventi(String tipo_evento) throws DataException {
        List<Evento> result = new ArrayList();

        try (ResultSet rs = sEventiByTipo.executeQuery(tipo_evento)) {
            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule", ex);
        }
        return result;
    }
    
    @Override
    public Calendario createCalendario() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Evento getEvento(String nome, Tipologia tipologia) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getCalendarioEvento(int evento_key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiCorso(int corso) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiAula(int id_aula, String campo_ricerca) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Calendario getCalendario(int evento_key, Date giorno) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiResponsabile(String email, String param) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteEvento(Calendario calendario, boolean singolo) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void storeEvento(Calendario calendario) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiSettimana(int id_aula, Date giorno) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiPerGiorno(int id_aula, Date giorno) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiCorsoSettimana(int id_corso, Date giorno) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiAttuali(int id_dipartimento) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getAllEventi(int id_dipartimento) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
