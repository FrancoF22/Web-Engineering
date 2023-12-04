package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.*;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class calendario extends AuleWebBaseController {

    //creiamo e inizializziamo gli array statici per i campi data
    private static final List<Integer> days;
    private static final List<Integer> months;
    private static final List<Integer> years;
    private static final List<Integer> startHours;
    private static final List<Integer> endHours;

    static {
        days = new ArrayList();
        months = new ArrayList();
        years = new ArrayList();
        startHours = new ArrayList();
        endHours = new ArrayList();
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
        for (int i = 0; i <= 24; ++i) {
            startHours.add(i);
            endHours.add(i);
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("calendari", ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendari());
            res.activate("lista_calendario.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_compose(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        request.setAttribute("page_title", "Gestisci Calendario");
        try {
            TemplateResult res = new TemplateResult(getServletContext());
             //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("days", days);
            request.setAttribute("months", months);
            request.setAttribute("years", years);
            request.setAttribute("oraInizio", startHours);
            request.setAttribute("oraFine", endHours);
            //request.setAttribute("minutiInizio", minutiInizio);
            //request.setAttribute("minutiFine", minutiFine);
            
            if (id_calendario > 0) {
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
                if (calendario != null) {
                    request.setAttribute("calendario", calendario);
                    request.setAttribute("unused", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAuleLibere());
                    request.setAttribute("used", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(calendario));
                    res.activate("add_mod_calendario.ftl.html", request, response);
                } else {
                    handleError("Undefined calendario", request, response);

                }
            } else {
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().createCalendario();
                //passa il giorno attuale (probabilmente cambiare)
                calendario.setGiorno(Calendar.getInstance().getTime());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm:ss");
                calendario.setOraInizio(LocalTime.parse(calendario.getOraInizio().toString(), formatter));
                calendario.setOraFine(LocalTime.parse(calendario.getOraFine().toString(), formatter));
                request.setAttribute("calendario", calendario);
                request.setAttribute("unused", Collections.EMPTY_LIST);
                request.setAttribute("used", Collections.EMPTY_LIST);
                res.activate("add_mod_calendario.ftl.html", request, response);
                action_default(request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_set_properties(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario;
            if (id_calendario > 0) {
                calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
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
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm:ss");
                calendario.setOraInizio(LocalTime.parse(calendario.getOraInizio().toString(), formatter));
                calendario.setOraFine(LocalTime.parse(calendario.getOraFine().toString(), formatter));
                /*
                LocalTime tempoInizio = LocalTime.now();
                tempoInizio = tempoInizio.withHour(SecurityHelpers.checkNumeric(request.getParameter("orai")));
                tempoInizio = tempoInizio.withMinute(SecurityHelpers.checkNumeric(request.getParameter("minutiI")));
                calendario.setOraInizio(tempoInizio);
                LocalTime tempoFine = LocalTime.now();
                tempoFine = tempoFine.withHour(SecurityHelpers.checkNumeric(request.getParameter("oraF")));
                tempoFine = tempoFine.withMinute(SecurityHelpers.checkNumeric(request.getParameter("minutiF")));
                calendario.setOraFine(tempoFine);
                */
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().storeCalendario(calendario);
                //delega il resto del processo all'azione compose
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

            if (calendario != null && request.getParameter("aaula") != null) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(SecurityHelpers.checkNumeric(request.getParameter("aaula")));
                if (aula != null) {
                    aula.setCalendario(calendario);
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
            if (calendario != null && request.getParameter("raula") != null) {
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
                 } else if (request.getParameter("remove") != null) {
                    action_remove_aula(request, response, id_calendario);
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
