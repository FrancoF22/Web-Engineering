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
    
    String getNome();
    
    String getDescrizione();
    
    TipoGruppo getTipoGruppo();
    
    void setNome(String n);
    
    void setDescrizione(String d);
    
    void setTipoGruppo(TipoGruppo tg);
}
