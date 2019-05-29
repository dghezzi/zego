/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb.impl;

import it.elbuild.zego.ejb.EntityControllerBean;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.persistence.DAOFactory;

/**
 *
 * @author Lu
 */
public class UserControllerBean extends EntityControllerBean<Integer, User> {

    public UserControllerBean(DAOFactory f) {
        super(f, User.class);
    }
    
}
