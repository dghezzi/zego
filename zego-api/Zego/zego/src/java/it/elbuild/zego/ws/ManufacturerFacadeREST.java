/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Area;
import it.elbuild.zego.entities.legacy.Manufacturer;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Lu
 */
@Stateless
@Path("manufacturer")
public class ManufacturerFacadeREST extends BaseFacadeREST<Integer, Manufacturer> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Manufacturer.class);
        changeMode(USER_ENDPOINT);
    }

    
}
