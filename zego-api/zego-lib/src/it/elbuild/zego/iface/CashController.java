/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.Pair;

/**
 *
 * @author Lu
 */
public interface CashController {
    
    public Pair<User,Riderequest> updateCashDue(Riderequest r, User driver, User rider) throws RESTException;
    public User insertUserPayment(Integer amount, String ref, User driver) throws RESTException;
}
