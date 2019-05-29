/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ejb;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import el.persistence.db.DBTuple;
import it.elbuild.zego.entities.Conf;
import it.elbuild.zego.entities.Riderequest;
import it.elbuild.zego.entities.Userpromo;
import it.elbuild.zego.entities.Zone;
import it.elbuild.zego.iface.ConfController;
import it.elbuild.zego.iface.DAOProvider;
import it.elbuild.zego.iface.EntityController;
import it.elbuild.zego.iface.PriceCalculator;
import it.elbuild.zego.rest.exception.RESTException;
import it.elbuild.zego.rest.request.ride.PriceRequest;
import it.elbuild.zego.rest.response.PriceEstimateResponse;
import it.elbuild.zego.util.RESTDateUtil;
import it.elbuild.zego.util.ZegoK;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.Instant;

/**
 *
 * @author Lu
 */
@Stateless
public class PriceCalculatorBean implements PriceCalculator {

    @EJB
    DAOProvider provider;
    
    EntityController<Integer, Zone> zoneCtrl;

    EntityController<Integer, Userpromo> promoCtrl;

    @EJB
    ConfController conf;

    private GeoApiContext geoCtx;
    private GeoApiContext backupCtx;
    private List<Zone> zones;
    
    private static Integer METERS_THRESHOLD = 2000;

    @PreDestroy
    public void end() {

    }

    @PostConstruct
    public void init() {
        zoneCtrl = provider.provide(Zone.class);
        promoCtrl = provider.provide(Userpromo.class);
        geoCtx = new GeoApiContext().setEnterpriseCredentials("", "");
        zones = zoneCtrl.findAll();
        METERS_THRESHOLD = conf.getConfByKey(Conf.METER_THRESHOLD).asInteger(2000);
    }

    @Override
    public Riderequest updatePrice(Riderequest r, boolean first) throws RESTException {
        Zone z = z(r.getPulat(), r.getPulng());

        if (!first) {
            Riderequest up =  updatePriceRequest(r, z, promo(r.getPid()));
            up.checkRouding();
            return up;
        } else {
            Integer passprice = Objects.equals(r.getPassprice(), r.getExtprice()) ? -1 : r.getPassprice();
            Integer extprice = r.getExtprice();
            Integer driverprice = r.getDriverprice() == 0 ? r.getExtprice() : r.getDriverprice();
            Riderequest rfirst = firstPriceRequest(r, z, promo(r.getPid()));
            
            if(passprice >= 0) {                
                rfirst = updatePriceRequest(rfirst, z, promo(r.getPid()));
                rfirst.setPassprice(passprice);
                rfirst.setExtprice(extprice);
                rfirst.setDriverprice(driverprice);
                rfirst.checkRouding();
                return rfirst;
            } else {
                rfirst.checkRouding();
                return rfirst;
            }
        }
    }
    
    

