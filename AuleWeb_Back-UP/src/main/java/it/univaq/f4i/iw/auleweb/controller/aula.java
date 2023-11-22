package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import java.util.*;

/**
 *
 * @author Syd
 */
public class aula extends AuleWebBaseController {

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
            //add to the template a wrapper object that allows to call the stripslashes function
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("aule", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule());
            res.activate("lista_aule.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_write(HttpServletRequest request, HttpServletResponse response, int id_aula) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());

            List<Professore> professori = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessori();
            List<String> attrezzature = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAttrezzature();
            request.setAttribute("ListaProfessori", professori);
            request.setAttribute("ListaAttrezzatura", attrezzature);
            if (id_aula > 0) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(id_aula);
                if (aula != null) {
                    request.setAttribute("aula", aula);
                    res.activate("agg_mod_aula.ftl.html", request, response);
                } else {
                    handleError("Undefined aula", request, response);
                }
            } else {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().createAula();
                request.setAttribute("aula", aula);
                res.activate("agg_mod_aula.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_update(HttpServletRequest request, HttpServletResponse response, int id_aula) throws IOException, ServletException, TemplateManagerException {
        try {
            Aula aula;
            if (id_aula > 0) {
                aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(id_aula);
            } else {
                aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().createAula();
            }
            if (aula != null && request.getParameter("professore") != null && request.getParameter("nome") != null && request.getParameter("luogo") != null && request.getParameter("edificio") != null && request.getParameter("piano") != null && request.getParameter("ListaAttrezzatura") != null) {
                Professore prof = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessoreById(SecurityHelpers.checkNumeric(request.getParameter("professore")));
                if (prof != null) {
                    aula.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                    aula.setProfessore(prof);
                    aula.setCapienza(SecurityHelpers.checkNumeric(request.getParameter("capienza")));
                    aula.setPreseElettriche(SecurityHelpers.checkNumeric(request.getParameter("prese_elettriche")));
                    aula.setPreseRete(SecurityHelpers.checkNumeric(request.getParameter("prese_rete")));
                    aula.setNota(SecurityHelpers.addSlashes(request.getParameter("nota")));
                    aula.setLuogo(SecurityHelpers.addSlashes(request.getParameter("luogo")));
                    aula.setEdificio(SecurityHelpers.addSlashes(request.getParameter("edificio")));
                    aula.setPiano(SecurityHelpers.addSlashes(request.getParameter("piano")));
                    String[] attrezzatura = request.getParameterValues("ListaAttrezzatura");
                    ArrayList<String> att = new ArrayList<>();
                    if (attrezzatura != null) {
                        att.addAll(Arrays.asList(attrezzatura));
                        aula.setAttrezzature(att);
                    }
                        
                    
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().storeAula(aula);
                    action_write(request, response, aula.getKey());
                } else {
                    handleError("Cannot update aula: undefined professor", request, response);
                }
            } else {
                handleError("Cannot update aula: insufficient parameters", request, response);

            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        request.setAttribute("page_title", "Modifica Aula");

        int aula_key;
        try {
            if (request.getParameter("k") != null) {
                aula_key = SecurityHelpers.checkNumeric(request.getParameter("k"));
                if (request.getParameter("update") != null) {
                    action_update(request, response, aula_key);
                } else {
                    action_write(request, response, aula_key);
                }
            } else {
                action_default(request, response);
            }
        } catch (NumberFormatException ex) {
            handleError("Invalid number submitted", request, response);
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
        return "Write Aula servlet";
    }// </editor-fold>
    
}
