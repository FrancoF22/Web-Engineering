/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

/**
 *
 * @author franc
 */
public interface Responsabile {
    
    Integer getId();
    
    String getNome();
    
    String getCognome();
    
    String getEmail();
    
    void setId(int id);
    
    void setNoem(String n);
    
    void setCognome(String cognome);
    
    void setEmail(String email);
    
}
