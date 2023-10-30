package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Syd
 */
public class ListaEventi extends AuleWebBaseController {
    private int id_aula;
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
             String idParam=request.getParameter("id");
            id_aula = Integer.parseInt(idParam);
            request.setAttribute("page_title", "Eventi");
            request.setAttribute("ListaEventi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getAllEventiAula(id_aula));
            res.activate("Mostra_Eventi.html", request, response);
        } catch (DataException ex) {
            //
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException{
        try{
            action_default(request, response);
        }catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
        
    }

    @Override
    public String getServletInfo() {
        return "Lista degli eventi";
    }// </editor-fold>

}
