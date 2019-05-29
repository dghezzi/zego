/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.util.ZegoK;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Lu
 */
@Provider
public class RESTExceptionHandler implements ExceptionMapper<RESTException> {

    @Override
    public Response toResponse(RESTException exception) {
        if (exception.getCode() != null && exception.getCode().equals(ZegoK.Error.AUTHFAIL)) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "403 Access Denied");
            return Response.status(Response.Status.FORBIDDEN).
                    entity("{\"code\": " + exception.getCode() + ",\"msg\": \"" + "Accesso non autorizzato" + "\",\"title\": \"" + "Forbidden" + "\"}").
                    type("application/json")
                    .build();
        } else {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "500 {0} [{1}]", new Object[]{exception.getMsg(), exception.getCode()});
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity("{\"code\": " + exception.getCode() + ",\"msg\": \"" + exception.getMsg() + "\",\"title\": \"" + exception.getTitle() + "\"}").
                    type("application/json")
                    .build();
        }
    }
}
