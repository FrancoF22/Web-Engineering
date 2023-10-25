/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Gruppo;
import it.univaq.f4i.iw.auleweb.data.model.TipoGruppo;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pcela
 */
public class CreaGruppo extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        if(request.getParameter("id") != null) {
            int id = SecurityHelpers.checkNumeric(request.getParameter("id"));
            //action_gruppo(request,response,id); //carica il gruppo da modificare o ne crea uno nuovo
             try {
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().createGruppo();
                gruppo.setKey(0);
                gruppo.setDipartimento(gruppo);
                if(id != 0){
                    gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppoById(id);
                }
                request.setAttribute("gruppo_modifica", gruppo); //carico il gruppo con le informazioni sulle aule
                if(gruppo.getTipoGruppo() == TipoGruppo.polo) request.setAttribute("tipo", true); else request.setAttribute("tipo", false);
                request.setAttribute("aule_gruppo", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAuleGI(gruppo.getKey()));
             } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        // mi serve che mi spiegate cosa significa questo if? - ema
        if(request.getParameter("luogo") != null){
            try {
                request.setAttribute("AuleFromLuogo", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAuleFromLuogo(request.getParameter("luogo")));
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        
        if(request.getParameter("id_aula") != null) {
            int id_aula = SecurityHelpers.checkNumeric(request.getParameter("id_aula"));
            //action_elimina_aula(request,response,id);
            try {
                int id_gruppo = SecurityHelpers.checkNumeric(request.getParameter("id"));
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().deleteAulaFromGruppo(id_gruppo, id_aula);
                response.sendRedirect("gruppo?id="+id_gruppo); //riaggiorno la pagina
            } catch (DataException | IOException ex) {
                handleError(ex, request, response);
            }
        }
        
        if(request.getParameter("newid_aula") != null) {
            int id_aula = SecurityHelpers.checkNumeric(request.getParameter("newid_aula"));
            //action_inserisci_aula(request,response,id);
            try {
                int id_gruppo = SecurityHelpers.checkNumeric(request.getParameter("id"));
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().addGruppo_Aula(id_gruppo, id_aula);
                response.sendRedirect("gruppo?id="+id_gruppo); //riaggiorno la pagina
            } catch (IOException | DataException ex) {
                handleError(ex, request, response);
            }
        }
        
        if(request.getParameter("salva") != null){
            int id;
            try {
                id = SecurityHelpers.checkNumeric(request.getParameter("gruppo_id"));
                Gruppo gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppoById(id);
                if(gruppo == null) gruppo = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().createGruppo();
                gruppo.setNome(request.getParameter("nome"));
                gruppo.setTipoGruppo(TipoGruppo.valueOf(request.getParameter("tipologia")));
                gruppo.setDescrizione(request.getParameter("descrizione"));
                Gruppo dipartimento = null;
                if(request.getParameter("dipartimento") != null || !request.getParameter("dipartimento").isEmpty()) dipartimento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getGruppo(request.getParameter("dipartimento"));
                gruppo.setDipartimento(dipartimento);
                System.out.println("DESCRIZIONE: " + gruppo.getDescrizione());
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().storeGruppo(gruppo);
                response.sendRedirect("modifica-gruppo");
            } catch (IOException | DataException ex) {
                handleError(ex, request, response);
            }
        } else {
            try {
                request.setAttribute("ListaAttrezzature", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAttrezzature());
                action_default(request,response);
            } catch (DataException | TemplateManagerException ex) {
                handleError(ex, request, response);
            }
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            List<String> luoghi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getLuoghi();
            request.setAttribute("luoghi", luoghi);
            request.setAttribute("Dipartimenti", ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getAllDipartimenti());
            res.activate("admin-gruppo.html", request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }
    
}
