/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.ride.RideRequestAction;
import it.elbuild.zego.rest.response.StripeCard;
import java.util.List;

/**
 *
 * @author Lu
 */
public interface PassengerController {
   
    public List<StripeCard> userCards(Integer uid) throws RESTException;
    public List<StripeCard> changeDefaultCard(StripeCard sd, Integer uid) throws RESTException;
    public List<StripeCard> deleteUserCard(StripeCard sd, Integer uid) throws RESTException;
    public Riderequest sendRequest(Riderequest req, boolean force) throws RESTException;
    public Riderequest priceUpdateRequest(Riderequest req) throws RESTException;

    public User paydebth(User u) throws RESTException;
    public User probono(User u) throws RESTException;
    public User changeMode(User uid, String newmode) throws RESTException;
    public Riderequest cancel(RideRequestAction act) throws RESTException;
    public Riderequest systemCancel(Riderequest req) throws RESTException;
    public Riderequest abort(RideRequestAction act) throws RESTException; 
    public Riderequest terminate(RideRequestAction act) throws RESTException; 
}
