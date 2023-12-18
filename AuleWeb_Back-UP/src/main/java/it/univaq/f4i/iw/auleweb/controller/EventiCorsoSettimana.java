package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
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

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            List<Corso> corsi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getAllCorsi();
            
            if(corsi != null){
                for(int i = 1; i < corsi.size(); i++){
                    Corso c = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorsoById(i);
                    Date giorno = new Date();
                    request.setAttribute("corsi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiCorsoSettimana(c.getKey(), giorno));
                    
                }
                res.activate("filtro_eventi_corso_settimana.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("page_title", "Filtro Corsi Settimana");
        try {
            action_default(request, response);
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

/*
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("corsi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getAllCorsi());
            res.activate("pagina_filtrata.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_settimana(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        request.setAttribute("page_title", "Corsi settimanali");
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            List<Corso> corsi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getAllCorsi();
            
            if(corsi != null){
                for(int i = 0; i < corsi.size(); i++){
                    Corso c = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getCorsoById(i);
                    Date giorno = new Date();
                    request.setAttribute("corsi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiCorsoSettimana(c.getKey(), giorno));
                    
                }
                res.activate("pagina_filtrata.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        request.setAttribute("page_title", "Aule");
        int corsi_key;
        try {
            if (request.getParameter("k") != null) {
                corsi_key = SecurityHelpers.checkNumeric(request.getParameter("k"));
                if (request.getParameter("update") != null) {
                    action_settimana(request, response);
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
*/