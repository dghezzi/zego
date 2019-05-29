/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import elbuild.file.FileTextUtil;
import it.elbuild.zego.entities.Errordata;
import it.elbuild.zego.entities.Errormsg;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.AuthController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.ErrorMessageController;
import it.elbuild.zego.persistence.DAOFactory;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.response.CountResponse;
import it.elbuild.zego.util.GenericSearch;
import it.elbuild.zego.util.MandrillMapper;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

/**
 *
 * @author Lu
 * @param <K>
 * @param <E>
 */
public class BaseFacadeREST<K, E> {

    @Context
    HttpHeaders headers;

    
    @EJB
    AuthController auth;

    @EJB
    DAOFactory factory;

    @EJB
    ErrorMessageController errorCtrl;
    
    
    EntityController<Integer,Errordata> errorLogCtrl;
    
    @EJB
    DAOProvider provider;

    EntityController<K, E> ctrl;

    private Integer mode;
    protected static final Integer ADMIN_ENDPOINT = 0;
    protected static final Integer USER_ENDPOINT = 1;

    @PostConstruct
    private void init() {
        mode = USER_ENDPOINT;
        errorLogCtrl = provider.provide(Errordata.class);
    }

    @GET
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public List<E> getAll() throws RESTException {
        authenticateAdmin();
        return ctrl.findAll();
    }

    @GET
    @Path("{id}")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public E getById(@PathParam("id") K id) throws RESTException {
        if (Objects.equals(mode, ADMIN_ENDPOINT)) {
            authenticateAdmin();
        } else {
            authenticate();
        }
        return ctrl.findById(id);
    }

    @GET
    @Path("range/{start}/{stop}/{dir}")
    @Produces({"application/json; charset=UTF-8"})
    public List<E> getRange(@PathParam("start") Integer start, @PathParam("stop") Integer stop, @PathParam("dir") String dir) throws RESTException {
        authenticateAdmin();
        return ctrl.findRange(start, stop, dir.equalsIgnoreCase("asc"));
    }

    @GET
    @Path("filter/{param}/{value}/range/{start}/{stop}")
    @Produces({"application/json; charset=UTF-8"})
    public List<E> getFiltered(@PathParam("start") Integer start, @PathParam("stop") Integer stop, @PathParam("param") String param, @PathParam("value") String value) throws RESTException {
        authenticateAdmin();
        return ctrl.findFilterAll(param, value, start, stop);
    }

    @POST
    @Path("advancedsearch/{start}/{stop}")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public List<E> advancedSearch(@PathParam("start") Integer start, @PathParam("stop") Integer stop, GenericSearch gs) throws RESTException {
        authenticateAdmin();
        gs.setEntity(ctrl.type());
        return (List<E>) factory.getNativeSQLDAO().searchByGenericSearch(gs, start, stop, ctrl.type());
    }
    
    @POST
    @Path("advancedexport/{start}/{stop}")
    @Produces("text/csv")
    @Consumes({"application/json; charset=UTF-8"})
    public Response advancedExport(@PathParam("start") Integer start, @PathParam("stop") Integer stop, GenericSearch gs) throws RESTException {
        authenticateAdmin();
        gs.setEntity(ctrl.type());
        List<E> ll =  (List<E>) factory.getNativeSQLDAO().searchByGenericSearch(gs, start, stop, ctrl.type());
        StringBuilder sb = new StringBuilder();
        Integer i = 0;
        Collection h = null;
        for (E e : ll) {
            
            if(i == 0){
                h = MandrillMapper.param(e).keySet();
                int j = 0;
                for(Object o : h){
                    sb.append(o.toString()).append( j == h.size()-1 ? "" : ";");                            
                }
                sb.append("\n");
            }
                Map<String, String> d = MandrillMapper.param(e);
                int j = 0;
                for(Object o : h){
                    sb.append(d.get(o.toString())).append( j == d.size()-1 ? "" : ";");                            
                }
                sb.append("\n");
                i++;
        }
        return fileize(sb, "zego_export_"+RESTDateUtil.getInstance().secondsNow()+".csv");
    }

