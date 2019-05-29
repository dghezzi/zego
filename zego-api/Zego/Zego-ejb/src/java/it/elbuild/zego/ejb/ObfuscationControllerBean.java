/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import it.elbuild.zego.entities.Vertex;
import it.elbuild.zego.entities.VertexNeighbors;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.ObfuscationController;
import it.elbuild.zego.util.Coordinate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.EJB;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 *
 * @author Lu
 */
@Startup
@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER) 
@Lock(LockType.READ) 
public class ObfuscationControllerBean implements ObfuscationController {

    @EJB
    DAOProvider provider;
    
    EntityController<Integer, Vertex> vertexCtrl;
    
    private final List<Double> miLat = Arrays.asList(45.35,45.359997,45.369995,45.379993,45.38999,45.39999,45.40999,45.419987,45.429985,45.439983,45.44998,45.45998,45.46998,45.479977,45.489975,45.499973,45.50997,45.51997,45.52997,45.539967,45.549965);
    private final List<Double> miLng = Arrays.asList(9.03,9.045,9.06,9.075001,9.090001,9.105001,9.120002,9.135002,9.1500025,9.165003,9.180003,9.1950035,9.210004,9.225004,9.240005,9.255005,9.270005,9.285006,9.300006,9.315006);
    private final List<Double> toLat = Arrays.asList(45.01,45.017,45.024,45.031,45.038,45.045,45.052,45.059,45.066,45.073,45.08,45.087,45.094,45.101,45.108,45.115);
    private final List<Double> toLng = Arrays.asList(7.59,7.597,7.604,7.611,7.618,7.625,7.632,7.639,7.646,7.653,7.66,7.667,7.674,7.681,7.688,7.695);
    
    
    private ConcurrentHashMap<Integer,Vertex> cache;
    
    private static final Integer MIBASE = 1;
    private static final Integer TOBASE = 2583;
    private static final Double MINSHIFT = 0d;
    
    @PostConstruct
    private void init() {
        vertexCtrl  = provider.provide(Vertex.class);
        cache = new ConcurrentHashMap<>();
        for(Vertex v : vertexCtrl.findAll()) {
            cache.put(v.getId(), v);
        }
    }
    
    @Override
    public Coordinate obfuscate(Coordinate c) {
        StringBuilder sb = new  StringBuilder();
        sb.append("OBFUSCATE: ").append(c.getLat()).append(",").append(c.getLng()).append("   -->   ");
        String prov = null;
        if(isMilano(c)) {
            prov = "MI";
        } else if (isTorino(c)) {
            prov = "TO";
        } 
        
        if(prov == null) {
            return c;
        } else {
            Integer latId = latIdx(c.getLat(), prov);
            Integer lngId = lngIdx(c.getLng(), prov);
            List<Vertex> vv = vertex(latId, lngId, prov);
            if(vv==null || vv.isEmpty()) {
                return c;
            } else {
                VertexNeighbors n = coord(c, vv);
                c.setLat(n.getVlat()+0.d);
                c.setLng(n.getVlon()+0.d);
                c.setVid(n.getId());
            }
            sb.append(c.getLat()).append(",").append(c.getLng()).append("\t [").append(latId).append(",").append(lngId).append("] {").append(c.getVid()).append("}");
        }
        
        //System.out.println(sb.toString());
        return c;
    }
    
    private Boolean isMilano(Coordinate c) {
        return (miLng.get(0) < c.getLng() && miLng.get(miLng.size()-1) > c.getLng()) && 
                (miLat.get(0) < c.getLat() && miLat.get(miLat.size()-1) > c.getLat());
    }
    
    private Boolean isTorino(Coordinate c) {
        return (toLng.get(0) < c.getLng() && toLng.get(toLng.size()-1) > c.getLng()) && 
                (toLat.get(0) < c.getLat() && toLat.get(toLat.size()-1) > c.getLat());
    }
    
    private Integer latIdx(Double lt, String prov) {
        Integer id = 0;
        for (Double dd : prov.equalsIgnoreCase("MI")? miLat : toLat) {
            if(dd>lt) {
                return prov.equalsIgnoreCase("MI")? miLat.indexOf(dd)-1 : toLat.indexOf(dd)-1;
            }
            id++;
        }
        return 0;
    }
    
    private Integer lngIdx(Double lt, String prov) {
        Integer id = 0;
        for (Double dd : prov.equalsIgnoreCase("MI")? miLng : toLng) {
            if(dd>lt) {
                return prov.equalsIgnoreCase("MI")? miLng.indexOf(dd)-1 : toLng.indexOf(dd)-1;
            }
            id++;
        }
        return 0;
    }
    
    private List<Vertex> vertex(Integer ltidx, Integer lngidx, String prov) {
        List<Vertex> vvx = new ArrayList<>();
        Integer idx = prov.equalsIgnoreCase("MI") ? MIBASE + ltidx * miLng.size() + lngidx : TOBASE + ltidx * toLng.size() + lngidx;        
        
        Vertex vbase = cache.get(idx);
        if(vbase!=null){
            vvx.add(vbase);
        }
        
        Vertex vright = cache.get(idx+1);
        if(vright!=null){
            vvx.add(vright);
        }
        
        Vertex vbott = cache.get(idx+(prov.equalsIgnoreCase("MI")?miLng.size():miLng.size()));
        
        if(vbott!=null){
            vvx.add(vbott);
        }
        
        Vertex vbotRight = cache.get(1+idx+(prov.equalsIgnoreCase("MI")?miLng.size():miLng.size()));
        
        if(vbotRight!=null){
            vvx.add(vbotRight);
        }
        
        return vvx;
    }
    
    private VertexNeighbors coord(Coordinate c, List<Vertex> vs) {
        Double minm = Double.MAX_VALUE;
        VertexNeighbors vnsel = null;
        for(Vertex v : vs){
            for(VertexNeighbors vn : v.getNeighbors()){
                Double mm = vn.metric(c);             
                if( mm > MINSHIFT && mm < minm){
                    vnsel = vn;
                    minm = mm;
                } 
            }
        }
        
        if(vnsel!=null) {
            return vnsel;
        }
        else if(!vs.isEmpty()) {
            return vs.get(0).getNeighbors().get(0);
        } else {
            return null;
        }
        
    }
}
