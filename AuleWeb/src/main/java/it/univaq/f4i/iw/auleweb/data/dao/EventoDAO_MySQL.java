package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;
import it.univaq.f4i.iw.auleweb.data.proxy.CalendarioProxy;
import it.univaq.f4i.iw.auleweb.data.proxy.EventoProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;
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
    private PreparedStatement sGiorniEvento, sDataOdierna;

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
            
            sGiorniEvento = connection.prepareStatement("SELECT calendario.* FROM calendario JOIN evento ON calendario.id_evento = evento.id WHERE calendario.id_evento=?");
            sDataOdierna = connection.prepareStatement("SELECT evento.* FROM evento JOIN calendario ON calendario.id_evento = evento.id WHERE calendario.giorno = NOW");
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
            
            sGiorniEvento.close();
            sDataOdierna.close();
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
            e.setTipo(Tipologia.valueOf( rs.getString("tipologia")));
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
    
    public void storeEvento(Evento evento) throws DataException {
         try {
            if (evento.getKey() != null && evento.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                if (evento instanceof DataItemProxy && !((DataItemProxy) evento).isModified()) {
                    return;
                }
                uEvento.setString(1, evento.getNome());
                uEvento.setString(2, evento.getDescrizione());
                uEvento.setObject(3, evento.getTipologia());
                if (evento.getResponsabile()!= null) {
                    uEvento.setInt(4, evento.getResponsabile().getKey());
                } else {
                    uEvento.setNull(4, java.sql.Types.INTEGER);
                }

                if (uEvento.executeUpdate() == 0) {
                    throw new OptimisticLockException(evento);
                } else {
                    //idk cosa deve fare
                }
            } else { //insert
                iEvento.setString(1, evento.getNome());
                iEvento.setString(2, evento.getDescrizione());
                iEvento.setObject(3, evento.getTipologia());
                if (evento.getResponsabile()!= null) {
                    iEvento.setInt(4, evento.getResponsabile().getKey());
                } else {
                    iEvento.setNull(4, java.sql.Types.INTEGER);
                }
                if (iEvento.executeUpdate() == 1) {
                    try (ResultSet keys = iEvento.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            evento.setKey(key);
                            dataLayer.getCache().add(Evento.class, evento);
                        }
                    }
                }
            }
            if (evento instanceof DataItemProxy) {
                ((DataItemProxy) evento).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store evento", ex);
        }
    }
    
    @Override
    public Calendario createCalendario() {
        return new CalendarioProxy(getDataLayer());
    }

    @Override //non sono sicuro sul funzionamento, dovrebbe creare una lista dei giorni in cui si tiene un evento
    public List<Calendario> getCalendarioEvento(int evento_key) throws DataException {
       List<Calendario> result =new ArrayList();
       
        try {
            sGiorniEvento.setInt(1, evento_key);
            try (ResultSet rs = sGiorniEvento.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getEvento(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Date", ex);
        }
        return result;
    }
    
    
    public List<Evento> getEventiOdierni() throws DataException{
        List<Evento> result = new ArrayList();
        try {
            
            try (ResultSet rs = sDataOdierna.executeQuery()) {
                while (rs.next()) {
                    result.add((Evento) getEvento(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load events of today", ex);
        }
        return result;
    }
    //da qui ci sono metodi non implementati
    @Override
    public Evento getEvento(String nome, Tipologia tipologia) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //ne ho creati due divisi
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

    @Override
    public void storeEvento(Calendario calendario) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
