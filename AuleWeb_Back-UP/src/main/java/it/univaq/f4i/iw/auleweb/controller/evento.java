/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.*;
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
public class evento extends AuleWebBaseController {

    private void action_write(HttpServletRequest request, HttpServletResponse response, int id_evento) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());

            List<Aula> aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule();
            request.setAttribute("ListaAule", aula);
            if (id_evento > 0) {
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(id_evento);
                if (aula != null) {
                    request.setAttribute("evento", evento);
                    res.activate("write_evento.html", request, response);
                } else {
                    handleError("Undefined evento", request, response);
                }
            } else {
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();
                request.setAttribute("evento", evento);
                res.activate("write_evento.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_update(HttpServletRequest request, HttpServletResponse response, int id_evento) throws IOException, ServletException, TemplateManagerException {
        try {
            Evento evento;
            if (id_evento > 0) {
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(id_evento);
            } else {
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().createEvento();
            }
            if (evento != null && request.getParameter("professore") != null && request.getParameter("nome") != null) {
                Professore prof = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessoreById(SecurityHelpers.checkNumeric(request.getParameter("professore")));
                Corso c = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorsoById(SecurityHelpers.checkNumeric(request.getParameter("corso")));
                if (prof != null && c != null) {
                    evento.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                    evento.setProfessore(prof);
                    evento.setDescrizione(SecurityHelpers.addSlashes(request.getParameter("desrizione")));
                    //inserire set per la  tipologia di eventi
                    evento.setCorso(c);
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);
                    action_write(request, response, evento.getKey());
                } else {
                    handleError("Cannot update aula: undefined professor", request, response);
                }
            } else {
                handleError("Cannot update aula: insufficient parameters", request, response);

            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("page_title", "Evento");

        int id_evento;
        try {
            if (request.getParameter("k") != null) {
                id_evento = SecurityHelpers.checkNumeric(request.getParameter("k"));
                if (request.getParameter("update") != null) {
                    action_update(request, response, id_evento);
                } else {
                    action_write(request, response, id_evento);
                }
            }
        } catch (NumberFormatException ex) {
            handleError("Invalid number submitted", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

}
