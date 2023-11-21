package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.proxy.CalendarioProxy;
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
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author franc
 */
public class CalendarioDAO_MySQL extends DAO implements CalendarioDAO {
    
    private PreparedStatement sCalendarioById, sCalendarioByEvento, sEventoSingolo, sCalendarioByAula, sAllCalendari;
    private PreparedStatement iCalendario, uCalendario, dCalendario;
    
    public CalendarioDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            sCalendarioById = connection.prepareStatement("SELECT * FROM calendario WHERE id=?");
            sCalendarioByEvento = connection.prepareStatement("SELECT id FROM calendario WHERE id_evento=?");
            sEventoSingolo = connection.prepareStatement("SELECT id FROM calendario WHERE id_evento=? AND giorno=?");
            sCalendarioByAula = connection.prepareStatement("SELECT id FROM calendario WHERE id_aula=?");
            sAllCalendari = connection.prepareStatement("SELECT id FROM calendario");
            
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
            sCalendarioById.close();
            sCalendarioByEvento.close();
            sCalendarioByAula.close();
            sAllCalendari.close();
            
            iCalendario.close();
            uCalendario.close();
            dCalendario.close();
            
        } catch (SQLException ex) {
            //
        }
        super.destroy();
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
    
    @Override
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
    
    @Override // permette di ottenere l'evento singolo a partire dall'id_Mastere e dal giorno
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
    
    @Override // permette di ottenere una lista dei giorni in cui si tiene un evento
    public List<Calendario> getCalendarioEvento(int evento_key) throws DataException {
       List<Calendario> result =new ArrayList();
        try {
            sCalendarioByEvento.setInt(1, evento_key);
            try (ResultSet rs = sCalendarioByEvento.executeQuery()) {
                while (rs.next()) {
                    result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Date", ex);
        }
        return result;
    }
    
    @Override // permette di ottenere tutti gli eventi del calendario
    public List<Calendario> getCalendari() throws DataException {
        List<Calendario> result =new ArrayList();
        try {
            try (ResultSet rs = sAllCalendari.executeQuery()) {
                while (rs.next()) {
                    result.add(getCalendarioById(rs.getInt("id")));
                    //result.add((Calendario) getCalendarioById(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Date", ex);
        }
        return result;
    };
    
    @Override // serve per salvare le informazioni riguardo i giorni di un evento nel database
    public void storeCalendario(Calendario calendario) throws DataException {
         try {
            if (calendario.getKey() != null && calendario.getKey() > 0) { 
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
                uCalendario.setDate(3, (Date) calendario.getGiorno());
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
            } else {
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
                iCalendario.setDate(3, (Date) calendario.getGiorno());
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

    
    // METODI NON IMPLEMENTATI IN QUESTA DAO
    
    @Override
    public List<Calendario> getEventiCorsoSettimana(int corso) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiAttuali(int id_dipartimento) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiCorso(int id_corso, Date data) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getAllEventiAula(int id_aula) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiAula(int id_aula, Date date) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Calendario> getEventiAulaGiorno(int id_aula, Date date) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
