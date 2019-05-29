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
public abstract class GenericSearchFilter {

    public GenericSearchFilter() {
    }
    
    
    protected String field;
    public abstract String toQuery();

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }
   
    
}
