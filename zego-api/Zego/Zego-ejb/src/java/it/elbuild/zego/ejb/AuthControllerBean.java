/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.AuthController;
import it.elbuild.zego.persistence.DAOFactory;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Lu
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN) 
public class AuthControllerBean implements AuthController {

    private ConcurrentHashMap<String, User> cache;

    @EJB
    DAOFactory factory;

    @PreDestroy
    public void end() {
        cache.clear();
        cache = null;
    }

    @PostConstruct
    public void init() {
        cache = new ConcurrentHashMap<>();
        for (User u : factory.getDAOForEntity(User.class).findAll()) {
            if (u.getStatus().equals(User.STATUS_ACTIVE)) {
                if (u.getZegotoken() != null) {
                    cache.put(u.getZegotoken(), u);
                }
            }
        }
    }

    @Override   
    public User get(String token) {
        if (token != null) {
            return cache.get(token);
        } else {
            return null;
        }
    }

    @Override
    public void erase(String token) {
        if (token != null) {
            cache.remove(token);
        }
    }

    @Override
    public void addOrUpdate(User p) {
        if(p.getZegotoken()!=null) {
            cache.put(p.getZegotoken(), p);
        }
    }

}
