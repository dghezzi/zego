/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb.impl;

import it.elbuild.zego.ejb.EntityControllerBean;
import it.elbuild.zego.entities.Paymentaction;
import it.elbuild.zego.persistence.DAOFactory;

/**
 *
 * @author Lu
 */
public class PaymentactionControllerBean extends EntityControllerBean<Integer, Paymentaction> {

    public PaymentactionControllerBean(DAOFactory f) {
        super(f, Paymentaction.class);
    }
    
}
