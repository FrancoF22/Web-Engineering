package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Utente;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.SplitSlashesFmkExt;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author user
 */
public class CreaResponsabile extends AuleWebBaseController {
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataException {
        try {
            //freemarker non legge i metodi getUtente().getUsername(), getUtente().getPassword() nella creazione. Nella modifica non ci sono problemi
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("utenti", ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getAllResponsabili()); //mi fa mettere il DataException, ma non lo usa il prof - ema 
            res.activate("admin-responsabile.html", request, response);
        } catch (TemplateManagerException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_salva(HttpServletRequest request, HttpServletResponse response, int utente_key) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            
            if (utente_key > 0) {
                Utente utente = ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(utente_key);
                if (utente != null) {
                    request.setAttribute("utente", utente);
                    res.activate("admin-responsabile.html", request, response);
                } else {
                    handleError("Undefined user", request, response);
                }
            } else {
                //utente_key==0 indica un nuovo utente
                Utente utente = ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().createUtente();
                request.setAttribute("utente", utente);
                res.activate("Tabella-Responsabile-admin.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_update(HttpServletRequest request, HttpServletResponse response, int utente_key) throws IOException, ServletException, TemplateManagerException, NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            Utente utente;
            if (utente_key > 0) {
                utente = ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtente(utente_key);
            } else {
                utente = ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().createUtente();
            }
            if (utente != null && request.getParameter("nome") != null && !request.getParameter("cognome").isEmpty() && request.getParameter("password") != null && !request.getParameter("email").isEmpty()) {
                
                    utente.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                    utente.setCognome(SecurityHelpers.addSlashes(request.getParameter("cognome")));
                    utente.setEmail(SecurityHelpers.addSlashes(request.getParameter("email")));
                    utente.setPassword(SecurityHelpers.getPasswordHashPBKDF2(request.getParameter("password")));
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().storeUtente(utente);
                    //delega il resto del processo all'azione salva
                    action_salva(request, response, utente.getKey());
                
            } else {
                handleError("Cannot update utente: insufficient parameters", request, response);

            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {

        request.setAttribute("page_title", "Modifica Responsabile");

        int utente_key;
        try {
            if (request.getParameter("k") != null) {
                utente_key = SecurityHelpers.checkNumeric(request.getParameter("k"));
                if (request.getParameter("update") != null) {
                    action_update(request, response, utente_key);
                } else {
                    action_salva(request, response, utente_key);
                }
            } else {
                action_default(request, response);
            }
        } catch (NumberFormatException ex) {
            handleError("Invalid number submitted", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | DataException ex) {
            Logger.getLogger(CreaResponsabile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
