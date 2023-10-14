/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;

/**
 *
 * @author Syd
 */
public interface Corso extends DataItem<Integer>{
    
    String getNome();

    String getDescrizione();
    
    void setNome(String n);
    
    void setDescrizione(String d);
    
}
