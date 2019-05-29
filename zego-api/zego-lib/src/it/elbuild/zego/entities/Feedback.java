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
@Table(name = "feedback")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Feedback.findAll", query = "SELECT f FROM Feedback f"),
    @NamedQuery(name = "Feedback.findById", query = "SELECT f FROM Feedback f WHERE f.id = :id"),
    @NamedQuery(name = "Feedback.findByRid", query = "SELECT f FROM Feedback f WHERE f.rid = :rid"),
    @NamedQuery(name = "Feedback.findByRidAndUid", query = "SELECT f FROM Feedback f WHERE f.rid = :rid and f.uid = :uid"),
    @NamedQuery(name = "Feedback.findByUid", query = "SELECT f FROM Feedback f WHERE f.uid = :uid"),
    @NamedQuery(name = "Feedback.findByUidDriver", query = "SELECT f FROM Feedback f WHERE f.uid = :uid and f.fromdriver = 0"),
    @NamedQuery(name = "Feedback.findBySender", query = "SELECT f FROM Feedback f WHERE f.sender = :sender"),
    @NamedQuery(name = "Feedback.findByInsdate", query = "SELECT f FROM Feedback f WHERE f.insdate = :insdate"),
    @NamedQuery(name = "Feedback.findByRating", query = "SELECT f FROM Feedback f WHERE f.rating = :rating"),
    @NamedQuery(name = "Feedback.countByUid", query = "SELECT count(f) FROM Feedback f WHERE f.uid = :uid"),
    @NamedQuery(name = "Feedback.countByUidAsDriver", query = "SELECT count(f) FROM Feedback f WHERE f.uid = :uid and f.fromdriver = 0"),
    @NamedQuery(name = "Feedback.countByUidAsRider", query = "SELECT count(f) FROM Feedback f WHERE f.uid = :uid and f.fromdriver = 1"),
    @NamedQuery(name = "Feedback.countLowByUidAsDriver", query = "SELECT count(f) FROM Feedback f WHERE f.uid = :uid and f.fromdriver = 0 and f.rating < 3"),
    @NamedQuery(name = "Feedback.countLowByUidAsRider", query = "SELECT count(f) FROM Feedback f WHERE f.uid = :uid and f.fromdriver = 1 and f.rating < 3"),
    @NamedQuery(name = "Feedback.findByReason", query = "SELECT f FROM Feedback f WHERE f.reason = :reason")})
public class Feedback implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "uid")
    private Integer uid;
    @Column(name = "sender")
    private Integer sender;
    @Column(name = "insdate")
    private String insdate;
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "fromdriver")
    private Integer fromdriver;
    @Column(name = "reason")
    private String reason;

    public Feedback() {
    }

    public Feedback(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public Integer getSender() {
        return sender;
    }

    public void setSender(Integer sender) {
        this.sender = sender;
    }

    public String getInsdate() {
        return insdate;
    }

    public void setInsdate(String insdate) {
        this.insdate = insdate;
    }

    public Integer getRating() {
        return rating == null ? 5 : rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
        if (!(object instanceof Feedback)) {
            return false;
        }
        Feedback other = (Feedback) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Feedback[ id=" + id + " ]";
    }

    /**
     * @return the fromdriver
     */
    public Integer getFromdriver() {
        return fromdriver == null ? 0 : fromdriver;
    }

    /**
     * @param fromdriver the fromdriver to set
     */
    public void setFromdriver(Integer fromdriver) {
        this.fromdriver = fromdriver;
    }
    
    
    
}
