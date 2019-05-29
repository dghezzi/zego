/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.iface.ObfuscationController;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.Coordinate;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
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
@Path("obfuscate")
public class ObfuscationFacadeREST  {

    @EJB
    ObfuscationController o;
    
    @PostConstruct
    private void init() {

    }
    
    
   
    
    @GET
    @Path("{lat}/{lng}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Coordinate except(@PathParam("lat") Double lat, @PathParam("lng") Double lng) throws RESTException {        
        return o.obfuscate(new Coordinate(lat,lng));
    }
}
