/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

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
public class OtherExceptionHandler implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
                    Logger.getLogger(getClass().getName()).log(Level.SEVERE, "500 {0} [{1}]", new Object[]{exception.getClass().getSimpleName(), exception.getLocalizedMessage()});

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).
                    entity("{\"code\": " + "500" +",\"msg\": \"" + exception.getClass().getSimpleName()+": "+exception.getLocalizedMessage()+ "\",\"title\": \"" + "\"}").
                    type("application/json")
                    .build();
    }
}


