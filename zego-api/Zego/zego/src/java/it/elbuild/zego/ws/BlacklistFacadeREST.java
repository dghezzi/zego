/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Blacklist;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Lu
 */
@Stateless
@Path("blacklist")
public class BlacklistFacadeREST extends BaseFacadeREST<Integer, Blacklist> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Blacklist.class);
        changeMode(ADMIN_ENDPOINT);
    }
    
   
}
