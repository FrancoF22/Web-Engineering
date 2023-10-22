/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pcela
 */
public class MostraAule extends AuleWebBaseController{

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try { 
            action_default(request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            if(request.getParameter("responsabile") != null){ //responsabile
                request.setAttribute("ListaAule", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule());
                request.setAttribute("responsabile", Boolean.valueOf(request.getParameter("responsabile")));
            } else {
                request.setAttribute("ListaAule", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAuleGI(SecurityHelpers.checkNumeric(request.getParameter("id_gruppo"))));
                Gruppo Facolta = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppoById(SecurityHelpers.checkNumeric(request.getParameter("id_gruppo")));
                request.setAttribute("id", Facolta.getDipartimento().getKey());
                
            }
            res.activate("tabella-aule-utente.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
        
    }
    
    
    
}
