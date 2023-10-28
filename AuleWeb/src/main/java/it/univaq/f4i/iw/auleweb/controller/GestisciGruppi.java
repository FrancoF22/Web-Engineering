/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * implementare la possibilità di modificare/aggiungere gruppi
 */
public class GestisciGruppi extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        int id_gruppo;

        try {
            if (request.getParameter("g") != null) {
                id_gruppo = SecurityHelpers.checkNumeric(request.getParameter("g"));

                action_modify(request, response, id_gruppo);
                //((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().deleteGruppo(id_gruppo);
            } else {
                action_default(request, response);
            }
        } catch (NumberFormatException ex) {
            handleError("Invalid number submitted", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("ListaDipartimenti", ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getAllDipartimenti());
            request.setAttribute("ListaPoli", ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getAllPoli());
            res.activate("Gestisci_Gruppi.html", request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }

    private void action_modify(HttpServletRequest request, HttpServletResponse response, int gruppo_key) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "modifica/crea gruppo");
            if (gruppo_key > 0) {
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppoById(gruppo_key);
                if (gruppo != null) {
                    request.setAttribute("gruppo", gruppo);
                    res.activate("Modifica_Gruppo.html", request, response);
                } else {
                    handleError("Undefined group", request, response);

                }
            } else {
                //gruppo_key==0 indica un nuovo gruppo
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().createGruppo();
                
                request.setAttribute("gruppo", gruppo);
                //forza prima a compilare i dati essenziali per creare un numero
                res.activate("Modifica_Gruppo.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

}
