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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.*;

/**
 *
 * @author Syd
 */
public class EventoDAO_MySQL extends DAO implements EventoDAO {

    private PreparedStatement sEventoById, sEventoByNome, sEventoByGiornoOra;
    private PreparedStatement sGiorniEvento, sEventoSingolo, sCalendarioById;
    private PreparedStatement sEventiAulaMese, sEventiAulaSettimana, sEventiAulaGiorno, sAllEventiAula, sEventiProssimeOre, sAllEventi;
    private PreparedStatement sEventiCorsoSettimana, sEventiGruppoGiorno;
    private PreparedStatement iEvento, uEvento, dEvento;
    private PreparedStatement iCalendario, uCalendario, dCalendario;

    public EventoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            //ricerca dell'evento per id, nome e ricerca del giorno del calendario
            sEventoById = connection.prepareStatement("SELECT * FROM evento WHERE id=?");
            sEventoByNome = connection.prepareStatement("SELECT id FROM evento WHERE nome=?");
            sCalendarioById = connection.prepareStatement("SELECT calendario.* FROM calendario WHERE id=");

            //questa sotto non è richiesta
            sEventoByGiornoOra = connection.prepareStatement("SELECT evento.id FROM evento JOIN calendario as c where c.giorno = ? AND c.ora_inizio = ? AND c.id_aula = ?");

            //ricerca dei giorni in cui ci sta un evento e ricerca del giorno singolo dell'evento
            sGiorniEvento = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_evento=?");
            sEventoSingolo = connection.prepareStatement("SELECT calendario.* FROM calendario WHERE id_evento=? AND giorno=?");

            //sAllEventi = connection.prepareStatement("SELECT DISTINCT calendario.id_evento FROM calendario");
            //ricerca di tutti gli eventi
            sAllEventi = connection.prepareStatement("SELECT * FROM evento");
            //ricerca di tutti gli eventi nelle prossime 3 ore
            sEventiProssimeOre = connection.prepareStatement("SELECT e.* FROM evento AS e JOIN calendario AS c ON c.id_evento=e.id JOIN gruppo_aula AS ga ON ga.id_aula= c.id_aula WHERE CONCAT(c.giorno, ' ', c.ora_inizio) <= NOW() + INTERVAL 3 HOUR AND CONCAT(c.giorno, ' ', c.ora_fine) >= NOW() AND ga.id_gruppo=?;");

            //ricerca di tutti gli eventi di un'aula data una mese/settimana/un giorno; ricerca di tutti gli eventi da svolgere in una data aula
            sEventiAulaMese = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_aula=? AND MONTH(c.giorno) = MONTH(?)");
            sEventiAulaSettimana = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_aula=? AND WEEK(c.giorno) = WEEK(?)"); //per settimana corrente l'ultimo valore è il giorno odierno
            sEventiAulaGiorno = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_aula=? AND c.giorno=?");
            sAllEventiAula = connection.prepareStatement("SELECT c.* FROM calendario AS c WHERE c.id_aula=?");

            //ricerca di tutti gli eventi di un corso in una data settimana; ricerca di tutti gli eventi di un dipartimento dato un determinato giorno
            sEventiCorsoSettimana = connection.prepareStatement("SELECT c.* FROM calendario AS c JOIN evento ON c.id_evento = evento.id WHERE evento.id_corso=? AND WEEK(c.giorno)=WEEK(?)"); //per la settimana corrente l'ultimo valore deve essere il giorno odierno
            sEventiGruppoGiorno = connection.prepareStatement("SELECT c.* FROM calendario AS c JOIN aula AS a ON a.id=c.id_aula JOIN gruppo_aula AS gp ON gp.id_aula = a.id WHERE c.giorno=CURDATE() AND gp.id_gruppo=?");

            //query per operare nella tabella evento
            iEvento = connection.prepareStatement("INSERT INTO evento (nome,descrizione,tipologia,id_corso,id_professore) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uEvento = connection.prepareStatement("UPDATE evento SET nome=?,descrizione=?,tipologia=?,id_corso=?,id_professore=? WHERE id=?");
            dEvento = connection.prepareStatement("DELETE FROM evento WHERE id=?");

            //query per operare nella tabella calendario
            iCalendario = connection.prepareStatement("INSERT INTO Calendario (id_aula,id_evento,giorno,ora_inizio,ora_fine) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uCalendario = connection.prepareStatement("UPDATE Calendario SET id_aula=?,id_evento=?,giorno=?,ora_inizio=?,ora_fine=? WHERE ID=?");
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

            sEventoByGiornoOra.close();

            sGiorniEvento.close();
            sEventoSingolo.close();
            sCalendarioById.close();

            sEventiAulaMese.close();
            sEventiAulaSettimana.close();
            sEventiAulaGiorno.close();
            sAllEventiAula.close();
            sAllEventi.close();
            sEventiProssimeOre.close();

            sEventiCorsoSettimana.close();
            sEventiGruppoGiorno.close();

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
            e.setTipo(Tipologia.valueOf(rs.getString("tipologia")));
            e.setCorsoKey(rs.getInt("id_corso"));
            e.setProfessoreKey(rs.getInt("id_professore"));

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
            c.setGiorno(rs.getDate("giorno"));
            c.setOraInizio(rs.getTime("ora_inizio").toLocalTime());
            c.setOraFine(rs.getTime("ora_fine").toLocalTime());

        } catch (SQLException ex) {
            throw new DataException("Unable to create calendar object form ResultSet", ex);
        }
        return c;
    }

