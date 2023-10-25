/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Attrezzatura;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.*;

/**
 *
 * @author franc
 */
public interface AulaDAO {
    
    //creazione ed inizializzazione opportune implementazioni
    //delle interfacce del modello dati
    Aula createAula();
    
    Attrezzatura createAttrezzatura();

    Aula getAula(int id) throws DataException;
    
    Aula getAula(String nome) throws DataException;
    
    String getAttrezzatura(String nome) throws DataException;
    
    List<Aula> getAllAule() throws DataException;
    
    List<Aula> getAllAuleGI(int id_gruppo) throws DataException;
    
    void storeAula(Aula aula) throws DataException;
    
    void addAttrezzatura(String nome) throws DataException;

    void deleteAula(int id) throws DataException;
    
    void deleteAttrezzatura(String nome) throws DataException;

    ArrayList<String> gettAllAttrezzature() throws DataException;
    
    List<String> getLuoghi() throws DataException;
    
    List<Aula> getAuleFromLuogo(String luogo) throws DataException;
    
}
