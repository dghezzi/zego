package it.sharethecity.mobile.letzgo.dao;

import java.io.Serializable;

/**
 * Created by lucabellaroba on 13/01/17.
 */

public class Area implements Serializable {
    private Integer id;
    private String name;
    private String insdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
    }
}
