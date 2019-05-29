package it.sharethecity.mobile.letzgo.network.interfaces;

import java.util.List;

import it.sharethecity.mobile.letzgo.dao.Address;
import it.sharethecity.mobile.letzgo.dao.Area;
import it.sharethecity.mobile.letzgo.dao.Driverdata;
import it.sharethecity.mobile.letzgo.dao.GeoCodeRequest;
import it.sharethecity.mobile.letzgo.dao.GeoCodeResponse;
import it.sharethecity.mobile.letzgo.dao.GooglePlaceResponse;
import it.sharethecity.mobile.letzgo.dao.GoogleRequest;
import it.sharethecity.mobile.letzgo.dao.Position;
import it.sharethecity.mobile.letzgo.dao.Promo;
import it.sharethecity.mobile.letzgo.dao.StripeCard;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.dao.Userpromo;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.request.BootRequest;
import it.sharethecity.mobile.letzgo.network.request.CompatRequest;
import it.sharethecity.mobile.letzgo.network.request.PinRequest;
import it.sharethecity.mobile.letzgo.network.request.RedeemRequest;
import it.sharethecity.mobile.letzgo.network.request.ReferralRequest;
import it.sharethecity.mobile.letzgo.network.request.ResendPinRequest;
import it.sharethecity.mobile.letzgo.network.request.StripeCreateCustomerRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;



/**
 * Created by lucabellaroba on 14/11/16.
 */

public interface UserRESTInterface {



    @POST("v1/user/signup")
    Call<User> signUp(@Body BootRequest bootRequest);

    @POST("v1/user/validate")
    Call<User> pinVerification(@Header(NetworkManager.ZEGOTOKEN) String token, @Body PinRequest pinRequest);

    @POST("v1/user/complete")
    Call<User> completeUser(@Header(NetworkManager.ZEGOTOKEN) String token, @Body User u);

    @POST("v1/user/referral")
    Call<User> promoCode(@Header(NetworkManager.ZEGOTOKEN) String token, @Body ReferralRequest rf);

    @POST("v1/user/stripe")
    Call<User> sendCardInfo(@Header(NetworkManager.ZEGOTOKEN) String token, @Body StripeCreateCustomerRequest str);

    @POST("v1/user/silent")
    Call<User> silentLogin(@Header(NetworkManager.ZEGOTOKEN) String token,@Body BootRequest br);

    @POST("v1/user/login")
    Call<User> login( @Body BootRequest br);

    @GET("v1/user/{uid}")
    Call<User> getUserById(@Header(NetworkManager.ZEGOTOKEN) String token, @Path("uid") Integer uid);

    @POST("v1/user")
    Call<User> postUser(@Header(NetworkManager.ZEGOTOKEN) String zegotoken, @Body User u);

    @POST("v1/user/resend")
    Call<User> postResend(@Header(NetworkManager.ZEGOTOKEN) String zegotoken, @Body ResendPinRequest rp);

    @POST("v1/address")
    Call<Address> postAddress(@Header(NetworkManager.ZEGOTOKEN) String token, @Body Address address);

    @POST("v1/google/geocomplete")
    Call<GooglePlaceResponse> postForPrediction(@Header(NetworkManager.ZEGOTOKEN) String token,@Body GoogleRequest strajobGoogleRequest);

    @POST("v1/google/geocode")
    Call<GeoCodeResponse> postForGeocode(@Header(NetworkManager.ZEGOTOKEN) String token,@Body GeoCodeRequest geoCodeRequest);

    @GET("v1/address/{uid}/last/all")
    Call<List<Address>> getUserAddress(@Header(NetworkManager.ZEGOTOKEN) String token, @Path("uid") Integer uid);

    @POST("v1/location")
    Call<Position> postLocation(@Header(NetworkManager.ZEGOTOKEN) String token, @Body Position loc);

    @GET("v1/user/cards/{uid}")
    Call<List<StripeCard>> getUserCards(@Header(NetworkManager.ZEGOTOKEN) String token, @Path("uid") Integer uid);

    @GET("v1/address/{uid}/last/{type}")
    Call<List<Address>> getAddress(@Header(NetworkManager.ZEGOTOKEN) String token, @Path("uid") Integer uid,@Path("type") String type);

    @POST("v1/google/addresscomplete")
    Call<List<Address>> postForPredictionRideAddress(@Header(NetworkManager.ZEGOTOKEN) String token,@Body GoogleRequest strajobGoogleRequest);

    @POST("v1/user/changemodeto/{mode}")
    Call<User> postChamgeMode(@Header(NetworkManager.ZEGOTOKEN) String token,@Path("mode") String mode, @Body User user);

    @POST("v1/user/{uid}/card/makedefault")
    Call<List<StripeCard>> postMakeDefault(@Header(NetworkManager.ZEGOTOKEN) String token,@Path("uid") Integer uid,@Body StripeCard card);

    @POST("v1/user/{uid}/card/delete")
    Call<List<StripeCard>> postDeleteCard(@Header(NetworkManager.ZEGOTOKEN) String token,@Path("uid") Integer uid,@Body StripeCard card);

    @GET("v1/userpromo/uid/{uid}")
    Call<List<Userpromo>> getAllMyPromo(@Header(NetworkManager.ZEGOTOKEN) String token, @Path("uid") Integer uid);

    @POST("v1/promo/redeem")
    Call<List<Userpromo>> postAddPromo(@Header(NetworkManager.ZEGOTOKEN) String token, @Body RedeemRequest redeemRequest);

    @GET("v1/driverdata/user/{uid}")
    Call<Driverdata> getDriverData(@Header(NetworkManager.ZEGOTOKEN) String token,@Path("uid") Integer uid);

    @GET("v1/area")
    Call<List<Area>> getAllAreas(@Header(NetworkManager.ZEGOTOKEN) String token);

    @POST("v1/driverdata")
    Call<Driverdata> postDriverData(@Header(NetworkManager.ZEGOTOKEN) String token, @Body Driverdata driverData);

    @POST("v1/user/kill")
    Call<User> logout(@Header(NetworkManager.ZEGOTOKEN) String token, @Body User user);

    @POST("v1/user/paydebt")
    Call<User> payDebt(@Header(NetworkManager.ZEGOTOKEN) String zegotoken, @Body User u);

}
