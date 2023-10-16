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
 * @author user
 */
public class EventiAttuali extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        int dipartimento_key = SecurityHelpers.checkNumeric(request.getParameter("id_dipartimento"));
        request.setAttribute("dipartimentoID", dipartimento_key);
        if(request.getParameter("button1") != null){
            try {
                request.setAttribute("ListaEventi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiAttuali(dipartimento_key));
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        } else {
            try {
                request.setAttribute("ListaEventi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiAttuali(dipartimento_key));
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        try {
            action_default(request,response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        res.activate("tabella-completa-eventi-utente.html", request, response);
    }
    
}
