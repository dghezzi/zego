/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.gcm;

/**
 *
 * @author Lu
 */
public class PushDetector {
    
    public static final Integer APPLE_DEVICE = 100;
    public static final Integer GOOGLE_DEVICE = 200;    
    
    public static Integer getType(String did)
    {
        return GOOGLE_DEVICE; 
    }
}
