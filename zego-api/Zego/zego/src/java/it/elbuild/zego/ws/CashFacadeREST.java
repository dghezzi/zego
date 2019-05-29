/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Cash;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Lu
 */
@Stateless
@Path("cash")
public class CashFacadeREST extends BaseFacadeREST<Integer, Cash> {

    @PostConstruct
    private void init() {
        ctrl = provider.provide(Cash.class);
        changeMode(ADMIN_ENDPOINT);
    }  
    
   
       
}
