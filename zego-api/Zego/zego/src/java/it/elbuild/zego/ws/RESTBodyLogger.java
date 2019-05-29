/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 *
 * @author Lu
 */

    @Provider
    public class RESTBodyLogger implements WriterInterceptor {


    @Override
    public void aroundWriteTo(WriterInterceptorContext context) throws IOException, WebApplicationException {
        OutputStream originalStream = context.getOutputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        context.setOutputStream(baos);
        try {
            context.proceed();
        } finally {            
            baos.writeTo(originalStream);
            baos.close();
            context.setOutputStream(originalStream);
        }
    }

}

