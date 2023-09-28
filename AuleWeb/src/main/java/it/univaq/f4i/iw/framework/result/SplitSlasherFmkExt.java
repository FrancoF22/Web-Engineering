/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package it.univaq.f4i.iw.framework.result;

import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import it.univaq.f4i.iw.framework.security.SecurityHelpers;
import java.util.List;

/**
 *
 * @author franc
 */
public class SplitSlasherFmkExt implements TemplateMethodModelEx {

    @Override
    public Object exec(List list) throws TemplateModelException {
        //la lista contiene i parametri passati alla funzione nel template
        //the list contains the parameters passed to the function in the template
        if (!list.isEmpty()) {
            return SecurityHelpers.stripSlashes(list.get(0).toString());
        } else {
            //e' possibile ritornare qualsiasi tipo che sia gestibile da Freemarker (scalari, hash, liste)
            //it is possible tor eturn any data type recognized by Freemarker (scalars, hashes, lists)
            return "";
        }
    }
}
