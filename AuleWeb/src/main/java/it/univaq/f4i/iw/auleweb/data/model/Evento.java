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
    
    Responsabile getResponsabile();
    
    Gruppo getCorso();
    
    Tipologia getTipologia(); 
    
    void setNome(String n);
    
    void setDescrizione(String descrizione);
    
    void setTipo(Tipologia t);
    
    void setResponsabile(Responsabile r);
    
    void setGruppo(Gruppo g);
    
    void setCorso(Gruppo c);
    
}
