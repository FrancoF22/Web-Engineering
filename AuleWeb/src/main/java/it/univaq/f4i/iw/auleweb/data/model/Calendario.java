/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author franc
 */
public interface Calendario {
    
    Aula getAula(); 
   
    Eventi getEvento(); 
    
    Ricorrenza getRicorrenza();
   
    Date getGiorno(); 
    
    Date getGiornoFine();
   
    Time getOraInizio(); 
   
    Time getOraFine(); 
    
    int getRipetizioni();
    
    void setAula(Aula aula);
    
    void setEvento(Eventi eventi);
    
    void setRicorrenza(Ricorrenza ricorrenza);
    
    void setGiorno(Date giorno);
    
    void setGiornoFine(Date giorno);

    void setOraInizio(Time oraInizio);
    
    void setOraFine(Time oraFine); 
    
    void setRipetizioni(int ripetizioni); 
}
