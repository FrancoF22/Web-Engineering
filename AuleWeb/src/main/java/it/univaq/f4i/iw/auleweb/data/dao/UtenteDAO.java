/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;

/**
 *
 * @author franc
 */
public interface UtenteDAO {
    
    Utente createUtente();

    Utente getUtente(int user_key) throws DataException;
    
    Utente getUtenteByEmail(String username) throws DataException;
    
    List<Utente> getAllResponsabili() throws DataException;

    void storeUtente(Utente user) throws DataException;
    
    void deleteUtente(Integer id) throws DataException;

    public void storeUtente(Utente responsabile, String parameter, Integer key);

    public Utente getResponsabile(String parameter);
    
}
