/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.framework.data.DataException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

/**
 *
 * @author franc
 */
public interface EventoDAO {
    
    Evento createEvento();
    
    Calendario createCalendario();
    
    Evento getEvento(int id) throws DataException;
    
    List<Calendario> getCalendarioEvento(int evento_key) throws DataException;    //ritorna le ripetizioni dell'evento sul calendario

    List<Evento> getEventoGiornoOra(java.util.Date g, java.time.LocalTime t, int aula_key) throws DataException;
    
    List<Evento> getAllProssimiEventi(int id_gruppo) throws DataException;

    List<Calendario> getEventiAttuali(int id_dipartimento) throws DataException;

    List<Evento> getAllEventi() throws DataException;
    
    List<Calendario> getEventiAulaGiorno(int id_aula, java.util.Date d) throws DataException;

    List<Calendario> getEventiAulaSettimana(int id_aula, java.util.Date d) throws DataException;
    
    List<Calendario> getEventiAulaMese(int aulaId, java.util.Date d) throws DataException;
    
    List<Calendario> getAllEventiAula(int id_aula) throws DataException;

    List<Calendario> getEventiCorsoSettimana(int id_corso, java.util.Date data) throws DataException;
    
    Calendario getCalendario(int evento_key, java.util.Date d) throws DataException;
    
    void deleteEvento(int id_evento) throws DataException;
    
    void storeEvento(Evento evento)throws DataException;
    
    void storeCalendario(Calendario calendario) throws DataException;

}
