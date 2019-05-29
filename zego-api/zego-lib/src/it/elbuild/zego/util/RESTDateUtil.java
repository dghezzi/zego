/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 *
 * @author Lu
 */
public class RESTDateUtil {
    
    private static RESTDateUtil instance;
    private final String pattern = "dd-MM-yyyy HH:mm:ss";
    private SimpleDateFormat formatter;
    private Calendar cal;
    private Calendar calII;
    
    private RESTDateUtil()
    {
        formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone("CET"));
        cal = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        calII = Calendar.getInstance(TimeZone.getTimeZone("CET"));
    }
    
    public static RESTDateUtil getInstance()
    {
        if(instance==null)
        {
            instance = new RESTDateUtil();
        }
        
        return instance;
    }
    
    public java.util.Date now()
    {
        return cal.getTime();
    }
    
    public String secondsNow()
    {
        cal = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        return String.valueOf(cal.getTime().getTime()/1000);
    }
    
    public String secondsMillisAgo(int agoSec)
    {
        cal = Calendar.getInstance(TimeZone.getTimeZone("CET"));
        cal.add(Calendar.SECOND, -1*agoSec);
        return String.valueOf(cal.getTime().getTime()/1000);
    }
   
    public Integer secondsDiffNow(String submit)
    {
        return Integer.parseInt(secondsNow()) - Integer.parseInt(submit);
    }
    
    public Integer secondsDiff(String submit, String handled)
    {
        return Integer.parseInt(handled) - Integer.parseInt(submit);
    }
    
    
    public Date formatDateSeconds(String seconds){        
        calII.setTimeInMillis(Long.parseLong(seconds)*1000);
        return calII.getTime();
    }
   
    public String formatSecondsDate(Date date, boolean addDay){
        calII.setTimeInMillis(date.getTime());
        if(addDay){
            calII.add(Calendar.DAY_OF_YEAR, 1);
        }
        return String.valueOf(calII.getTime().getTime()/1000);
    }
    
    public Integer field(Integer f)
    {
        return cal.get(f);
    }
}
