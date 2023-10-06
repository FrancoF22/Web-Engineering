package it.univaq.f4i.iw.auleweb.controller;

import it.univaq.f4i.iw.auleweb.data.dao.AuleWebDataLayer;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.result.DataModelFiller;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;


public class DummyModelFiller implements DataModelFiller {

    @Override
    public void fillDataModel(Map datamodel, HttpServletRequest request, ServletContext context) {
        //datamodel.put("current_timestamp", Calendar.getInstance().getTime());
        try {
            datamodel.put("home", ((AuleWebDataLayer) request.getAttribute("datalayer")).getGruppoDAO().getAllDipartimenti());
        } catch (DataException ex) {
            Logger.getLogger(DummyModelFiller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
