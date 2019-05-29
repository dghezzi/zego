/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities.legacy;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "zgfeedback")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zgfeedback.findAll", query = "SELECT z FROM Zgfeedback z"),
    @NamedQuery(name = "Zgfeedback.findPassengerFeedback", query = "SELECT z FROM Zgfeedback z WHERE z.riderequestId = :riderequestId and z.type = 0"),
    @NamedQuery(name = "Zgfeedback.findDriverFeedback", query = "SELECT z FROM Zgfeedback z WHERE z.riderequestId = :riderequestId and z.type = 1"),
    @NamedQuery(name = "Zgfeedback.findByFeedbackId", query = "SELECT z FROM Zgfeedback z WHERE z.feedbackId = :feedbackId"),
    @NamedQuery(name = "Zgfeedback.findByRiderequestId", query = "SELECT z FROM Zgfeedback z WHERE z.riderequestId = :riderequestId"),
    @NamedQuery(name = "Zgfeedback.findByType", query = "SELECT z FROM Zgfeedback z WHERE z.type = :type"),
    @NamedQuery(name = "Zgfeedback.findByRating", query = "SELECT z FROM Zgfeedback z WHERE z.rating = :rating"),
    @NamedQuery(name = "Zgfeedback.findByCreatedate", query = "SELECT z FROM Zgfeedback z WHERE z.createdate = :createdate"),
    @NamedQuery(name = "Zgfeedback.findByLastmoddate", query = "SELECT z FROM Zgfeedback z WHERE z.lastmoddate = :lastmoddate")})
public class Zgfeedback implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "feedback_id")
    private Integer feedbackId;
    @Basic(optional = false)
    @Column(name = "riderequest_id")
    private int riderequestId;
    @Basic(optional = false)
    @Column(name = "type")
    private short type;
    @Column(name = "rating")
    private Integer rating;
    @Lob
    @Column(name = "comment")
    private String comment;
    @Column(name = "createdate")
    private String createdate;
    @Column(name = "lastmoddate")
    private String lastmoddate;

    public Zgfeedback() {
    }

    public Zgfeedback(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public Zgfeedback(Integer feedbackId, int riderequestId, short type) {
        this.feedbackId = feedbackId;
        this.riderequestId = riderequestId;
        this.type = type;
    }

    public Integer getFeedbackId() {
        return feedbackId;
    }

    public void setFeedbackId(Integer feedbackId) {
        this.feedbackId = feedbackId;
    }

    public int getRiderequestId() {
        return riderequestId;
    }

    public void setRiderequestId(int riderequestId) {
        this.riderequestId = riderequestId;
    }

    public short getType() {
        return type;
    }

    public void setType(short type) {
        this.type = type;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getLastmoddate() {
        return lastmoddate;
    }

    public void setLastmoddate(String lastmoddate) {
        this.lastmoddate = lastmoddate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (feedbackId != null ? feedbackId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zgfeedback)) {
            return false;
        }
        Zgfeedback other = (Zgfeedback) object;
        if ((this.feedbackId == null && other.feedbackId != null) || (this.feedbackId != null && !this.feedbackId.equals(other.feedbackId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.legacy.Zgfeedback[ feedbackId=" + feedbackId + " ]";
    }
    
}
