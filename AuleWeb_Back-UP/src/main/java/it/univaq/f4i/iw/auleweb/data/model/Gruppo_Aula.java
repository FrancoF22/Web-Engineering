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
public interface Gruppo_Aula extends DataItem<Integer>{
    
    Gruppo getGruppo();
    
    Aula getAula();
    
    void setGruppo(Gruppo g);
    
    void setAula(Aula a);
    
}
