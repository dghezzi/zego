/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Address;
import it.elbuild.zego.entities.City;
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
@Path("city")
public class CityFacadeREST extends BaseFacadeREST<Integer, City> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(City.class);
        changeMode(ADMIN_ENDPOINT);
    }  
    
    @GET
    @Path("pfx/{pfx}")
    @Produces({"application/json; charset=UTF-8"})
    public List<City> getFilteredUid(@PathParam("pfx") String pfx) throws RESTException {
        authenticate();
        return ctrl.findListCustom("findByPfx", Arrays.asList(new DBTuple("pfx", pfx.toLowerCase()+"%")));
    }
       
}
