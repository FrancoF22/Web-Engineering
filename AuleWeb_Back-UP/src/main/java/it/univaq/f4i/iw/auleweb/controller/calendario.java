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

    //creiamo e inizializziamo gli array statici per i campi selectedDate
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
            request.setAttribute("calendari", ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendari());
            res.activate("lista_calendari.ftl.html", request, response);
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_compose(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        request.setAttribute("page_title", "Gestisci Calendario");
        try {
            TemplateResult res = new TemplateResult(getServletContext());
             //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes            
            request.setAttribute("days", days);
            request.setAttribute("months", months);
            request.setAttribute("years", years);
            request.setAttribute("aule", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAllAule());
            request.setAttribute("eventi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getAllEventi());
                    
            if (id_calendario > 0) {
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
                if (calendario != null) {
                    request.setAttribute("calendario", calendario);
                    res.activate("compose_calendario.ftl.html", request, response);
                } else {
                    handleError("Undefined calendario", request, response);

                }
            } else {
                Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().createCalendario();
                //passa il giorno attuale (probabilmente cambiare)
                calendario.setGiorno(Calendar.getInstance().getTime());
               
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("H:mm");
                request.setAttribute("inizio", LocalTime.parse(calendario.getOraInizio().toString(), formatter));
                request.setAttribute("fine", LocalTime.parse(calendario.getOraFine().toString(), formatter));
                request.setAttribute("calendario", calendario);
                res.activate("compose_calendario.ftl.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_update(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario;
            if(id_calendario > 0){
                calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
            }else{
                calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().createCalendario();
            }
            if (calendario != null) {
                Aula a = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(SecurityHelpers.checkNumeric(request.getParameter("aula")));
                Evento e = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(SecurityHelpers.checkNumeric(request.getParameter("evento")));
                // Recupera i parametri dal form di modifica
                String inizioOra = request.getParameter("oraInizio");
                String inizioMinuti = request.getParameter("minutiInizio");
                String fineOra = request.getParameter("oraFine");
                String fineMinuti = request.getParameter("minutiFine");
                int oraInizio = Integer.parseInt(inizioOra);
                int oraFine = Integer.parseInt(fineOra);
                int minutiInizio = Integer.parseInt(inizioMinuti);
                int minutiFine = Integer.parseInt(fineMinuti);
                LocalTime oraI = LocalTime.of(oraInizio, minutiInizio);
                LocalTime oraF = LocalTime.of(oraFine, minutiFine);
                // Aggiorna le informazioni del calendario
                calendario.setOraInizio(oraI);
                calendario.setOraFine(oraF);
                calendario.setAula(a);
                calendario.setEvento(e);
                // Salva il calendario aggiornato nel database
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().storeCalendario(calendario);
                // Reindirizza alla pagina di visualizzazione dei calendari
                action_default(request, response);
            } else {
                    handleError("Cannot update calendario: insufficient parameters", request, response);
                }
            } catch (NumberFormatException ex) {
                handleError("Invalid number format for time", request, response);
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
                int day = Integer.parseInt(request.getParameter("day"));
                int month = Integer.parseInt(request.getParameter("month"));
                int year = Integer.parseInt(request.getParameter("year"));
                LocalDate selectedDate = LocalDate.of(year, month, day);
                calendario.setGiorno(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                System.out.println(selectedDate);
                String inizioOra = request.getParameter("oraInizio");
                String inizioMinuti = request.getParameter("minutiInizio");
                String fineOra = request.getParameter("oraFine");
                String fineMinuti = request.getParameter("minutiFine");
                int oraInizio = Integer.parseInt(inizioOra);
                int oraFine = Integer.parseInt(fineOra);
                int minutiInizio = Integer.parseInt(inizioMinuti);
                int minutiFine = Integer.parseInt(fineMinuti);
                LocalTime oraI = LocalTime.of(oraInizio, minutiInizio);
                LocalTime oraF = LocalTime.of(oraFine, minutiFine);
                calendario.setOraInizio(oraI);
                calendario.setOraFine(oraF);
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().storeCalendario(calendario);
                //delega il resto del processo all'azione compose
                action_default(request, response);
                
            } else {
                handleError("Cannot update calendario: insufficient parameters", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_add_dipendenze(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().createCalendario();
            
            int day = Integer.parseInt(request.getParameter("day"));
            int month = Integer.parseInt(request.getParameter("month"));
            int year = Integer.parseInt(request.getParameter("year"));
            LocalDate selectedDate = LocalDate.of(year, month, day);
            calendario.setGiorno(Date.from(selectedDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            String inizioOra = request.getParameter("oraInizio");
            String inizioMinuti = request.getParameter("minutiInizio");
            String fineOra = request.getParameter("oraFine");
            String fineMinuti = request.getParameter("minutiFine");
            int oraInizio = Integer.parseInt(inizioOra);
            int oraFine = Integer.parseInt(fineOra);
            int minutiInizio = Integer.parseInt(inizioMinuti);
            int minutiFine = Integer.parseInt(fineMinuti);
            LocalTime oraI = LocalTime.of(oraInizio, minutiInizio);
            LocalTime oraF = LocalTime.of(oraFine, minutiFine);
            calendario.setOraInizio(oraI);
            calendario.setOraFine(oraF);
                
            if (request.getParameter("aaula") != null && request.getParameter("aevento") != null) {
                Aula aula = ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(SecurityHelpers.checkNumeric(request.getParameter("aaula")));
                Evento evento = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getEvento(SecurityHelpers.checkNumeric(request.getParameter("aevento")));
                if (aula != null && evento != null) {
                    aula.setCalendario(calendario);
                    evento.setCalendario(calendario);
                    calendario.setAula(aula);
                    calendario.setEvento(evento);
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().storeAula(aula);
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeEvento(evento);
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().storeCalendario(calendario);
                    action_default(request, response);
                } else {
                    handleError("Cannot add undefined aula e/o evento", request, response);
                }
            } else {
                handleError("Cannot add dipendenze: insufficient parameters", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }

    private void action_remove(HttpServletRequest request, HttpServletResponse response, int id_calendario) throws IOException, ServletException, TemplateManagerException {
        try {
            Calendario calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().getCalendarioById(id_calendario);
            if (calendario != null) {
                ((AuleWebDataLayer) request.getAttribute("datalayer")).getCalendarioDAO().deleteCalendario(id_calendario);
                action_default(request, response);
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
        request.setAttribute("page_title", "Aggiungi/Modifica Calendario");
        int id_calendario;
        try {
            if (request.getParameter("n") != null) {
                id_calendario = SecurityHelpers.checkNumeric(request.getParameter("n"));
                if (request.getParameter("update") != null) {
                    action_update(request, response, id_calendario);
                } else if (request.getParameter("add") != null) {
                    action_add_dipendenze(request, response);
                 } else if (request.getParameter("remove") != null) {
                    action_remove(request, response, id_calendario);
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
