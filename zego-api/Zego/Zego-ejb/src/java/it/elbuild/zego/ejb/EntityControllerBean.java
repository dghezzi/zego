/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

/**
 *
 * @author Lu
 */
import el.persistence.db.DBTuple;
import el.persistence.db.JpaDAO;
import it.elbuild.zego.persistence.DAOFactory;
import java.util.List;
import javax.persistence.NonUniqueResultException;

/**
 *
 * @author Lu
 * @param <K>
 * @param <E>
 */
public abstract class EntityControllerBean<K,E> implements it.elbuild.zego.iface.EntityController<K,E>
{

    private Class entity;
    

    DAOFactory factory;
    
    
    public EntityControllerBean(DAOFactory f, Class c) {
        factory = f;
        entity = c;
    }
    
    @Override
    public E save(E e) {
        return dao().insertOrUpdate(e);
    }

    @Override
    public boolean delete(K k) {
        return dao().delete(k);
    }

    @Override
    public List<E> findAll() {
        return dao().findAll();
    }

    @Override
    public List<E> findRange(Integer start, Integer stop, Boolean asc) {
        return dao().findAll(start, stop-start);
    }

    @Override
    public Long countAll() {
        return dao().count();
    }

    @Override
    public List<E> findFilterAll(String filter, Object o, Integer start, Integer stop) {
        Integer intval = null;
            try
            {
                intval = Integer.parseInt(o.toString());
            }
            catch (Exception e){}
        return dao().findByField(filter, intval == null ? o : intval, start, stop);
    }

    @Override
    public Long findFilterCount(String filter, Object o) {
        Integer intval = null;
            try
            {
                intval = Integer.parseInt(o.toString());
            }
            catch (Exception e){}
        return dao().countByField(filter, intval == null ? o : intval);
    }

    @Override
    public void setType(Class c) {
        entity = c;
    }
    
    protected JpaDAO<K,E> dao()
    {      
        return factory.getDAOForEntity(entity);
    }

    @Override
    public E findById(K k) {
        return dao().findById(k);
    }

    @Override
    public List<E> findListCustom(String query, List<DBTuple> tuples) {  
        return factory.getDAOForEntity(entity).findListCustom(query, tuples.toArray(new DBTuple[tuples.size()]));
    }

    @Override
    public E findFirst(String query, List<DBTuple> tuples, boolean throwOnMultiple) throws javax.persistence.NonUniqueResultException {
        List<E> es = findListCustom(query, tuples);
        if(throwOnMultiple && es != null && es.size()> 1) {
            throw new NonUniqueResultException("The query returned "+es.size()+" results!");
        }
        return es == null || es.isEmpty() ? null : es.get(0);
    }

    @Override
    public Class type() {        
        return entity;
    }

    @Override
    public List<E> findListCustom(String query, List<DBTuple> tuples, Integer start, Integer stop) {       
        return factory.getDAOForEntity(entity).findListCustomPaged(query, start, stop-start, tuples.toArray(new DBTuple[tuples.size()]));
    }
    
    @Override
    public Long countListCustom(String query, List<DBTuple> tuples) {
        return factory.getDAOForEntity(entity).countListCustom(query, tuples.toArray(new DBTuple[tuples.size()]));
    }
    
}
