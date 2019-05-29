/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Payment;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.RiderequestDrivers;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.entities.Useraction;
import it.elbuild.zego.iface.DriverController;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.PassengerController;
import it.elbuild.zego.iface.RideController;
import it.elbuild.zego.iface.StripeInterface;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.StripeActionRequest;
import it.elbuild.zego.rest.request.UpdateRatingRequest;
import it.elbuild.zego.rest.request.ride.EtaRequest;
import it.elbuild.zego.rest.request.ride.PriceRequest;
import it.elbuild.zego.rest.request.ride.RideRequestAction;
import it.elbuild.zego.rest.response.HeathMapResponse;
import it.elbuild.zego.rest.response.ride.CompatRequest;
import it.elbuild.zego.rest.response.ride.EtaResponse;
import it.elbuild.zego.rest.response.ride.PriceResponse;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import static it.elbuild.zego.ws.BaseFacadeREST.ADMIN_ENDPOINT;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author Lu
 */
@Stateless
@Path("riderequest")
public class RequestFacadeREST extends BaseFacadeREST<Integer, Riderequest> {

    @EJB
    PassengerController passengerController;

    @EJB
    DriverController driverController;

    @EJB
    RideController rideController;
    
    @EJB
    StripeInterface stripe;

    EntityController<Integer, Useraction> actionCtrl;
    
    @PostConstruct
    private void init() {
        ctrl = provider.provide(Riderequest.class);
        actionCtrl = provider.provide(Useraction.class);
        
        changeMode(ADMIN_ENDPOINT);       
    }

    @GET
    @Path("{id}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public Riderequest getById(@PathParam("id") Integer id) throws RESTException {
        authenticate();
        return rideController.get(id, caller());
    }

    @GET
    @Path("shid/{shid}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Riderequest getByShid(@PathParam("shid") String shid) throws RESTException {
        authenticate();
        return rideController.getByShid(shid, caller());
    }

    @GET
    @Path("{id}/drivers")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public List<RiderequestDrivers> getDriversById(@PathParam("id") Integer id) throws RESTException {
        authenticate();
        return rideController.drivers(id);
    }
    
    @Override
    @POST
    @Produces({"application/json; charset=UTF-8"})
    public Riderequest createRecord(Riderequest req) throws RESTException {
        authenticate(req.getPid());
        try {
            return passengerController.sendRequest(req, false);
        } catch (RESTException e) {
            killWithCode(((RESTException) e).getCode());
            return null;
        } catch (Exception e) {
            killWithCode(ZegoK.Error.DEFAULT_ERROR);
            return null;
        }
    }

    @POST
    @Path("force")
    @Produces({"application/json; charset=UTF-8"})
    public Riderequest createRecordForce(Riderequest req) throws RESTException {
        authenticate(req.getPid());
        try {
            return passengerController.sendRequest(req, true);
        } catch (RESTException e) {
            killWithCode(((RESTException) e).getCode());
            return null;
        } catch (Exception e) {
            killWithCode(ZegoK.Error.DEFAULT_ERROR);
            return null;
        }
    }

    @POST
    @Path("eta")
    @Produces({"application/json; charset=UTF-8"})
    public EtaResponse eta(EtaRequest req) throws RESTException {
        authenticate();
        try {
            User u = caller();
            return rideController.eta(req,(u==null? null : u));
        } catch (RESTException e) {
            killWithCode(((RESTException) e).getCode());
            return null;
        } catch (Exception e) {
            killWithCode(ZegoK.Error.DEFAULT_ERROR);
            return null;
        }
    }

    @POST
    @Path("price")
    @Produces({"application/json; charset=UTF-8"})
    public PriceResponse price(PriceRequest req) throws RESTException {
        authenticate();
        try {
            User u = caller();
            if (u == null) {
                killWithCode(ZegoK.Error.USER_NOT_FOUND);
                return null;
            } else {
                return rideController.price(req, u.getId());
            }
        } catch (RESTException e) {
            killWithCode(((RESTException) e).getCode());
            return null;
        } catch (Exception e) {
            killWithCode(ZegoK.Error.DEFAULT_ERROR);
            return null;
        }
    }

