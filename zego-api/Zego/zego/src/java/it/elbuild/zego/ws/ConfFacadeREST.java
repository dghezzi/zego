/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.SignupFunnelController;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.BootRequest;
import static it.elbuild.zego.ws.BaseFacadeREST.ADMIN_ENDPOINT;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Lu
 */
@Stateless
@Path("conf")
public class ConfFacadeREST extends BaseFacadeREST<Integer, Conf> {

    @EJB
    SignupFunnelController signupController;
    
    @EJB
    ConfController confController;
    
    @PostConstruct
    private void init() {
        ctrl = provider.provide(Conf.class);
        changeMode(ADMIN_ENDPOINT);
    }
    
    @POST
    @Path("global")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public List<Conf> getGlobal(BootRequest br) throws RESTException {
        return signupController.globalConf(br);
    }
   
    @POST
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public Conf createRecord(Conf entity) throws RESTException {
        entity = super.createRecord(entity);
        confController.refreshConf();
        return entity;
    }
}
