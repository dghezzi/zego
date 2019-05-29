/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Area;
import it.elbuild.zego.entities.Nextipcall;
import it.elbuild.zego.rest.exception.RESTException;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author Lu
 */
@Stateless
@Path("nextipcall")
public class NextipInternalFacadeREST extends BaseFacadeREST<Integer, Nextipcall> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Nextipcall.class);
        changeMode(ADMIN_ENDPOINT);
    }

    @GET
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public List<Nextipcall> getAll() throws RESTException {
        authenticate();
        return ctrl.findAll();
    }
    
    @GET
    @Path("user/{uid}")
    @Produces({"application/json; charset=UTF-8"})
    public List<Nextipcall> getCombined(@PathParam("uid") Integer uid) throws RESTException {
        authenticateAdmin();
        return ctrl.findListCustom("findCombined", Arrays.asList(new DBTuple("calldst", uid), new DBTuple("callsrc", uid)));
    }
    
}
