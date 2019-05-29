/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Zone;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Lu
 */
@Stateless
@Path("zone")
public class ZoneFacadeREST extends BaseFacadeREST<Integer, Zone> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Zone.class);
        changeMode(ADMIN_ENDPOINT);
    }
    
   
}
