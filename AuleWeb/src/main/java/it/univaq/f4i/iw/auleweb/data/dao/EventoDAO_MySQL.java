package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.auleweb.data.model.Ricorrenza;
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

    private PreparedStatement sEventoById, sEventoByNome, sEventiByTipo, sEventiByResponsabile;
    private PreparedStatement sGiorniEvento, sEventoSingolo, sCalendarioById;
    private PreparedStatement sEventiAulaSettCur, sEventiAulaSettimana, sEventiAulaOggi, sAllEventiAula;
    private PreparedStatement sEventiCorsoOggi, sEventiCorso, sEventiGruppo;
    private PreparedStatement iEvento, uEvento, dEvento;
    private PreparedStatement iCalendario, uCalendario, dCalendario;

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
            sEventiByResponsabile = connection.prepareStatement("SELECT id FROM evento WHERE id_responsabile=?");

            sGiorniEvento = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_evento=?");
            sEventoSingolo = connection.prepareStatement("SELECT calendario.* FROM calendario WHERE id_evento=? AND giorno=?");
            sCalendarioById = connection.prepareStatement("SELECT calendario.* FROM calendario WHERE id=");
            
            sEventiAulaSettCur = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_aula=? AND YEAR(c.giorno)=YEAR(CURDATE()) AND WEEK(c.giorno,1)=WEEK(CURDATE(),1)");
            sEventiAulaSettimana = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_aula=? AND YEAR(c.giorno)=YEAR(?) AND WEEK(c.giorno,1)=WEEK(?,1)");
            sEventiAulaOggi =  connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_aula=? AND c.giorno=?");
            sAllEventiAula = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_aula=?");
            
            sEventiCorsoOggi = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE evento.id_corso=? AND YEAR(c.giorno)=YEAR(CURDATE()) AND WEEK(c.giorno,1)=WEEK(CURDATE(),1)");
            sEventiCorso = connection.prepareStatement("SELECT c.* FROM calendario AS c JOIN evento ON c.id_evento = evento.id WHERE evento.id_corso=? AND YEAR(c.giorno)=YEAR(?) AND WEEK(c.giorno,1)=WEEK(?,1)");
            sEventiGruppo = connection.prepareStatement("SELECT c.* FROM calendario AS c JOIN aula AS a ON a.id=c.id_aula JOIN gruppo_aula AS gp ON gp.id_aula = a.id WHERE c.giorno=CURDATE() AND gp.id_gruppo=?");
            
            iEvento = connection.prepareStatement("INSERT INTO evento (nome,descrizione,tipologia,id_corso,id_responsabile) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uEvento = connection.prepareStatement("UPDATE evento SET nome=?,descrizione=?,tipologia=?,id_corso=?,id_responsabile=? WHERE ID=?");
            dEvento = connection.prepareStatement("DELETE FROM evento WHERE id=?");
            
            iCalendario = connection.prepareStatement("INSERT INTO Calendario (id_aula,id_evento,ricorrenza,giorno,giorno_fine,ora_inizio,ora_fine) VALUES(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uCalendario = connection.prepareStatement("UPDATE Calendario SET id_aula=?,id_evento=?,ricorrenza=?,giorno=?,giorno_fine=?,ora_inizio=?,ora_fine=? WHERE ID=?");
            dCalendario = connection.prepareStatement("DELETE FROM Calendario WHERE id=?");
            
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
            sEventiByResponsabile.close();

            sGiorniEvento.close();
            sEventoSingolo.close();
            sCalendarioById.close();
            
            sEventiAulaSettCur.close();
            sEventiAulaSettimana.close();
            sEventiAulaOggi.close();
            sAllEventiAula.close();
            
            sEventiCorsoOggi.close();
            sEventiCorso.close();
            sEventiGruppo.close();
            
            iEvento.close();
            uEvento.close();
            dEvento.close();
            
            iCalendario.close();
            uCalendario.close();
            dCalendario.close();
            
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
            throw new DataException("Unable to create event object form ResultSet", ex);
        }
        return e;
    }

    @Override
    public Calendario createCalendario() {
        return new CalendarioProxy(getDataLayer());
    }

    //helper
    private CalendarioProxy createCalendario(ResultSet rs) throws DataException {
        CalendarioProxy c = (CalendarioProxy) createCalendario();
        try {
            c.setKey(rs.getInt("id"));
            c.setAulaKey(rs.getInt("id_aula"));
            c.setEventoKey(rs.getInt("id_evento"));
            c.setRicorrenza(Ricorrenza.valueOf( rs.getString("ricorrenza")));
            c.setGiorno(rs.getDate("giorno"));
            c.setGiornoFine(rs.getDate("giorno_fine"));
            c.setOraInizio(rs.getTime("ora_inizio"));
            c.setOraFine(rs.getTime("ora_fine"));

        } catch (SQLException ex) {
            throw new DataException("Unable to create calendar object form ResultSet", ex);
        }
        return c;
    }
    
    @Override //permette di ottenere un evento a partire dall'id
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

    //permette di ottenere un evento a partire dal nome
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
    
    //permette di ottenere l'evento singolo nel calendario
    public Calendario getCalendarioById(int key) throws DataException {
        Calendario c = null;
        if (dataLayer.getCache().has(Calendario.class, key)) {
            c = dataLayer.getCache().get(Calendario.class, key);
        } else {
            try {
                sCalendarioById.setInt(1, key);
                try (ResultSet rs = sCalendarioById.executeQuery()) {
                    if (rs.next()) {
                        c = createCalendario(rs);

                        dataLayer.getCache().add(Calendario.class, c);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load event", ex);
            }
        }
        return c;
    }
    
    @Override //permette di ottenere l'evento singolo nel calendario
    public Calendario getCalendario(int evento_key, Date giorno) throws DataException {
        Calendario c = null;
        if (dataLayer.getCache().has(Calendario.class, evento_key)) {
            c = dataLayer.getCache().get(Calendario.class, evento_key);
        } else {
            try {
                sEventoSingolo.setInt(1, evento_key);
                sEventoSingolo.setDate(2, giorno);
                try (ResultSet rs = sEventoSingolo.executeQuery()) {
                    if (rs.next()) {
                        c = createCalendario(rs);

                        dataLayer.getCache().add(Calendario.class, c);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load event", ex);
            }
        }
        return c;

    }
    
    @Override //permette di ottenere una lista dei giorni in cui si tiene un evento
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
    
    //il metodo che segue permette di ottenere gli eventi nell'arco della settimana corrente
    public List<Calendario> getEventiAulaSettimana(int aulaId) throws DataException{
        List<Calendario> result = new ArrayList();
        try {
            sEventiAulaSettCur.setInt(1,aulaId);
            try (ResultSet rs = sEventiAulaSettCur.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi dell'aula", ex);
        }
        return result;
    }
    
    @Override//il metodo che segue permette di ottenere gli eventi nell'arco di una settimana di una determinata aula
    public List<Calendario> getEventiAula(int aulaId, Date d) throws DataException{
        List<Calendario> result = new ArrayList();
        try {
            sEventiAulaSettimana.setInt(1,aulaId);
            sEventiAulaSettimana.setDate(2, d);
            sEventiAulaSettimana.setDate(3, d);
            try (ResultSet rs = sEventiAulaSettimana.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi dell'aula", ex);
        }
        return result;
    }
    
    @Override //il metodo che segue permette di ottenere gli eventi di una determinata aula
    public List<Calendario> getAllEventiAula(int aulaId) throws DataException{
        List<Calendario> result = new ArrayList();
        try {
            sAllEventiAula.setInt(1,aulaId);
            try (ResultSet rs = sAllEventiAula.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi dell'aula", ex);
        }
        return result;
    }

    @Override //permette di ottenere gli eventi del corso nell'arco della settimana corrente
    public List<Calendario> getEventiCorsoSettimana(int corsoId) throws DataException {
         List<Calendario> result = new ArrayList();
        try {
            sEventiCorsoOggi.setInt(1,corsoId);
            try (ResultSet rs = sEventiCorsoOggi.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi del corso", ex);
        }
        return result;
    }

    @Override //il metodo che segue permette di ottenere gli eventi del corso nell'arco di una settimana
    public List<Calendario> getEventiCorso(int corsoId, Date d) throws DataException{
        List<Calendario> result = new ArrayList();
        try {
            sEventiAulaSettimana.setInt(1,corsoId);
            sEventiAulaSettimana.setDate(2, d);
            sEventiAulaSettimana.setDate(3, d);
            try (ResultSet rs = sEventiAulaSettimana.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi del corso", ex);
        }
        return result;
    }

    @Override //permette di ottenere gli eventi di tutte le aule del dipartimento in data odierna
    public List<Calendario> getEventiAttuali(int id_dipartimento) throws DataException {
         List<Calendario> result = new ArrayList();
        try {
            sEventiGruppo.setInt(1,id_dipartimento);
            try (ResultSet rs = sEventiGruppo.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi di oggi", ex);
        }
        return result;
    }
    
    @Override //permette di ottenere gli eventi dell'aula in data odierna
    public List<Calendario> getEventiAulaGiorno(int id_aula, Date d) throws DataException{
        List<Calendario> result = new ArrayList();
        try {
            sEventiCorsoOggi.setInt(1,id_aula);
            sEventiCorsoOggi.setDate(2,d);
            try (ResultSet rs = sEventiCorsoOggi.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi dell'aula del giorno scelto", ex);
        }
        return result;
    }
    
    //permette di ottenere tutti gli eventi a seconda della tipologia
    public List<Evento> getAllEventi(String tipo_evento) throws DataException {
        List<Evento> result = new ArrayList();

        try (ResultSet rs = sEventiByTipo.executeQuery(tipo_evento)) {
            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load events", ex);
        }
        return result;
    }
    
    @Override //permette di ottenere tutti gli eventi gestiti dalla stessa persona (responsabile)
    public List<Evento> getEventiResponsabile(String email) throws DataException {
        List<Evento> result = new ArrayList();

        try (ResultSet rs = sEventiByResponsabile.executeQuery(email)) {
            while (rs.next()) {
                result.add((Evento) getEvento(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load events", ex);
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
                uEvento.setString(3, evento.getTipologia().toString());
                if (evento.getCorso()!= null) {
                    uEvento.setInt(4, evento.getCorso().getKey());
                } else {
                    uEvento.setNull(4, java.sql.Types.INTEGER);
                }
                if (evento.getUtente()!= null) {
                    uEvento.setInt(5, evento.getUtente().getKey());
                } else {
                    uEvento.setNull(5, java.sql.Types.INTEGER);
                }

                if (uEvento.executeUpdate() == 0) {
                    throw new OptimisticLockException(evento);
                } else {
                    //idk cosa deve fare
                }
            } else { //insert
                iEvento.setString(1, evento.getNome());
                iEvento.setString(2, evento.getDescrizione());
                iEvento.setString(3, evento.getTipologia().toString());
                if (evento.getCorso()!= null) {
                    iEvento.setInt(4, evento.getCorso().getKey());
                } else {
                    iEvento.setNull(4, java.sql.Types.INTEGER);
                }
                if (evento.getUtente()!= null) {
                    iEvento.setInt(5, evento.getUtente().getKey());
                } else {
                    iEvento.setNull(5, java.sql.Types.INTEGER);
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
    public void storeCalendario(Calendario calendario) throws DataException {
         try {
            if (calendario.getKey() != null && calendario.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                if (calendario instanceof DataItemProxy && !((DataItemProxy) calendario).isModified()) {
                    return;
                }
                
                if (calendario.getAula()!= null) {
                    uCalendario.setInt(1, calendario.getAula().getKey());
                } else {
                    uCalendario.setNull(1, java.sql.Types.INTEGER);
                }
                if (calendario.getEvento()!= null) {
                    uCalendario.setInt(2, calendario.getEvento().getKey());
                } else {
                    uCalendario.setNull(2, java.sql.Types.INTEGER);
                }
                uCalendario.setString(3,calendario.getRicorrenza().toString());
                uCalendario.setDate(4, calendario.getGiorno());
                uCalendario.setDate(5, calendario.getGiornoFine());
                uCalendario.setTime(6, calendario.getOraInizio());
                uCalendario.setTime(7, calendario.getOraFine());

                if (uEvento.executeUpdate() == 0) {
                    throw new OptimisticLockException(calendario);
                } else {
                    //idk cosa deve fare
                }
            } else { //insert
                if (calendario.getAula()!= null) {
                    iCalendario.setInt(1, calendario.getAula().getKey());
                } else {
                    iCalendario.setNull(1, java.sql.Types.INTEGER);
                }
                if (calendario.getEvento()!= null) {
                    iCalendario.setInt(2, calendario.getEvento().getKey());
                } else {
                    iCalendario.setNull(2, java.sql.Types.INTEGER);
                }
                iCalendario.setString(3,calendario.getRicorrenza().toString());
                iCalendario.setDate(4, calendario.getGiorno());
                iCalendario.setDate(5, calendario.getGiornoFine());
                iCalendario.setTime(6, calendario.getOraInizio());
                iCalendario.setTime(7, calendario.getOraFine());
                
                if (iCalendario.executeUpdate() == 1) {
                    try (ResultSet keys = iCalendario.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            calendario.setKey(key);
                            dataLayer.getCache().add(Calendario.class, calendario);
                        }
                    }
                }
            }
            if (calendario instanceof DataItemProxy) {
                ((DataItemProxy) calendario).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store calendario", ex);
        }
    }
     
    public void deleteEvento(int id_evento) throws DataException {
         try {
            dEvento.setInt(1, id_evento);
        } catch (SQLException ex) {
            throw new DataException("Unable to delete Event by ID", ex);
        }
    }

    public void deleteCalendario(int id_calendario) throws DataException {
         try {
            dCalendario.setInt(1, id_calendario);
        } catch (SQLException ex) {
            throw new DataException("Unable to delete Event by ID", ex);
        }
    }
    
    @Override
    public void deleteEvento(Calendario calendario, boolean singolo) throws DataException {
        
    }
    
}
