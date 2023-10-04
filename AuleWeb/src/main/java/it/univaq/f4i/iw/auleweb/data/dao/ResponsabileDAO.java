/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Responsabile;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;

/**
 *
 * @author franc
 */
public interface ResponsabileDAO {
    
    Responsabile createResponsabile();

    Responsabile getResponsabile(String email) throws DataException;

    List<?> getAllResponsabili() throws DataException; //restituisce i dati dell'utente
    
    void storeResponsabile(Responsabile responsabile, String nuovaMail, Integer id_utente) throws DataException;
    
    void deleteResponsabile(Responsabile responsabile) throws DataException;
    
}
