package it.sharethecity.mobile.letzgo.utilities;

import java.io.Serializable;

/**
 * Created by lucabellaroba on 22/05/15.
 */
public class AutocompleteResult implements Serializable {
    private String street;
    private String location;
    private String placeID;

    public AutocompleteResult(String location, String street){
        this.street = street;
        this.location = location;

    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }
}
