package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
public class HomeAdmin extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        try {
            action_default(request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws TemplateManagerException {
        TemplateResult res = new TemplateResult(getServletContext());
        request.setAttribute("page_title", "Home Admin");
        res.activate("admin-home.html", request, response);
    }
}
