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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "paymentaction")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paymentaction.findAll", query = "SELECT p FROM Paymentaction p"),
    @NamedQuery(name = "Paymentaction.findById", query = "SELECT p FROM Paymentaction p WHERE p.id = :id"),
    @NamedQuery(name = "Paymentaction.findByPid", query = "SELECT p FROM Paymentaction p WHERE p.pid = :pid"),
    @NamedQuery(name = "Paymentaction.findByChargeid", query = "SELECT p FROM Paymentaction p WHERE p.chargeid = :chargeid"),
    @NamedQuery(name = "Paymentaction.findByActiontype", query = "SELECT p FROM Paymentaction p WHERE p.actiontype = :actiontype"),
    @NamedQuery(name = "Paymentaction.findByActor", query = "SELECT p FROM Paymentaction p WHERE p.actor = :actor"),
    @NamedQuery(name = "Paymentaction.findByActiondate", query = "SELECT p FROM Paymentaction p WHERE p.actiondate = :actiondate"),
    @NamedQuery(name = "Paymentaction.findByCapture", query = "SELECT p FROM Paymentaction p WHERE p.capture = :capture"),
    @NamedQuery(name = "Paymentaction.findByRefund", query = "SELECT p FROM Paymentaction p WHERE p.refund = :refund")})
public class Paymentaction implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "chargeid")
    private String chargeid;
    @Column(name = "actiontype")
    private String actiontype;
    @Column(name = "actor")
    private String actor;
    @Column(name = "actiondate")
    private String actiondate;
    @Column(name = "capture")
    private Integer capture;
    @Column(name = "refund")
    private Integer refund;

    public Paymentaction() {
    }

    public Paymentaction(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getChargeid() {
        return chargeid;
    }

    public void setChargeid(String chargeid) {
        this.chargeid = chargeid;
    }

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getActiondate() {
        return actiondate;
    }

    public void setActiondate(String actiondate) {
        this.actiondate = actiondate;
    }

    public Integer getCapture() {
        return capture;
    }

    public void setCapture(Integer capture) {
        this.capture = capture;
    }

    public Integer getRefund() {
        return refund;
    }

    public void setRefund(Integer refund) {
        this.refund = refund;
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
        if (!(object instanceof Paymentaction)) {
            return false;
        }
        Paymentaction other = (Paymentaction) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Paymentaction[ id=" + id + " ]";
    }
    
}
