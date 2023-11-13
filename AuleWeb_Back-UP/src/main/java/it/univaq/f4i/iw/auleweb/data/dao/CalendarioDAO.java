/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.framework.data.DataException;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author franc
 */
public interface CalendarioDAO {
    
    Calendario createCalendario();
    
    Calendario getCAlendario(int id) throws DataException;
    
    List<Calendario> getCalendari() throws DataException;
    
    List<Calendario> getCalendarioEvento(int evento_key) throws DataException;    //ritorna le ripetizioni dell'evento sul calendario
    
    List<Calendario> getEventiCorsoSettimana(int corso)throws DataException;

    List<Calendario> getEventiAttuali(int id_dipartimento) throws DataException;

    List<Calendario> getEventiCorso(int id_corso, Date data) throws DataException;
    
    List<Calendario> getAllEventiAula(int id_aula) throws DataException;

    List<Calendario> getEventiAula(int id_aula, Date date) throws DataException;
    
    Calendario getCalendario(int evento_key, Date giorno) throws DataException;

    List<Calendario> getEventiAulaGiorno(int id_aula, Date date) throws DataException;
    
    void storeCalendario(Calendario calendario) throws DataException;
    
}
