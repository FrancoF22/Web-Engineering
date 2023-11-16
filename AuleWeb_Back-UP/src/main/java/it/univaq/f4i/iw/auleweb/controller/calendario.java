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
import java.time.LocalTime;
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
                    request.setAttribute("evento", calendario);
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
            Aula evento;
            if (id_aula > 0) {
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(id_aula);
            } else {
                evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().createAula();
            }
            if (evento != null && request.getParameter("professore") != null && request.getParameter("nome") != null && request.getParameter("luogo") != null && request.getParameter("edificio") != null && request.getParameter("piano") != null && request.getParameter("ListaAttrezzatura") != null) {
                Professore prof = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessoreById(SecurityHelpers.checkNumeric(request.getParameter("professore")));
                if (prof != null) {
                    evento.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                    evento.setProfessore(prof);
                    evento.setCapienza(SecurityHelpers.checkNumeric(request.getParameter("capienza")));
                    evento.setPreseElettriche(SecurityHelpers.checkNumeric(request.getParameter("prese_elettriche")));
                    evento.setPreseRete(SecurityHelpers.checkNumeric(request.getParameter("prese_rete")));
                    evento.setNota(SecurityHelpers.addSlashes(request.getParameter("nota")));
                    evento.setLuogo(SecurityHelpers.addSlashes(request.getParameter("luogo")));
                    evento.setEdificio(SecurityHelpers.addSlashes(request.getParameter("edificio")));
                    evento.setPiano(SecurityHelpers.addSlashes(request.getParameter("piano")));
                    String[] attrezzatura = request.getParameterValues("ListaAttrezzatura");
                    ArrayList<String> att = new ArrayList<>();
                    if (attrezzatura != null) {
                        att.addAll(Arrays.asList(attrezzatura));
                        evento.setAttrezzature(att);
                    }
                        
                    
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().storeAula(evento);
                    action_write(request, response, evento.getKey());
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
            res.activate("eventi_calendario.ftl.html", request, response);
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
                    request.setAttribute("occupate", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule(calendario));
                    res.activate("calendario.ftl.html", request, response);
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
                request.setAttribute("unused", Collections.EMPTY_LIST);
                request.setAttribute("used", Collections.EMPTY_LIST);
                res.activate("calendario.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_set_properties(HttpServletRequest request, HttpServletResponse response, int id_calendaio) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario;
            if (id_calendaio > 0) {
                calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendaio);
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
                calendario.setGiorno(date.getTime());
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

    private void action_add_aula(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);

            if (calendario != null && request.getParameter("aarticle") != null && request.getParameter("page") != null) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(SecurityHelpers.checkNumeric(request.getParameter("aaula")));
                if (aula != null) {
                    //article.setIssue(calendario);
                    aula.setCalendario(calendario);
                    //article.setPage(SecurityHelpers.checkNumeric(request.getParameter("page")));
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

    private void action_remove_aula(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
            if (calendario != null && request.getParameter("rarticle") != null) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(SecurityHelpers.checkNumeric(request.getParameter("raaula")));
                if (aula != null) {
                    if (aula.getCalendario().getKey() == calendario.getKey()) {
                        //article.setPage(0);
                        aula.setCalendario(null);
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

        private void action_add_evento(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);

            if (calendario != null && request.getParameter("aarticle") != null && request.getParameter("page") != null) {
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(SecurityHelpers.checkNumeric(request.getParameter("aevento")));
                if (evento != null) {
                    //article.setIssue(calendario);
                    evento.setCalendario(calendario);
                    //article.setPage(SecurityHelpers.checkNumeric(request.getParameter("page")));
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);
                    //delega il resto del processo all'azione compose
                    //delegates the rest of the process to the compose action
                    action_compose(request, response, id_calendario);
                } else {
                    handleError("Cannot add undefined evento", request, response);
                }
            } else {
                handleError("Cannot add evento: insufficient parameters", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_remove_evento(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
            if (calendario != null && request.getParameter("rarticle") != null) {
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(SecurityHelpers.checkNumeric(request.getParameter("aevento")));
                if (evento != null) {
                    if (evento.getCalendario().getKey() == calendario.getKey()) {
                        //article.setPage(0);
                        evento.setCalendario(null);
                        //modificae in modo da avere storeAula
                        ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);
                    }
                    //delega il resto del processo all'azione di default
                    //delegates the rest of the process to the default action
                    action_compose(request, response, id_calendario);
                } else {
                    handleError("Cannot remove undefined evento", request, response);
                }
            } else {
                handleError("Cannot remove evento: insufficient parameters", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    //modificare in modo che lavori per la pagina Calendario
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        request.setAttribute("page_title", "Calendario");

        int id_calendario;
        try {
            if (request.getParameter("n") != null) {
                id_calendario = SecurityHelpers.checkNumeric(request.getParameter("n"));
                if (request.getParameter("add") != null) {
                    action_add_aula(request, response, id_calendario);
                    action_add_evento(request, response, id_calendario);
                } else if (request.getParameter("remove") != null) {
                    action_remove_aula(request, response, id_calendario);
                    action_remove_evento(request, response, id_calendario);
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
    
    /**
    * Returns a short description of the servlet.
    *
    * @return a String containing servlet description
    */
    @Override
    public String getServletInfo() {
        return "Caledario servlet";
    }// </editor-fold>
    
}
