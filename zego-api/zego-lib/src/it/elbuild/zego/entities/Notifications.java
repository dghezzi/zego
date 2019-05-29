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
@Table(name = "notifications")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Notifications.findAll", query = "SELECT n FROM Notifications n"),
    @NamedQuery(name = "Notifications.findById", query = "SELECT n FROM Notifications n WHERE n.id = :id"),
    @NamedQuery(name = "Notifications.findNotSent", query = "SELECT n FROM Notifications n WHERE n.status = 0 and n.notbefore < :now"),
    @NamedQuery(name = "Notifications.findByUid", query = "SELECT n FROM Notifications n WHERE n.uid = :uid"),
    @NamedQuery(name = "Notifications.findByRid", query = "SELECT n FROM Notifications n WHERE n.rid = :rid"),
    @NamedQuery(name = "Notifications.findByMessage", query = "SELECT n FROM Notifications n WHERE n.message = :message"),
    @NamedQuery(name = "Notifications.findByToken", query = "SELECT n FROM Notifications n WHERE n.token = :token"),
    @NamedQuery(name = "Notifications.findByNotbefore", query = "SELECT n FROM Notifications n WHERE n.notbefore = :notbefore"),
    @NamedQuery(name = "Notifications.findBySentdate", query = "SELECT n FROM Notifications n WHERE n.sentdate = :sentdate"),
    @NamedQuery(name = "Notifications.findByStatus", query = "SELECT n FROM Notifications n WHERE n.status = :status")})
public class Notifications implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "message")
    private String message;
    @Column(name = "token")
    private String token;
    @Column(name = "notbefore")
    private String notbefore;
    @Column(name = "sentdate")
    private String sentdate;
    @Column(name = "status")
    private Integer status;

    
    public static final Integer STATUS_SCHEDULED = 0;
    public static final Integer STATUS_SENT = 1;
    public static final Integer STATUS_CANCELED = 2;
    
    public static final int DRIVER_NEW_REQUEST = 100;
    public static final int USER_DRIVER_ACCEPT = 101;
    public static final int USER_DRIVER_IAMHERE = 102;
    
    public static final String NEW_REQUEST_TEXT = "C'è una nuova richiesta per te!";
    public static final String PASSENGER_CANCELED_TEXT = "La richiesta è stata cancellata dal passeggero.";
    public static final String PASSENGER_ABORTED_TEXT = "La richiesta è stata annullata dal passeggero.";
    public static final String DRIVER_ABORTED_TEXT = "La richiesta è stata annullata dal driver.";
    public Notifications() {
    }

    public Notifications(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNotbefore() {
        return notbefore;
    }

    public void setNotbefore(String notbefore) {
        this.notbefore = notbefore;
    }

    public String getSentdate() {
        return sentdate;
    }

    public void setSentdate(String sentdate) {
        this.sentdate = sentdate;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
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
        if (!(object instanceof Notifications)) {
            return false;
        }
        Notifications other = (Notifications) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Notifications[ id=" + id + " ]";
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }
 
    
}
