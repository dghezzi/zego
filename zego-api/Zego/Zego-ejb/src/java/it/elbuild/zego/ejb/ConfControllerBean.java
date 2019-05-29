/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.persistence.DAOFactory;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;

/**
 *
 * @author Lu
 */
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN) 
public class ConfControllerBean implements ConfController {

    private ConcurrentHashMap<String,Conf> cache;
    
    @EJB
    DAOFactory factory;
    
    @PreDestroy
    public void end()
    {
       cache.clear();
       cache = null;     
    }
    
    @PostConstruct
    public void init()
    {
        refreshConf();
    }
    
    @Override
    public void refreshConf() {
        cache = new ConcurrentHashMap<>();
        for(Conf c : factory.getDAOForEntity(Conf.class).findAll())
        {
            cache.put(c.getK(), c);
        }            
    }

    @Override
    public Conf getConfByKey(String k) {
        return cache.get(k);
    }
    
}
