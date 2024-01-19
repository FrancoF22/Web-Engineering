package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
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

    private void action_default(HttpServletRequest request, HttpServletResponse response, int id_gruppo) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Lista Aule");
            request.setAttribute("k", id_gruppo);
            request.setAttribute("ListaAule", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAuleGI(id_gruppo));
            res.activate("aula.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_prev(HttpServletRequest request, HttpServletResponse response, int aula_key, LocalDate g) throws IOException, ServletException, TemplateManagerException {

        g = g.minus(1, ChronoUnit.WEEKS);
        action_filtro(request, response, aula_key, g);

    }

    private void action_next(HttpServletRequest request, HttpServletResponse response, int aula_key, LocalDate g) throws IOException, ServletException, TemplateManagerException {
        g = g.plus(1, ChronoUnit.WEEKS);
        action_filtro(request, response, aula_key, g);

    }

    private void action_filtro(HttpServletRequest request, HttpServletResponse response, int aula_key, LocalDate g) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());

            ZoneId defaultZoneId = ZoneId.systemDefault();
            List<Calendario> calendari = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiAulaSettimana(aula_key, Date.from(g.atStartOfDay(defaultZoneId).toInstant()));
            request.setAttribute("ListaEventi", calendari);
            request.setAttribute("Day", g);
            request.setAttribute("i", aula_key);

            res.activate("aula_settimana.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("page_title", "Eventi Settimanali");
        int id_aula;
        int gruppo_key = SecurityHelpers.checkNumeric(request.getParameter("k"));
        LocalDate day = LocalDate.now();

        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM- d");
        try {
            if (request.getParameter("i") != null) {
                id_aula = SecurityHelpers.checkNumeric(request.getParameter("i"));
                if (request.getParameter("next_week") != null) {
                    day = LocalDate.parse(request.getParameter("d"));
                    action_next(request, response, id_aula, day);
                } else if (request.getParameter("previous_week") != null) {
                    day = LocalDate.parse(request.getParameter("d"));
                    action_prev(request, response, id_aula, day);
                } else {
                    action_filtro(request, response, id_aula, day);
                }
            } else {
                action_default(request, response, gruppo_key);
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
