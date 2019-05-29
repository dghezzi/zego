/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

/**
 *
 * @author Lu
 */
public class GenericSearchSimpleFilter extends GenericSearchFilter{

    public GenericSearchSimpleFilter() {
    
    }

    private String filter;
    
    @Override
    public String toQuery() {
        return field+" = '"+filter+"' ";
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }
    
    
}
