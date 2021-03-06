package it.sharethecity.mobile.letzgo.dao;

/**
 * Created by lucabellaroba on 21/09/16.
 */
public class GeoCodeResponse {

    private double lat;
    private double lng;
    private String address;
    private Integer partial;

    /**
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * @param lat the lat to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * @param lng the lng to set
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the partial
     */
    public Integer getPartial() {
        return partial;
    }

    /**
     * @param partial the partial to set
     */
    public void setPartial(Integer partial) {
        this.partial = partial;
    }
}
