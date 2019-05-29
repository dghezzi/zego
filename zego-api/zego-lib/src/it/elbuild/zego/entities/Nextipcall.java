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
@Table(name = "nextipcall")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Nextipcall.findAll", query = "SELECT n FROM Nextipcall n"),
    @NamedQuery(name = "Nextipcall.findById", query = "SELECT n FROM Nextipcall n WHERE n.id = :id"),
    @NamedQuery(name = "Nextipcall.findCombined", query = "SELECT n FROM Nextipcall n WHERE n.callsrc = :callsrc or n.calldst = :calldst order by n.id desc"),
    @NamedQuery(name = "Nextipcall.findByRid", query = "SELECT n FROM Nextipcall n WHERE n.rid = :rid"),
    @NamedQuery(name = "Nextipcall.findByCallsrc", query = "SELECT n FROM Nextipcall n WHERE n.callsrc = :callsrc"),
    @NamedQuery(name = "Nextipcall.findByCalldst", query = "SELECT n FROM Nextipcall n WHERE n.calldst = :calldst"),
    @NamedQuery(name = "Nextipcall.findByCaller", query = "SELECT n FROM Nextipcall n WHERE n.caller = :caller"),
    @NamedQuery(name = "Nextipcall.findByCalldate", query = "SELECT n FROM Nextipcall n WHERE n.calldate = :calldate"),
    @NamedQuery(name = "Nextipcall.findByApiresult", query = "SELECT n FROM Nextipcall n WHERE n.apiresult = :apiresult")})
public class Nextipcall implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "rid")
    private Integer rid;
    @Column(name = "callsrc")
    private Integer callsrc;
    @Column(name = "calldst")
    private Integer calldst;
    @Column(name = "caller")
    private String caller;
    @Column(name = "calldate")
    private String calldate;
    @Column(name = "apiresult")
    private Integer apiresult;

    public Nextipcall() {
    }

    public Nextipcall(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCallsrc() {
        return callsrc;
    }

    public void setCallsrc(Integer callsrc) {
        this.callsrc = callsrc;
    }

    public Integer getCalldst() {
        return calldst;
    }

    public void setCalldst(Integer calldst) {
        this.calldst = calldst;
    }

    public String getCaller() {
        return caller;
    }

    public void setCaller(String caller) {
        this.caller = caller;
    }

    public String getCalldate() {
        return calldate;
    }

    public void setCalldate(String calldate) {
        this.calldate = calldate;
    }

    public Integer getApiresult() {
        return apiresult;
    }

    public void setApiresult(Integer apiresult) {
        this.apiresult = apiresult;
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
        if (!(object instanceof Nextipcall)) {
            return false;
        }
        Nextipcall other = (Nextipcall) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Nextipcall[ id=" + id + " ]";
    }

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }
    
    
}
