/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;
import java.util.*;
import java.time.LocalTime;

/**
 *
 * @author franc
 */
public interface Calendario extends DataItem<Integer>{
    
    Aula getAula(); 
   
    Evento getEvento(); 
   
    Date getGiorno(); 
    
    LocalTime getOraInizio(); 
   
    LocalTime getOraFine();
    
    void setAula(Aula aula);
    
    void setEvento(Evento eventi);
    
    void setGiorno(Date giorno);

    void setOraInizio(LocalTime oraInizio);
    
    void setOraFine(LocalTime oraFine);
}
