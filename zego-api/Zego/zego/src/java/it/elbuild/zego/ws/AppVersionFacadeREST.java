/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Address;
import it.elbuild.zego.entities.Appversion;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Lu
 */
@Stateless
@Path("appversion")
public class AppVersionFacadeREST extends BaseFacadeREST<Integer, Appversion> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Appversion.class);
        changeMode(ADMIN_ENDPOINT);
    }
    
   
}
