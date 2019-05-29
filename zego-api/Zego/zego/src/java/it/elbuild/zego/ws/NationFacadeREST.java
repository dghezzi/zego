/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Area;
import it.elbuild.zego.entities.Nation;
import it.elbuild.zego.rest.exception.RESTException;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

/**
 *
 * @author Lu
 */
@Stateless
@Path("nation")
public class NationFacadeREST extends BaseFacadeREST<Integer, Nation> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Nation.class);
        changeMode(ADMIN_ENDPOINT);
    }

    @GET
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    @Override
    public List<Nation> getAll() throws RESTException {
        authenticate();
        return ctrl.findAll();
    }
    
}
