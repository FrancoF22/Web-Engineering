/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Calendario;
import it.univaq.f4i.iw.auleweb.data.model.Corso;
import it.univaq.f4i.iw.auleweb.data.model.Evento;
import it.univaq.f4i.iw.auleweb.data.model.Ricorrenza;
import it.univaq.f4i.iw.auleweb.data.model.Tipologia;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
public class CreaEvento extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //Evento di default
        AuleWebDataLayer dataLayer = ((AuleWebDataLayer) request.getAttribute("datalayer"));
        Calendario calendario = dataLayer.getEventoDAO().createCalendario();
        Evento evento = dataLayer.getEventoDAO().createEvento();
        evento.setKey(0);
        //evento.setTipologia(Tipologia.lezione);
        Corso corso = dataLayer.getCorsoDAO().createCorso(); //continua a segnare errore -ema
        corso.setKey(0);
        evento.setCorso(corso);
        evento.setResponsabile(dataLayer.getResponsabileDAO().createResponsabile());
        calendario.setEvento(evento);
        if(request.getParameter("id") != null) {
            try {
                int id = SecurityHelpers.checkNumeric(request.getParameter("id"));
                Date giorno = Date.valueOf(request.getParameter("data"));
                calendario = ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().getCalendario(id, giorno);
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        request.setAttribute("calendario_modifica", calendario);
        request.setAttribute("dataEvento", LocalDate.parse(calendario.getGiorno().toString()));
        
        if(request.getParameter("crea") != null){
                int id_aula = SecurityHelpers.checkNumeric(request.getParameter("aula_selezionata"));
                //controllo validazione data, orario e ripetizioni dell'evento sull'aula scelta
                try {
                    Ricorrenza ricorrenzaEvento = Ricorrenza.giornaliera; //default value che poi verrà sovrascritto in caso di creazione, non verrà preso in considerazioone nel caso di modifica
                    if(request.getParameter("frequenza") != null) {
                        ricorrenzaEvento = Ricorrenza.valueOf(request.getParameter("frequenza"));
                    }
                    int ripetizioniEvento = Integer.parseInt(request.getParameter("ripetizioni"));
                    Date dataEvento = Date.valueOf(request.getParameter("data"));
                    Time orainizio = Time.valueOf(request.getParameter("oraInizio"));
                    Time orafine = Time.valueOf(request.getParameter("oraFine"));
                    
                    List<Calendario> calendarioEventiAulaSelezionata = dataLayer.getEventoDAO().getEventiAula(id_aula, "tutti"); //non so cosa vuole, probabilmente il metodo si chiama diversamente e richiede input diversi -ema
                    for(Calendario eventoPresente : calendarioEventiAulaSelezionata){
                        while(ripetizioniEvento > 0){
                            if(eventoPresente.getEvento().getKey() == SecurityHelpers.checkNumeric(request.getParameter("id_evento"))) {
                                ripetizioniEvento --;
                                continue;
                            }
                            if(dataEvento.compareTo(eventoPresente.getGiorno()) == 0) {
                                //controllare gli orari degli eventi
                                if((orainizio.after(eventoPresente.getOraInizio()) && orainizio.before(eventoPresente.getOraFine())) || (orafine.after(eventoPresente.getOraInizio()) && orafine.before(eventoPresente.getOraFine()))) {
                                    throw new DataException("l'evento o le ripetizioni dell'evento per il giorno " + dataEvento + " nella fascia oraria " + orainizio + "-" + orafine + " coincidono con un evento già presente nell'aula");
                                }
                            }
                            switch (ricorrenzaEvento) {
                                case giornaliera:
                                    dataEvento = Date.valueOf(dataEvento.toLocalDate().plusDays(1));
                                    break;
                                case settimanale:
                                    dataEvento = Date.valueOf(dataEvento.toLocalDate().plusWeeks(1));
                                    break;
                                case mensile:
                                    dataEvento = Date.valueOf(dataEvento.toLocalDate().plusMonths(1));
                                    break;
                                default:
                                    throw new DataException("la ricorrenza inserita non è valida");
                            }
                            ripetizioniEvento --;
                        }
                    }
                    
                    //procediamo con l'inserimento dell'evento e le sue ripetizioni
                    int id_evento = SecurityHelpers.checkNumeric(request.getParameter("id_evento"));
                    if(id_evento > 0) calendario.setEvento(dataLayer.getEventoDAO().getEvento(id_evento)); //modifica
                    calendario.getEvento().setNome(request.getParameter("nome"));
                    //calendario.getEvento().setTipologia(Tipologia.valueOf(request.getParameter("tipo")));
                    calendario.getEvento().setCorso(dataLayer.getCorsoDAO().getCorsoById(SecurityHelpers.checkNumeric(request.getParameter("corso"))));
                    calendario.getEvento().setDescrizione(request.getParameter("descrizione"));
                    calendario.getEvento().setResponsabile(dataLayer.getResponsabileDAO().getResponsabile((String) SecurityHelpers.checkSession(request).getAttribute("username")));
                    if(id_aula != 0) {
                        calendario.setAula(dataLayer.getAulaDAO().getAula(id_aula));
                    } else {
                        calendario.setAula(dataLayer.getAulaDAO().getAula(request.getParameter("aulaevento")));
                    }
                    calendario.setGiorno(Date.valueOf(request.getParameter("data")));
                    calendario.setOraFine(Time.valueOf(request.getParameter("oraFine")));
                    calendario.setOraInizio(Time.valueOf(request.getParameter("oraInizio")));
                    calendario.setRicorrenza(ricorrenzaEvento);
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getEventoDAO().storeCalendario(calendario);
                    response.sendRedirect("home_responsabile");
                } catch (IOException | DataException ex) {
                    handleError(ex, request, response);
                }
        } else {
            try {
                action_default(request,response);
            } catch (TemplateManagerException ex) {
                handleError(ex, request, response);
            }
        }
    }

    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("resp", SecurityHelpers.checkSession(request).getAttribute("username"));
            request.setAttribute("aula", ((AuleWebDataLayer) request.getAttribute("datalayer")).getAulaDAO().getAula(SecurityHelpers.checkNumeric(request.getParameter("id_aula"))));
            request.setAttribute("Tipologia", Tipologia.values());
            request.setAttribute("modifica", request.getParameter("attribute"));
            request.setAttribute("sendredirect", request.getParameter("redirect"));
            System.out.println("REDIRECT: " + request.getParameter("redirect"));
            System.out.println("ATTRIBUTO: " + request.getParameter("attribute"));
            request.setAttribute("Corsi", ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getAllCorsi());
            res.activate("aggiungi-evento-responsabile.html", request, response);
        } catch (DataException ex) {
            handleError(ex, request, response);
        }
    }
    
}
