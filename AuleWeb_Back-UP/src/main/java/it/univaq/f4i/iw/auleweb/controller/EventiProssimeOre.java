/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
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

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            List<Evento> evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getAllEventi();
            
            if(evento != null){
                for(int i = 0; i < evento.size(); i++){
                    LocalTime ora = LocalTime.now();
                    Date giorno = new Date();
                    request.setAttribute("aule", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventoGiornoOra(giorno, ora, evento.get(i).getKey()));
                    
                }
                res.activate("prossime_ore.ftl.html", request, response);
            }
            
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_filtro(HttpServletRequest request, HttpServletResponse response, int id_gruppo)  throws IOException, ServletException, TemplateManagerException {
        try{
            TemplateResult res = new TemplateResult(getServletContext());
            List<Evento> eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getAllProssimiEventi(id_gruppo);
            request.setAttribute("eventi",eventi);
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
            if(request.getParameter("k") != null) {
                id_gruppo = SecurityHelpers.checkNumeric(request.getParameter("k"));
                action_filtro(request,response,id_gruppo);
            }else{
            action_default(request, response);
            }
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