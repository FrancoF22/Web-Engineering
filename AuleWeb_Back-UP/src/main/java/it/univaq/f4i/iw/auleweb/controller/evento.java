/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.*;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author franc
 */
public class evento extends AuleWebBaseController {
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("eventi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getAllEventi());
            res.activate("evento.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_write(HttpServletRequest request, HttpServletResponse response, int id_evento) throws IOException, ServletException, TemplateManagerException {
        request.setAttribute("page_title", "Gestisci Evento");
        try {
            TemplateResult res = new TemplateResult(getServletContext());

            List<Corso> corso = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getAllCorsi();
            List<Professore> professori = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessori();
            Tipologia[] tipo = Tipologia.values();
            request.setAttribute("ListaCorsi", corso);
            request.setAttribute("ListaProfessori", professori);
            request.setAttribute("tipo", tipo);
            if (id_evento > 0) {
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(id_evento);
                if (evento != null) {
                    request.setAttribute("evento", evento);
                    res.activate("agg_mod_evento.ftl.html", request, response);
                } else {
                    handleError("Undefined evento", request, response);
                }
            } else {
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();
                request.setAttribute("evento", evento);
                res.activate("agg_mod_evento.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_update(HttpServletRequest request, HttpServletResponse response, int id_evento) throws IOException, ServletException, TemplateManagerException {
        request.setAttribute("page_title", "Gestisci Evento");
        try {
            Evento evento;
            if (id_evento > 0) {
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(id_evento);
            } else {
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();
            }
            if (evento != null && request.getParameter("professore") != null && request.getParameter("nome") != null && request.getParameter("corso") != null && request.getParameter("tipo") != null) {
                Professore prof = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessoreById(SecurityHelpers.checkNumeric(request.getParameter("professore")));
                Corso corso = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorsoById(SecurityHelpers.checkNumeric(request.getParameter("corso")));
                String tipologiaValue = request.getParameter("tipo");
                
                if (prof != null && corso != null) {
                    evento.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                    evento.setProfessore(prof);
                    if (request.getParameter("descrizione") != null) {
                        evento.setDescrizione(SecurityHelpers.addSlashes(request.getParameter("descrizione")));
                    }
                    if(request.getParameter("tipo") != null){
                        if (tipologiaValue != null) {
                            Tipologia tipologia = Tipologia.valueOf(tipologiaValue);
                            evento.setTipo(tipologia);
                        }
                    }
                    evento.setCorso(corso);
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);
                    action_default(request, response);
                } else {
                    handleError("Cannot update evento: undefined professor", request, response);
                }
            } else {
                handleError("Cannot update evento: insufficient parameters", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        request.setAttribute("page_title", "Evento");

        int evento_key;
        try {
            if (request.getParameter("k") != null) {
                evento_key = SecurityHelpers.checkNumeric(request.getParameter("k"));
                if (request.getParameter("update") != null) {
                    action_update(request, response, evento_key);
                } else {
                    action_write(request, response, evento_key);
                }
            } else {
                action_default(request, response);
            }
        } catch (NumberFormatException ex) {
            handleError("Invalid number submitted", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Write Aula servlet";
    }// </editor-fold>
    
}
