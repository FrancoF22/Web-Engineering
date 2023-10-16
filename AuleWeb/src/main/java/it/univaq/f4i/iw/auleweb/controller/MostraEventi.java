/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.dao.EventoDAO;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo_Aula;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Lenovo
 */
public class MostraEventi extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        EventoDAO eventoDAO = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO();
        int id_aula = SecurityHelpers.checkNumeric(request.getParameter("id_aula"));
        if(request.getParameter("datepick") != null){
            if(request.getParameter("button1") != null){
                try {
                    request.setAttribute("ListaEventi", eventoDAO.getEventiAula(id_aula, Date.valueOf(request.getParameter("datepick"))));
                } catch (DataException ex) {
                    handleError(ex, request, response);
                }
            } else {
                try {
                    request.setAttribute("ListaEventi", eventoDAO.getEventiAulaGiorno(id_aula, Date.valueOf(request.getParameter("datepick"))));
                } catch (DataException ex) {
                    handleError(ex, request, response);
                }
            }
        } else {
            try {
                request.setAttribute("ListaEventi", eventoDAO.getAllEventiAula(SecurityHelpers.checkNumeric(request.getParameter("id_aula"))));
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        request.setAttribute("aulaID", id_aula);
        try { 
            action_default(request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response)throws IOException, ServletException, TemplateManagerException  {
       try {
            TemplateResult res = new TemplateResult(getServletContext());
            Gruppo_Aula gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo_Aula(SecurityHelpers.checkNumeric(request.getParameter("id_aula"))); //cosa si vuole qui§?^ -ema
            request.setAttribute("id", gruppo.getGruppo().getKey());
            res.activate("tabella-eventi-utente.html", request, response);
       } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
}
