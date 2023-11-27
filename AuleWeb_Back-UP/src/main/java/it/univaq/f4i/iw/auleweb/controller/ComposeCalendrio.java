package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Aula;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ComposeCalendrio extends  AuleWebBaseController {
    
    //creiamo e inizializziamo gli array statici per i campi data
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
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
            //add to the template a wrapper object that allows to call the stripslashes function
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("calendari", ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendari());
            res.activate("calendario.ftl.html", request, response);
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
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendaio);
                if (calendario != null) {
                    request.setAttribute("calendario", calendario);
                    request.setAttribute("libere", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAuleLibere());
                    request.setAttribute("occupate", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAuleOccupate());
                    res.activate("eventi_calendario.ftl.html", request, response);
                } else {
                    handleError("Undefined calendario", request, response);

                }
            } else {
                //issue_key==0 indica un nuovo numero 
                //issue_key==0 indicates a new calendario
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().createCalendario();
                //issue.setNumber(((NewspaperDataLayer) request.getAttribute("datalayer")).getIssueDAO().getLatestIssueNumber() + 1);
                calendario.setGiorno(Calendar.getInstance().getTime());
                //probabile modifica
                calendario.setOraInizio(LocalTime.MIN);
                calendario.setOraFine(LocalTime.MIN);
                request.setAttribute("calendario", calendario);
                //forza prima a compilare i dati essenziali per creare un numero
                //forces first to compile the mandatory fields to create an calendario
                request.setAttribute("libera", Collections.EMPTY_LIST);
                request.setAttribute("occupata", Collections.EMPTY_LIST);
                res.activate("eventi_calendario.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_write(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());

            List<Evento> evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getAllEventi();
            List<Aula> aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule();
            request.setAttribute("ListaEventi", evento);
            request.setAttribute("ListaAule", aula);
            if (id_calendario > 0) {
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
                if (calendario != null) {
                    request.setAttribute("evento", evento);
                    res.activate("agg_mod_calendario.ftl.html", request, response);
                } else {
                    handleError("Undefined evento", request, response);
                }
            } else {
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
                request.setAttribute("calendario", calendario);
                res.activate("agg_mod_calendario.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_update(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario;
            if (id_calendario > 0) {
                calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
            } else {
                calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().createCalendario();
            }
            if (calendario != null
                    && request.getParameter("aula") != null
                    && request.getParameter("evento") != null
                    && request.getParameter("day") != null
                    && request.getParameter("month") != null
                    && request.getParameter("year") != null) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(SecurityHelpers.checkNumeric(request.getParameter("aula")));
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(SecurityHelpers.checkNumeric(request.getParameter("evento")));
                
                if (aula != null && evento != null) {
                    Calendar date = Calendar.getInstance();
                        date.set(SecurityHelpers.checkNumeric(request.getParameter("year")),
                        SecurityHelpers.checkNumeric(request.getParameter("month")) - 1,
                        SecurityHelpers.checkNumeric(request.getParameter("day")));
                    calendario.setGiorno(date.getTime());
                    calendario.setAula(aula);
                    calendario.setEvento(evento);
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().storeCalendario(calendario);
                    action_compose(request, response, calendario.getKey());
                    action_default(request,response);
                } else {
                    handleError("Cannot update evento: undefined professor", request, response);
                }
            } else {
                handleError("Cannot update evento: insufficient parameters", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
        
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        request.setAttribute("page_title", "Gestisci Calendario");
        int calendario_key;
        try {
            if (request.getParameter("k") != null) {
                calendario_key = SecurityHelpers.checkNumeric(request.getParameter("k"));
                if (request.getParameter("update") != null) {
                    action_update(request, response, calendario_key);
                } else {
                    action_write(request, response, calendario_key);
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
        return "Write Calendario servlet";
    }// </editor-fold>
    
}