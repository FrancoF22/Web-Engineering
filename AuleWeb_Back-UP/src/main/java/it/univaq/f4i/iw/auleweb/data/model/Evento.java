/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;

/**
 *
 * @author franc
 */
public interface Evento extends DataItem<Integer>{
    
    String getNome();
    
    String getDescrizione();
    
    Tipologia getTipo();
    
    Professore getProfessore();
    
    Corso getCorso();
    
    Tipologia getTipologia();
    
    Calendario getCalendario();
    
    void setNome(String n);
    
    void setDescrizione(String descrizione);
    
    void setTipo(Tipologia t);
    
    void setProfessore(Professore p);
    
    void setCorso(Corso c);
    
    void setCalendario(Calendario calendario);
    
}
