/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import it.elbuild.zego.entities.Errormsg;
import it.elbuild.zego.iface.ErrorMessageController;
import it.elbuild.zego.persistence.DAOFactory;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Lu
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class ErrorMsgControllerBean implements ErrorMessageController {
    
    private ConcurrentHashMap<String,Errormsg> cache;
    
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
        refreshAll();
    }

    @Override
    public void refreshAll() {
        cache = new ConcurrentHashMap<>();
        for(Errormsg e : factory.getDAOForEntity(Errormsg.class).findAll())
        {
            cache.put(e.getCode()+e.getLang().toLowerCase(), e);
        }     
    }

    @Override
    public Errormsg getErrorByCodeAndLang(Integer code, String lang) {
        return cache.get(code+mapLang(lang).toLowerCase());
    }
    
    private String mapLang(String lang) {
        return lang == null || lang.equals("it") ? "it" : "en";
    }
}
