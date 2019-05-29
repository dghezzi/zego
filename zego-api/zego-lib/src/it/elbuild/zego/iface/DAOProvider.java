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
public interface DAOProvider {
    
    public EntityController provide(Class c);
    
}
