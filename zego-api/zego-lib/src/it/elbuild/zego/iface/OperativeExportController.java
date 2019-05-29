/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.iface;

/**
 *
 * @author Lu
 */
public interface OperativeExportController {
    
    public StringBuilder login(StringBuilder sb, String start, String stop);
    public StringBuilder error(StringBuilder sb, String start, String stop);
    public StringBuilder third(StringBuilder sb, String start, String stop);   
    public StringBuilder driver(StringBuilder sb, String start, String stop, Integer city);
    
}
