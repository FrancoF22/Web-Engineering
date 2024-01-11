/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 *
 * @author franc
 */
public class EventiProssimeOre extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response, int gruppo_key) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            List<Calendario> calendari = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getAllProssimiEventi(gruppo_key);
            request.setAttribute("ListaEventi", calendari);
            res.activate("prossime_ore.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        int id_gruppo;
        request.setAttribute("page_title", "Corsi Prossime Ore");
        try {
            id_gruppo = SecurityHelpers.checkNumeric(request.getParameter("k"));
            action_default(request, response, id_gruppo);
            
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        } catch (IOException ex) {
            Logger.getLogger(EventiCorsoSettimana.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Week Eventi Aule servlet";
    }// </editor-fold>
}
