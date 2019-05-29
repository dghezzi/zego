/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.ride.RideRequestAction;

/**
 *
 * @author Lu
 */
public interface DriverController {
    
    public Riderequest notify(RideRequestAction act) throws RESTException;
    public Riderequest reject(RideRequestAction act) throws RESTException;
    public Riderequest accept(RideRequestAction act) throws RESTException;
    public Riderequest abort(RideRequestAction act) throws RESTException;
    public Riderequest start(RideRequestAction act) throws RESTException;
    public Riderequest end(RideRequestAction act) throws RESTException;
    
}
