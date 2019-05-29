/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb.impl;

import it.elbuild.zego.ejb.EntityControllerBean;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.persistence.DAOFactory;

/**
 *
 * @author Lu
 */
public class RiderequestControllerBean extends EntityControllerBean<Integer, Riderequest> {

    public RiderequestControllerBean(DAOFactory f) {
        super(f, Riderequest.class);
    }
    
}
