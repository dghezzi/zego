/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import com.google.gson.Gson;
import com.google.maps.model.GeocodingResult;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import it.elbuild.zego.iface.GooglePlaceInterface;
import it.elbuild.zego.rest.request.ZegoAutoCompleteGoogleRequest;
import it.elbuild.zego.rest.response.GeoCodeResponse;
import it.elbuild.zego.util.google.GooglePlaceResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;

/**
 *
 * @author Lu
 */
@Stateless
public class GooglePlaceController implements GooglePlaceInterface {

    private Gson g;
    private OkHttpClient client;

    @PostConstruct
    private void init() {
        client = new OkHttpClient();
        client.setReadTimeout(10, TimeUnit.SECONDS);
        client.setConnectTimeout(10, TimeUnit.SECONDS);
        g = new Gson();
    }

    @Override
    public GooglePlaceResponse autocomplete(ZegoAutoCompleteGoogleRequest req) {

        try {

            Request request = new Request.Builder()
                    .url(urlFromRequest(req))
                    .build();
            Response responses = null;

            responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            return g.fromJson(jsonData, GooglePlaceResponse.class);            
        } catch (IOException ex) {
            Logger.getLogger(GooglePlaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    @Override
    public GeoCodeResponse geocodePlaceIdOrAddress(String placeId, String address)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/geocode/json?");
        if(placeId != null) {
            sb.append("place_id=").append(placeId);
        }
        else {
            sb.append("address=").append(address.replace(" ", "+"));
        }
        sb.append("&key=AIzaSyC08pSmpuMMKTuhtcaXZIxN8HMxamqNKek");

        try {

            Request request = new Request.Builder()
                    .url(sb.toString())
                    .build();
            Response responses = null;

            responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            GeocodeFullResult gr = g.fromJson(jsonData, GeocodeFullResult.class);            
            if(gr!= null && gr.getResults()!=null && gr.getResults().length > 0) {
                GeoCodeResponse gcr = new GeoCodeResponse();
                gcr.setAddress(gr.getResults()[0].getFormatted_address());
                gcr.setLat(gr.getResults()[0].geometry.location.lat);
                gcr.setLng(gr.getResults()[0].geometry.location.lng);
                gcr.setPartial(0);
                return  gcr;
            }
        } catch (IOException ex) {
            Logger.getLogger(GooglePlaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private String urlFromRequest(ZegoAutoCompleteGoogleRequest req)
    {
        double lat = 0;
        double lng = 0;
        Integer radius = 25000;
        String country = "it";
        
        if(req.getLat() != null)
        {
            lat = req.getLat();
        }
        
        if(req.getLng()!= null)
        {
            lng = req.getLng();
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/place/autocomplete/json?input=");
        sb.append(req.getTerm());
        
        sb.append("&location=").append(lat).append(",").append(lng);
        sb.append("&radius=").append(radius);
        sb.append("&language=").append("it");
        sb.append("&components=country:").append(country).
                append("&key=AIzaSyC08pSmpuMMKTuhtcaXZIxN8HMxamqNKek");
        
        return sb.toString();
    }

    @Override
    public GeoCodeResponse addressForCoord(double lat, double lng) {
        try {

            Request request = new Request.Builder()
                    .url(urlFromLatLng(lat, lng))
                    .build();
            Response responses = null;

            responses = client.newCall(request).execute();
            String jsonData = responses.body().string();
            GeocodeFullResult gr = g.fromJson(jsonData, GeocodeFullResult.class);            
            if(gr!= null && gr.getResults()!=null && gr.getResults().length > 0) {
                GeoCodeResponse gcr = new GeoCodeResponse();
                gcr.setAddress(gr.getResults()[0].getFormatted_address());
                gcr.setLat(gr.getResults()[0].geometry.location.lat);
                gcr.setLng(gr.getResults()[0].geometry.location.lng);
                gcr.setPartial(0);
                return  gcr;
            } else {
                GeoCodeResponse gcr = new GeoCodeResponse();
                gcr.setAddress(lat+","+lng);
                gcr.setLat(lat);
                gcr.setLng(lng);
                gcr.setPartial(0);
                return gcr;
            }
        } catch (IOException ex) {
            Logger.getLogger(GooglePlaceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private String urlFromLatLng(double lat, double lng)
    {               
        StringBuilder sb = new StringBuilder();
        sb.append("https://maps.googleapis.com/maps/api/geocode/json?latlng=");
        sb.append(lat).append(",").append(lng);
        sb.append("&key=AIzaSyC08pSmpuMMKTuhtcaXZIxN8HMxamqNKek");   
        System.out.println(sb.toString());
        return sb.toString();
    }
    
    
    public class GeocodeFullResult {
        
        public GeocodeFullResult() {
            
        }
        private MyGeocodeResult[] results;

        /**
         * @return the results
         */
        public MyGeocodeResult[] getResults() {
            return results;
        }

        /**
         * @param results the results to set
         */
        public void setResults(MyGeocodeResult[] results) {
            this.results = results;
        }
    }
    
    public class MyGeocodeResult extends GeocodingResult {
        
        public MyGeocodeResult() {
            super();
        }
        private String formatted_address;

        /**
         * @return the formatted_address
         */
        public String getFormatted_address() {
            return formatted_address;
        }

        /**
         * @param formatted_address the formatted_address to set
         */
        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }
    }
}
