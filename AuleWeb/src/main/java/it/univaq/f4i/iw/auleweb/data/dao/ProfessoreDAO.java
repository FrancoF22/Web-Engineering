/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.data.dao;

import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.framework.data.DataException;
import java.util.List;

/**
 *
 * @author franc
 */
public interface ProfessoreDAO {
    
    Professore createProfessore();
    
    Professore getProfessoreById(int professore_key) throws DataException;
    
    List<Professore> getProfessori() throws DataException;
    
    void storeProfessore(Professore Professore) throws DataException;
    
    void deleteProfessore(Integer id_prof) throws DataException;
}
