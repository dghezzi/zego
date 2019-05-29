/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.Payment;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.StripeActionRequest;
import it.elbuild.zego.rest.request.StripeCreateCustomerRequest;
import it.elbuild.zego.rest.response.StripeCard;
import java.util.List;




/**
 *
 * @author Lu
 */
public interface StripeInterface {
        
    public User createStripeUser(StripeCreateCustomerRequest req) throws RESTException;
    public List<StripeCard> userCards(Integer uid) throws RESTException;
    public List<StripeCard> chageDefaultCard(Integer uid, StripeCard card) throws RESTException;
    public List<StripeCard> deleteCard(Integer uid, StripeCard card) throws RESTException;
    public Riderequest preauthorize(Riderequest req) throws RESTException;
    public Riderequest capture(Riderequest req, boolean penalty) throws RESTException;
    public Riderequest release(Riderequest req) throws RESTException;
    public boolean attemptPayUserDebt(User u, boolean fromscheduler) throws RESTException;
    
    public Payment payExtra(Integer uid, Integer rid, Integer amount, String note) throws RESTException;
    public Payment refund(Integer p, Integer amount) throws RESTException;
    public Riderequest action(StripeActionRequest act) throws RESTException;
}
