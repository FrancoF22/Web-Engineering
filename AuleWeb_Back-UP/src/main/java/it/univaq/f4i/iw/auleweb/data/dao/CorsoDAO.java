package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;

/**
 *
 * @author Syd
 */
public interface CorsoDAO {
    
    Corso createCorso();
    
    Corso getCorsoById(Integer key) throws DataException;
    
    List<Corso> getAllCorsi() throws DataException;
    
    List<Corso> getCorsiDipartimento(int id_dipartimento) throws DataException;
    
    void deleteCorso(Integer key) throws DataException;
    
    void storeCorso(Corso corso) throws DataException; 
}
