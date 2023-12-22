package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.proxy.AulaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import java.sql.*;
import java.util.*;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;

/**
 *
 * @author Syd
 */
public class AulaDAO_MySQL extends DAO implements AulaDAO {

    public AulaDAO_MySQL(DataLayer d) {
        super(d);
    }
    private PreparedStatement sAulaById, sAulaByNome, sAuleByGruppoN, sAuleByGruppo, sAuleByLuogo;
    private PreparedStatement sAllAule, sUsedAule, sAuleCalendario, sAllAttrezzature;
    private PreparedStatement iAula, uAula, dAula;

    @Override

    public void init() throws DataException {
        try {
            super.init();

            //procedure di ricerca delle aule
            sAulaById = connection.prepareStatement("SELECT * FROM aula WHERE id=?");
            sAulaByNome = connection.prepareStatement("SELECT aula.* FROM aula WHERE nome=?");
            sAuleByGruppoN = connection.prepareStatement("SELECT aula.* FROM aula JOIN gruppo_aula AS ga ON ga.id_aula = aula.id JOIN gruppo AS g ON ga.id_gruppo = g.id WHERE g.nome=?");
            sAuleByGruppo = connection.prepareStatement("SELECT aula.* FROM aula JOIN gruppo_aula AS ga ON ga.id_aula = aula.id WHERE ga.id_gruppo=?");
            sAuleByLuogo = connection.prepareStatement("SELECT aula.* FROM aula WHERE aula.luogo = ?");

            sAllAule = connection.prepareStatement("SELECT * FROM aula");

            sUsedAule = connection.prepareStatement("SELECT DISTINCT id_aula FROM calendario");
            sAuleCalendario = connection.prepareStatement("SELECT DISTINCT id_aula FROM calendario WHERE calendario.id_evento=?");
            sAllAttrezzature = connection.prepareStatement("SELECT COLUMN_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'aula' AND COLUMN_NAME = 'attrezzatura'");
            //procedure di inserimento, aggiornamento e eliminazione delle aule
            iAula = connection.prepareStatement("INSERT INTO aula (nome, capienza, prese_elettriche, prese_rete, attrezzatura, nota, luogo, edificio, piano, id_professore) VALUES (?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uAula = connection.prepareStatement("UPDATE aula SET nome=?, capienza=?, prese_elettriche=?, prese_rete=?, attrezzatura=?, nota=?, luogo=?, edificio=?, piano=?, id_professore=? WHERE id=?");
            dAula = connection.prepareStatement("DELETE FROM aula WHERE id=?");

        } catch (SQLException ex) {
            throw new DataException("Error initializing newspaper data layer", ex);
        }
    }

    @Override
    public void destroy() throws DataException {
        //chiusura dei preprared statement
        try {
            sAulaById.close();
            sAulaByNome.close();
            sAuleByGruppoN.close();
            sAuleByGruppo.close();
            sAuleByLuogo.close();

            sAllAule.close();
            sUsedAule.close();
            sAuleCalendario.close();
            sAllAttrezzature.close();

            iAula.close();
            uAula.close();
            dAula.close();
        } catch (SQLException ex) {
            //
        }
        super.destroy();
    }

    @Override
    public Aula createAula() {
        return new AulaProxy(getDataLayer());
    }

    //helper
    private AulaProxy createAula(ResultSet rs) throws DataException {
        AulaProxy a = (AulaProxy) createAula();
        try {
            a.setKey(rs.getInt("id"));
            a.setNome(rs.getString("nome"));
            a.setCapienza(rs.getInt("capienza"));
            a.setPreseElettriche(rs.getInt("prese_elettriche"));
            a.setPreseRete(rs.getInt("prese_rete"));

            String attrezzature = rs.getString("attrezzatura");
            String[] attArr = attrezzature.split(",");
            List<String> attList = new ArrayList<>(Arrays.asList(attArr));
            a.setAttrezzature(attList);

            a.setNota(rs.getString("nota"));
            a.setLuogo(rs.getString("luogo"));
            a.setEdificio(rs.getString("edificio"));
            a.setPiano(rs.getString("piano"));
            a.setProfessoreKey(rs.getInt("id_professore"));

        } catch (SQLException ex) {
            throw new DataException("Unable to create aula object form ResultSet", ex);
        }
        return a;
    }

    @Override //serve a ottenere un'aula a partire dall'id
    public Aula getAula(int id_aula) throws DataException {
        Aula a = null;
        if (dataLayer.getCache().has(Aula.class, id_aula)) {
            a = dataLayer.getCache().get(Aula.class, id_aula);
        } else {
            try {
                sAulaById.setInt(1, id_aula);
                try (ResultSet rs = sAulaById.executeQuery()) {
                    if (rs.next()) {
                        a = createAula(rs);
                        //e lo mettiamo anche nella cache
                        dataLayer.getCache().add(Aula.class, a);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Aula by ID", ex);
            }
        }
        return a;
    }

    @Override //serve a ottenere un'aula a partire dal nome
    public Aula getAula(String nome) throws DataException {
        Aula a = null;
        if (dataLayer.getCache().has(Aula.class, nome)) {
            a = dataLayer.getCache().get(Aula.class, nome);
        } else {
            try {
                sAulaByNome.setString(1, nome);
                try (ResultSet rs = sAulaByNome.executeQuery()) {
                    if (rs.next()) {
                        a = createAula(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Aula.class, a);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Aula by ID", ex);
            }
        }
        return a;
    }

    @Override //permette di ottenere tutte le aule
    public List<Aula> getAllAule() throws DataException {
        List<Aula> result = new ArrayList();

        try (ResultSet rs = sAllAule.executeQuery()) {
            while (rs.next()) {
                result.add((Aula) getAula(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule", ex);
        }
        return result;
    }

    //serve a ottenere la lista delle aule appartenenti a un determinato gruppo (serve il nome del gruppo)
    public List<Aula> getAllAuleGN(String gruppo_nome) throws DataException {
        List<Aula> result = new ArrayList();

        try (ResultSet rs = sAuleByGruppoN.executeQuery(gruppo_nome)) {
            while (rs.next()) {
                result.add((Aula) getAula(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule", ex);
        }
        return result;
    }

    @Override //serve a ottenere la lista delle aule appartenenti a un determinato gruppo (serve l'id del gruppo)
    public List<Aula> getAllAuleGI(int id_gruppo) throws DataException {
        List<Aula> result = new ArrayList();
        try {
            sAuleByGruppo.setInt(1, id_gruppo);
            try (ResultSet rs = sAuleByGruppo.executeQuery()) {
                while (rs.next()) {
                    result.add((Aula) getAula(rs.getInt("id")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule", ex);
        }
        return result;
    }

    @Override //serve a ottenere la lista delle aule che sono situate nello stesso luogo
    public List<Aula> getAuleFromLuogo(String luogo) throws DataException {
        List<Aula> result = new ArrayList();

        try (ResultSet rs = sAuleByLuogo.executeQuery(luogo)) {
            while (rs.next()) {
                result.add((Aula) getAula(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule", ex);
        }
        return result;
    }

    @Override
    public void deleteAula(int id_aula) throws DataException {
    try {
        // Elimina i record correlati da gruppo_aula
        PreparedStatement deleteGruppoAula = connection.prepareStatement("DELETE FROM gruppo_aula WHERE id_aula=?");
        deleteGruppoAula.setInt(1, id_aula);
        deleteGruppoAula.executeUpdate();
        deleteGruppoAula.close(); // Chiudi il PreparedStatement

        // Elimina la riga dalla tabella 'aula'
        PreparedStatement deleteAula = connection.prepareStatement("DELETE FROM aula WHERE id=?");
        deleteAula.setInt(1, id_aula);
        int rowsAffected = deleteAula.executeUpdate(); // Esegui la query di eliminazione
        deleteAula.close(); // Chiudi il PreparedStatement
        
        if (rowsAffected == 0) {
            // Gestione nel caso in cui nessuna riga sia stata eliminata
        }
    } catch (SQLException ex) {
        throw new DataException("Unable to delete Aula by ID", ex);
    }
}

    @Override //serve per ottenere una lista di attrezzature
    public List<String> getAllAttrezzature() throws DataException {
        List<String> result = new ArrayList();
        try {
            try (ResultSet rs = sAllAttrezzature.executeQuery()) {
                if (rs.next()) {
                    String tipo = rs.getString("COLUMN_TYPE");
                    String[] values = tipo.replaceAll("set|\\(|\\)", "").split(",");
                    for (String value : values) {
                        result.add(value.trim());
                    }
                } else {
                    System.out.println("rs e vuoto");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Attrezzature", ex);
        }
        return result;
    }

    @Override //serve per ottenere una lista di aule disponibili
    public List<Aula> getAula(Calendario calendario) throws DataException {
        List<Aula> result = new ArrayList();
        try {
            sAuleCalendario.setInt(1, calendario.getEvento().getKey());
            try (ResultSet rs = sAuleCalendario.executeQuery()) {
                while (rs.next()) {
                    result.add((Aula) getAula(rs.getInt("id_aula")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule with Calendar", ex);
        }
        return result;
    }

    @Override //serve per ottenere una lista di aule disponibili
    public List<Aula> getAuleLibere() throws DataException {
        List<Aula> result = new ArrayList<>();
        List<Aula> used = new ArrayList<>();
        List<Aula> all = this.getAllAule();
        try {
            try (ResultSet rs = sUsedAule.executeQuery()) {
                while (rs.next()) {
                    used.add((Aula) getAula(rs.getInt("id_aula")));
                }
            }
            for (Aula elemento : all) {
                if (!used.contains(elemento)) {
                    result.add(elemento);
                    System.out.println("### unused: "+elemento+" ###");
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule", ex);
        }
        return result;
    }

    @Override //serve per ottenere una lista di aule disponibili
    public List<Aula> getAuleOccupate() throws DataException {
        List<Aula> result = new ArrayList<>();
        try {
            try (ResultSet rs = sUsedAule.executeQuery()) {
                while (rs.next()) {
                    result.add((Aula) getAula(rs.getInt("id_aula")));
                }
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule", ex);
        }
        return result;
    }
    
    @Override //serve per salvare modifiche ad un'aula nel database o crearne una nuova
    public void storeAula(Aula aula) throws DataException {
        try {
            if (aula.getKey() != null && aula.getKey() > 0) {
                if (aula instanceof DataItemProxy && !((DataItemProxy) aula).isModified()) {
                    return;
                }
                uAula.setString(1, aula.getNome());
                uAula.setInt(2, aula.getCapienza());
                uAula.setInt(3, aula.getPreseElettriche());
                uAula.setInt(4, aula.getPreseRete());
                String attStr = String.join(",", aula.getAttrezzature());
                uAula.setString(5, attStr);
                uAula.setString(6, aula.getNota());
                uAula.setString(7, aula.getLuogo());
                uAula.setString(8, aula.getEdificio());
                uAula.setString(9, aula.getPiano());
                if (aula.getProfessore() != null) {
                    uAula.setInt(10, aula.getProfessore().getKey());
                } else {
                    uAula.setNull(10, java.sql.Types.INTEGER);
                }
                uAula.setInt(11, aula.getKey());
                if (uAula.executeUpdate() == 0) {
                    throw new OptimisticLockException(aula);
                } else {
                    //idk cosa deve fare
                }
            } else { //insert
                iAula.setString(1, aula.getNome());
                iAula.setInt(2, aula.getCapienza());
                iAula.setInt(3, aula.getPreseElettriche());
                iAula.setInt(4, aula.getPreseRete());
                String attStr = String.join(",", aula.getAttrezzature());
                System.out.println("### " + attStr + " ###");
                iAula.setString(5, attStr);
                iAula.setString(6, aula.getNota());
                iAula.setString(7, aula.getLuogo());
                iAula.setString(8, aula.getEdificio());
                iAula.setString(9, aula.getPiano());
                if (aula.getProfessore() != null) {
                    iAula.setInt(10, aula.getProfessore().getKey());
                } else {
                    iAula.setNull(10, java.sql.Types.INTEGER);
                }
                if (iAula.executeUpdate() == 1) {

                    try (ResultSet keys = iAula.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            aula.setKey(key);
                            dataLayer.getCache().add(Aula.class, aula);
                        }
                    }
                }
            }
            if (aula instanceof DataItemProxy) {
                ((DataItemProxy) aula).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store aula", ex);
        }
    }

}
