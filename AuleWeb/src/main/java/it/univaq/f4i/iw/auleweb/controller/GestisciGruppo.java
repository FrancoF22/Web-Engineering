/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pcela
 */
public class GestisciGruppo extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        if (request.getParameter("id") != null) {
            try {
                int id = SecurityHelpers.checkNumeric(request.getParameter("id"));
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().deleteGruppo(id);
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        try {
            action_default(request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("ListaDipartimenti", ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getAllDipartimenti());
            request.setAttribute("ListaPoli", ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getAllPoli(SecurityHelpers.checkNumeric(request.getParameter("dipartimento_key"))));
            res.activate("tabella-gruppi-admin.html", request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

}
