/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

/**
 *
 * @author franc
 */
public interface Eventi {
    
    String getNome();
    
    String getDescrizione();
    
    Tipologia getTipo();
    
    Responsabile getResponsabile();
    
    Gruppo getCorso();
    
    void setNome(String n);
    
    void setDescrizione(String descrizione);
    
    void setTipo(Tipologia t);
    
    void setResponsabile(Responsabile r);
    
    void setGruppo(Gruppo g);
    
}
