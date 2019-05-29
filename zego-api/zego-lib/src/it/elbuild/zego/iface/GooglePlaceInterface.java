/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;


import it.elbuild.zego.rest.request.ZegoAutoCompleteGoogleRequest;
import it.elbuild.zego.rest.response.GeoCodeResponse;
import it.elbuild.zego.util.google.GooglePlaceResponse;



/**
 *
 * @author Lu
 */
public interface GooglePlaceInterface {
    
    public GooglePlaceResponse autocomplete(ZegoAutoCompleteGoogleRequest req);
    public GeoCodeResponse geocodePlaceIdOrAddress(String placeId, String address);
    public GeoCodeResponse addressForCoord(double lat, double lng);
}
