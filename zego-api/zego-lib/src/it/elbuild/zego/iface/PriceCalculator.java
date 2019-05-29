/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.ride.PriceRequest;
import it.elbuild.zego.rest.response.PriceEstimateResponse;




/**
 *
 * @author Lu
 */
public interface PriceCalculator {

    public Riderequest updatePrice(Riderequest r, boolean first) throws RESTException;
    public PriceEstimateResponse estimate(PriceRequest pr, Integer uid) throws RESTException;
    
}
