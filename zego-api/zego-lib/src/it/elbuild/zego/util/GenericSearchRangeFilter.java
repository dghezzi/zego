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
public class GenericSearchRangeFilter extends GenericSearchFilter {

    public GenericSearchRangeFilter() {
    
    }
     
    private String start;
    private String stop;
    
    @Override
    public String toQuery() {
        return field+" BETWEEN '"+start+"' and '"+stop+"'";
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getStop() {
        return stop;
    }

    public void setStop(String stop) {
        this.stop = stop;
    }

}
