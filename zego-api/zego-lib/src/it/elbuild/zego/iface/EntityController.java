/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import el.persistence.db.DBTuple;
import java.util.List;

/**
 *
 * @author Lu
 * @param <K>
 * @param <E>
 */
public interface EntityController<K,E> {
    
    public void setType(Class c);
    public Class type();
    public E save(E e);
    public boolean delete(K k);
    public List<E> findAll();
    public E findById(K k);
    public List<E> findRange(Integer start, Integer stop, Boolean asc);
    public Long countAll();
    public List<E> findFilterAll(String filter, Object o, Integer start, Integer stop);
    public Long findFilterCount(String filter, Object o);
    public List<E> findListCustom(String query, List<DBTuple> tuples);
    public Long countListCustom(String query, List<DBTuple> tuples);
    public List<E> findListCustom(String query, List<DBTuple> tuples, Integer start, Integer stop);
    public E findFirst(String query, List<DBTuple> tuples, boolean throwOnMultiple) throws javax.persistence.NonUniqueResultException;
}
