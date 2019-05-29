/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lu
 */
public class MandrillMapper {
  
    public static Map<String,String> param(Object o) {
        Map<String,String> m = new HashMap<>();
        Field[] f = o.getClass().getDeclaredFields();
        for(Field ff : f) {
            String fname = ff.getName();
            fname = fname.substring(0, 1).toUpperCase()+fname.substring(1);
            Class c = ff.getType();
            Method method;
            try {
                method = o.getClass().getMethod("get"+fname); 
                if(method != null) {
                    Object status = (Object) method.invoke(o.getClass().cast(o));
                    
                        m.put(ff.getName(), status == null ? "" : status.toString());
                    
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex ) {
               // Logger.getLogger(MandrillMapper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
               Logger.getLogger(MandrillMapper.class.getName()).log(Level.SEVERE, null, e);

            } 
            
        }
        return m;
    }
    
    public static Map<String,String> param(Object o, String pfx) {
        Map<String,String> m = new HashMap<>();
        Field[] f = o.getClass().getDeclaredFields();
        for(Field ff : f) {
            String fname = ff.getName();
            fname = fname.substring(0, 1).toUpperCase()+fname.substring(1);
            Class c = ff.getType();
            Method method;
            try {
                method = o.getClass().getMethod("get"+fname); 
                if(method != null) {
                    Object status = (Object) method.invoke(o.getClass().cast(o));
                    if(status != null) {
                    m.put(pfx+"_"+ff.getName(), status.toString());
                    }
                }
            } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex ) {
               // Logger.getLogger(MandrillMapper.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception e) {
               Logger.getLogger(MandrillMapper.class.getName()).log(Level.SEVERE, null, e);

            } 
            
        }
        return m;
    }
    
    
}