    @Override //permette di ottenere un evento a partire dall'id
    public Evento getEvento(int id_evento) throws DataException {
        Evento e = null;
        if (dataLayer.getCache().has(Evento.class, id_evento)) {
            e = dataLayer.getCache().get(Evento.class, id_evento);
        } else {
            try {
                sEventoById.setInt(1, id_evento);
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

    @Override
    public List<Evento> getEventoGiornoOra(java.util.Date g, java.time.LocalTime  t, int aula_key) throws DataException {
        List<Evento> result = new ArrayList();
        try {
            sEventoByGiornoOra.setDate(1, new java.sql.Date(g.getTime()));
            sEventoByGiornoOra.setTime(2, new java.sql.Time(t.getHour()));
            sEventoByGiornoOra.setInt(3, aula_key);
            try (ResultSet rs = sEventoByGiornoOra.executeQuery()) {
                while (rs.next()) {
                    result.add(getEvento(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Events", ex);
        }
        return result;
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
    public Calendario getCalendario(int evento_key, java.util.Date d) throws DataException {
        Calendario c = null;
        if (dataLayer.getCache().has(Calendario.class, evento_key)) {
            c = dataLayer.getCache().get(Calendario.class, evento_key);
        } else {
            try {
                sEventoSingolo.setInt(1, evento_key);
                sEventoSingolo.setDate(2, new java.sql.Date(d.getTime()));
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
        List<Calendario> result = new ArrayList();

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

    @Override //permette di ottenere gli eventi dell'aula in un determinato giorno
    public List<Calendario> getEventiAulaGiorno(int id_aula, java.util.Date d) throws DataException {
        List<Calendario> result = new ArrayList();
        try {
            sEventiAulaGiorno.setInt(1, id_aula);
            sEventiAulaGiorno.setDate(2, new java.sql.Date(d.getTime()));
            try (ResultSet rs = sEventiAulaGiorno.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi dell'aula del giorno scelto", ex);
        }
        return result;
    }

    @Override//permette di ottenere gli eventi nell'arco di una settimana di una determinata aula
    public List<Calendario> getEventiAulaSettimana(int aulaId, java.util.Date d) throws DataException {
        List<Calendario> result = new ArrayList();
        try {
            sEventiAulaSettimana.setInt(1, aulaId);
            sEventiAulaSettimana.setDate(2, new java.sql.Date(d.getTime()));
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

    @Override //permette di ottenere gli eventi nell'arco di un mese di una determinata aula
    public List<Calendario> getEventiAulaMese(int aulaId, java.util.Date d) throws DataException {
        List<Calendario> result = new ArrayList();
        try {
            sEventiAulaMese.setInt(1, aulaId);
            sEventiAulaMese.setDate(2, new java.sql.Date(d.getTime()));
            try (ResultSet rs = sEventiAulaMese.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi dell'aula", ex);
        }
        return result;
    }

    @Override //permette di ottenere gli eventi di una determinata aula
    public List<Calendario> getAllEventiAula(int aulaId) throws DataException {
        List<Calendario> result = new ArrayList();
        try {
            sAllEventiAula.setInt(1, aulaId);
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

    @Override //permette di ottenere gli eventi del corso nell'arco di una settimana
    public List<Calendario> getEventiCorsoSettimana(int corsoId, java.util.Date d) throws DataException {
        List<Calendario> result = new ArrayList();
        try {
            sEventiCorsoSettimana.setInt(1, corsoId);
            sEventiCorsoSettimana.setDate(2, new java.sql.Date(d.getTime()));
            try (ResultSet rs = sEventiCorsoSettimana.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi del corso", ex);
        }
        return result;
    }

    @Override //serve per ottenere tutti gli eventi
    public List<Evento> getAllProssimiEventi(int id_gruppo) throws DataException {
        List<Evento> result = new ArrayList();
        try {
            sEventiProssimeOre.setInt(1, id_gruppo);
            try (ResultSet rs = sEventiProssimeOre.executeQuery()) {
                while (rs.next()) {
                    result.add(getEvento(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Eventi of next hours", ex);
        }
        return result;
    }

    @Override //permette di ottenere gli eventi di tutte le aule del dipartimento in data odierna
    public List<Calendario> getEventiAttuali(int id_dipartimento) throws DataException {
        List<Calendario> result = new ArrayList();
        try {
            sEventiGruppoGiorno.setInt(1, id_dipartimento);
            try (ResultSet rs = sEventiGruppoGiorno.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Impossibile caricare gli eventi di oggi", ex);
        }
        return result;
    }

    @Override //serve per ottenere tutti gli eventi
    public List<Evento> getAllEventi() throws DataException {
        List<Evento> result = new ArrayList();

        try (ResultSet rs = sAllEventi.executeQuery()) {
            while (rs.next()) {
                result.add(getEvento(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Eventi", ex);
        }
        return result;
    }

    @Override //serve per salvare le informazioni riguardo un evento nel database
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
                if (evento.getCorso() != null) {
                    uEvento.setInt(4, evento.getCorso().getKey());
                } else {
                    uEvento.setNull(4, java.sql.Types.INTEGER);
                }
                if (evento.getProfessore() != null) {
                    uEvento.setInt(5, evento.getProfessore().getKey());
                } else {
                    uEvento.setNull(5, java.sql.Types.INTEGER);
                }
                uEvento.setInt(6, evento.getKey());
                if (uEvento.executeUpdate() == 0) {
                    throw new OptimisticLockException(evento);
                } else {
                    //idk cosa deve fare
                }
            } else { //insert
                iEvento.setString(1, evento.getNome());
                iEvento.setString(2, evento.getDescrizione());
                iEvento.setString(3, evento.getTipologia().toString());
                if (evento.getCorso() != null) {
                    iEvento.setInt(4, evento.getCorso().getKey());
                } else {
                    iEvento.setNull(4, java.sql.Types.INTEGER);
                }
                if (evento.getProfessore() != null) {
                    iEvento.setInt(5, evento.getProfessore().getKey());
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

    @Override //serve per salvare le informazioni riguardo i giorni di un evento nel database
    public void storeCalendario(Calendario calendario) throws DataException {
        try {
            if (calendario.getKey() != null && calendario.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                if (calendario instanceof DataItemProxy && !((DataItemProxy) calendario).isModified()) {
                    return;
                }

                if (calendario.getAula() != null) {
                    uCalendario.setInt(1, calendario.getAula().getKey());
                } else {
                    uCalendario.setNull(1, java.sql.Types.INTEGER);
                }
                if (calendario.getEvento() != null) {
                    uCalendario.setInt(2, calendario.getEvento().getKey());
                } else {
                    uCalendario.setNull(2, java.sql.Types.INTEGER);
                }
                uCalendario.setDate(3, new java.sql.Date(calendario.getGiorno().getTime()));
                Time t = Time.valueOf(calendario.getOraInizio());
                uCalendario.setTime(4, t);
                Time f = Time.valueOf(calendario.getOraFine());
                uCalendario.setTime(5, f);
                uCalendario.setInt(6, calendario.getKey());
                if (uCalendario.executeUpdate() == 0) {
                    throw new OptimisticLockException(calendario);
                } else {
                    //idk cosa deve fare
                }
            } else { //insert
                if (calendario.getAula() != null) {
                    iCalendario.setInt(1, calendario.getAula().getKey());
                } else {
                    iCalendario.setNull(1, java.sql.Types.INTEGER);
                }
                if (calendario.getEvento() != null) {
                    iCalendario.setInt(2, calendario.getEvento().getKey());
                } else {
                    iCalendario.setNull(2, java.sql.Types.INTEGER);
                }
                iCalendario.setDate(3, new java.sql.Date(calendario.getGiorno().getTime()));
                Time t = Time.valueOf(calendario.getOraInizio());
                iCalendario.setTime(4, t);
                Time f = Time.valueOf(calendario.getOraFine());
                iCalendario.setTime(5, f);

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

    //serve per cancellare un evento
    public void deleteEvento(int id_evento) throws DataException {
        try {
            dEvento.setInt(1, id_evento);
        } catch (SQLException ex) {
            throw new DataException("Unable to delete Event by ID", ex);
        }
    }

    //serve per cancellare il giorno in cui si presenta un evento
    public void deleteCalendario(int id_calendario) throws DataException {
        try {
            dCalendario.setInt(1, id_calendario);
        } catch (SQLException ex) {
            throw new DataException("Unable to delete Event by ID", ex);
        }
    }

    //a seguire i NON IMPLEMENTATI
    @Override //serve per cancellare un evento NON IMPLEMENTATO
    public void deleteEvento(Calendario calendario, boolean singolo) throws DataException {

    }

}
