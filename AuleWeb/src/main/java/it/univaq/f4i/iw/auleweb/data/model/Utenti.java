/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

/**
 *
 * @author franc
 */
public interface Utenti {
    
    Integer getId();
    
    String getNome();
    
    String getCognome();
    
    String getEmail();
    
    String getPassword();
    
    void setId(int id);
    
    void setNome(String n);
    
    void setCome(String c);
    
    void setEmail(String email);
    
    void setPassword(String password);
}
