/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.model;

import it.univaq.f4i.iw.framework.data.DataItem;
import java.util.*;

/**
 *
 * @author franc
 */
public interface Aula extends DataItem<Integer>{
    
    String getNome();
    
    Integer getCapienza();
    
    Integer getPreseElettriche();
    
    Integer getPreseRete(); 
    
    List<String> getAttrezzature();
    
    String getNota();
    
    String getLuogo();
    
    String getEdificio();
    
    String getPiano();
  
    Professore getProfessore();
    
    Calendario getCalendario();
    
    void setNome(String nome);
    
    void setCapienza(Integer capienza);
    
    void setPreseElettriche(Integer preseE);
    
    void setPreseRete(Integer preseR); 
    
    void setAttrezzature(List<String> a);
    
    void setNota(String nome);
    
    void setLuogo(String luogo);
    
    void setEdificio(String edificio);
    
    void setPiano(String piano);
  
    void setProfessore(Professore resp);
    
    void setCalendario(Calendario calendario);
    
}
