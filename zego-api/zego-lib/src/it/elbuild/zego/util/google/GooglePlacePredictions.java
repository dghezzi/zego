/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util.google;

import java.util.ArrayList;

/**
 *
 * @author Lu
 */
public class GooglePlacePredictions {
    
    public GooglePlacePredictions()
    {
        
    }
    
    private String description;
    private String place_id;
    private ArrayList<String> types;
    private String reference;

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the place_id
     */
    public String getPlace_id() {
        return place_id;
    }

    /**
     * @param place_id the place_id to set
     */
    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    /**
     * @return the types
     */
    public ArrayList<String> getTypes() {
        return types;
    }

    /**
     * @param types the types to set
     */
    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    /**
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }
}
