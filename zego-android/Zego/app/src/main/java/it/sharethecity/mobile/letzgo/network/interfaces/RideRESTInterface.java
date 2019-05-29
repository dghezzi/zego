package it.sharethecity.mobile.letzgo.network.interfaces;

import java.util.List;

import it.sharethecity.mobile.letzgo.dao.Feedback;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.request.CompatRequest;
import it.sharethecity.mobile.letzgo.network.request.RideRequestAction;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by lucabellaroba on 13/12/16.
 */

public interface RideRESTInterface {

    @GET("v1/riderequest/{rid}")
    Call<Riderequest> getRideRequest(@Header(NetworkManager.ZEGOTOKEN) String token, @Path("rid") Integer rid);

    @POST("v1/riderequest/action")
    Call<Riderequest> postRideRequestAction(@Header(NetworkManager.ZEGOTOKEN) String token, @Body RideRequestAction reqa);

    @POST("v1/feedback")
    Call<Feedback> postFeedback(@Header(NetworkManager.ZEGOTOKEN) String token, @Body Feedback feedback);

    @GET("v1/riderequest/history/{uid}/{mode}/{start}/{stop}")
    Call<List<CompatRequest>> getRideHistory(@Header(NetworkManager.ZEGOTOKEN) String token, @Path("uid") Integer uid, @Path("mode") String mode, @Path("start") Integer start, @Path("stop") Integer stop);
}
