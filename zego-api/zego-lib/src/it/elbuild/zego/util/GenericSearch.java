/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

import java.util.List;

/**
 *
 * @author Lu
 */
public class GenericSearch {

    public GenericSearch() {
    }
    
    private String table;
    private String sortfield;
    private String direction;
    private List<String> headers;
    private List<GenericSearchLikeFilter> like;
    private List<GenericSearchRangeFilter> range;
    private List<GenericSearchSimpleFilter> simple;
    private Class entity;
    
    public List<String> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }

    public List<GenericSearchLikeFilter> getLike() {
        return like;
    }

    public void setLike(List<GenericSearchLikeFilter> like) {
        this.like = like;
    }

    public List<GenericSearchRangeFilter> getRange() {
        return range;
    }

    public void setRange(List<GenericSearchRangeFilter> range) {
        this.range = range;
    }

    public List<GenericSearchSimpleFilter> getSimple() {
        return simple;
    }

    public void setSimple(List<GenericSearchSimpleFilter> simple) {
        this.simple = simple;
    }

    public String getSortfield() {
        return sortfield;
    }

    public void setSortfield(String sortfield) {
        this.sortfield = sortfield;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Class getEntity() {
        return entity;
    }

    public void setEntity(Class entity) {
        this.entity = entity;
    }


    public String toQuery(boolean count){
        StringBuilder sb = new StringBuilder();
        int condCount = 0 + (like == null ? 0 : like.size()) + (range == null ? 0 : range.size()) + (simple == null ? 0 : simple.size());
        sb.append("select ").append(!count ? "* from " : "count(*) from ").append(table == null?entity.getSimpleName().toLowerCase():table);
        int i = 1;
        
        if(condCount>0){
             sb.append(" where ");
             
             for(GenericSearchLikeFilter f : like) {
                 sb.append(f.toQuery()).append(" ");
                 if(i < condCount){
                     sb.append(" ").append("and").append(" ");
                 }
                 i++;
             }
             
             for(GenericSearchRangeFilter f : range) {
                 sb.append(f.toQuery()).append(" ");
                 if(i < condCount){
                     sb.append(" ").append("and").append(" ");
                 }
                 i++;
             }
             
             for(GenericSearchSimpleFilter f : simple) {
                 sb.append(f.toQuery()).append(" ");
                 if(i < condCount){
                     sb.append(" ").append("and").append(" ");
                 }
                 i++;
             }
             
        }
        
        if(sortfield != null) {
            sb.append(" ").append("order by ").append(sortfield).append(" ").append(direction);
        }
       
        
        String q =  sb.toString();
        //System.out.println(q);
        return q;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }
    
    
    
}
