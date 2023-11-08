/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
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
    
    void deleteCorso(Integer key) throws DataException;
    
    void storeCorso(Corso corso) throws DataException; 
}
