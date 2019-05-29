/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.persistence;

/**
 *
 * @author Lu
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import el.persistence.db.JpaDAO;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.User;
import it.elbuild.zego.rest.request.ride.EtaRequest;
import it.elbuild.zego.rest.response.HeathMapResponse;
import it.elbuild.zego.util.GenericSearch;
import it.elbuild.zego.util.RESTDateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.CacheStoreMode;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 *
 * @author Luca
 */
    public class NativeSQLDAO extends JpaDAO<Integer, Object> {

    public NativeSQLDAO(EntityManagerFactory emf) {
        super(emf);
    }

    
    public List<User> getActiveUsersWithinRadius(EtaRequest inr, Integer maxMeters, Integer maxSecondsAgo) {
        List<User> us = new ArrayList<>();
        Integer level = inr.getLevel();
        String methodQuery = "";
        if(level != null && (level.equals(Riderequest.REQUEST_LEVEL_CONTROL) || level.equals(Riderequest.REQUEST_LEVEL_PINK))) {
            if(level.equals(Riderequest.REQUEST_LEVEL_CONTROL)) {
                methodQuery = methodQuery+"and bitmask = 16";
            } else if(level.equals(Riderequest.REQUEST_LEVEL_PINK)) {
                methodQuery = methodQuery+"and LOWER(gender) LIKE 'f%'";
            }
        }
        Query q = em.createNativeQuery("select * from user where ? > (6378137*(ACOS(COS(RADIANS(llat)) * \n"
                + "COS(RADIANS(?)) * COS(RADIANS(?) - \n"
                + "RADIANS(llon)) + SIN(RADIANS(llat)) * SIN(RADIANS(?))))) "
                + "and umode = 'driver' and status = " + User.STATUS_ACTIVE + " and rtstatus IN (" + User.REALTIME_STATUS_DRIVER_IDLE + ","+User.REALTIME_STATUS_DRIVER_ANSWERING+") "+methodQuery+" and llocdate > ?; ", User.class);
        q.setHint("javax.persistence.cache.storeMode", "REFRESH");
        try {
            q.setParameter(1, maxMeters);
            q.setParameter(2, inr.getLat());
            q.setParameter(3, inr.getLng());
            q.setParameter(4, inr.getLat());
            q.setParameter(5, RESTDateUtil.getInstance().secondsMillisAgo(maxSecondsAgo));
            us = q.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return us;
    }

    public List<User> getDriverCandidates(Double pulat, Double pulng, Integer maxMeters, Integer maxSecondsAgo, Integer limit, String method, Integer maxcashdue, Integer level) {

        List<User> us = new ArrayList<>();
        
        String methodQuery = "";
        if(method!= null && method.equalsIgnoreCase(Riderequest.REQUEST_METHOD_CASH)) {
            methodQuery = "and cashdue < ? and cardonly = 0 ";
        }
        
        if(level != null && (level.equals(Riderequest.REQUEST_LEVEL_CONTROL) || level.equals(Riderequest.REQUEST_LEVEL_PINK))) {
            if(level.equals(Riderequest.REQUEST_LEVEL_CONTROL)) {
                methodQuery = methodQuery+"and bitmask = 16";
            } else if(level.equals(Riderequest.REQUEST_LEVEL_PINK)) {
                methodQuery = methodQuery+"and LOWER(gender) LIKE 'f%'";
            }
        }
        
        
        Query q = em.createNativeQuery("select * from user where ? > (6378137*(ACOS(COS(RADIANS(llat)) * \n"
                + "COS(RADIANS(?)) * COS(RADIANS(?) - \n"
                + "RADIANS(llon)) + SIN(RADIANS(llat)) * SIN(RADIANS(?))))) "
                + "and llocdate > ? and umode = 'driver' and status = " + User.STATUS_ACTIVE + " and rtstatus = ? and candrive = 1 "+methodQuery+" order by (6378137*(ACOS(COS(RADIANS(llat)) * \n"
                + "COS(RADIANS(?)) * COS(RADIANS(?) - \n"
                + "RADIANS(llon)) + SIN(RADIANS(llat)) * SIN(RADIANS(?))))) asc", User.class);

        String millisago = RESTDateUtil.getInstance().secondsMillisAgo(maxSecondsAgo);
        try {
            q.setParameter(1, maxMeters);
            q.setParameter(2, pulat);
            q.setParameter(3, pulng);
            q.setParameter(4, pulat);
            q.setParameter(5, millisago);
            q.setParameter(6, User.REALTIME_STATUS_DRIVER_IDLE);
            if(!methodQuery.isEmpty()) {
                q.setParameter(7, maxcashdue);
            }
            q.setParameter(methodQuery.isEmpty() ? 7 :8 , pulat);
            q.setParameter(methodQuery.isEmpty() ? 8 : 9, pulng);
            q.setParameter(methodQuery.isEmpty() ? 9 : 10, pulat);
            q.setMaxResults(limit);
            q.setHint("javax.persistence.cache.storeMode", "REFRESH");
            us = q.getResultList();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return us;
    }

    public List<Object> searchByGenericSearch(GenericSearch s, Integer start, Integer stop, Class c) {
        Query q = em.createNativeQuery(s.toQuery(false), c);
        q.setFirstResult(start);
        q.setMaxResults(stop - start);
        return q.getResultList();
    }

    public Long countByGenericSearch(GenericSearch s, Class c) {
        Query q = em.createNativeQuery(s.toQuery(true));       
        Long res = 0l;
        try {
            res = (Long) q.getSingleResult();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
    
    public List<HeathMapResponse> getHeathMap(String start, String stop, Integer sta) {
        List<HeathMapResponse> res = new ArrayList<>();
        Query q =  em.createNativeQuery("select pulat,pulng from riderequest where reqdate between ? and ? "+(sta!=0?" and status = ?":""));
        q.setParameter(1, start);
        q.setParameter(2, stop);
        if(sta!=0) {
            q.setParameter(3, sta);
        }
        
        List<Object> l;
        try {
            l = q.getResultList();
            String status = null;
            Double lat = null;
            Double lng = null;
            Object[] oarr = null;
            for (Object oo : l) {
                oarr = (Object[]) oo;
                lat = Double.parseDouble(oarr[0].toString());
                lng = Double.parseDouble(oarr[1].toString());
                res.add(new HeathMapResponse(lat,lng));
            }
            
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }

}
