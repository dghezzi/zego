/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.entities.Dialog;
import static it.elbuild.zego.ws.BaseFacadeREST.ADMIN_ENDPOINT;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.Path;

/**
 *
 * @author Lu
 */
@Stateless
@Path("dialog")
public class DialogFacadeREST extends BaseFacadeREST<Integer, Dialog> {
           
    
    @PostConstruct
    private void init() {
        ctrl = provider.provide(Dialog.class);
        changeMode(ADMIN_ENDPOINT);
    }
        
}
