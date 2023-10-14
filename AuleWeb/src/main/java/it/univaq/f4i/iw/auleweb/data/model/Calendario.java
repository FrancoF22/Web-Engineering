/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;
import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author franc
 */
public interface Calendario extends DataItem<Integer>{
    
    Aula getAula(); 
   
    Evento getEvento(); 
    
    Ricorrenza getRicorrenza();
   
    Date getGiorno(); 
    
    Date getGiornoFine();
   
    Time getOraInizio(); 
   
    Time getOraFine();
    
    void setAula(Aula aula);
    
    void setEvento(Evento eventi);
    
    void setRicorrenza(Ricorrenza ricorrenza);
    
    void setGiorno(Date giorno);
    
    void setGiornoFine(Date giorno);

    void setOraInizio(Time oraInizio);
    
    void setOraFine(Time oraFine);
}
