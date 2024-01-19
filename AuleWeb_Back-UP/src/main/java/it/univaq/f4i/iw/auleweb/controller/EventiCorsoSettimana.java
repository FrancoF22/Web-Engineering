package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
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
public class EventiCorsoSettimana extends AuleWebBaseController {

    private void action_prev(HttpServletRequest request, HttpServletResponse response, int corso_id, LocalDate g) throws IOException, ServletException, TemplateManagerException {

        g = g.minus(1, ChronoUnit.WEEKS);
        action_filtro(request, response, corso_id, g);

    }

    private void action_next(HttpServletRequest request, HttpServletResponse response, int corso_id, LocalDate g) throws IOException, ServletException, TemplateManagerException {
        g = g.plus(1, ChronoUnit.WEEKS);
        action_filtro(request, response, corso_id, g);

    }

    private void action_filtro(HttpServletRequest request, HttpServletResponse response, int corso_key, LocalDate g) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            ZoneId defaultZoneId = ZoneId.systemDefault();
            List<Calendario> ListaEventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiCorsoSettimana(corso_key, Date.from(g.atStartOfDay(defaultZoneId).toInstant()));
            request.setAttribute("ListaEventi", ListaEventi);
            request.setAttribute("Day", g);
            request.setAttribute("i", corso_key);
            res.activate("corso_settimana.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("page_title", "Corsi Settimanali");
        int id_corso;
        LocalDate day = LocalDate.now();
        try {
            id_corso = SecurityHelpers.checkNumeric(request.getParameter("i"));

            if (request.getParameter("next_week") != null) {
                day = LocalDate.parse(request.getParameter("d"));
                action_next(request, response, id_corso, day);
            } else if (request.getParameter("previous_week") != null) {
                day = LocalDate.parse(request.getParameter("d"));
                action_prev(request, response, id_corso, day);
            } else {
                action_filtro(request, response, id_corso, day);
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
        return "Week Eventi Corsi servlet";
    }// </editor-fold>
}
