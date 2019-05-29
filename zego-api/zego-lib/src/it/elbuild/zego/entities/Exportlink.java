/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "exportlink")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exportlink.findAll", query = "SELECT e FROM Exportlink e"),
    @NamedQuery(name = "Exportlink.findById", query = "SELECT e FROM Exportlink e WHERE e.id = :id"),
    @NamedQuery(name = "Exportlink.findByRid", query = "SELECT e FROM Exportlink e WHERE e.rid = :rid"),
    @NamedQuery(name = "Exportlink.findByShortid", query = "SELECT e FROM Exportlink e WHERE e.shortid = :shortid"),
    @NamedQuery(name = "Exportlink.findByReqdate", query = "SELECT e FROM Exportlink e WHERE e.reqdate = :reqdate"),
    @NamedQuery(name = "Exportlink.findByReqdateBetween", query = "SELECT e FROM Exportlink e WHERE e.reqdate BETWEEN :start and :stop"),
    @NamedQuery(name = "Exportlink.findByZonename", query = "SELECT e FROM Exportlink e WHERE e.zonename = :zonename"),
    @NamedQuery(name = "Exportlink.findByPid", query = "SELECT e FROM Exportlink e WHERE e.pid = :pid"),
    @NamedQuery(name = "Exportlink.findByExtmeters", query = "SELECT e FROM Exportlink e WHERE e.extmeters = :extmeters"),
    @NamedQuery(name = "Exportlink.findByDriverfee", query = "SELECT e FROM Exportlink e WHERE e.driverfee = :driverfee"),
    @NamedQuery(name = "Exportlink.findByDid", query = "SELECT e FROM Exportlink e WHERE e.did = :did"),
    @NamedQuery(name = "Exportlink.findByDmeters", query = "SELECT e FROM Exportlink e WHERE e.dmeters = :dmeters"),
    @NamedQuery(name = "Exportlink.findByLinkstatus", query = "SELECT e FROM Exportlink e WHERE e.linkstatus = :linkstatus"),
    @NamedQuery(name = "Exportlink.findByRidestatus", query = "SELECT e FROM Exportlink e WHERE e.ridestatus = :ridestatus")})
public class Exportlink implements Serializable {
    private static final long serialVersionUID = 1L;
    @Basic(optional = false)
    @Column(name = "id")
    @Id
    private int id;
    @Basic(optional = false)
    @Column(name = "rid")
    private int rid;
    @Column(name = "shortid")
    private String shortid;
    @Column(name = "reqdate")
    private String reqdate;
    @Column(name = "zonename")
    private String zonename;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "extmeters")
    private Integer extmeters;
    @Column(name = "driverfee")
    private Integer driverfee;
        @Column(name = "did")
    private Integer did;
    @Column(name = "driverid")
    private Integer driverid;
    @Column(name = "dmeters")
    private Integer dmeters;
    @Column(name = "linkstatus")
    private Integer linkstatus;
    @Column(name = "ridestatus")
    private Integer ridestatus;

    public Exportlink() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getShortid() {
        return shortid;
    }

    public void setShortid(String shortid) {
        this.shortid = shortid;
    }

    public String getReqdate() {
        return reqdate;
    }

    public void setReqdate(String reqdate) {
        this.reqdate = reqdate;
    }

    public String getZonename() {
        return zonename;
    }

    public void setZonename(String zonename) {
        this.zonename = zonename;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getExtmeters() {
        return extmeters;
    }

    public void setExtmeters(Integer extmeters) {
        this.extmeters = extmeters;
    }

    public Integer getDriverfee() {
        return driverfee;
    }

    public void setDriverfee(Integer driverfee) {
        this.driverfee = driverfee;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getDmeters() {
        return dmeters;
    }

    public void setDmeters(Integer dmeters) {
        this.dmeters = dmeters;
    }

    public Integer getLinkstatus() {
        return linkstatus;
    }

    public void setLinkstatus(Integer linkstatus) {
        this.linkstatus = linkstatus;
    }

    public Integer getRidestatus() {
        return ridestatus;
    }

    public void setRidestatus(Integer ridestatus) {
        this.ridestatus = ridestatus;
    }

    public Integer getDriverid() {
        return driverid;
    }

    public void setDriverid(Integer driverid) {
        this.driverid = driverid;
    }
    
    
}
