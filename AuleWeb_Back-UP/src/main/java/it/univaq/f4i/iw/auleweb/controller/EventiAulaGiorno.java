/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.io.PrintWriter;
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

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
    try {
        TemplateResult res = new TemplateResult(getServletContext());
        List<Aula> aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule();
        
        if (aula != null) {
            Date giorno = new Date();
            
            if (request.getParameter("k") != null) {
                int idAula = Integer.parseInt(request.getParameter("k"));
                List<Calendario> eventiAula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiAulaGiorno(idAula, giorno);
                request.setAttribute("aule", eventiAula);
            } else {
                List<List<Calendario>> eventiAule = new ArrayList<>();
                
                for (Aula aulaItem : aula) {
                    List<Calendario> eventiAula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiAulaGiorno(aulaItem.getKey(), giorno);
                    eventiAule.add(eventiAula);
                }
                
                request.setAttribute("aule", eventiAule);
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
            action_default(request, response);
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
