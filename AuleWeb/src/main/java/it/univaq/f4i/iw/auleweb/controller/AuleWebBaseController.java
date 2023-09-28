/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.framework.controller.AbstractBaseController;
import it.univaq.f4i.iw.framework.data.DataLayer;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.sql.DataSource;

/**
 *
 * @author franc
 */
public abstract class AuleWebBaseController extends AbstractBaseController{
    /*
    @Override
    protected DataLayer createDataLayer(DataSource ds) throws ServletException {
        try {
            return new AuleWebDataLayer(ds);
        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    } 
*/
}
