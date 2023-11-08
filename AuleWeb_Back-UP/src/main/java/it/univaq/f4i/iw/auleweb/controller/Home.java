/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author franc
 */
public class Home extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Home");
            request.setAttribute("dipartimenti", ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getAllGruppi());
            res.activate("home.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    /*
    private void action_choose(HttpServletRequest request, HttpServletResponse response, int gruppo_key) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Aule");
            request.setAttribute("ListaAule", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAuleGI(gruppo_key));
            res.activate("Mostra_Aule.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        int gruppo_key;
        try {
            if (request.getParameter("k") != null) {
                gruppo_key = SecurityHelpers.checkNumeric(request.getParameter("k"));

                action_choose(request, response, gruppo_key);

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
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        try {
            action_default(request, response);

        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Main AuleWeb servlet";
    }// </editor-fold>

}
