/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Feedback;
import it.elbuild.zego.entities.Location;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Lu
 */
@Stateless
@Path("feedback")
public class FeedbackFacadeREST extends BaseFacadeREST<Integer, Feedback> {

    EntityController<Integer,User> userCtrl;
    
    EntityController<Integer,Riderequest> reqCtrl;
    
    @PostConstruct
    private void init() {
        ctrl = provider.provide(Feedback.class);
        userCtrl = provider.provide(User.class);
        reqCtrl = provider.provide(Riderequest.class);
        
        changeMode(USER_ENDPOINT);
    }
    
    
    @POST
    @Override
    @Produces({"application/json; charset=UTF-8"})    
    public Feedback createRecord(Feedback entity) throws RESTException {
        authenticate();
        User u = userCtrl.findById(entity.getUid());
        User sender = userCtrl.findById(entity.getSender());
        Riderequest req = reqCtrl.findById(entity.getRid());
        
        if(u == null || sender == null) {
            throw new RESTException(ZegoK.Error.USER_NOT_FOUND);
        }
        
        if(req == null){
            throw new RESTException(ZegoK.Error.REQUEST_NOT_FOUND);
        }
        
        if(!(req.getStatus().equals(Riderequest.REQUEST_STATUS_ENDED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_PAID) || req.getStatus().equals(Riderequest.REQUEST_PAYMENT_FAILED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED) || req.getStatus().equals(Riderequest.REQUEST_STATUS_REFUNDED))) {
            throw new RESTException(ZegoK.Error.CANNOT_CREATE_FEEDBACK);
        }
        
        if(entity.getRating() == 0) {
            entity.setRating(5);
        }
        Boolean fromDriver = sender.getId().equals(req.getDid());
        
        if(fromDriver) {            
            // feedback already released
            if(req.getPassrating() != null && req.getPassrating() > 0) {
                // do nothing
            } else {
                req.setPassrating(entity.getRating());
                reqCtrl.save(req);
                
                Long ctr = ctrl.findFilterCount("uid", u.getId());
                u.setPavg((ctr*u.getPavg()+entity.getRating())/(ctr+1.0d));
                userCtrl.save(u);
            }
            
            sender.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
            sender.setCurrent(null);
            userCtrl.save(sender);            
        } else {            
            // feedback already released
            if(req.getDrivrating() != null && req.getDrivrating() > 0) {
                // do nothing
            } else {
                req.setDrivrating(entity.getRating());
                reqCtrl.save(req);
                
                Long ctr = ctrl.findFilterCount("uid", u.getId());
                u.setDavg((ctr*u.getDavg()+entity.getRating())/(ctr+1.0d));
                userCtrl.save(u);
            }
            
            sender.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
            sender.setCurrent(null);
            userCtrl.save(sender);
        }
        entity.setFromdriver(fromDriver ? 1 : 0);
        entity.setInsdate(RESTDateUtil.getInstance().secondsNow());
        return ctrl.save(entity);
    }
    
   
}
