/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

import el.persistence.db.JpaDAO;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author Lu
 */
@Singleton
public class DAOFactory {
    
    public final String PERSISTENCE_UNIT = "zegopu";
    private EntityManagerFactory emfa;

    @PostConstruct
    public void init() {
        emfa = null;
    }

    @PreDestroy
    public void clean() {
        if (emfa != null && emfa.isOpen()) {
            emfa.close();
        }
    }
    
    private void createEmfa() {
        if (emfa == null) {
            emfa = javax.persistence.Persistence.createEntityManagerFactory(PERSISTENCE_UNIT);
        }
    }
    
    @SuppressWarnings("unchecked")
    public <K,E> JpaDAO<K, E> getDAOForEntity(Class<E> e)
    {
        createEmfa();
        
        try {
            String name = DAOFactory.class.getPackage().getName()+"."+e.getSimpleName()+"DAO";
            Class eDao = Class.forName(name);
            Constructor<JpaDAO<K, E>> cons = eDao.getConstructor(EntityManagerFactory.class);            
            return  cons.newInstance(emfa);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NullPointerException ex) {
            Logger.getLogger(DAOFactory.class.getName()).log(Level.SEVERE, null, ex);
            return new JpaDAO<K,E>(emfa) {};            
        }         
    }
    
    public NativeSQLDAO getNativeSQLDAO() {
        createEmfa();
        return new NativeSQLDAO(emfa);
    }
}
