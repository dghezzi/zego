package it.sharethecity.mobile.letzgo.dao;

/**
 * Created by lucabellaroba on 21/09/16.
 */
public class GeoCodeRequest {

    private String address;
    private String placeid;

    /**
     * @return the address
     */
    public String getAddress() {
        return address == null ? "" : address.startsWith("Elizabeth Quay, The Esplanade") ?
                address.replace("Elizabeth Quay, The Esplanade", "Elizabeth Quay") : address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @return the placeid
     */
    public String getPlaceid() {
        return placeid;
    }

    /**
     * @param placeid the placeid to set
     */
    public void setPlaceid(String placeid) {
        this.placeid = placeid;
    }
}
