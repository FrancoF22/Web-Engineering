package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pcela
 */
public class GestisciResponsabile extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String email;
        if(request.getParameter("resp") != null){
            email = request.getParameter("resp");
            try {
                Utente responsabile = ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtenteByEmail(email);
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().deleteUtente(responsabile.getKey());
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        action_default(request, response);
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Gestione Responsabili");
            request.setAttribute("ListaResponsabili", ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getAllResponsabili());
            res.activate("Tabella-Responsabile-admin.html", request, response);
        } catch (TemplateManagerException | DataException ex) {
            handleError(ex, request, response);
        }
    }
    
}
