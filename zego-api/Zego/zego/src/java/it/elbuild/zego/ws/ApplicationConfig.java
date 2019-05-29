/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.elbuild.zego.ws;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author Lu
 */
@javax.ws.rs.ApplicationPath("v1")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(it.elbuild.zego.ws.AddressFacadeREST.class);
        resources.add(it.elbuild.zego.ws.ApiTestFacadeREST.class);
        resources.add(it.elbuild.zego.ws.AppVersionFacadeREST.class);
        resources.add(it.elbuild.zego.ws.AreaFacadeREST.class);
        resources.add(it.elbuild.zego.ws.BanHistoryFacadeREST.class);
        resources.add(it.elbuild.zego.ws.BaseFacadeREST.class);
        resources.add(it.elbuild.zego.ws.BaseRequestFilter.class);
        resources.add(it.elbuild.zego.ws.BlacklistFacadeREST.class);
        resources.add(it.elbuild.zego.ws.CORSFilter.class);
        resources.add(it.elbuild.zego.ws.CashFacadeREST.class);
        resources.add(it.elbuild.zego.ws.CityFacadeREST.class);
        resources.add(it.elbuild.zego.ws.ConfFacadeREST.class);
        resources.add(it.elbuild.zego.ws.DialogFacadeREST.class);
        resources.add(it.elbuild.zego.ws.DriverdataFacadeREST.class);
        resources.add(it.elbuild.zego.ws.ErrormsgFacadeREST.class);
        resources.add(it.elbuild.zego.ws.ExportFacadeREST.class);
        resources.add(it.elbuild.zego.ws.FeedbackFacadeREST.class);
        resources.add(it.elbuild.zego.ws.GoogleAPIFacadeREST.class);
        resources.add(it.elbuild.zego.ws.LangFacadeREST.class);
        resources.add(it.elbuild.zego.ws.LocationFacadeREST.class);
        resources.add(it.elbuild.zego.ws.ManufacturerFacadeREST.class);
        resources.add(it.elbuild.zego.ws.NationFacadeREST.class);
        resources.add(it.elbuild.zego.ws.NextipFacadeREST.class);
        resources.add(it.elbuild.zego.ws.NextipInternalFacadeREST.class);
        resources.add(it.elbuild.zego.ws.NotificationsFacadeREST.class);
        resources.add(it.elbuild.zego.ws.ObfuscationFacadeREST.class);
        resources.add(it.elbuild.zego.ws.OtherExceptionHandler.class);
        resources.add(it.elbuild.zego.ws.PaymentFacadeREST.class);
        resources.add(it.elbuild.zego.ws.PinFacadeREST.class);
        resources.add(it.elbuild.zego.ws.PromoFacadeREST.class);
        resources.add(it.elbuild.zego.ws.RESTBodyLogger.class);
        resources.add(it.elbuild.zego.ws.RESTExceptionHandler.class);
        resources.add(it.elbuild.zego.ws.RequestFacadeREST.class);
        resources.add(it.elbuild.zego.ws.RiderequestDriversFacadeREST.class);
        resources.add(it.elbuild.zego.ws.ServiceFacadeREST.class);
        resources.add(it.elbuild.zego.ws.UserFacadeREST.class);
        resources.add(it.elbuild.zego.ws.UserPromoFacadeREST.class);
        resources.add(it.elbuild.zego.ws.UseractionFacadeREST.class);
        resources.add(it.elbuild.zego.ws.ZoneFacadeREST.class);
        resources.add(it.elbuild.zego.ws.ZoneServiceFacadeREST.class);
    }
    
}
