/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.*;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.*;
import java.util.*;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author franc
 */
public class calendario extends  AuleWebBaseController {
    
        //creiamo e inizializziamo gli array statici per i campi data
    //create and initialize static arrays for date fields
    private static final List<Integer> days;
    private static final List<Integer> months;
    private static final List<Integer> years;

    static {
        days = new ArrayList();
        months = new ArrayList();
        years = new ArrayList();
        for (int i = 1; i <= 31; ++i) {
            days.add(i);
        }
        for (int i = 1; i <= 12; ++i) {
            months.add(i);
        }
        int base_year = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = -5; i <= 5; ++i) {
            years.add(base_year + i);
        }

    }
/*
    private void action_write(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());

            List<Evento> eventi = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getAllEventi();
            request.setAttribute("ListaProfessori", eventi);
            List<Aula> aule = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule();
            request.setAttribute("ListaProfessori", aule);
            //request.setAttribute("ListaAttrezzatura", attrezzature);
            if (id_calendario > 0) {
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCAlendario(id_calendario);
                if (calendario != null) {
                    request.setAttribute("aula", calendario);
                    res.activate("write_calendario.html", request, response);
                } else {
                    handleError("Undefined calendario", request, response);
                }
            } else {
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().createCalendario();
                request.setAttribute("calendario", calendario);
                res.activate("write_calendario.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_update(HttpServletRequest request, HttpServletResponse response,int id_evento, int id_aula) throws IOException, ServletException, TemplateManagerException {
        try {
            Evento evento;
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
*/
        private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("calendario", ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendari());
            res.activate(".ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_compose(HttpServletRequest request, HttpServletResponse response, int id_calendaio) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
            //add to the template a wrapper object that allows to call the stripslashes function
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("days", days);
            request.setAttribute("months", months);
            request.setAttribute("years", years);
            if (id_calendaio > 0) {
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCAlendario(id_calendaio);
                if (calendario != null) {
                    request.setAttribute("calendario", calendario);
                    request.setAttribute("unused", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getUnassignedArticles());
                    request.setAttribute("used", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getArticles(issue));
                    res.activate("compose_single.ftl.html", request, response);
                } else {
                    handleError("Undefined issue", request, response);

                }
            } else {
                //issue_key==0 indica un nuovo numero 
                //issue_key==0 indicates a new calendario
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().createCalendario();
                issue.setNumber(((NewspaperDataLayer) request.getAttribute("datalayer")).getIssueDAO().getLatestIssueNumber() + 1);
                issue.setDate(Calendar.getInstance().getTime());
                request.setAttribute("calendario", calendario);
                //forza prima a compilare i dati essenziali per creare un numero
                //forces first to compile the mandatory fields to create an calendario
                request.setAttribute("unused", Collections.EMPTY_LIST);
                request.setAttribute("used", Collections.EMPTY_LIST);
                res.activate("compose_single.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_set_properties(HttpServletRequest request, HttpServletResponse response, int id_calendaio) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario;
            if (id_calendaio > 0) {
                calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCAlendario(id_calendaio);
            } else {
                calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().createCalendario();
            }
            if (calendario != null
                    && request.getParameter("day") != null
                    && request.getParameter("month") != null
                    && request.getParameter("year") != null) {
                Calendar date = Calendar.getInstance();
                date.set(SecurityHelpers.checkNumeric(request.getParameter("year")),
                        SecurityHelpers.checkNumeric(request.getParameter("month")) - 1,
                        SecurityHelpers.checkNumeric(request.getParameter("day")));
                calendario.setDate(date.getTime());
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().storeCalendario(calendario);
                //delega il resto del processo all'azione compose
                //delegates the rest of the process to the compose action
                action_compose(request, response, calendario.getKey());
            } else {
                handleError("Cannot update calendario: insufficient parameters", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_add_article(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCAlendario(id_calendario);

            if (calendario != null && request.getParameter("aarticle") != null && request.getParameter("page") != null) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(SecurityHelpers.checkNumeric(request.getParameter("aaaula")));
                Article article = ((NewspaperDataLayer) request.getAttribute("datalayer")).getArticleDAO().getArticle(SecurityHelpers.checkNumeric(request.getParameter("aarticle")));
                if (aula != null) {
                    article.setIssue(calendario);
                    article.setPage(SecurityHelpers.checkNumeric(request.getParameter("page")));
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().storeAula(aula);
                    //delega il resto del processo all'azione compose
                    //delegates the rest of the process to the compose action
                    action_compose(request, response, id_calendario);
                } else {
                    handleError("Cannot add undefined aula", request, response);
                }
            } else {
                handleError("Cannot add aula: insufficient parameters", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    //cambiare in remove Aula (forse fare anche per remove Evento)
    private void action_remove_article(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCAlendario(id_calendario);
            if (calendario != null && request.getParameter("rarticle") != null) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(SecurityHelpers.checkNumeric(request.getParameter("raaula")));
                Article article = ((NewspaperDataLayer) request.getAttribute("datalayer")).getArticleDAO().getArticle(SecurityHelpers.checkNumeric(request.getParameter("rarticle")));
                if (aula != null) {
                    if (article.getIssue().getKey() == calendario.getKey()) {
                        article.setPage(0);
                        article.setIssue(null);
                        //modificae in modo da avere storeAula
                        ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().storeAula(aula);
                    }
                    //delega il resto del processo all'azione di default
                    //delegates the rest of the process to the default action
                    action_compose(request, response, id_calendario);
                } else {
                    handleError("Cannot remove undefined aula", request, response);
                }
            } else {
                handleError("Cannot remove aula: insufficient parameters", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    //modificare in modo che lavori per la pagina Calendario
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        request.setAttribute("page_title", "Aggiungi calendario");

        int id_calendario;
        try {
            if (request.getParameter("n") != null) {
                id_calendario = SecurityHelpers.checkNumeric(request.getParameter("n"));
                if (request.getParameter("add") != null) {
                    action_add_article(request, response, id_calendario);
                } else if (request.getParameter("remove") != null) {
                    action_remove_article(request, response, id_calendario);
                } else if (request.getParameter("update") != null) {
                    action_set_properties(request, response, id_calendario);
                } else {
                    action_compose(request, response, id_calendario);
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
}