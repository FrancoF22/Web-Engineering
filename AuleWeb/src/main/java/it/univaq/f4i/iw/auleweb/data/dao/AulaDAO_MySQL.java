package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Attrezzatura;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.proxy.AulaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import it.univaq.f4i.iw.framework.data.DataLayer;
import it.univaq.f4i.iw.framework.data.OptimisticLockException;
import java.sql.Array;
import java.util.Set;

/**
 *
 * @author Syd
 */
public class AulaDAO_MySQL extends DAO implements AulaDAO {
    
    public AulaDAO_MySQL(DataLayer d) {
        super(d);
    }
    private PreparedStatement sAulaById, sAulaByNome, sAuleByGruppo, sAuleByLuogo;
    private PreparedStatement iAula, uAula, dAula;
    
    @Override
    
    public void init() throws DataException {
        try{
            super.init();
            
            //procedure di ricerca delle aule
            
            sAulaById = connection.prepareStatement("SELECT * FROM aula WHERE id=?");
            sAulaByNome = connection.prepareStatement("SELECT aula.* FROM aula WHERE nome=?");
            sAuleByGruppo = connection.prepareStatement("SELECT aula.* FROM aula JOIN gruppo_aula AS ga ON ga.id_aula = aula.id JOIN gruppo AS g ON ga.id_gruppo = g.id WHERE g.nome=?");
            sAuleByLuogo = connection.prepareStatement("SELECT aula.* FROM aula WHERE aula.luogo = ?");
            //procedure di inserimento, aggiornamento e eliminazione delle aule
            
            iAula = connection.prepareStatement("INSERT INTO aula (nome, capienza, prese_elettriche, prese_rete, attrezzatura, nota, luogo, edificio, piano, id_responsabile) VALUES (?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            uAula = connection.prepareStatement("UPDATE aula SET nome=?, capienza=?, prese_elettriche=?, prese_rete=?, attrezzatura=?, nota=?, luogo=?, edificio=?, piano=?, id_responsabile=? WHERE id=?");
            dAula = connection.prepareStatement("DELETE FROM aula WHERE id=?");
            
        } catch (SQLException ex) {
            throw new DataException("Error initializing newspaper data layer", ex);
        }
    }
    
    @Override
    public void destroy() throws DataException {
        //chiusura dei preprared statement
        try{
            sAulaById.close();
            sAulaByNome.close();
            sAuleByGruppo.close();
            sAuleByLuogo.close();
            
            iAula.close();
            uAula.close();
            dAula.close();
        }catch (SQLException ex) {
            //
        }
        super.destroy();
    }
    
    @Override
    public Aula createAula() {
        return new AulaProxy(getDataLayer());
    }
    
    //helper
    // è incompleta e ci sono incongruenze con attrezzature e responabile
    private AulaProxy createAula(ResultSet rs) throws DataException {
        AulaProxy a = (AulaProxy) createAula();
        try {
            a.setKey(rs.getInt("id"));
            a.setNome(rs.getString("nome"));
            a.setCapienza(rs.getInt("capienza"));
            a.setPreseElettriche(rs.getInt("prese_elettriche"));
            a.setPreseRete(rs.getInt("prese_rete"));
            a.setAttrezzature((Set<Attrezzatura>) rs.getArray("attrezzatura"));
            a.setNota(rs.getString("nota"));
            a.setLuogo(rs.getString("luogo"));
            a.setEdificio(rs.getString("edificio"));
            a.setPiano(rs.getString("piano"));
            a.setResponsabileKey(rs.getInt("id_responsabile"));
            
        }catch (SQLException ex) {
            throw new DataException("Unable to create article object form ResultSet", ex);
        }
        return a;
    }
    
    @Override
    public Aula getAula(int id_aula) throws DataException{
        Aula a = null;
        if (dataLayer.getCache().has(Aula.class, id_aula)) {
            a = dataLayer.getCache().get(Aula.class, id_aula);
        }else{
            try{
                sAulaById.setInt(1, id_aula);
                try (ResultSet rs = sAulaById.executeQuery()) {
                    if (rs.next()) {
                        a = createAula(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Aula.class, a);
                    }
                }
            }catch (SQLException ex) {
                throw new DataException("Unable to load Aula by ID", ex);
            }
        }
        return a;
    }
    
     @Override
    public Aula getAula(String nome) throws DataException {
        Aula a = null;
        if (dataLayer.getCache().has(Aula.class, nome)) {
            a = dataLayer.getCache().get(Aula.class, nome);
        }else{
            try{
                try (ResultSet rs = sAulaByNome.executeQuery(nome)) {
                    if (rs.next()) {
                        a = createAula(rs);
                        //e lo mettiamo anche nella cache
                        //and put it also in the cache
                        dataLayer.getCache().add(Aula.class, a);
                    }
                }
            }catch (SQLException ex) {
                throw new DataException("Unable to load Aula by ID", ex);
            }
        }
        return a;
    }

    public List<Aula> getAllAule(String gruppo) throws DataException {
        List<Aula> result = new ArrayList();
        
        try (ResultSet rs = sAuleByGruppo.executeQuery(gruppo)) {
             while (rs.next()) {
                result.add((Aula) getAula(rs.getInt("id")));
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule", ex);
        }
        return result;
    }
    
    @Override
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
    public void storeAula(Aula aula) throws DataException {
         try {
            if (aula.getKey() != null && aula.getKey() > 0) { //update
                //non facciamo nulla se l'oggetto è un proxy e indica di non aver subito modifiche
                if (aula instanceof DataItemProxy && !((DataItemProxy) aula).isModified()) {
                    return;
                }
                uAula.setString(1, aula.getNome());
                uAula.setInt(2, aula.getCapienza());
                uAula.setInt(3, aula.getPreseElettriche());
                uAula.setInt(4, aula.getPreseRete());
                uAula.setArray(5, (Array) aula.getAttrezzature()); //non so quanto è corretto
                uAula.setString(6, aula.getNota());
                uAula.setString(7, aula.getLuogo());
                uAula.setString(8, aula.getEdificio());
                uAula.setString(9, aula.getPiano());
                if (aula.getResponsabile()!= null) {
                    uAula.setInt(10, aula.getResponsabile().getKey());
                } else {
                    uAula.setNull(10, java.sql.Types.INTEGER);
                }

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
                iAula.setArray(5, (Array) aula.getAttrezzature()); //non so quanto è corretto
                iAula.setString(6, aula.getNota());
                iAula.setString(7, aula.getLuogo());
                iAula.setString(8, aula.getEdificio());
                iAula.setString(9, aula.getPiano());
                if (aula.getResponsabile()!= null) {
                    iAula.setInt(10, aula.getResponsabile().getKey());
                } else {
                    iAula.setNull(10, java.sql.Types.INTEGER);
                }
                if (iAula.executeUpdate() == 1) {
                    //per leggere la chiave generata dal database
                    //per il record appena inserito, usiamo il metodo
                    //getGeneratedKeys sullo statement.
                    try (ResultSet keys = iAula.getGeneratedKeys()) {
                        //il valore restituito è un ResultSet con un record
                        //per ciascuna chiave generata (uno solo nel nostro caso)
                        if (keys.next()) {
                            //i campi del record sono le componenti della chiave
                            //(nel nostro caso, un solo intero)
                            int key = keys.getInt(1);
                            //aggiornaimo la chiave in caso di inserimento
                            aula.setKey(key);
                            //inseriamo il nuovo oggetto nella cache
                            dataLayer.getCache().add(Aula.class, aula);
                        }
                    }
                }
            }

//            //se possibile, restituiamo l'oggetto appena inserito RICARICATO
//            //dal database tramite le API del modello. In tal
//            //modo terremo conto di ogni modifica apportata
//            //durante la fase di inserimento
//            //if possible, we return the just-inserted object RELOADED from the
//            //database through our API. In this way, the resulting
//            //object will ambed any data correction performed by
//            //the DBMS
//            if (key > 0) {
//                article.copyFrom(getArticle(key));
//            }
            //se abbiamo un proxy, resettiamo il suo attributo dirty
            //if we have a proxy, reset its dirty attribute
            if (aula instanceof DataItemProxy) {
                ((DataItemProxy) aula).setModified(false);
            }
        } catch (SQLException | OptimisticLockException ex) {
            throw new DataException("Unable to store aula", ex);
        }
    }
    
    @Override
    public void deleteAula(int id) throws DataException {
        
    }
    
    
    //i metodi di seguito vanno modificati, oppure non servono
    
    @Override
    public Attrezzatura createAttrezzatura() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public Attrezzatura getAttrezzatura(String nome) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    

    @Override
    public void addAttrezzatura(String nome) throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void deleteAttrezzatura(String nome) throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<Attrezzatura> gettAllAttrezzature() throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<String> getLuoghi() throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }    
    
    //??
   @Override
    public List<Aula> getAllAule() throws DataException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
