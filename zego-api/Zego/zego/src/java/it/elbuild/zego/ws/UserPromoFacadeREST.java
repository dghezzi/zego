/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Promo;
import it.elbuild.zego.entities.Userpromo;
import it.elbuild.zego.util.RESTDateUtil;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author Lu
 */
@Stateless
@Path("userpromo")
public class UserPromoFacadeREST extends BaseFacadeREST<Integer, Userpromo> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Userpromo.class);
        changeMode(ADMIN_ENDPOINT);
    }
    
    @GET
    @Path("uid/{uid}")
    @Produces({"application/json; charset=UTF-8"}) 
    public List<Userpromo> getUserPromo(@PathParam("uid") Integer uid) {
       List<Userpromo> up = ctrl.findListCustom("findNotUsedByUid", Arrays.asList(new DBTuple("uid", uid),new DBTuple("now", RESTDateUtil.getInstance().secondsNow())));        
       for(Userpromo u : up) {
           u.getPromo().setCode("");
           if(u.getPromo().getType().equals(Promo.FREERIDE) || u.getPromo().getType().equals(Promo.FREERIDE)) {
               u.getPromo().setValue(u.getValueleft());
           }
       }
       return up;
    }
    
    @GET
    @Path("uid/all/{uid}")
    @Produces({"application/json; charset=UTF-8"}) 
    public List<Userpromo> getAllUserPromo(@PathParam("uid") Integer uid) {
       List<Userpromo> up = ctrl.findListCustom("findByUid", Arrays.asList(new DBTuple("uid", uid))); 
       for(Userpromo u : up) {
           u.getPromo().setCode("");
           if(u.getPromo().getType().equals(Promo.FREERIDE) || u.getPromo().getType().equals(Promo.FREERIDE)) {
               u.getPromo().setValue(u.getValueleft());
           }
       }
       return up;
    }
    
    
   
}
