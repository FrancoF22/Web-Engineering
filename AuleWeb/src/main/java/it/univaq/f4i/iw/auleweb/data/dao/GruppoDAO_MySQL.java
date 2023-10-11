/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo_Aula;
import it.univaq.f4i.iw.auleweb.data.model.TipoGruppo;
import it.univaq.f4i.iw.auleweb.data.proxy.GruppoProxy;
import it.univaq.f4i.iw.auleweb.data.proxy.Gruppo_AulaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import java.util.List;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;
import java.util.ArrayList;

/**
 *
 * @author Syd
 */
public class GruppoDAO_MySQL extends DAO implements GruppoDAO {

    private PreparedStatement sGruppoById, sGruppoByName;
    private PreparedStatement sAllDepartments, sAllPolo;
    private PreparedStatement iGruppo, uGruppo, dGruppo;
    private PreparedStatement iGruppoAula, dGruppoAula;

    public GruppoDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();

            sGruppoById = connection.prepareStatement("SELECT * FROM gruppo WHERE id=?");
            sGruppoByName = connection.prepareStatement("SELECT * FROM gruppo WHERE name=?");
            sAllDepartments = connection.prepareStatement("SELECT * FROM gruppo WHERE tipologia='dipartimento'");
            sAllPolo = connection.prepareStatement("SELECT * FROM gruppo WHERE tipologia='polo'");

