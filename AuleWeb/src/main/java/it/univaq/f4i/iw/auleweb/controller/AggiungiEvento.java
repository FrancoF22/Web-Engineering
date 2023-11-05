/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author franc
 */
public class AggiungiEvento extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
       
        Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();
        evento.setKey(0);
        
        if(request.getParameter("id") != null) {
            try {
                int id = SecurityHelpers.checkNumeric(request.getParameter("id"));
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(id);
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        request.setAttribute("aula_modifica", evento); //inserisco evento vuota oppure compilata
        if(request.getParameter("salva") != null){
            int id;
            try {
                id = SecurityHelpers.checkNumeric(request.getParameter("id_aula"));
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(id);
                if(evento == null) evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();
                evento.setNome(request.getParameter("nome"));
                evento.setDescrizione("descrizione");
                Tipologia[] tipo = Tipologia.values();
                request.setAttribute("tipo", tipo);
                List<Professore> professori = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessori();
                request.setAttribute("professori", professori);
                List<Corso> corsi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getAllCorsi();
                request.setAttribute("corsi", corsi);
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);
                response.sendRedirect("modifica-eventi");
            } catch (IOException | DataException ex) {
                handleError(ex, request, response);
            }
        } else {
            try {
                request.setAttribute("ListaEventi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getAllEventi());
                action_default(request,response);
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            request.setAttribute("page-title", "Crea e modifica Evento");
            result.activate("Crea_Evento.html", request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

}
