/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Errormsg;
import it.elbuild.zego.iface.ErrorMessageController;
import it.elbuild.zego.rest.exception.RESTException;
import static it.elbuild.zego.ws.BaseFacadeREST.ADMIN_ENDPOINT;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Lu
 */
@Stateless
@Path("errormsg")
public class ErrormsgFacadeREST extends BaseFacadeREST<Integer, Errormsg> {

    @EJB
    ErrorMessageController errorMng;
    
    
    @PostConstruct
    private void init() {
        ctrl = provider.provide(Errormsg.class);
        changeMode(ADMIN_ENDPOINT);
    }
    
   @Override
    @POST
    @Produces({"application/json; charset=UTF-8"})
    public Errormsg createRecord(Errormsg entity) throws RESTException {
        entity = super.createRecord(entity);
        errorMng.refreshAll();
        return entity;
    }
    
    
    @PUT
    @Path("{id}")
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public Errormsg updateRecord(Errormsg entity) throws RESTException {
        entity = super.updateRecord(entity);
        errorMng.refreshAll();
        return entity;
    }
   
}
