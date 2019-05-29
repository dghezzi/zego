package it.sharethecity.mobile.letzgo.network.interfaces;

import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.request.EtaRequest;
import it.sharethecity.mobile.letzgo.network.request.PriceRequest;
import it.sharethecity.mobile.letzgo.network.request.RideRequestAction;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.network.response.EtaResponse;
import it.sharethecity.mobile.letzgo.network.response.PriceResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;


/**
 * Created by lucabellaroba on 12/12/16.
 */

public interface DriverRESTInterface {

    @POST("v1/riderequest/eta")
    Call<EtaResponse> addDriversRequest(@Header(NetworkManager.ZEGOTOKEN) String token, @Body EtaRequest etaRequest);

}
