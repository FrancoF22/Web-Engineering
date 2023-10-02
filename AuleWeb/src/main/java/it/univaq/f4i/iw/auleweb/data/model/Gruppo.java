/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

/**
 *
 * @author franc
 */
public interface Gruppo {
    
    Integer getId();
    
    String getNome();
    
    String getDescrizione();
    
    TipoGruppo getTipoGruppo();
    
    void setId(int id);
    
    void setNome(String n);
    
    void setDescrizione(String d);
    
    void setTipoGruppo(TipoGruppo tg);
}
