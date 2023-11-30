package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.*;

/**
 *
 * @author franc
 */
public interface AulaDAO {
    
    Aula createAula();

    Aula getAula(int id) throws DataException;
    
    Aula getAula(String nome) throws DataException;
    
    List<Aula> getAllAule() throws DataException;
    
    List<Aula> getAula(Calendario calendario) throws DataException;
    
    List<Aula> getAllAuleGI(int id_gruppo) throws DataException;
    
    List<String> getAllAttrezzature() throws DataException;
    
    List<Aula> getAuleFromLuogo(String luogo) throws DataException;

    List<Aula> getAuleLibere() throws DataException;
    
    List<Aula> getAuleOccupate() throws DataException;
    
    void storeAula(Aula aula) throws DataException;

    void deleteAula(int id) throws DataException;

}
