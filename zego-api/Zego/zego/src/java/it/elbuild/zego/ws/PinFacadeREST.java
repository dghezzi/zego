/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Lang;
import it.elbuild.zego.entities.Pin;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Lu
 */
@Stateless
@Path("pin")
public class PinFacadeREST extends BaseFacadeREST<Integer, Pin> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Pin.class);
        changeMode(ADMIN_ENDPOINT);
    }
    
   
}
