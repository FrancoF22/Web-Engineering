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

/**
 *
 * @author Syd
 */
public class aula extends AuleWebBaseController {

    private void action_write(HttpServletRequest request, HttpServletResponse response, int id_aula) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());

            List<Professore> professori = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessori();
            request.setAttribute("ListaProfessori", professori);
            if (id_aula > 0) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(id_aula);
                if (aula != null) {
                    request.setAttribute("aula", aula);
                    res.activate("write_aula.html", request, response);
                } else {
                    handleError("Undefined aula", request, response);
                }
            } else {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().createAula();
                request.setAttribute("aula", aula);
                res.activate("write_aula.html", request, response);
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
            if (aula != null && request.getParameter("professore") != null && request.getParameter("nome") != null && request.getParameter("luogo") != null && request.getParameter("edificio") != null && request.getParameter("piano") != null) {
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
                    if (request.getParameter("attrezzatura") != null) {
                        // devo vedere come gestire la lista di attrezzature che vuole
                        //aula.setAttrezzature(SecurityHelpers.addSlashes(request.getParameter("attrezzatura")));
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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        request.setAttribute("page_title", "Aula");

        int id_aula;
        try {
            if (request.getParameter("k") != null) {
                id_aula = SecurityHelpers.checkNumeric(request.getParameter("k"));
                if (request.getParameter("update") != null) {
                    action_update(request, response, id_aula);
                } else {
                    action_write(request, response, id_aula);
                }
            }
        } catch (NumberFormatException ex) {
            handleError("Invalid number submitted", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }

}
