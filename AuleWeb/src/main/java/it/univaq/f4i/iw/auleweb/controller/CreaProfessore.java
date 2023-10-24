package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.auleweb.data.model.Professore;
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
public class CreaProfessore extends AuleWebBaseController {
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, TemplateManagerException, DataException {
        try {
            //freemarker non legge i metodi getUtente().getUsername(), getUtente().getPassword() nella creazione. Nella modifica non ci sono problemi
            TemplateResult res = new TemplateResult(getServletContext());
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            request.setAttribute("utenti", ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessori()); //mi fa mettere il DataException, ma non lo usa il prof - ema 
            res.activate("admin-responsabile.html", request, response);
        } catch (TemplateManagerException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_salva(HttpServletRequest request, HttpServletResponse response, int prof_key) throws IOException, ServletException, TemplateManagerException {
        try {
            TemplateResult res = new TemplateResult(getServletContext());
            //aggiungiamo al template un wrapper che ci permette di chiamare la funzione stripSlashes
            request.setAttribute("strip_slashes", new SplitSlashesFmkExt());
            
            if (prof_key > 0) {
                Professore professore = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessoreById(prof_key);
                if (professore != null) {
                    request.setAttribute("professore", professore);
                    res.activate("admin-responsabile.html", request, response);
                } else {
                    handleError("Undefined professor", request, response);
                }
            } else {
                //utente_key==0 indica un nuovo utente
                Professore professore = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().createProfessore();
                request.setAttribute("utente", professore);
                res.activate("Tabella-Responsabile-admin.html", request, response);
            }
        } catch (DataException ex) {
            handleError("Data access exception: " + ex.getMessage(), request, response);
        }
    }
    
    private void action_update(HttpServletRequest request, HttpServletResponse response, int prof_key) throws IOException, ServletException, TemplateManagerException, NoSuchAlgorithmException, InvalidKeySpecException {
        try {
            Professore professore;
            if (prof_key > 0) {
                professore = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().getProfessoreById(prof_key);
            } else {
                professore = ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().createProfessore();
            }
            if (professore != null && request.getParameter("nome") != null && !request.getParameter("cognome").isEmpty() && request.getParameter("password") != null && !request.getParameter("email").isEmpty()) {
                
                    professore.setNome(SecurityHelpers.addSlashes(request.getParameter("nome")));
                    professore.setCognome(SecurityHelpers.addSlashes(request.getParameter("cognome")));
                    ((AuleWebDataLayer) request.getAttribute("datalayer")).getProfessoreDAO().storeProfessore(professore);
                    //delega il resto del processo all'azione salva
                    action_salva(request, response, professore.getKey());
                
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

        request.setAttribute("page_title", "Modifica Professore");

        int prof_key;
        try {
            if (request.getParameter("k") != null) {
                prof_key = SecurityHelpers.checkNumeric(request.getParameter("k"));
                if (request.getParameter("update") != null) {
                    action_update(request, response, prof_key);
                } else {
                    action_salva(request, response, prof_key);
                }
            } else {
                action_default(request, response);
            }
        } catch (NumberFormatException ex) {
            handleError("Invalid number submitted", request, response);
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | DataException ex) {
            Logger.getLogger(CreaProfessore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
