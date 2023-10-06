package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class MostraDipartimenti extends AuleWebBaseController {
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Dipartimenti");
            request.setAttribute("ListaDipartimenti", ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getAllDipartimenti());
            res.activate("home.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException {
        
        try { 
            action_default(request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
        
    }
    
    
    
    
    @Override
    public String getServletInfo() {
        return "Main AuleWeb servlet";
    }

}