            iGruppo = connection.prepareStatement("INSERT INTO gruppo (nome,tipologia) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            uGruppo = connection.prepareStatement("UPDATE gruppo SET nome=?,tipologia=? WHERE ID=?");
            dGruppo = connection.prepareStatement("DELETE FROM gruppo WHERE ID=?");

            iGruppoAula = connection.prepareStatement("INSERT INTO gruppo_aula (id_gruppo,id_aula) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            iGruppoAula = connection.prepareStatement("DELETE FROM gruppo_aula WHERE id=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing aula web data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {

            sGruppoById.close();
            sGruppoByName.close();

            sAllDepartments.close();
            sAllPolo.close();

            iGruppo.close();
            uGruppo.close();
            dGruppo.close();

            iGruppoAula.close();
            dGruppoAula.close();

        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Gruppo createGruppo() {
        return new GruppoProxy(getDataLayer());
    }

    //helper
    private GruppoProxy createGruppo(ResultSet rs) throws DataException {
        GruppoProxy g = (GruppoProxy) createGruppo();
        try {
            g.setKey(rs.getInt("id"));
            g.setNome(rs.getString("nome"));
            g.setTipoGruppo((TipoGruppo) rs.getObject("tipologia"));
            g.setDescrizione(rs.getString("descriziopne"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create gruppo object form ResultSet", ex);
        }
        return g;
    }

    public Gruppo getGruppo(int gruppo_key) throws DataException {
        Gruppo g = null;
        //prima vediamo se l'oggetto è già stato caricato
        //first look for this object in the cache
        if (dataLayer.getCache().has(Gruppo.class, gruppo_key)) {
            g = dataLayer.getCache().get(Gruppo.class, gruppo_key);
        } else {
            //altrimenti lo carichiamo dal database
            //otherwise load it from database
            try {
                sGruppoById.setInt(1, gruppo_key);
                try (ResultSet rs = sGruppoById.executeQuery()) {
                    if (rs.next()) {
                        g = createGruppo(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Gruppo.class, g);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load gruppo by ID", ex);
            }
        }
        return g;
    }

    @Override
    public Gruppo getGruppo(String gruppo_nome) throws DataException {
        Gruppo g = null;
        //prima vediamo se l'oggetto è già stato caricato
        if (dataLayer.getCache().has(Gruppo.class, gruppo_nome)) {
            g = dataLayer.getCache().get(Gruppo.class, gruppo_nome);
        } else {
            //altrimenti lo carichiamo dal database
            try {
                sGruppoByName.setString(1, gruppo_nome);
                try (ResultSet rs = sGruppoByName.executeQuery()) {
                    if (rs.next()) {
                        g = createGruppo(rs);
                        //e lo mettiamo anche nella cache
                        dataLayer.getCache().add(Gruppo.class, g);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load gruppo by nome", ex);
            }
        }
        return g;
    }

    @Override
    public List<Gruppo> getAllDipartimenti() throws DataException {
        List<Gruppo> result = new ArrayList();

        try (ResultSet rs = sAllDepartments.executeQuery()) {
            while (rs.next()) {
                result.add((Gruppo) getGruppoById(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Gruppi", ex);
        }
        return result;
    }

    public List<Gruppo> getAllPoli() throws DataException {
        List<Gruppo> result = new ArrayList();

        try (ResultSet rs = sAllPolo.executeQuery()) {
            while (rs.next()) {
                result.add((Gruppo) getGruppoById(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Gruppi", ex);
        }
        return result;
    }

    @Override
    public void storeGruppo(Gruppo gruppo) throws DataException {
        try {
            if (gruppo.getKey() != null && gruppo.getKey() > 0) {
                if (gruppo instanceof DataItemProxy && !((DataItemProxy) gruppo).isModified()) {
                    return;
                }
                uGruppo.setString(1, gruppo.getNome());
                uGruppo.setString(2, gruppo.getDescrizione());
                uGruppo.setObject(3, gruppo.getTipoGruppo());

                if (uGruppo.executeUpdate() == 0) {
                    throw new OptimisticLockException(gruppo);
                } else {
                    //
                }
            } else { //insert
                iGruppo.setString(1, gruppo.getNome());
                iGruppo.setString(2, gruppo.getDescrizione());
                iGruppo.setObject(3, gruppo.getTipoGruppo());
                if (iGruppo.executeUpdate() == 1) {
                    try (ResultSet keys = iGruppo.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            gruppo.setKey(key);
                            dataLayer.getCache().add(Gruppo.class, gruppo);
                        }
                    }
                }
            }
            if (gruppo instanceof DataItemProxy) {
                ((DataItemProxy) gruppo).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store gruppo", ex);
        }
    }

    @Override
    public Gruppo_Aula createGruppoAula() {
        return new Gruppo_AulaProxy(getDataLayer());
    }

    //helper
    private Gruppo_AulaProxy createGruppoAula(ResultSet rs) throws DataException {
        Gruppo_AulaProxy ga = (Gruppo_AulaProxy) createGruppoAula();
        try {
            ga.setKey(rs.getInt("id"));
            ga.setGruppoKey(rs.getInt("id_gruppo"));
            ga.setAulaKey(rs.getInt("id_aula"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return ga;
    }

    public void storeGruppoAula(Gruppo_Aula gruppoaula) throws DataException {
        try {
            iGruppoAula.setInt(1, gruppoaula.getGruppo().getKey());
            iGruppoAula.setInt(2, gruppoaula.getAula().getKey());
            if (iGruppoAula.executeUpdate() == 1) {
                try (ResultSet keys = iGruppo.getGeneratedKeys()) {
                    if (keys.next()) {
                        int key = keys.getInt(1);
                        gruppoaula.setKey(key);
                        dataLayer.getCache().add(Gruppo_Aula.class, gruppoaula);
                    }
                }
            }

            if (gruppoaula instanceof DataItemProxy) {
                ((DataItemProxy) gruppoaula).setModified(false);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store gruppo", ex);
        }
    }
    //i metodi che seguono non sono stati implementati, qualcuno potrebbe non servire

    @Override
    public void addGruppo_Aula(Integer gruppo_key, Integer id_aula) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // fatto diversamente, ma dovrei avercelo
    }

    @Override
    public Gruppo_Aula getGruppo_Aula(int id_aula) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Gruppo getGruppoById(int gruppo_key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // ce l'ho, è chiamato diversamente
    }

    @Override
    public List<Gruppo> getAllCorsi() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //le aule non sono più raggruppate in corsi
    }

    @Override
    public List<Gruppo> getAllFacolta(int dipartimento_key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //le aule non sono raggruppate in facoltà
    }

    @Override
    public List<Gruppo> getAllFacolta() throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Aula> getAllAule(int gruppo_key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // ce l'ho altrove
    }

    @Override
    public List<Aula> getAuleGruppo(Integer id_gruppo) throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteGruppo(Integer gruppo_key) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //devo implementare la logica
    }

    @Override
    public void deleteAulaFromGruppo(Integer id_gruppo, Integer id_aula) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //devo implementare la logica
    }

    @Override
    public List<Aula> getAuleDisponibili(int id_gruppo) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); //devo implementare la logica, potenzialmente starà in un file diverso
    }

}
