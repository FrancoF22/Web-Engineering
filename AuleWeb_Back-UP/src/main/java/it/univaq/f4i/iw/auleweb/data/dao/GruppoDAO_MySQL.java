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
    private PreparedStatement sAllGruppi, sAllDepartments, sAllPolo;
    private PreparedStatement sGruppiAula, sDipartimentoAula, sPoloAula;
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
            sAllGruppi = connection.prepareStatement("SELECT * FROM gruppo");
            sAllDepartments = connection.prepareStatement("SELECT * FROM gruppo WHERE tipologia='dipartimento'");
            sAllPolo = connection.prepareStatement("SELECT * FROM gruppo WHERE tipologia='polo'");

            sGruppiAula = connection.prepareStatement("SELECT g.* FROM gruppo AS g JOIN gruppo_aula AS ga ON ga.id_gruppo=g.id WHERE ga.id_aula=?");
            sDipartimentoAula = connection.prepareStatement("Select g.* FROM gruppo AS g JOIN gruppo_aula AS ga ON ga.id_gruppo=g.id WHERE ga.id_aula=? AND g.tipologia=dipartimento");
            sPoloAula = connection.prepareStatement("Select g.* FROM gruppo AS g JOIN gruppo_aula AS ga ON ga.id_gruppo=g.id WHERE ga.id_aula=? AND g.tipologia=polo");

            iGruppo = connection.prepareStatement("INSERT INTO gruppo (nome,tipologia) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            uGruppo = connection.prepareStatement("UPDATE gruppo SET nome=?,tipologia=? WHERE id=?");
            dGruppo = connection.prepareStatement("DELETE FROM gruppo WHERE id=?");

            iGruppoAula = connection.prepareStatement("INSERT INTO gruppo_aula (id_gruppo,id_aula) VALUES(?,?)", Statement.RETURN_GENERATED_KEYS);
            dGruppoAula = connection.prepareStatement("DELETE FROM gruppo_aula WHERE id=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing aula web data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {

            sGruppoById.close();
            sGruppoByName.close();

            sAllGruppi.close();
            sAllDepartments.close();
            sAllPolo.close();

            sGruppiAula.close();
            sDipartimentoAula.close();
            sPoloAula.close();

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
            g.setTipoGruppo(TipoGruppo.valueOf(rs.getString("tipologia")));
            g.setDescrizione(rs.getString("descrizione"));
        } catch (SQLException ex) {
            throw new DataException("Unable to create Group object form ResultSet", ex);
        }
        return g;
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
            throw new DataException("Unable to create connection object form ResultSet", ex);
        }
        return ga;
    }

    @Override //permette di ottenere il gruppo tramite l'id
    public Gruppo getGruppoById(int gruppo_key) throws DataException {
        Gruppo g = null;
        //prima vediamo se l'oggetto è già stato caricato
        if (dataLayer.getCache().has(Gruppo.class, gruppo_key)) {
            g = dataLayer.getCache().get(Gruppo.class, gruppo_key);
        } else {
            //altrimenti lo carichiamo dal database
            try {
                sGruppoById.setInt(1, gruppo_key);
                try (ResultSet rs = sGruppoById.executeQuery()) {
                    if (rs.next()) {
                        g = createGruppo(rs);
                        //e lo mettiamo anche nella cache

                        dataLayer.getCache().add(Gruppo.class, g);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Group by id", ex);
            }
        }
        return g;
    }

    @Override //permette di ottenere il gruppo tramite il nome
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
                throw new DataException("Unable to load Group by name", ex);
            }
        }
        return g;
    }

    @Override //permette di ottenere il dipartimento di un'aula
    public Gruppo getDipartimento(int id_aula) throws DataException {
        Gruppo g = null;
        try {
            sDipartimentoAula.setInt(1, id_aula);
            try (ResultSet rs = sDipartimentoAula.executeQuery()) {
                if (rs.next()) {
                    g = createGruppo(rs);
                    //e lo mettiamo anche nella cache
                    dataLayer.getCache().add(Gruppo.class, g);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Group", ex);

        }
        return g;
    }

    @Override //permette di ottenere il polo di un'aula
    public Gruppo getPolo(int id_aula) throws DataException {
        Gruppo g = null;
        try {
            sPoloAula.setInt(1, id_aula);
            try (ResultSet rs = sPoloAula.executeQuery()) {
                if (rs.next()) {
                    g = createGruppo(rs);
                    //e lo mettiamo anche nella cache
                    dataLayer.getCache().add(Gruppo.class, g);
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Group", ex);

        }
        return g;
    }

    @Override //permette di ottenere tutti i gruppi ai quali appartiene un'aula
    public List<Gruppo> getGruppo_Aula(int id_aula) throws DataException {
        List<Gruppo> g = new ArrayList();

        try (ResultSet rs = sGruppiAula.executeQuery()) {
            while (rs.next()) {
                g.add((Gruppo) getGruppoById(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Groups", ex);
        }
        return g;
    }

    @Override //permette di ottenere tutti i dipartimenti
    public List<Gruppo> getAllGruppi() throws DataException {
        List<Gruppo> result = new ArrayList();

        try (ResultSet rs = sAllGruppi.executeQuery()) {
            while (rs.next()) {
                result.add((Gruppo) getGruppoById(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Groups", ex);
        }
        return result;
    }

    @Override //permette di ottenere tutti i dipartimenti
    public List<Gruppo> getAllDipartimenti() throws DataException {
        List<Gruppo> result = new ArrayList();

        try (ResultSet rs = sAllDepartments.executeQuery()) {
            while (rs.next()) {
                result.add((Gruppo) getGruppoById(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Groups", ex);
        }
        return result;
    }

    @Override //permette ddi ottenere tutti i poli
    public List<Gruppo> getAllPoli() throws DataException {
        List<Gruppo> result = new ArrayList();

        try (ResultSet rs = sAllPolo.executeQuery()) {
            while (rs.next()) {
                result.add((Gruppo) getGruppoById(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Groups", ex);
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
                uGruppo.setInt(4,gruppo.getKey());
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

    public void storeGruppoAula(Gruppo_Aula gruppoaula) throws DataException {
        try {
            iGruppoAula.setInt(1, gruppoaula.getGruppo().getKey());
            iGruppoAula.setInt(2, gruppoaula.getAula().getKey());
            if (iGruppoAula.executeUpdate() == 1) {
                try (ResultSet keys = iGruppoAula.getGeneratedKeys()) {
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
            throw new DataException("Unable to store Group", ex);
        }
    }

    @Override
    public void deleteGruppo(Integer gruppo_key) throws DataException {
        try {
            dGruppo.setInt(1, gruppo_key);
        } catch (SQLException ex) {
            throw new DataException("Unable to delete Group by ID", ex);
        }
    }

    //i metodi che seguono non sono stati implementati, qualcuno potrebbe non servire
    @Override
    public void addGruppo_Aula(Integer gruppo_key, Integer id_aula) throws DataException {
        throw new UnsupportedOperationException("addGruppo_Aula not supported yet.");
    }

    @Override
    public void deleteAulaFromGruppo(Integer id_gruppo, Integer id_aula) throws DataException {
        throw new UnsupportedOperationException("deleteAulaFromGruppo not supported yet."); //devo implementare la logica
    }

    @Override
    public List<Aula> getAuleDisponibili(int id_gruppo) throws DataException {
        throw new UnsupportedOperationException("getAuleDisponibili Not supported yet."); //devo implementare la logica, potenzialmente starà in un file diverso
    }

}
