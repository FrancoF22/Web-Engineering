/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo_Aula;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Lenovo
 */
public class InfoEvento extends AuleWebBaseController  {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        try { 
            action_default(request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException  {
      
        try {
            int id = SecurityHelpers.checkNumeric(request.getParameter("evento_key"));
            Date giorno = Date.valueOf(request.getParameter("giorno"));
            TemplateResult res = new TemplateResult(getServletContext());
            Calendario calendarioEvento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getCalendario(id, giorno);
            Gruppo_Aula grupo_aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo_Aula(calendarioEvento.getAula().getKey()); //intendi cercare l'aula in cui si situa un evento? -ema
            request.setAttribute("dipartimento", grupo_aula.getGruppo().getDipartimento());
            request.setAttribute("InfoEvento", calendarioEvento);
            res.activate("informazioni.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    
}
