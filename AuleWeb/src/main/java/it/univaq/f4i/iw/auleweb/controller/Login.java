package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.TemplateManagerException;
import it.univaq.f4i.iw.framework.result.TemplateResult;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.*;
import javax.servlet.http.*;
import it.univaq.f4i.iw.auleweb.data.model.Utente;

/**
 *
 * @author pcela
 */

public class Login extends AuleWebBaseController {
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException {
        try {
            if(request.getParameter("indietro") != null) response.sendRedirect("home");
            if (request.getParameter("login") != null) {
                action_login(request, response);
            } else {
                action_default(request, response);
            }
        } catch (IOException | TemplateManagerException ex) {
            handleError(ex, request, response);
        }
    }
    
    private void action_default(HttpServletRequest request, HttpServletResponse response) throws IOException, TemplateManagerException {
        TemplateResult result = new TemplateResult(getServletContext());
        request.setAttribute("page_title","Accedi");
        result.activate("iscrizione.ftl.html", request, response);
    }
    
    
    
    
    //nota: utente di default nel database: nome n, password p
    //note: default user in the database: name: a, password p
    private void action_login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        String username = request.getParameter("u");    //input username html
        String password = request.getParameter("p");    //input password html

        if (!username.isEmpty() && !password.isEmpty()) {
            if("admin".equals(username) && "admin".equals(password)){
                SecurityHelpers.createSession(request, "amministratore", 0);
                
                response.sendRedirect("home_admin");
                
            } else {
                try {
                    Utente u = ((AuleWebDataLayer) request.getAttribute("datalayer")).getUtenteDAO().getUtenteByEmail(username);
                    if(u == null) throw new DataException("Utente non trovato, username o password errate");
                    System.out.println("PASSWORD: " + u.getPassword());
                    if (SecurityHelpers.checkPasswordHashPBKDF2(password, u.getPassword())) {
                        //se la validazione ha successo
                        SecurityHelpers.createSession(request, u.getEmail(), u.getKey());
                        System.out.println("SESSION RESPONSABILE: " + SecurityHelpers.checkSession(request).getAttribute("username"));
                        //creazione vista responsabile
                        //request.setAttribute("email", r.getEmail());
                        response.sendRedirect("home_responsabile");
                    }
                } catch (NoSuchAlgorithmException | InvalidKeySpecException | DataException ex) {
                    handleError(ex, request, response);
                }
            }
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws javax.servlet.ServletException
     */
    
}
