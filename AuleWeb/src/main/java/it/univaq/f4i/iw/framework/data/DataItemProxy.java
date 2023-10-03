/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.framework.data;

/**
 *
 * @author franc
 */
public interface DataItemProxy {

    boolean isModified();

    void setModified(boolean dirty);

}