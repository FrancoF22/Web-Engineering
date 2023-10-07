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

/**
 *
 * @author Syd
 */
public class AulaDAO_MySQL extends DAO implements AulaDAO {
    
    public AulaDAO_MySQL(DataLayer d) {
        super(d);
    }
    private PreparedStatement sAulaById, sAuleByGruppo, sAuleByPolo;
    private PreparedStatement iAula, uAula, dAula;
    
    @Override
    
    public void init() throws DataException {
        try{
            super.init();
            
            //procedure di ricerca delle aule
            
            sAulaById = connection.prepareStatement("SELECT * FROM aula WHERE id=?");
            sAuleByGruppo = connection.prepareStatement("SELECT aula.* FROM aula JOIN gruppo_aula AS ga ON ga.id_aula = aula.id JOIN gruppo AS g ON ga.id_gruppo = g.id WHERE g.nome=?");
            sAuleByPolo = connection.prepareStatement("SELECT aula.* FROM aula JOIN gruppo_aula AS ga JOIN gruppo AS g WHERE id_gruppo=? AND g.tipologia = 'polo'");
            
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
            sAuleByGruppo.close();
            sAuleByPolo.close();
            
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
            //a.setAttrezzature(rs.getAttrezzature("attrezzatura"));
            a.setNota(rs.getString("nota"));
            a.setLuogo(rs.getString("luogo"));
            a.setEdificio(rs.getString("edificio"));
            a.setPiano(rs.getString("piano"));
            //a.setResponsabile(rs.getObject("responsabile", responsabile));
            
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
    
    //i metodi di seguito vanno modificati
    
    
    //serve questo metodo che segue?
    @Override
    public Attrezzatura createAttrezzatura() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public Attrezzatura getAttrezzatura(String nome) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    @Override
    public List<Aula> getAllAule(String) throws DataException {
        List<Aula> result = new ArrayList();
        try (ResultSet rs = sAuleByGruppo.) {
            
        } catch (SQLException ex) {
            throw new DataException("Unable to load Aule", ex);
        }
    }

    @Override
    public void storeAula(Aula aula) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void addAttrezzatura(String nome) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAula(int id) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void deleteAttrezzatura(String nome) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Attrezzatura> gettAllAttrezzature() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<String> getLuoghi() throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Aula> getAuleFromLuogo(String luogo) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    //??
    @Override
    public Aula getAula(String nome) throws DataException {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
