/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import it.elbuild.zego.util.Coordinate;
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
@Table(name = "vertex_neighbors")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "VertexNeighbors.findAll", query = "SELECT v FROM VertexNeighbors v"),
    @NamedQuery(name = "VertexNeighbors.findById", query = "SELECT v FROM VertexNeighbors v WHERE v.id = :id"),
    @NamedQuery(name = "VertexNeighbors.findByVid", query = "SELECT v FROM VertexNeighbors v WHERE v.vid = :vid"),
    @NamedQuery(name = "VertexNeighbors.findByVlat", query = "SELECT v FROM VertexNeighbors v WHERE v.vlat = :vlat"),
    @NamedQuery(name = "VertexNeighbors.findByVlon", query = "SELECT v FROM VertexNeighbors v WHERE v.vlon = :vlon"),
    @NamedQuery(name = "VertexNeighbors.findByDistance", query = "SELECT v FROM VertexNeighbors v WHERE v.distance = :distance"),
    @NamedQuery(name = "VertexNeighbors.findByProv", query = "SELECT v FROM VertexNeighbors v WHERE v.prov = :prov")})
public class VertexNeighbors implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "vid")
    private int vid;
    @Basic(optional = false)
    @Column(name = "vlat")
    private float vlat;
    @Basic(optional = false)
    @Column(name = "vlon")
    private float vlon;
    @Basic(optional = false)
    @Column(name = "distance")
    private float distance;
    @Column(name = "prov")
    private String prov;

    public VertexNeighbors() {
    }

    public VertexNeighbors(Integer id) {
        this.id = id;
    }

    public VertexNeighbors(Integer id, int vid, float vlat, float vlon, float distance) {
        this.id = id;
        this.vid = vid;
        this.vlat = vlat;
        this.vlon = vlon;
        this.distance = distance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getVid() {
        return vid;
    }

    public void setVid(int vid) {
        this.vid = vid;
    }

    public float getVlat() {
        return vlat;
    }

    public void setVlat(float vlat) {
        this.vlat = vlat;
    }

    public float getVlon() {
        return vlon;
    }

    public void setVlon(float vlon) {
        this.vlon = vlon;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public String getProv() {
        return prov;
    }

    public void setProv(String prov) {
        this.prov = prov;
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
        if (!(object instanceof VertexNeighbors)) {
            return false;
        }
        VertexNeighbors other = (VertexNeighbors) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.VertexNeighbors[ id=" + id + " ]";
    }
    
    public Double metric(Coordinate c) {
        return Math.pow(Math.abs(this.vlat-c.getLat()),2)+Math.pow(Math.abs(this.vlon-c.getLng()),2);
    }
}
