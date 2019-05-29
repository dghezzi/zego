/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Nextipcall;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.Thirdparty;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.NextipRequest;
import it.elbuild.zego.util.NextipResponse;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author Lu
 */
@Stateless
@Path("nextip_connection")
public class NextipFacadeREST {

    private static final String NEXTIPTOKEN = "nxt_6a383aa8-d9b2-4fff-a2a3-f64a03699190";

    @Context
    HttpHeaders headers;
    @EJB
    DAOProvider provider;
    
    EntityController<Integer,User> userCtrl;
    
    EntityController<Integer,Riderequest> rideCtrl;
    
    EntityController<Integer,Nextipcall> nextCtrl;
    
    EntityController<Integer,Thirdparty> tpCtrl;
    
    @PostConstruct
    private void init() {
        userCtrl = provider.provide(User.class);
        rideCtrl = provider.provide(Riderequest.class);
        nextCtrl = provider.provide(Nextipcall.class);
        tpCtrl = provider.provide(Thirdparty.class);
        
    }
    
    @POST
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public NextipResponse nextip(NextipRequest req) throws RESTException {
        
        List<String> tks = headers.getRequestHeader(ZegoK.Auth.ACCESSTOKEN);
        String token =  tks == null || tks.isEmpty() ? null : tks.get(0);
        if(token == null || !token.equalsIgnoreCase(NEXTIPTOKEN)) {
            throw new RESTException(ZegoK.Error.AUTHFAIL, "You are not authorized to call this API.");
        } else {
            Nextipcall nc = new Nextipcall();
            nc.setCalldate(RESTDateUtil.getInstance().secondsNow());
            nc.setCaller(req.getPhone_number());
            
            Thirdparty tp = new Thirdparty();
            tp.setPath("/zego/v1/nextip_connection");
            tp.setReqdate(RESTDateUtil.getInstance().secondsNow());
            
            // FIXME, se ci sono due utenti con lo stesso mobile e area code diverso prende uno a caso
            User u = userCtrl.findFirst("findByMobile", Arrays.asList(new DBTuple("mobile", req.getPhone_number())), false);
            if(u==null) {
                nc.setApiresult(NextipResponse.CALLER_NOT_FOUND);
                nextCtrl.save(nc);
                tp.setRespcode("500");
                tpCtrl.save(tp);
                return new NextipResponse(NextipResponse.CALLER_NOT_FOUND, "");
            } else if(u.getCurrent() == null || u.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_FEEDBACKDUE) || u.getRtstatus().equals(User.REALTIME_STATUS_PASSENGER_FEEDBACK_DUE)){
                nc.setCallsrc(u.getId());
                nc.setApiresult(NextipResponse.CONNECTION_NOT_FOUND);
                nextCtrl.save(nc);
                tp.setRespcode("500");
                tpCtrl.save(tp);
                return new NextipResponse(NextipResponse.CONNECTION_NOT_FOUND, "");
            } else {
                Riderequest rr = rideCtrl.findById(u.getCurrent());
                nc.setCallsrc(u.getId());
                
                if(rr == null){
                    nc.setApiresult(NextipResponse.CONNECTION_NOT_FOUND);
                    nextCtrl.save(nc);
                    tp.setRespcode("500");
                    tpCtrl.save(tp);
                    return new NextipResponse(NextipResponse.CONNECTION_NOT_FOUND, "");
                } else {
                    User connection = null;
                    if(u.getId().equals(rr.getPid())) {
                        connection = userCtrl.findById(rr.getDid());                        
                    } else {
                        connection = userCtrl.findById(rr.getPid());        
                    }
                    
                    if(connection!=null){
                        nc.setCalldst(connection.getId());
                        nc.setApiresult(NextipResponse.OK);
                        nc.setRid(rr.getId());
                        nextCtrl.save(nc);
                        tp.setRespcode("200");
                        tpCtrl.save(tp);
                        return new NextipResponse(NextipResponse.OK, (connection.getPrefix()+connection.getMobile()).trim().replaceAll(" ", ""));
                    } else {
                        nc.setApiresult(NextipResponse.CONNECTION_NOT_FOUND);
                        nextCtrl.save(nc);
                        tp.setRespcode("500");
                        tpCtrl.save(tp);
                        return new NextipResponse(NextipResponse.CONNECTION_NOT_FOUND, "");
                    }
                }
            }
        }
    }
       
    
}