    protected Response fileize(StringBuilder sb, String fname) {
        File file = new File("/tmp/"+fname);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException ex) {
                    Logger.getLogger(PaymentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            try {
                FileTextUtil.setContents(file, sb.toString());
            } catch (IOException ex) {
                Logger.getLogger(PaymentFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            }
            Response.ResponseBuilder response = Response.ok((Object) file);
            response.header("Content-Disposition",
                    "attachment; filename="+fname);
            return response.build();
    }
    @POST
    @Path("advancedcount")
    @Produces({"application/json; charset=UTF-8"})
    @Consumes({"application/json; charset=UTF-8"})
    public CountResponse advancedCount(GenericSearch gs) throws RESTException {
        authenticateAdmin();
        gs.setEntity(ctrl.type());
        CountResponse cr = new CountResponse();
        cr.setEntity(ctrl.type().getSimpleName());
        cr.setCt(factory.getNativeSQLDAO().countByGenericSearch(gs, ctrl.type()));
        return cr;
    }

    /**
     *
     * @param param
     * @param value
     * @return
     */
    @GET
    @Path("filter/{param}/{value}/count")
    @Produces({"application/json; charset=UTF-8"})
    public CountResponse countFiltered(@PathParam("param") String param, @PathParam("value") String value) throws RESTException {
        authenticateAdmin();
        Long l = ctrl.findFilterCount(param, value);
        CountResponse cr = new CountResponse();
        cr.setCt(l);
        cr.setEntity(getClass().getSimpleName().replace("FacadeREST", "") + " [" + param + "=" + value + "]");
        return cr;
    }

    @GET
    @Path("count")
    @Produces({"application/json; charset=UTF-8"})
    public CountResponse countAll() throws RESTException {
        authenticateAdmin();
        Long l = ctrl.countAll();
        CountResponse cr = new CountResponse();
        cr.setCt(l);
        cr.setEntity(getClass().getSimpleName().replace("FacadeREST", "") + " [ALL]");
        return cr;
    }

    @POST
    @Produces({"application/json; charset=UTF-8"})
    public E createRecord(E entity) throws RESTException {
        if (Objects.equals(mode, ADMIN_ENDPOINT)) {
            authenticateAdmin();
        } else {
            authenticate();
        }
        return ctrl.save(entity);
    }

    @PUT
    @Path("{id}")
    @Produces({"application/json; charset=UTF-8"})
    public E updateRecord(E entity) throws RESTException {
        if (Objects.equals(mode, ADMIN_ENDPOINT)) {
            authenticateAdmin();
        } else {
            authenticate();
        }
        return ctrl.save(entity);
    }

    @DELETE
    @Path("{id}")
    @Produces({"application/json; charset=UTF-8"})
    public E deleteRecord(@PathParam("id") K id, E e) throws RESTException {
        authenticateAdmin();
        ctrl.delete(id);
        return e;
    }

    
    protected void activate(E entity) {
        Method ins = null;
        try {
            ins = entity.getClass().getMethod("setActive", Integer.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            // skip logging
        } finally {
            if (ins != null) {
                try {
                    ins.invoke(entity, 1);
                } catch (IllegalArgumentException ex) {
                    if (ZegoK.Tuning.LOGGING) {
                        Logger.getLogger(BaseFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Logger.getLogger(BaseFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected void updateInsdate(E entity) {
        Method ins = null;
        try {
            ins = entity.getClass().getMethod("setInsdate", String.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            // skip logging
        } finally {
            if (ins != null) {
                try {
                    ins.invoke(entity, RESTDateUtil.getInstance().secondsNow());
                } catch (IllegalArgumentException ex) {
                    if (ZegoK.Tuning.LOGGING) {
                        Logger.getLogger(BaseFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Logger.getLogger(BaseFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected void updateModDate(E entity) {
        Method ins = null;
        try {
            ins = entity.getClass().getMethod("setModdate", String.class);
        } catch (NoSuchMethodException | SecurityException ex) {
            // skip logging
        } finally {
            if (ins != null) {
                try {
                    ins.invoke(entity, RESTDateUtil.getInstance().secondsNow());
                } catch (IllegalArgumentException ex) {
                    if (ZegoK.Tuning.LOGGING) {
                        Logger.getLogger(BaseFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IllegalAccessException | InvocationTargetException ex) {
                    Logger.getLogger(BaseFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    protected User caller() {
        try {
            List<String> tks = headers.getRequestHeader(ZegoK.Auth.ACCESSTOKEN);
            return tks == null || tks.isEmpty() ? null : auth.get(tks.get(0));
        } catch (Exception ex) {
            Logger.getLogger(BaseFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    protected boolean skipAuth() {
        List<String> sk = headers.getRequestHeader(ZegoK.Auth.SKIPTOKEN);
        return sk != null && !sk.isEmpty() && sk.get(0).equals(ZegoK.Auth.SKIPTOKENVALUE);
    }

    protected void authenticate() throws RESTException {
        User u = caller();
        boolean ok = u != null || skipAuth();
        if (!ok) {
            throw new RESTException(ZegoK.Error.AUTHFAIL, "You are not authorized to call this API.");
        }
    }

    protected void authenticate(K id) throws RESTException {
        User u = caller();
        boolean ok = (u != null && (u.getId().equals(id) || u.getUtype().equals(User.UTYPE_ADMIN))) || skipAuth();;
        if (!ok) {
            throw new RESTException(ZegoK.Error.AUTHFAIL, "You are not authorized to call this API.");
        }
    }

    protected void authenticateAdmin() throws RESTException {
        User u = caller();
        boolean ok = (u != null && u.getUtype().equals(User.UTYPE_ADMIN)) || skipAuth();
        if (!ok) {
            throw new RESTException(ZegoK.Error.AUTHFAIL, "You are not authorized to call this API.");
        }
    }

    protected void killWithCode(Integer code) throws RESTException {
        User u = caller();
        Errormsg em = errorCtrl.getErrorByCodeAndLang(code, u == null ? "it" : u.getCountry());
        Errordata ed = new Errordata();
        ed.setCode(code);
        ed.setErrdate(RESTDateUtil.getInstance().secondsNow());
        ed.setMsg(em == null ? "" : (em.getTitle()==null?"":em.getTitle())+" "+em.getMessage());
        ed.setUid(u == null ? 0 : u.getId());
        errorLogCtrl.save(ed);
        throw new RESTException(code, em);
    }

    protected Object killWithCodeObj(Integer code) throws RESTException {
        User u = caller();
        Errormsg em = errorCtrl.getErrorByCodeAndLang(code, u == null ? "it" : u.getCountry());
        Errordata ed = new Errordata();
        ed.setCode(code);
        ed.setErrdate(RESTDateUtil.getInstance().secondsNow());
        ed.setMsg(em == null ? "" : (em.getTitle()==null?"":em.getTitle())+" "+em.getMessage());
        ed.setUid(u == null ? 0 : u.getId());
        errorLogCtrl.save(ed);
        throw new RESTException(code, em);
    }

    protected void changeMode(Integer m) {
        mode = m;
    }

}
