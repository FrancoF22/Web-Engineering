package it.univaq.f4i.iw.auleweb.data.impl;

import it.univaq.f4i.iw.auleweb.data.model.Professore;
import it.univaq.f4i.iw.framework.data.DataItemImpl;

/**
 *
 * @author franc
 */
public class ProfessoreImpl extends DataItemImpl<Integer> implements Professore{
    
    private String nome, cognome;
    
    public ProfessoreImpl(){
        super();
        this.nome = "" ;
        this.cognome = "";
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getCognome() {
        return cognome;
    }

    @Override
    public void setNome(String n) {
        this.nome = n;
    }

    @Override
    public void setCognome(String c) {
        this.cognome = c;
    }
    
}