    @Override
    public PriceEstimateResponse estimate(PriceRequest pr, Integer uid) throws RESTException {
        try {
            PriceEstimateResponse per = new PriceEstimateResponse();
            Zone z = z(pr.getPulat(), pr.getPulng());
            Userpromo promo;
            Integer pkm = conf.getConfByKey(Conf.PRICING_CENT_PER_KM).asInteger(120);
            Integer minfee = conf.getConfByKey(Conf.PRICING_CENT_MINIMUM_FEE).asInteger(600);
            Integer maxfee = conf.getConfByKey(Conf.PRICING_CENT_MAXIMUM_FEE).asInteger(6000);
            Double commission = conf.getConfByKey(Conf.PRICING_ZEGO_COMMISSION).asDouble(20.d);
            Integer minZegoFee = conf.getConfByKey(Conf.PRICING_CENT_MINIMUM_ZEGOFEE).asInteger(100);

            if (z != null) {
                if (z.getPriceperkm() != null) {
                    pkm = z.getPriceperkm();
                }

                if (z.getBaseprice() != null) {
                    minfee = z.getBaseprice();
                }
            }

            
            DirectionsApiRequest req = DirectionsApi.newRequest(geoCtx).origin(new LatLng(pr.getPulat(), pr.getPulng())).
                    destination(new LatLng(pr.getDolat(), pr.getDolng())).mode(TravelMode.DRIVING).departureTime(Instant.now()).alternatives(true);
            
            DirectionsRoute[] routes = req.await();
            DirectionsRoute shortest = null;
            for(DirectionsRoute dr : routes) {
                if(shortest == null || shortest.legs[0].distance.inMeters > dr.legs[0].distance.inMeters) {
                    shortest = dr;
                }
                System.out.println("ROUTE LENGHT: "+shortest.legs[0].distance.inMeters);
            }
            /*
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(geoCtx)
                    .origins(new LatLng(pr.getPulat(), pr.getPulng()))
                    .destinations(new LatLng(pr.getDolat(), pr.getDolng()));

            DistanceMatrix dm = req.await();
            */
            if (shortest != null) {

                
                long distanceInM = shortest.legs[0].distance.inMeters;
                distanceInM = (1 + (distanceInM / 500)) * 500l; // rounding 500 m superiori

                Integer price = 0;
                if (distanceInM < METERS_THRESHOLD) {
                    price = minfee;
                } else {
                    price = minfee + ((Double) (pkm * ((distanceInM - METERS_THRESHOLD) / 1000.d))).intValue();
                }
                //price = ((Double) (pkm * ((distanceInM) / 1000.d))).intValue();
                if(price < minfee) {
                    price = minfee;
                }
                
                
                if (price % 10 != 0) {
                    price = (1 + (price / 10)) * 10; // rounding 
                }

                if (price > maxfee) {
                    price = maxfee;
                }

                Integer zgf = ((Double) (price * commission / 100.d)).intValue();
                if (zgf % 10 != 0) {
                    zgf = (zgf / 10) * 10;
                }

                if (zgf < minZegoFee) {
                    zgf = minZegoFee;
                }
                
                
                // euro rounding, to support cash
                if((price+zgf)%100 != 0) {
                    Integer intround = (price+zgf)%100;
                    if(intround >=50) {
                        price = price + (100-intround);
                    } else {
                        zgf = zgf - (intround);
                    }
                }

                per.setExtprice(price + zgf);
                per.setZegofee(zgf);
                per.setDriverfee(price);
                per.setDropoff(nice(shortest.legs[0].endAddress));
                per.setSeconds((int) shortest.legs[0].duration.inSeconds);
                per.setMeters((int) shortest.legs[0].distance.inMeters);

                promo = promo(uid);
                if (promo != null) {
                    per.setPromocode(promo.getPromo().getCode());
                    Integer discount = promo.discount(promo.getPromo().getFeeonly().equals(1)  ? zgf : price+zgf);
                    if (discount % 10 != 0) {
                        discount = (discount / 10) * 10;
                    }
                    per.setDiscount(discount);
                    Integer zegoFeeAfterDiscount = per.getZegofee() - per.getDiscount();
                    if (zegoFeeAfterDiscount >= 0) {
                        per.setStripezegofee(zegoFeeAfterDiscount);
                        per.setStripedriverfee(per.getDriverfee());
                    } else {
                        per.setStripezegofee(0);
                        if (promo.getPromo().getFeeonly() != null && promo.getPromo().getFeeonly().equals(0)) {
                            per.setStripedriverfee(Math.max(0, per.getDriverfee() + zegoFeeAfterDiscount));
                        } else {
                            per.setStripedriverfee(per.getDriverfee());
                        }
                    }
                } else {
                    per.setStripedriverfee(per.getDriverfee());
                    per.setStripezegofee(per.getZegofee());

                }

                return per;
            } else {
                    throw new RESTException(ZegoK.Error.CANNOT_FIND_ROUTE);
                }
        } catch (Exception ex) {
            Logger.getLogger(PriceCalculatorBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Zone z(double lat, double lng) {
        Zone z = null;
        for (Zone zz : zones) {
            if (zz.include(lat, lng)) {
                z = zz;
                break;
            }
        }
        return z;
    }

    private Userpromo promo(Integer uid) {        
        List<Userpromo> promos = promoCtrl.findListCustom("findNotUsedByUid", Arrays.asList(new DBTuple("uid", uid),new DBTuple("now", RESTDateUtil.getInstance().secondsNow())));
        return promos == null || promos.isEmpty() ? null : promos.get(0);
    }

    private Riderequest updatePriceRequest(Riderequest r, Zone z, Userpromo promo) throws RESTException {
        Integer minZegoFee = conf.getConfByKey(Conf.PRICING_CENT_MINIMUM_ZEGOFEE).asInteger(100);
        Double commission = conf.getConfByKey(Conf.PRICING_ZEGO_COMMISSION).asDouble(20.d);
        Integer price = r.getDriverfee();
        
        if(r.getPassprice() != null && r.getPassprice()<minZegoFee) {
            r.setPassprice(minZegoFee);
        }

        System.out.println("UP PRICING: "+ r.getPassprice() +  " " + r.getExtprice() + " " + r.getDriverprice());
        int modifiedBy = 0;
        if (r.getPassprice() != null && (!Objects.equals(r.getPassprice(), r.getExtprice()))) {
            price = r.getPassprice();
            modifiedBy = 1;
        }

        if (r.getDriverprice() != null && (r.getDriverprice() < r.getExtprice())) {
            price = r.getDriverprice();
            modifiedBy = 1;
        }

        if (price % 10 != 0) {
            price = (1 + (price / 10)) * 10; // rounding 
        }
        Integer zgf = ((Double) (price * commission / 100.d)).intValue();
        System.out.println("UP PRICING [2]: "+ price +  " " + zgf + " " + r.getExtprice());

        if (modifiedBy == 1) {
            Integer selPrice = price;
            price = ((Double) (price / ((100 + commission) / 100.d))).intValue();
            if (price % 10 != 0) {
                price = (1 + (price / 10)) * 10; // rounding 
            }
            zgf = selPrice - price;
        }

        if (zgf % 10 != 0) {
            zgf = (zgf / 10) * 10;
        }
        Integer delta = 0;
        if (zgf < minZegoFee) {
            delta = minZegoFee - zgf;
            zgf = minZegoFee;
        }
        System.out.println("UP PRICING [4]: "+ price +  " " + zgf + " " + delta + " "+modifiedBy);
        if(modifiedBy == 1) {
            r.setZegofee(zgf);
            r.setDriverfee(price-delta);
        } else if(modifiedBy == 2) {
             r.setZegofee(zgf);
             r.setDriverfee(price);
        } else {
            r.setZegofee(zgf);
            r.setDriverfee(price);
        }
       
        if((r.getDriverfee()+r.getZegofee())%100 != 0) {
                    Integer intround = (r.getDriverfee()+r.getZegofee())%100;
                    if(intround >=50) {
                        r.setDriverfee(r.getDriverfee() + (100-intround));
                    } else {
                        r.setZegofee(r.getZegofee() - (intround));
                    }
                }

        if (promo != null && (r.getMethod() == null || r.getMethod().equals(Riderequest.REQUEST_METHOD_CARD))) {

            Integer discount = promo.discount(promo.getPromo().getFeeonly().equals(1)  ? zgf : price+zgf);
            
            System.out.println("UP PRICING [5]: "+ price +  " " + zgf + " " + discount);
            if (discount % 10 != 0) {
                discount = (discount / 10) * 10;
            }
            r.setDiscount(Math.min(price+zgf, discount));
            Integer zegoFeeAfterDiscount = r.getZegofee() - r.getDiscount();
            if (zegoFeeAfterDiscount >= 0) {
                r.setStripezegofee(zegoFeeAfterDiscount);
                r.setStripedriverfee(r.getDriverfee());
            } else {
                r.setStripezegofee(0);
                if (promo.getPromo().getFeeonly() != null && promo.getPromo().getFeeonly().equals(0)) {
                    r.setStripedriverfee(Math.max(0, r.getDriverfee() + zegoFeeAfterDiscount));
                } else {
                    r.setStripedriverfee(r.getDriverfee());
                }
            }

            r.setPromoid(promo.getId());
        } else {
            r.setStripedriverfee(r.getDriverfee());
            r.setStripezegofee(r.getZegofee());
            r.setDiscount(0);
            r.setPromoid(null);
        }

        return r;

    }

    private Riderequest firstPriceRequest(Riderequest r, Zone z, Userpromo promo) throws RESTException {

        try {

            Integer pkm = conf.getConfByKey(Conf.PRICING_CENT_PER_KM).asInteger(120);
            Integer minfee = conf.getConfByKey(Conf.PRICING_CENT_MINIMUM_FEE).asInteger(600);
            Integer maxfee = conf.getConfByKey(Conf.PRICING_CENT_MAXIMUM_FEE).asInteger(6000);
            Double commission = conf.getConfByKey(Conf.PRICING_ZEGO_COMMISSION).asDouble(20.d);
            Integer minZegoFee = conf.getConfByKey(Conf.PRICING_CENT_MINIMUM_ZEGOFEE).asInteger(100);
            Integer mindist = conf.getConfByKey(Conf.ZEGO_SHORTEST_ROUTE).asInteger(1000);

            if (z != null) {
                if (z.getPriceperkm() != null) {
                    pkm = z.getPriceperkm();
                }

                if (z.getBaseprice() != null) {
                    minfee = z.getBaseprice();
                }

                r.setZid(z.getId());
                r.setZonename(z.getName());
            }

            DirectionsApiRequest req = DirectionsApi.newRequest(geoCtx).origin(new LatLng(r.getPulat(), r.getPulng())).
                    destination(new LatLng(r.getDolat(), r.getDolng())).mode(TravelMode.DRIVING).alternatives(true)
                    .departureTime(Instant.now());
            
            DirectionsRoute[] routes = req.await();
            DirectionsRoute shortest = null;
            for(DirectionsRoute dr : routes) {
                if(shortest == null || shortest.legs[0].distance.inMeters > dr.legs[0].distance.inMeters) {
                    shortest = dr;
                }
            }
            /*
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(geoCtx)
                    .origins(new LatLng(r.getPulat(), r.getPulng()))
                    .destinations(new LatLng(r.getDolat(), r.getDolng()));

            DistanceMatrix dm = req.await();
                    */
            if (shortest != null) {
                
                 r.setExtmeters(((Long) shortest.legs[0].distance.inMeters).intValue());
                 r.setExtsecond(((Long) shortest.legs[0].duration.inSeconds).intValue());
                 r.setExtshort(r.getExtsecond());

                long distanceInM = shortest.legs[0].distance.inMeters;
                distanceInM = (1 + (distanceInM / 500)) * 500l; // rounding 500 m superiori

                Integer price = 0;
                if (distanceInM < METERS_THRESHOLD) {
                    price = minfee;
                } else {
                    price = minfee + ((Double) (pkm * ((distanceInM - METERS_THRESHOLD) / 1000.d))).intValue();
                }

                //price = ((Double) (pkm * ((distanceInM) / 1000.d))).intValue();
                if(price < minfee) {
                    price = minfee;
                } 
                System.out.println("FIRST PRICING [3]: "+ price +  " " + minfee + " " + r.getExtprice());

                boolean modified = false;
                if (r.getPassprice() > r.getExtprice()) {
                    price = ((Double) (r.getPassprice() / ((100 + commission) / 100.d))).intValue();
                    modified = true;
                } else if (r.getPassprice() < r.getExtprice()) {
                    price = ((Double) (r.getPassprice() / ((100 + commission) / 100.d))).intValue();
                    modified = true;
                }

                if (price % 10 != 0) {
                    price = (1 + (price / 10)) * 10; // rounding 
                }

                if (price > maxfee) {
                    price = maxfee;
                }

                Integer zgf = ((Double) (price * commission / 100.d)).intValue();
                if (zgf % 10 != 0) {
                    zgf = (zgf / 10) * 10;
                }

                Integer delta = 0;
                if (zgf < minZegoFee) {
                    if(modified){
                        delta = minZegoFee - zgf;
                    }
                    zgf = minZegoFee;
                }

                // euro rounding, to support cash
                if((price+zgf)%100 != 0) {
                    Integer intround = (price+zgf)%100;
                    if(intround >=50) {
                        price = price + (100-intround);
                    } else {
                        zgf = zgf - (intround);
                    }
                }
                r.setZegofee(zgf);
                r.setDriverfee(price-delta);

                System.out.println("FIRST PRICING [3]: "+ price +  " " + minfee + " " + r.getExtprice());
                System.out.println("FIRST PRICING [4]: "+ price +  " " + zgf + " " + delta);
                r.setExtprice(price + zgf);
                r.setDriverprice(price + zgf);
                r.setPassprice(price + zgf);

                if (promo != null && (r.getMethod() == null || r.getMethod().equalsIgnoreCase(Riderequest.REQUEST_METHOD_CARD))) {

                    Integer discount = promo.discount(promo.getPromo().getFeeonly().equals(1)  ? zgf : price+zgf);
                    if (discount % 10 != 0) {
                        discount = (discount / 10) * 10;
                    }
                    
                    
                    
                    r.setDiscount(Math.min(price+zgf, discount));

                    Integer zegoFeeAfterDiscount = r.getZegofee() - r.getDiscount();
                    if (zegoFeeAfterDiscount >= 0) {
                        r.setStripezegofee(zegoFeeAfterDiscount);
                        r.setStripedriverfee(r.getDriverfee());

                    } else {
                        r.setStripezegofee(0);
                        if (promo.getPromo().getFeeonly() != null && promo.getPromo().getFeeonly().equals(0)) {
                            r.setStripedriverfee(Math.max(0, r.getDriverfee() + zegoFeeAfterDiscount));
                        } else {
                            r.setStripedriverfee(r.getDriverfee());
                        }

                    }
                    r.setPromoid(promo.getId());
                } else {
                    r.setStripedriverfee(r.getDriverfee());
                    r.setStripezegofee(r.getZegofee());
                    r.setDiscount(0);
                    r.setPromoid(null);
                }
                System.out.println("PRICING: "+ r.getPassprice() +  " " + r.getExtprice() + " " + r.getDriverprice());

                return r;
            } else {
                    throw new RESTException(ZegoK.Error.CANNOT_FIND_ROUTE);
                }
        } catch (Exception ex) {
            Logger.getLogger(PassengerControllerBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return r;
        }
    }

    private String nice(String address) {
        int comma = StringUtils.countMatches(address, ",");
        if (comma >= 2) {
            int idx = 0;

            for (int i = 0; i < 2; i++) {
                idx = address.indexOf(",", idx + 1);
            }
            return address.substring(0, idx);
        } else {
            return address;
        }
    }
}
