/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Lu
 */
@Entity
@Table(name = "vertex")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vertex.findAll", query = "SELECT v FROM Vertex v"),
    @NamedQuery(name = "Vertex.findById", query = "SELECT v FROM Vertex v WHERE v.id = :id"),
    @NamedQuery(name = "Vertex.findByLat", query = "SELECT v FROM Vertex v WHERE v.lat = :lat"),
    @NamedQuery(name = "Vertex.findByLng", query = "SELECT v FROM Vertex v WHERE v.lng = :lng"),
    @NamedQuery(name = "Vertex.findByProv", query = "SELECT v FROM Vertex v WHERE v.prov = :prov")})
public class Vertex implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "lat")
    private double lat;
    @Basic(optional = false)
    @Column(name = "lng")
    private double lng;
    @Column(name = "prov")
    private String prov;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "vid", referencedColumnName = "id")
    private List<VertexNeighbors> neighbors;
    
    public Vertex() {
    }

    public Vertex(Integer id) {
        this.id = id;
    }

    public Vertex(Integer id, double lat, double lng) {
        this.id = id;
        this.lat = lat;
        this.lng = lng;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
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
        if (!(object instanceof Vertex)) {
            return false;
        }
        Vertex other = (Vertex) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "it.elbuild.zego.entities.Vertex[ id=" + id + " ]";
    }

    public List<VertexNeighbors> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<VertexNeighbors> neighbors) {
        this.neighbors = neighbors;
    }
    
    
}
