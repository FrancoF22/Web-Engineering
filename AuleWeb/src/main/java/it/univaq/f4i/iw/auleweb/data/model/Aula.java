/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

import java.util.List;
import java.util.Set;

/**
 *
 * @author franc
 */
public interface Aula {
    
    String getNome();
    
    Integer getCapienza();
    
    Integer getPreseElettriche();
    
    Integer getPreseRete(); 
    
    List<Attrezzatura> getAttrezzature();
    
    String getNota();
    
    String getLuogo();
    
    String getEdificio();
    
    String getPiano();
  
    Responsabile getIdResponsabile();
    
    void setNome(String nome);
    
    void setCapienza(Integer capienza);
    
    void setPreseElettriche(Integer preseE);
    
    void setPreseRete(Integer preseR); 
    
    void setAttrezzature(Set<Attrezzatura> attrezzature);
    
    void setNota(String nome);
    
    void setLuogo(String luogo);
    
    void setEdificio(String edificio);
    
    void setPiano(String piano);
  
    void setIdResponsabile(Responsabile resp);
}
