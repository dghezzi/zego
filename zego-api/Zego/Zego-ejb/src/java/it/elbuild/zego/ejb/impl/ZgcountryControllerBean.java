/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb.impl;

import it.elbuild.zego.ejb.EntityControllerBean;
import it.elbuild.zego.entities.legacy.Zgcountry;
import it.elbuild.zego.persistence.DAOFactory;

/**
 *
 * @author Lu
 */
public class ZgcountryControllerBean extends EntityControllerBean<Integer, Zgcountry> {

    public ZgcountryControllerBean(DAOFactory f) {
        super(f, Zgcountry.class);
    }
    
}
