/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.proxy.CorsoProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;
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
public class CorsoDAO_MySQL extends DAO implements CorsoDAO {
    
    private PreparedStatement sCorsoById, sAllCorsi, iCorso, uCorso, dCorso;

    public CorsoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException{
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            sCorsoById = connection.prepareStatement("SELECT * FROM corso WHERE Id=?");
            sAllCorsi = connection.prepareStatement("SELECT * FROM corso WHERE");
            
            iCorso = connection.prepareStatement("INSERT INTO corso (nome,descrizione) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS);
            uCorso = connection.prepareStatement("UPDATE corso SET nome=?,descrizione=? WHERE ID=?");
            dCorso = connection.prepareStatement("DELETE FROM corso WHERE ID=?");
            
        } catch (SQLException ex) {
            throw new DataException("Error initializing aula web data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //chiusura dei preprared statement
        try {
            sCorsoById.close();
            sAllCorsi.close();
            
            iCorso.close();
            uCorso.close();
            dCorso.close();
            
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }
    
    @Override
    public Corso createCorso() {
        return new CorsoProxy(getDataLayer());
    }

    //helper
    private CorsoProxy createCorso(ResultSet rs) throws DataException {
        CorsoProxy c = (CorsoProxy) createCorso();
        try {
            c.setKey(rs.getInt("id"));
            c.setNome(rs.getString("nome"));
            c.setDescrizione(rs.getString("descrizione"));

        } catch (SQLException ex) {
            throw new DataException("Unable to create corso object form ResultSet", ex);
        }
        return c;
    }
    
    @Override //permette di ottenere il corso tramite l'id
    public Corso getCorsoById(Integer corso_key) throws DataException {
        Corso c = null;
        if (dataLayer.getCache().has(Corso.class, corso_key)) {
            c = dataLayer.getCache().get(Corso.class, corso_key);
        } else {
            try {
                sCorsoById.setInt(1, corso_key);
                try (ResultSet rs = sCorsoById.executeQuery()) {
                    if (rs.next()) {
                        c = createCorso(rs);

                        dataLayer.getCache().add(Corso.class, c);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load corso by ID", ex);
            }
        }
        return c;
    }

    @Override //permette di ottenere la lista dei corsi
    public List<Corso> getAllCorsi() throws DataException {
        List<Corso> result = new ArrayList();

        try (ResultSet rs = sAllCorsi.executeQuery()) {
            while (rs.next()) {
                result.add((Corso) getCorsoById(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load corsi", ex);
        }
        return result;
    }
    
    @Override //permette di salvare le informazioni relative ad un corso nel database
    public void storeCorso(Corso corso) throws DataException {
        
         try {
            if (corso.getKey() != null && corso.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                if (corso instanceof DataItemProxy && !((DataItemProxy) corso).isModified()) {
                    return;
                }
                uCorso.setString(1, corso.getNome());
                uCorso.setString(2, corso.getDescrizione());
                uCorso.setInt(11,corso.getKey());
                if (uCorso.executeUpdate() == 0) {
                    throw new OptimisticLockException(corso);
                } else {
                    //idk cosa deve fare
                }
            } else { //insert
                iCorso.setString(1, corso.getNome());
                iCorso.setString(2, corso.getDescrizione());
                if (iCorso.executeUpdate() == 1) {
                    try (ResultSet keys = iCorso.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            corso.setKey(key);
                            dataLayer.getCache().add(Corso.class, corso);
                        }
                    }
                }
            }
            if (corso instanceof DataItemProxy) {
                ((DataItemProxy) corso).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store corso", ex);
        }
    }

    @Override //permette di eliminare un corso
    public void deleteCorso(Integer id_corso) throws DataException {
       try {
            dCorso.setInt(1, id_corso);
        } catch (SQLException ex) {
            throw new DataException("Unable to delete Corso by ID", ex);
        }
    }

}
