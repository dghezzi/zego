/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.UpdateRatingRequest;
import it.elbuild.zego.rest.request.ride.EtaRequest;
import it.elbuild.zego.rest.request.ride.PriceRequest;
import it.elbuild.zego.rest.response.HeathMapResponse;
import it.elbuild.zego.rest.response.ride.CompatRequest;
import it.elbuild.zego.rest.response.ride.EtaResponse;
import it.elbuild.zego.rest.response.ride.PriceResponse;
import java.util.List;

/**
 *
 * @author Lu
 */
public interface RideController {
    
    public Riderequest updateRating(UpdateRatingRequest req, Integer rid) throws RESTException;
    public EtaResponse eta(EtaRequest er, User uid) throws RESTException;
    public PriceResponse price(PriceRequest pr, Integer uid) throws RESTException;
    public Riderequest getByShid(String shid, User caller) throws RESTException;
    public Riderequest get(Integer rid, User caller) throws RESTException;
    public List<RiderequestDrivers> drivers(Integer id) throws RESTException;
    public List<CompatRequest> riderHistory(Integer uid, Integer start, Integer stop);
    public List<CompatRequest> driverHistory(Integer uid, Integer start, Integer stop);
    public List<Riderequest> webRiderHistory(Integer uid, String start, String stop);
    public List<Riderequest> webDriverHistory(Integer uid, String start, String stop);
    public List<HeathMapResponse> heathMap(String start, String stop, Integer status);
}
