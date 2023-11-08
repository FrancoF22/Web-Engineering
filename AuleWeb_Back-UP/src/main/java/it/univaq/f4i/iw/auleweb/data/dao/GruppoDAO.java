/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo_Aula;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;

/**
 *
 * @author franc
 */
public interface GruppoDAO {
    
    Gruppo createGruppo();
    
    Gruppo_Aula createGruppoAula();

    void addGruppo_Aula(Integer gruppo_key, Integer id_aula) throws DataException;

    List<Gruppo> getGruppo_Aula(int id_aula) throws DataException;

    Gruppo getGruppoById(int gruppo_key) throws DataException;
    
    Gruppo getGruppo(String nome) throws DataException;
    
    Gruppo getDipartimento(int id_aula) throws DataException;
    
    Gruppo getPolo(int id_aula) throws DataException;
    
    List<Gruppo> getAllGruppi() throws DataException;
    
    List<Gruppo> getAllDipartimenti() throws DataException;
    
    List<Gruppo> getAllPoli() throws DataException;
    
    void deleteGruppo(Integer gruppo_key) throws DataException;
    
    void deleteAulaFromGruppo(Integer id_gruppo, Integer id_aula) throws DataException;

    void storeGruppo(Gruppo gruppo) throws DataException;
    
    List<Aula> getAuleDisponibili(int id_gruppo) throws DataException;

}
