/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package it.univaq.f4i.iw.framework.data;

/**
 *
 * @author franc
 * @param <KT>
 */
public interface DataItem<KT> {

    KT getKey();

    long getVersion();

    void setKey(KT key);

    void setVersion(long version);

}
