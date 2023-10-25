package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Attrezzatura;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 
 */
public class ModificaAttrezzatura extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        String nome = "";
        if(request.getParameter("nome") != null){ //elimina
            try {
                nome = request.getParameter("nome");
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().deleteAttrezzatura(nome);
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        if(request.getParameter("aggiungi") != null){ //aggiungi
            try {
                nome = request.getParameter("attrezzatura");
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().addAttrezzatura(nome);
            } catch (DataException ex) {
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
            request.setAttribute("ListaAttrezzature", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAttrezzature());
            res.activate("Modifica_Attrezzatura.html", request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }
    
}
