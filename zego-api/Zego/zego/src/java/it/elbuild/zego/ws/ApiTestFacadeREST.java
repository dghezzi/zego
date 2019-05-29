/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Address;
import it.elbuild.zego.entities.Apitest;
import it.elbuild.zego.iface.APNSPusher;
import it.elbuild.zego.iface.LegacyMapper;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.ZegoK;
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
@Path("apitest")
public class ApiTestFacadeREST extends BaseFacadeREST<Integer, Apitest> {

    @EJB
    APNSPusher pusher;
    
    @EJB
    LegacyMapper mapper;
    
    @PostConstruct
    private void init() {
        ctrl = provider.provide(Apitest.class);
    }
    
    
    @GET
    @Path("public")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Apitest publicApi() throws RESTException {        
        return ctrl.findById(1);
    }
    
    @GET
    @Path("notification")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Apitest test() throws RESTException {        
        pusher.notifyUser("Ciao sono una notifica push Android", "APA91bEd1rlshQ_Grmg-HO3BlhMvsVn0QsIi7muWK1VXkKhzr70FwnCWWY2OtQ1BywyPHvsjNI6smLox1orl-ZlXNgLjHK_Nq-oNwplYgyhhUXLHsr6zJcsZiJ7KOi1WAcw5A91ZQf0t");
        pusher.notifyUser("C'è una nuova richiesta per te!\nPickup: Viale Adua 405\nDropoff: Viale della Libertà 440\nMarco 4,40 €", "EB62D33B9729917A379BF40F148522D41A0874D42D778633A4AFC9C035B4D2D2");
        return ctrl.findById(1);
    }
    
    @GET
    @Path("private/{id}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Apitest privateApi(@PathParam("id") Integer id) throws RESTException {        
        authenticate(id);
        return ctrl.findById(1);
    }
    
    @GET
    @Path("exception")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Apitest except(@PathParam("id") Integer id) throws RESTException {        
        killWithCode(ZegoK.Error.DRIVER_CANNOT_END_A_RIDE);
        return ctrl.findById(1);
    }
    
    @GET
    @Path("legacy/user")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Apitest legacyuser(@PathParam("id") Integer id) throws RESTException {        
        mapper.createUsers();
        return ctrl.findById(1);
    }
    
    @GET
    @Path("legacy/feedback")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Apitest legacyFeedback(@PathParam("id") Integer id) throws RESTException {        
        mapper.recomputeAvg();
        return ctrl.findById(1);
    }
    
    @GET
    @Path("legacy/ride")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Apitest ride(@PathParam("id") Integer id) throws RESTException {        
        mapper.createRides();
        return ctrl.findById(1);
    }
    
    @GET
    @Path("legacy/driver")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Apitest driver(@PathParam("id") Integer id) throws RESTException {        
        mapper.driver();
        return ctrl.findById(1);
    }
    
    @GET
    @Path("legacy/bday")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public Apitest bday(@PathParam("id") Integer id) throws RESTException {        
        mapper.bday();
        return ctrl.findById(1);
    }
}