    @POST
    @Path("action")
    @Produces({"application/json; charset=UTF-8"})
    public Riderequest act(RideRequestAction action) throws RESTException {
        authenticate(action.getUid());
        Useraction a = new Useraction();
        try {
            Riderequest ret = null;
            
            
            a.setInsdate(RESTDateUtil.getInstance().secondsNow());
            a.setRid(action.getRid());
            a.setType(action.getAction());
            a.setUid(action.getUid());
            a.setUmode(action.getAction() != null && action.getAction().contains("driver") ? "driver" : "rider");
            a.setRaw("");
            
            if(action.getPriceupdate() != null) {
                a.setRaw("PriceUpdate: "+action.getPriceupdate()+"\t");
            }
            
            if(action.getText()!= null) {
                a.setRaw(a.getRaw()+"Text: "+action.getText()+"\t");
            }
            
            a = actionCtrl.save(a);
            
            switch (action.getAction()) {
                case RideRequestAction.DRIVER_REJECT_ACTION:
                    ret = driverController.reject(action);
                    break;
                case RideRequestAction.DRIVER_ACCEPT_ACTION:
                    ret = driverController.accept(action);
                    break;
                case RideRequestAction.DRIVER_ABORT_ACTION:
                    ret = driverController.abort(action);
                    break;
                case RideRequestAction.DRIVER_START_ACTION:
                    ret = driverController.start(action);
                    break;
                case RideRequestAction.DRIVER_END_ACTION:
                    ret = driverController.end(action);
                    break;
                case RideRequestAction.RIDER_ABORT_ACTION:
                    ret = passengerController.abort(action);
                    break;
                case RideRequestAction.RIDER_CANCEL_ACTION:
                    ret = passengerController.cancel(action);
                    break;
                case RideRequestAction.RIDER_TERMINATE_ACTION:
                    ret = passengerController.terminate(action);
                    break;
                case RideRequestAction.DRIVER_IAM_HERE:
                    ret = driverController.notify(action);
                    break;
                default:
                    throw new RESTException(ZegoK.Error.UNKNOWN_ACTION);
            }
            return ret;
        } catch (RESTException e) {
            a.setErrdata(String.valueOf(e.getCode()));
            actionCtrl.save(a);
            killWithCode(((RESTException) e).getCode());
            return null;
        } catch (Exception e) {
            a.setErrdata(String.valueOf("999 "+e.getClass().getName()));
            actionCtrl.save(a);
            killWithCode(ZegoK.Error.DEFAULT_ERROR);
            return null;
        }
    }

    @GET
    @Path("history/{uid}/{mode}/{start}/{stop}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public List<CompatRequest> getDriversById(@PathParam("uid") Integer uid, @PathParam("mode") String mode, @PathParam("start") Integer start, @PathParam("stop") Integer stop) throws RESTException {
        authenticate(uid);
        if (mode.equals(User.UMODE_DRIVER)) {
            return rideController.driverHistory(uid, start, stop);
        } else if (mode.equals(User.UMODE_RIDER)) {
            return rideController.riderHistory(uid, start, stop);
        } else {
            killWithCode(ZegoK.Error.MODE_NOT_SUPPORTED);
            return null;
        }
    }
    
    @GET
    @Path("heathmap/{start}/{stop}/{status}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public List<HeathMapResponse> getDriversById(@PathParam("status") Integer status, @PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {
        authenticate();
        return rideController.heathMap(start, stop, status);
    }
    
    
       
    @GET
    @Path("webhistory/{uid}/{mode}/{start}/{stop}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public List<Riderequest> getWeDriverHistory(@PathParam("uid") Integer uid, @PathParam("mode") String mode, @PathParam("start") String start, @PathParam("stop") String stop) throws RESTException {
        authenticate(uid);
        if (mode.equals(User.UMODE_DRIVER)) {
            return rideController.webDriverHistory(uid, start, stop);
        } else if (mode.equals(User.UMODE_RIDER)) {
            return rideController.webRiderHistory(uid, start, stop);
        } else {
            killWithCode(ZegoK.Error.MODE_NOT_SUPPORTED);
            return null;
        }
    }
    
    @POST
    @Path("payment/main")
    @Produces({"application/json; charset=UTF-8"})
    public Riderequest act(StripeActionRequest action) throws RESTException {
        if(action.getRid() == null || action.getUid() == null){
            killWithCode(ZegoK.Error.MISSING_MANDATORY_FIELDS);
            return null;
        } else {
            Riderequest rr = stripe.action(action);

            return rr;
        }
    }
    
    @POST
    @Path("payment/other")
    @Produces({"application/json; charset=UTF-8"})
    public Payment newpayment(StripeActionRequest action) throws RESTException {
        if(action.getRid() == null || action.getUid() == null || (action.getPid() == null && !action.getAction().equals("newpayment"))){
            killWithCode(ZegoK.Error.MISSING_MANDATORY_FIELDS);
            return null;
        } else {
            if(action.getPid()!=null){
                if(action.getAction().equalsIgnoreCase("refund")) {
                   return stripe.refund(action.getPid(), action.getAmount());
                } else {
                   killWithCode(ZegoK.Error.PAYMENT_ACTION_NOT_ALLOWED);
                   return null; 
                }                
            } else {
                if(!action.getAction().equalsIgnoreCase("newpayment")) {
                   killWithCode(ZegoK.Error.PAYMENT_ACTION_NOT_ALLOWED);
                   return null; 
                } else {
                    return stripe.payExtra (action.getUid(), action.getRid(), action.getAmount(), action.getNote());
                }
            }
            
        }
    }
    
    @POST
    @Path("updaterating/{id}")
    @Produces({"application/json; charset=UTF-8"})
    public Riderequest updateRating(@PathParam("id") Integer id, UpdateRatingRequest action) throws RESTException {
        authenticateAdmin();
        // type = "rider" -> cambia passrating        
        // type = "driver" -> cambia drivrating
        return rideController.updateRating(action,id);
        
    }
    
    @POST
    @Path("priceupdate")
    @Produces({"application/json; charset=UTF-8"})
    public Riderequest updateRating(Riderequest req) throws RESTException {        
        authenticate();
        try {
            return passengerController.priceUpdateRequest(req);
        } catch (RESTException e) {
            killWithCode(((RESTException) e).getCode());
            return null;
        } catch (Exception e) {
            killWithCode(ZegoK.Error.DEFAULT_ERROR);
            return null;
        }
    }
    

}
