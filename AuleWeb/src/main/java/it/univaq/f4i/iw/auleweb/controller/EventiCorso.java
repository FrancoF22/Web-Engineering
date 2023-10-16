/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Lenovo
 */
public class EventiCorso extends AuleWebBaseController{

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        int id_corso = SecurityHelpers.checkNumeric(request.getParameter("corso"));
        if(request.getParameter("filtra") != null){
            try {
                request.setAttribute("EventiCorso", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiCorso(id_corso,Date.valueOf(request.getParameter("datepicker"))));
            } catch (DataException ex) {
                Logger.getLogger(EventiCorso.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                request.setAttribute("EventiCorso", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiCorsoSettimana(id_corso));
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        try {
            action_default(request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            Corso corso = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorsoById(SecurityHelpers.checkNumeric(request.getParameter("corso")));
            request.setAttribute("corsoID", corso.getKey());
            request.setAttribute("aulaID", request.getParameter("id_aula"));
            TemplateResult res = new TemplateResult(getServletContext());
            res.activate("tabella-corso-utente.html", request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
        
    }
    
    
}
