package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Utente;
import it.univaq.f4i.iw.auleweb.data.proxy.UtentiProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;
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
public class UtenteDAO_MySQL extends DAO implements UtenteDAO {
    
    private PreparedStatement sUtenteById, sUtenteByEmail, iUtente, uUtente;

    public UtenteDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            sUtenteById = connection.prepareStatement("SELECT * FROM utente WHERE Id=?");
            sUtenteByEmail = connection.prepareStatement("SELECT utente.* FROM utente WHERE email=?");
            
            iUtente = connection.prepareStatement("INSERT INTO utente (email,password) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            uUtente = connection.prepareStatement("UPDATE utente SET email=?,password=? WHERE ID=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing aula web data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent è una buona pratica...
        try {
            sUtenteById.close();
            sUtenteByEmail.close();
            
            iUtente.close();
            uUtente.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }
    
    //metodi "factory" che permettono di creare
    //e inizializzare opportune implementazioni
    //delle interfacce del modello dati, nascondendo
    //all'utente tutti i particolari
    
    @Override
    public Utente createUtente() {
        return new UtentiProxy(getDataLayer());
    }

    //helper
    private UtentiProxy createUtente(ResultSet rs) throws DataException {
        try {
            UtentiProxy a = (UtentiProxy) createUtente();
            a.setKey(rs.getInt("Id"));
            a.setEmail(rs.getString("email"));
            a.setPassword(rs.getString("password"));
            return a;
        } catch (SQLException ex) {
            throw new DataException("Unable to create user object form ResultSet", ex);
        }
    }
    
    @Override //permette di ottenere un utente tramite l'id
    public Utente getUtente(int user_key) throws DataException {
         Utente u = null;
        //prima vediamo se l'oggetto è già stato caricato
        if (dataLayer.getCache().has(Utente.class, user_key)) {
            u = dataLayer.getCache().get(Utente.class, user_key);
        } else {
            //altrimenti lo carichiamo dal database
            try {
                sUtenteById.setInt(1, user_key);
                try ( ResultSet rs = sUtenteById.executeQuery()) {
                    if (rs.next()) {
                        //notare come utilizziamo il costrutture
                        //"helper" della classe UtenteImpl
                        //per creare rapidamente un'istanza a
                        //partire dal record corrente
                        u = createUtente(rs);
                        //e lo mettiamo anche nella cache
                        dataLayer.getCache().add(Utente.class, u);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load user by ID", ex);
            }
        }
        return u;
    }

    @Override //permette di ottenere un utente tramite l'email
    public Utente getUtenteByEmail(String email) throws DataException {
        try {
            sUtenteByEmail.setString(1, email);
            try ( ResultSet rs = sUtenteByEmail.executeQuery()) {
                if (rs.next()) {
                    return getUtente(rs.getInt("id"));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to find user", ex);
        }
        return null;
    }
    
    @Override
    public void storeUtente(Utente user) throws DataException {
       try {
            if (user.getKey() != null && user.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                //do not store the object if it is a proxy and does not indicate any modification
                if (user instanceof DataItemProxy && !((DataItemProxy) user).isModified()) {
                    return;
                }
                uUtente.setString(1, user.getEmail());
                uUtente.setString(2, user.getPassword());

                uUtente.setInt(3, user.getKey());

                if (uUtente.executeUpdate() == 0) {
                    throw new OptimisticLockException(user);
                } else {
                   //?
                }
            } else { //insert
                iUtente.setString(1, user.getEmail());
                iUtente.setString(2, user.getPassword());

                if (iUtente.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    try ( ResultSet keys = iUtente.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave in caso di inserimento
                            user.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            dataLayer.getCache().add(Utente.class, user);
                        }
                    }
                }
            }

            if (user instanceof DataItemProxy) {
                ((DataItemProxy) user).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store user", ex);
        }
    }
    
    public void deleteUtente(Integer id) throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
    public void storeUtente(Utente responsabile, String parameter, Integer key) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
