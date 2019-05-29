package it.sharethecity.mobile.letzgo.network.interfaces;

import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.request.EtaRequest;
import it.sharethecity.mobile.letzgo.network.request.PriceRequest;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.network.response.EtaResponse;
import it.sharethecity.mobile.letzgo.network.response.PriceResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;



/**
 * Created by lucabellaroba on 12/12/16.
 */

public interface PassengerRESTInterface {

    @POST("v1/riderequest/eta")
    Call<EtaResponse> etaRequest(@Header(NetworkManager.ZEGOTOKEN) String token, @Body EtaRequest etaRequest);

    @POST("v1/riderequest/price")
    Call<PriceResponse> estimatedPriceRequest(@Header(NetworkManager.ZEGOTOKEN) String token, @Body PriceRequest priceRequest);

    @POST("v1/riderequest")
    Call<Riderequest> postRideRequest(@Header(NetworkManager.ZEGOTOKEN) String token, @Body Riderequest riderequest);

    @POST("v1/riderequest/force")
    Call<Riderequest> postForcedRideRequest(@Header(NetworkManager.ZEGOTOKEN) String token, @Body Riderequest currentRide);

    @POST("v1/riderequest/priceupdate")
    Call<Riderequest> postPassPriceRideRequest(@Header(NetworkManager.ZEGOTOKEN) String token, @Body Riderequest currentRide);
}
