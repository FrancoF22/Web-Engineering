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
                List<Professore> professori = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessori();
                request.setAttribute("professori", professori);
                List<String> attrezzature = new ArrayList<>();
                List<String> allAttrezzature = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAttrezzature();
                for(String s : allAttrezzature){
                    if(request.getParameter(s) != null) attrezzature.add(s); //checkbox null or on
                }
                aula.setAttrezzature(attrezzature);
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().storeAula(aula);
                response.sendRedirect("modifica-aule");
            } catch (IOException | DataException ex) {
                handleError(ex, request, response);
            }
        } else {
            try {
                request.setAttribute("ListaAttrezzature", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAttrezzature());
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
