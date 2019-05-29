/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import it.elbuild.zego.util.ZegoK;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Lu
 */
@Provider
@Stateless
public class BaseRequestFilter implements ContainerRequestFilter {

    private final int maxEntitySize = 1024 * 8;
    private HashSet<String> SKIPAUTHPATHS;
    private HashSet<String> SKIPAUTHPFX;
    
    
    @PostConstruct
    private void init() {
        SKIPAUTHPATHS = new HashSet<>(Arrays.asList("/apitest/public","/apitest/notification", "/user/signup", "/user/adminexport","/user/login", "/user/autologin", "/conf/global"));
        SKIPAUTHPFX = new HashSet<>(Arrays.asList("a", "b"));
    }
    
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String tk = requestContext.getHeaders().getFirst(ZegoK.Auth.ACCESSTOKEN);
        String sk = requestContext.getHeaders().getFirst(ZegoK.Auth.SKIPTOKEN);
        String path = requestContext.getUriInfo().getPath();
        String method = requestContext.getMethod();

        // if the request already contains a skiptoken, kill it with a 401
        if(sk!=null) {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"code\": 401, \"msg\": \"Authentication needed.\"}").build());
        } else if (tk != null || SKIPAUTHPATHS.contains(path) || path.startsWith("/export") || method.equalsIgnoreCase("OPTIONS")) {
            // delegate to the facade
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("{\"code\": 401, \"msg\": \"Authentication needed.\"}").build());
        }

        if (ZegoK.Tuning.LOGGING) {
            Logger.getLogger(getClass().getName()).log(Level.INFO, "REQUEST: {0} {1}\t{2}", new Object[]{method, path, tk == null ? "NOAUTH" : tk});
            final StringBuilder b = new StringBuilder();
            if (requestContext.hasEntity()) {
                requestContext.setEntityStream(
                        logInboundEntity(b, requestContext.getEntityStream(),
                                Charset.forName("UTF-8")));

            }
        }

    }

    private InputStream logInboundEntity(final StringBuilder b, InputStream stream, final Charset charset) throws IOException {
        if (!stream.markSupported()) {
            stream = new BufferedInputStream(stream);
        }
        stream.mark(maxEntitySize + 1);
        final byte[] entity = new byte[maxEntitySize + 1];
        final int entitySize = stream.read(entity);
        b.append(new String(entity, 0, Math.min(entitySize, maxEntitySize), charset));
        if (entitySize > maxEntitySize) {
            b.append("...more...");
        }
        b.append('\n');
        stream.reset();
        return stream;
    }

}
