/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;
import it.univaq.f4i.iw.framework.data.DataException;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author franc
 */
public interface EventoDAO {
    
    Evento createEvento();
    
    Calendario createCalendario();
    
    Evento getEvento(Integer key) throws DataException;

    List<Calendario> getCalendarioEvento(int evento_key) throws DataException;    //ritorna le ripetizioni dell'evento sul calendario
    
    List<Calendario> getEventiCorso(int corso)throws DataException;
    
    List<Calendario> getEventiAula(int id_aula,String campo_ricerca) throws DataException; //come parametro passo il c alendario in modo tale da avere l'unicità sull'evento (evento,giorno,ora_inizio,ora_fine...)

    Calendario getCalendario(int evento_key, Date giorno) throws DataException;

    List<Calendario> getEventiResponsabile(String email, String param) throws DataException;

    void deleteEvento(Calendario calendario, boolean singolo) throws DataException;
    
    void storeEvento(Calendario calendario) throws DataException;
    
    List<Calendario> getEventiSettimana(int id_aula, Date giorno) throws DataException;
    
    List<Calendario> getEventiPerGiorno(int id_aula, Date giorno) throws DataException;

    List<Calendario> getEventiCorsoSettimana(int id_corso, Date giorno) throws DataException;

    List<Calendario> getEventiAttuali(int id_dipartimento) throws DataException;

    List<Calendario> getAllEventi(int id_dipartimento) throws DataException;
    
}
