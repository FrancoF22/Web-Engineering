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
public interface Utente extends DataItem<Integer>{
    
    String getNome();
    
    String getCognome();
    
    String getEmail();
    
    String getPassword();
    
    void setNome(String n);
    
    void setCognome(String c);
    
    void setEmail(String email);
    
    void setPassword(String password);
}
