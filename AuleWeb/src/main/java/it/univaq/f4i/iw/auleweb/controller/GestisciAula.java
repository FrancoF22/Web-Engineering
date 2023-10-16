/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pcela
 */
public class GestisciAula extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        int id;
        try {
            if(request.getParameter("id") != null){
                id = SecurityHelpers.checkNumeric(request.getParameter("id"));
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().deleteAula(id);
            }
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
        
        action_default(request,response);
        
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            request.setAttribute("page-title", "Gestione Aule");
            request.setAttribute("ListaAula", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule());
            result.activate("tabella-aula-admin.html", request, response);
        } catch (DataException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
        
    }
    
}
