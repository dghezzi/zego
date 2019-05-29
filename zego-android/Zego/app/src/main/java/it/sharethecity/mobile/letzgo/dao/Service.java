/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.sharethecity.mobile.letzgo.dao;

import java.io.Serializable;

import it.sharethecity.mobile.letzgo.application.ApplicationController;


public class Service implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private String details;
    private String insdate;
    private Integer level;
    private String nameen;
    private String detailsen;

    public Service() {
    }

    public Service(Integer id) {
        this.id = id;
    }

    public Service(Service s) {
        this.id = s.getId();
        this.name = s.getName();
        this.details = s.getDetails();
        this.insdate = s.getInsdate();
        this.level = s.getLevel();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Service)) {
            return false;
        }
        Service other = (Service) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Service[ id=" + id + " ]";
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getNameen() {
        return nameen;
    }

    public void setNameen(String nameen) {
        this.nameen = nameen;
    }

    public String getDetailsen() {
        return detailsen;
    }

    public void setDetailsen(String detailsen) {
        this.detailsen = detailsen;
    }

    public String getNameByLang(){
        return ApplicationController.getInstance().getsDefSystemLanguage().equalsIgnoreCase("it") ? name : nameen;
    }

    public String getDetailsByLang(){
        return ApplicationController.getInstance().getsDefSystemLanguage().equalsIgnoreCase("it") ? details : detailsen;
    }
}
