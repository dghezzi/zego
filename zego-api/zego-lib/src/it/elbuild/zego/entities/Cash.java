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
@Table(name = "cash")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Cash.findAll", query = "SELECT c FROM Cash c"),
    @NamedQuery(name = "Cash.findById", query = "SELECT c FROM Cash c WHERE c.id = :id"),
    @NamedQuery(name = "Cash.findByDid", query = "SELECT c FROM Cash c WHERE c.did = :did"),
    @NamedQuery(name = "Cash.findByRid", query = "SELECT c FROM Cash c WHERE c.rid = :rid"),
    @NamedQuery(name = "Cash.findByPid", query = "SELECT c FROM Cash c WHERE c.pid = :pid"),
    @NamedQuery(name = "Cash.findToBePaid", query = "SELECT c FROM Cash c WHERE c.due > 0 and c.did = :did"),    
    @NamedQuery(name = "Cash.findByCollected", query = "SELECT c FROM Cash c WHERE c.collected = :collected"),
    @NamedQuery(name = "Cash.findByCollectdate", query = "SELECT c FROM Cash c WHERE c.collectdate = :collectdate"),
    @NamedQuery(name = "Cash.findByDue", query = "SELECT c FROM Cash c WHERE c.due = :due"),
    @NamedQuery(name = "Cash.findByZegopaiddate", query = "SELECT c FROM Cash c WHERE c.zegopaiddate = :zegopaiddate")})
public class Cash implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "did")
    private Integer did;
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "pid")
    private Integer pid;
    @Column(name = "collected")
    private Integer collected;
    @Column(name = "collectdate")
    private String collectdate;
    @Column(name = "due")
    private Integer due;
    @Column(name = "zegopaiddate")
    private String zegopaiddate;
    @Column(name = "reference")
    private String reference;
    @Column(name = "reftype")
    private String reftype;
    
    public static final String REFTYPE_BANKTRANSFER = "bonifico";
    public static final String REFTYPE_RIDEPAYMENT = "ride";
    
    public Cash() {
    }

    public Cash(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public Integer getCollected() {
        return collected;
    }

    public void setCollected(Integer collected) {
        this.collected = collected;
    }

    public String getCollectdate() {
        return collectdate;
    }

    public void setCollectdate(String collectdate) {
        this.collectdate = collectdate;
    }

    public Integer getDue() {
        return due;
    }

    public void setDue(Integer due) {
        this.due = due;
    }

    public String getZegopaiddate() {
        return zegopaiddate;
    }

    public void setZegopaiddate(String zegopaiddate) {
        this.zegopaiddate = zegopaiddate;
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
        if (!(object instanceof Cash)) {
            return false;
        }
        Cash other = (Cash) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Cash[ id=" + id + " ]";
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String ref) {
        this.reference = ref;
    }

    public String getReftype() {
        return reftype;
    }

    public void setReftype(String reftype) {
        this.reftype = reftype;
    }
    
    
    
}
