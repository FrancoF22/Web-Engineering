package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Attrezzatura;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author pcela
 */
public class CreaAula extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().createAula();
        aula.setKey(0);
        Professore prof = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().createProfessore();
        aula.setProfessore(prof); //inserisco un responsabile fittizio
        if(request.getParameter("id") != null) {
            try {
                int id = SecurityHelpers.checkNumeric(request.getParameter("id"));
                aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(id);
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        request.setAttribute("aula_modifica", aula); //inserisco l'aula vuota oppure l'aula compilata
        if(request.getParameter("salva") != null){
            int id;
            try {
                id = SecurityHelpers.checkNumeric(request.getParameter("id_aula"));
                aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(id);
                if(aula == null) aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().createAula();
                aula.setNome(request.getParameter("nome"));
                String piano = request.getParameter("piano");
                aula.setPiano(piano);
                int capienza = SecurityHelpers.checkNumeric(request.getParameter("capienza"));
                aula.setCapienza(capienza);
                aula.setEdificio(request.getParameter("edificio"));
                aula.setLuogo(request.getParameter("luogo"));
                int npe = SecurityHelpers.checkNumeric(request.getParameter("prese_elettriche"));
                aula.setPreseElettriche(npe);
                int npr = SecurityHelpers.checkNumeric(request.getParameter("prese_rete"));
                aula.setPreseRete(npr);
                aula.setNota(request.getParameter("note"));
                prof = null;
                if(!request.getParameter("prof").isEmpty() || request.getParameter("prof") != null) prof = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessore(request.getParameter("responsabile")); //il Professore non ha email tra i campi - ema
                aula.setProfessore(prof);
                Set<Attrezzatura> attrezzature = new HashSet<>();
                Set<Attrezzatura> allAttrezzature = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().gettAllAttrezzature();
                for(Attrezzatura a : allAttrezzature){
                    if(request.getParameter(a.getNome()) != null) attrezzature.add(a); //checkbox null or on
                }
                aula.setAttrezzature(attrezzature);
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().storeAula(aula);
                response.sendRedirect("modifica-aule");
            } catch (IOException | DataException ex) {
                handleError(ex, request, response);
            }
        } else {
            try {
                request.setAttribute("ListaAttrezzature", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().gettAllAttrezzature());
                action_default(request,response);
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) {
        try {
            TemplateResult result = new TemplateResult(getServletContext());
            request.setAttribute("page-title", "Crea e modifica aula");
            result.activate("Crea_Aula.html", request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
    
}
