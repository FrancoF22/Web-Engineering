package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Responsabile;
import it.univaq.f4i.iw.auleweb.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
public class CreaResponsabile extends AuleWebBaseController {

    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        
        Responsabile responsabile = ((AuleWebDataLayer) request.getAttribute("datalayer")).getResponsabileDAO().createResponsabile();
        //creo un utente fittizio per il responsabile vuoto, in quanto responsabile.getUtente() restituisce null altrimenti
        Utente utente = ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().createUtente();
        utente.setKey(0);
        responsabile.setUtente(utente);
        if(request.getParameter("resp") != null) {
            try {
                responsabile = ((AuleWebDataLayer) request.getAttribute("datalayer")).getResponsabileDAO().getResponsabile(request.getParameter("resp"));
            } catch (DataException ex) {
                handleError(ex, request, response);
            }
        }
        request.setAttribute("responsabile_modifica", responsabile); //inserisco il responsabile vuoto oppure il responsabile compilato
        if(request.getParameter("salva") != null) { //premo pulsante salva
            if(request.getParameter("u") != null && !request.getParameter("u").isEmpty() //controllo validazione campi
               && request.getParameter("p") != null && !request.getParameter("p").isEmpty()
               && request.getParameter("nome") != null && !request.getParameter("nome").isEmpty()
               && request.getParameter("cognome") != null && !request.getParameter("cognome").isEmpty()
               && request.getParameter("email") != null && !request.getParameter("email").isEmpty()
               ) {
                try {
                    if(request.getParameter("responsabile").isEmpty()) create_responsabile(request,response); else modifica_responsabile(request,response);
                } catch (DataException | InvalidKeySpecException | NoSuchAlgorithmException ex) {
                    handleError(ex, request, response);
                }
            }
        } else action_default(request, response);
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            //freemarker non legge i metodi getUtente().getUsername(), getUtente().getPassword() nella creazione. Nella modifica non ci sono problemi
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("page_title", "Crea responsabile");
            res.activate("admin-responsabile.html", request, response);
        } catch (TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
    
    private void create_responsabile(HttpServletRequest request, HttpServletResponse response) throws DataException, InvalidKeySpecException, NoSuchAlgorithmException {
        AuleWebDataLayer datalayer = ((AuleWebDataLayer) request.getAttribute("datalayer"));
        Utente utente = datalayer.getUtenteDAO().createUtente();
        utente.setPassword(SecurityHelpers.getPasswordHashPBKDF2(request.getParameter("p")));
        utente.setNome(request.getParameter("u"));
        Responsabile responsabile = datalayer.getResponsabileDAO().createResponsabile();
        responsabile.setNome(request.getParameter("nome"));
        responsabile.setCognome(request.getParameter("cognome"));
        responsabile.setEmail(request.getParameter("email"));
        if(utente.getNome().length() > 25) throw new DataException("username troppo lungo (>25)");
        if(responsabile.getEmail().length() > 60) throw new DataException("email troppo lunga (>60)");
        if(datalayer.getUtenteDAO().getUtenteByUsername(utente.getNome()) == null 
           && datalayer.getResponsabileDAO().getResponsabile(responsabile.getEmail()) == null) {
            datalayer.getUtenteDAO().storeUtente(utente);
            datalayer.getResponsabileDAO().storeResponsabile(responsabile, request.getParameter("nuovaMail"), datalayer.getUtenteDAO().getUtenteByUsername(utente.getNome()).getKey());
        } else throw new DataException("l'username o l'email inserita sono già presenti nel db, si prega di inserire nuovi dati");
        try {
            response.sendRedirect("gestisci_responsabile");
        } catch (IOException ex) {
            handleError(ex, request, response);
        }
    }

    private void modifica_responsabile(HttpServletRequest request, HttpServletResponse response) throws DataException, InvalidKeySpecException, NoSuchAlgorithmException {
        AuleWebDataLayer dataLayer = ((AuleWebDataLayer) request.getAttribute("datalayer"));
        Responsabile responsabile = dataLayer.getResponsabileDAO().getResponsabile(request.getParameter("responsabile"));
        Utente utente = responsabile.getUtente();
        //modifica del responsabile
        responsabile.setNome(request.getParameter("nome")); //modifica nome
        responsabile.setCognome(request.getParameter("cognome")); //modifica cognome
        //come email lascio la precedente per ritrovare il responsabile da modificare, verrà aggiornata successivamente dalla query
        if(request.getParameter("email").length() > 60) throw new DataException("email troppo lunga (>60)");
        dataLayer.getResponsabileDAO().storeResponsabile(responsabile, request.getParameter("email"), utente.getKey());
        //modifica dell'utente
        utente.setNome(request.getParameter("u"));
        utente.setPassword(SecurityHelpers.getPasswordHashPBKDF2(request.getParameter("p")));
        if(utente.getNome().length() > 25) throw new DataException("username troppo lungo (>25)");
        dataLayer.getUtenteDAO().storeUtente(utente);
        //redirect
        try {
            response.sendRedirect("gestisci_responsabile");
        } catch (IOException ex) {
            handleError(ex, request, response);
        }
    }

}
