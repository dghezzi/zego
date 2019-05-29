/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.persistence.DAOFactory;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author Lu
 */
@Stateless
public class DAOProviderBean implements DAOProvider{

    @EJB
    DAOFactory factory;
    
    @Override
    public EntityController provide(Class c) {
        try {
            String name = DAOProviderBean.class.getPackage().getName()+".impl."+c.getSimpleName()+"ControllerBean";
            Class controller = Class.forName(name);
            Constructor<EntityController> cons = controller.getConstructor(DAOFactory.class);            
            return  cons.newInstance(factory);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NullPointerException ex) {
            Logger.getLogger(DAOFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;            
        }
        
    }
    
}
