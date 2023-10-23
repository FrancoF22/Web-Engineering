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
public interface Professore extends DataItem<Integer>{
    
    String getNome();
    
    String getCognome();
    
    void setNome(String c);

    void setCognome(String c);
    
}
