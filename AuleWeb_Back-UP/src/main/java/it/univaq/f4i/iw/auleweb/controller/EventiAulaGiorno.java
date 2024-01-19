/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author franc
 */
public class EventiAulaGiorno extends AuleWebBaseController {

    private void action_prev(HttpServletRequest request, HttpServletResponse response, int aula_key, LocalDate g) throws IOException, ServletException, TemplateManagerException {
        g = g.minus(1, ChronoUnit.DAYS);
        action_filtro(request, response, aula_key, g);
    }

    private void action_next(HttpServletRequest request, HttpServletResponse response, int aula_key, LocalDate g) throws IOException, ServletException, TemplateManagerException {
        g = g.plus(1, ChronoUnit.DAYS);
        action_filtro(request, response, aula_key, g);
    }

    private void action_filtro(HttpServletRequest request, HttpServletResponse response, int aula_key, LocalDate g) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());

            ZoneId defaultZoneId = ZoneId.systemDefault();
            List<Calendario> calendari = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiAulaGiorno(aula_key, Date.from(g.atStartOfDay(defaultZoneId).toInstant()));
            request.setAttribute("eventi", calendari);
            request.setAttribute("Day", g);
            request.setAttribute("i", aula_key);
            res.activate("aula_giorno.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }


    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("page_title", "Eventi Giornalieri");
        int id_aula;
        LocalDate day = LocalDate.now();
        try {
            id_aula = SecurityHelpers.checkNumeric(request.getParameter("i"));
            if (request.getParameter("next_day") != null) {
               day = LocalDate.parse(request.getParameter("d"));
                action_next(request, response, id_aula, day);
            }
            else if (request.getParameter("previous_day") != null) {
                day = LocalDate.parse(request.getParameter("d"));
                action_prev(request, response, id_aula, day);
            }
            action_filtro(request, response, id_aula, day);

        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        } catch (IOException ex) {
            Logger.getLogger(EventiCorsoSettimana.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getServletInfo() {
        return "Week Eventi Aule servlet";
    }// </editor-fold>
}

/*
public class EventiAulaGiorno extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            List<Aula> aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule();
            
            if(aula != null){
                for(int i = 0; i < aula.size(); i++){
                    Date giorno = new Date();
                    List<Calendario> c = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getEventiAula(aula.get(i).getKey(), giorno);
                    request.setAttribute("aule", c);
                    
                }
                res.activate("aula_giorno.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("page_title", "Aule Giornalieri");
        try {
            if(request.getParameter("k") != null) {
            action_default(request, response);
            }
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        } catch (IOException ex) {
            Logger.getLogger(EventiCorsoSettimana.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
*/
