package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.auleweb.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        String email; //dovrei modificarlo per gestire i Professori
        if(request.getParameter("resp") != null){
            email = request.getParameter("resp");
            try {
                Professore prof = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getUtenteByEmail(email); //devo cambiarlo per un metodo di ProfessoreDAO_MySQL, ma non so quale - ema
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().deleteProfessore(prof.getKey());
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        try {
            action_default(request, response);
        } catch (DataException ex) {
            Logger.getLogger(GestisciResponsabile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, DataException {
        
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Gestione professori");
            request.setAttribute("ListaResponsabili", ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessori());
            res.activate("Tabella-Responsabile-admin.html", request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
    
}
