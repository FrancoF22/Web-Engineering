/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author franc
 */
public class EventiAulaSettimana extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            List<Aula> aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule();
            
            if(aula != null){
                for(int i = 0; i < aula.size(); i++){
                    Date giorno = new Date();
                    request.setAttribute("aule", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiAulaSettimana(aula.get(i).getKey(), giorno));
                    
                }
                res.activate("aula_settimana.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_filtro(HttpServletRequest request, HttpServletResponse response)  throws IOException, ServletException, TemplateManagerException {
        try{
            TemplateResult res = new TemplateResult(getServletContext());
            List<Aula> aule = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule();
            request.setAttribute("aule",aule);
            res.activate("aula_settimana.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("page_title", "Aule Settimanali");
        try {
            if(request.getParameter("k") != null) {
                action_filtro(request,response);
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