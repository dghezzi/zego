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
public interface MarketingExportController {
    
    public StringBuilder user(StringBuilder sb, String start, String stop);
    public StringBuilder ride(StringBuilder sb, String start, String stop);
    public StringBuilder link(StringBuilder sb, String start, String stop);
    public StringBuilder promo(StringBuilder sb, String start, String stop);
    public StringBuilder fullpromo(StringBuilder sb, String start, String stop);
    public StringBuilder signup(StringBuilder sb, String start, String stop);
    public StringBuilder fullride(StringBuilder sb, String start, String stop, Integer city);
    public StringBuilder fullridefixed(StringBuilder sb, String start, String stop, Integer city);
    
}
