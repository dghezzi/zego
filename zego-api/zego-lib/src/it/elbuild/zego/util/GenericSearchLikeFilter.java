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
public class GenericSearchLikeFilter extends GenericSearchFilter{

    public GenericSearchLikeFilter() {
    
    }
    
    private String term;
    
    @Override
    public String toQuery() {
        return field+" like '%"+term+"%' ";
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }
    
    
   
}
