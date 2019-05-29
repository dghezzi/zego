/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Banhistory;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Lu
 */
@Stateless
@Path("banhistory")
public class BanHistoryFacadeREST extends BaseFacadeREST<Integer, Banhistory> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Banhistory.class);
        changeMode(ADMIN_ENDPOINT);
    }

    
    
}
