package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.sql.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
public class HomeResponsabile extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if(request.getParameter("data") != null){
            try {
                Date giorno = Date.valueOf(request.getParameter("data"));
                int id = SecurityHelpers.checkNumeric(request.getParameter("id"));
                List<Calendario> ripetizioni = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getCalendarioEvento(id);
                
                if(ripetizioni.size() > 1){
                    response.sendRedirect("modifica-calendario?id="+id+"&giorno="+giorno);
                } else {
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().deleteEvento(ripetizioni.get(0),false);
                }
                
            } catch (DataException ex) {
                handleError(ex, request, response);
            } catch (IOException ex) {
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
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Home Responsabile");
            request.setAttribute("email_responsabile",SecurityHelpers.checkSession(request).getAttribute("username"));
            request.setAttribute("EventiResponsabile", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiResponsabile((String) SecurityHelpers.checkSession(request).getAttribute("username")/*email responsabile da riprendere dalla sessione*/,"tutti"));
            res.activate("home-responsabile.html", request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }
}
