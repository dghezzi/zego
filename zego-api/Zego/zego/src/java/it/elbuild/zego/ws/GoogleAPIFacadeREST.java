/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import it.elbuild.zego.entities.Address;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.iface.AuthController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.GooglePlaceInterface;
import it.elbuild.zego.rest.request.GeoCodeRequest;
import it.elbuild.zego.rest.request.ZegoAutoCompleteGoogleRequest;
import it.elbuild.zego.rest.response.GeoCodeResponse;
import it.elbuild.zego.util.ZegoK;
import it.elbuild.zego.util.google.GooglePlacePredictions;
import it.elbuild.zego.util.google.GooglePlaceResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

/**
 *
 * @author Lu
 */
@Stateless
@Path("google")
public class GoogleAPIFacadeREST {
    
    @Context
    HttpHeaders headers;

    @EJB
    AuthController auth;
    
    @EJB
    DAOProvider provider;
    
    @EJB
    GooglePlaceInterface googleController;
    
    EntityController<Integer,User> userCtrl;
    
    private GeoApiContext geoCtx;
    
    @PostConstruct
    private void init() {
        geoCtx = new GeoApiContext().setEnterpriseCredentials("", "");
        userCtrl = provider.provide(User.class);
    }
    @POST
    @Path("geocode")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public GeoCodeResponse lastLocation(GeoCodeRequest req) throws Exception {
        System.out.println(req.getAddress());
        GeoCodeResponse gresp = googleController.geocodePlaceIdOrAddress(req.getPlaceid(), req.getAddress());
        if(gresp == null){
        GeocodingResult[] res = GeocodingApi.geocode(geoCtx, req.getAddress()).awaitIgnoreError();
        gresp = new GeoCodeResponse();
        try {
            if (res != null && res.length > 0) {
                gresp.setLat(res[0].geometry.location.lat);
                gresp.setLng(res[0].geometry.location.lng);
                gresp.setAddress(req.getAddress() == null ? res[0].formattedAddress : req.getAddress());
                gresp.setPartial(res[0].partialMatch ? 1 : 0);
            }
        } catch (Exception e) {
            gresp.setLat(0);
            gresp.setLng(0);
        }
        }
        return gresp;
    }
    
    
    @POST
    @Path("geocomplete")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public GooglePlaceResponse autoCompleteTerm(ZegoAutoCompleteGoogleRequest req) throws Exception {
        GooglePlaceResponse res = null;
        
        try
        {
            res = googleController.autocomplete(req);
        }
        catch(Exception e)
        {
            throw new Exception("An error occurred while autocompleting. "+e.getLocalizedMessage());
        }
        
        return res;
    }
    
    @POST
    @Path("addresscomplete")
    @Consumes({"application/json; charset=UTF-8"})
    @Produces({"application/json; charset=UTF-8"})
    public List<Address> addressCompleteTerm(ZegoAutoCompleteGoogleRequest req) throws Exception {
        List<Address> res = new ArrayList<>();
        
        try
        {
            if(req.getLat() == null || req.getLng() == null) {
                User u = caller();
                u = userCtrl.findById(u.getId());
                req.setLat(u.getLlat());
                req.setLng(u.getLlon());
            }
            GooglePlaceResponse resp = googleController.autocomplete(req);
            for (GooglePlacePredictions pr : resp.getPredictions()){
                Address a = new Address();
                a.setAddress(pr.getDescription());
                a.setPlaceid(pr.getPlace_id());
                a.setUid(0);
                a.setType("search");
                res.add(a);
            }
        }
        catch(Exception e)
        {
            throw new Exception("An error occurred while autocompleting. "+e.getLocalizedMessage());
        }
        
        return res;
    }
    
    private User caller() {
        List<String> tks = headers.getRequestHeader(ZegoK.Auth.ACCESSTOKEN);
        return tks == null || tks.isEmpty() ? null : auth.get(tks.get(0));
    }
}
