/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
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
 * @author pcela
 */
public class GestisciCalendarioEventi extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        if(request.getParameter("scelta")!=null){
            try {
                int id = SecurityHelpers.checkNumeric(request.getParameter("id_evento")); //input type hidden
                Date giorno = Date.valueOf(request.getParameter("data_evento")); //input type hidden
                boolean singolo = Boolean.parseBoolean(request.getParameter("radio")); //input type radio
                System.out.println("ID : "+id);
                System.out.println("GIORNO : "+giorno);
                System.out.println("SINGOLO : "+singolo);
                
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getCalendario(id, giorno);
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().deleteEvento(calendario, singolo);
                
                response.sendRedirect("home_responsabile");
                return;
            } catch (DataException | IOException ex) {
                handleError(ex, request, response);
            }
        }
        try {
            action_default(request,response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, TemplateManagerException {
        try {
            int id = SecurityHelpers.checkNumeric(request.getParameter("id"));
            Date giorno = Date.valueOf(request.getParameter("data"));
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("EventoSelezionato", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getCalendario(id, giorno));
            res.activate("elimina-evento.html", request, response);
        } catch (TemplateManagerException | DataException ex) {
            handleError(ex, request, response);
        }
    }
    
}
