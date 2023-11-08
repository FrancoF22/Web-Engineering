package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.auleweb.data.proxy.ProfessoreProxy;
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
public class ProfessoreDAO_MySQL extends DAO implements ProfessoreDAO {
    
    private PreparedStatement sProfById, sProfBC;
    private PreparedStatement sProfessori;
    private PreparedStatement iProfessore, uProfessore, dProfessore;
    
    @Override
    public void init() throws DataException {
        try {
            super.init();

            //precompiliamo tutte le query utilizzate nella classe
            sProfById = connection.prepareStatement("SELECT * FROM professore WHERE id=?");
            sProfBC = connection.prepareStatement("SELECT * FROM professore WHERE cognome=?");
            sProfessori = connection.prepareStatement("SELECT professore.* FROM professore");
            
            iProfessore = connection.prepareStatement("INSERT INTO professore (nome,cognome,email) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uProfessore = connection.prepareStatement("UPDATE professore SET nome=?,cognome=? WHERE id=?");
            dProfessore = connection.prepareStatement("DELETE FROM professore WHERE id=?");
        } catch (SQLException ex) {
            throw new DataException("Error initializing aula web data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //anche chiudere i PreparedStamenent è una buona pratica...
        try {
            sProfById.close();
            sProfBC.close();
            sProfessori.close();
            
            iProfessore.close();
            uProfessore.close();
            dProfessore.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }
    
    public ProfessoreDAO_MySQL(DataLayer d) {
        super(d);
    }
    
    @Override
    public Professore createProfessore() {
        return new ProfessoreProxy(getDataLayer());
    }

    public Professore createProfessore(ResultSet rs) throws DataException {
        try {
            ProfessoreProxy a = (ProfessoreProxy) createProfessore();
            a.setKey(rs.getInt("Id"));
            a.setNome(rs.getString("nome"));
            a.setCognome(rs.getString("cognome"));
            return a;
        } catch (SQLException ex) {
            throw new DataException("Unable to create professor object form ResultSet", ex);
        }
    }
    
    @Override
    public Professore getProfessoreById(int professore_key) throws DataException {
        
         Professore p = null;
        if (dataLayer.getCache().has(Professore.class, professore_key)) {
            p = dataLayer.getCache().get(Professore.class, professore_key);
        } else {
            try {
                sProfById.setInt(1, professore_key);
                try ( ResultSet rs = sProfById.executeQuery()) {
                    if (rs.next()) {
                        p = createProfessore(rs);
                        //e lo mettiamo anche nella cache
                        dataLayer.getCache().add(Professore.class, p);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load professor by ID", ex);
            }
        }
        return p;
    }

    @Override
    public Professore getProfessore(String cognome) throws DataException {
        
         Professore p = null;
        if (dataLayer.getCache().has(Professore.class, cognome)) {
            p = dataLayer.getCache().get(Professore.class, cognome);
        } else {
            try {
                sProfBC.setString(1, cognome);
                try ( ResultSet rs = sProfBC.executeQuery()) {
                    if (rs.next()) {
                        p = createProfessore(rs);
                        //e lo mettiamo anche nella cache
                        dataLayer.getCache().add(Professore.class, p);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load professor", ex);
            }
        }
        return p;
    }
    
    @Override
    public List<Professore> getProfessori() throws DataException {
         List<Professore> u = new ArrayList();
        try (ResultSet rs = sProfessori.executeQuery()) {
            while (rs.next()) {
                u.add((Professore) getProfessoreById(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load professors", ex);
        }
        return u;
    }
    
    @Override
    public void storeProfessore(Professore prof) throws DataException{
        try {
            if (prof.getKey() != null && prof.getKey() > 0) { 
                if (prof instanceof DataItemProxy && !((DataItemProxy) prof).isModified()) {
                    return;
                }
                uProfessore.setString(1, prof.getNome());
                uProfessore.setString(2, prof.getCognome());

                uProfessore.setInt(3, prof.getKey());

                if (uProfessore.executeUpdate() == 0) {
                    throw new OptimisticLockException(prof);
                } else {
                   //? se non ricordo male qui c'era il controllo di versione, o qualcosa del genere
                }
            } else { //insert
                iProfessore.setString(1, prof.getNome());
                iProfessore.setString(2, prof.getCognome());

                if (iProfessore.executeUpdate() == 1) {
                    try ( ResultSet keys = iProfessore.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            prof.setKey(key);
                            dataLayer.getCache().add(Professore.class, prof);
                        }
                    }
                }
            }

            if (prof instanceof DataItemProxy) {
                ((DataItemProxy) prof).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store professor", ex);
        }
    }
    
    
    @Override
    public void deleteProfessore(Integer id_prof) throws DataException {
         try {
            dProfessore.setInt(1, id_prof);
        } catch (SQLException ex) {
            throw new DataException("Unable to delete professor by ID", ex);
        }
    }
}
