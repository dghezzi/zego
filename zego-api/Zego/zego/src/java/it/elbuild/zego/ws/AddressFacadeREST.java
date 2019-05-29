/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Address;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.RESTDateUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
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
@Path("address")
public class AddressFacadeREST extends BaseFacadeREST<Integer, Address> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Address.class);
    }

    @Override
    @POST
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public Address createRecord(Address entity) throws RESTException {
        authenticate(entity.getUid());
        
        List<Address> adds = ctrl.findListCustom("findByUidAndType", Arrays.asList(new DBTuple("uid", entity.getUid()),new DBTuple("type", entity.getType())));
        for(Address a : adds) {
            ctrl.delete(a.getId());
        }
        entity.setInsdate(RESTDateUtil.getInstance().secondsNow());
        
        return ctrl.save(entity);
    }
    
    @GET
    @Path("filter/uid/{uid}/range/{start}/{stop}")
    @Produces({"application/json; charset=UTF-8"})
    public List<Address> getFilteredUid(@PathParam("start") Integer start, @PathParam("stop") Integer stop, @PathParam("param") String param, @PathParam("uid") Integer uid) throws RESTException {
        authenticate(uid);
        return ctrl.findFilterAll("uid", String.valueOf(uid), start, stop);
    }
    
    @GET
    @Path("{uid}/last/{type}")
    @Produces({"application/json; charset=UTF-8"})
    public List<Address> getLastNumPickup(@PathParam("type") String type, @PathParam("uid") Integer uid) throws RESTException {
        authenticate(uid);
        Address home = ctrl.findFirst("findByUidAndType", Arrays.asList(new DBTuple("uid",uid),new DBTuple("type", "home")), false);
        Address work = ctrl.findFirst("findByUidAndType", Arrays.asList(new DBTuple("uid",uid),new DBTuple("type", "work")), false);
        List<Address> addr = new ArrayList<>();
        if(home!=null){
            addr.add(home);
        }
        
        if(work!=null) {
            addr.add(work);
        }            
        addr.addAll(ctrl.findListCustom("findLastByTypeAndUidNative", Arrays.asList(new DBTuple("uid", uid))));
        return addr;
    }
    
    
   
}
