package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
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
                for(int i = 0; i < corsi.size(); i++){
                    Date giorno = new Date();
                    request.setAttribute("corsi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEventiCorsoSettimana(corsi.get(i).getKey(), giorno));
                    
                }
                res.activate("corso_settimana.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_filtro(HttpServletRequest request, HttpServletResponse response)  throws IOException, ServletException, TemplateManagerException {
        try{
            TemplateResult res = new TemplateResult(getServletContext());
            List<Corso> corsi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCorsoDAO().getAllCorsi();
            request.setAttribute("corsi",corsi);
            res.activate("corso_settimana.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("page_title", "Corsi Settimanali");
        try {
            if(request.getParameter("k") != null) {
                action_filtro(request,response);
            }else{
            action_default(request, response);
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