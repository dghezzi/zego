package it.sharethecity.mobile.letzgo.services;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.otto.Subscribe;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.bus.BusRequestMessage;
import it.sharethecity.mobile.letzgo.bus.BusResponseMessage;
import it.sharethecity.mobile.letzgo.dao.Position;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private BroadcastReceiver locationProviderBrdReceiver;
    public static final int PASSENGER_LOCATION_FREQUENCY =  10 * 60 * 1000;  //period in ms
    public static final int DRIVER_LOCATION_FREQUENCY = 5000;  //period in ms
    private User userLogged;
    private Location lastLocation;

    private CountDownTimer locationTimerPoller;

    @Override
    public void onCreate() {
        super.onCreate();

        if(ZegoConstants.DEBUG){
            Log.d("POSITION SERVICE:","CREATED");
            Toast.makeText(getBaseContext(),"Location Service created",Toast.LENGTH_SHORT).show();
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        registerLocationProviderBrdReceiver();
        ApplicationController.getInstance().getOttoBus().register(this);
//        mGoogleApiClient.connect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(ZegoConstants.DEBUG){
            Log.d("POSITION_SERVICE:","KILLED");
            Toast.makeText(getBaseContext(),"Location Service Killed",Toast.LENGTH_SHORT).show();
        }
        unregisterReceiver(locationProviderBrdReceiver);
        ApplicationController.getInstance().getOttoBus().unregister(this);
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
    }

    public LocationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
            return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userLogged = ApplicationController.getInstance().getUserLogged();
        if(mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }

        mGoogleApiClient.connect();
        return Service.START_NOT_STICKY;
    }

//    private void setLocationRequest(Integer periodInMilli) {
//        // imposto il periodo di campionamento della posizione e quindi il periodo di invio delle locations
//        if (mLocationRequest == null)
//            mLocationRequest = new LocationRequest();
//
//        mLocationRequest.setInterval(periodInMilli);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setFastestInterval(periodInMilli);
//        if (mGoogleApiClient.isConnected()) {
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//
//                    return;
//                }
//                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, LocationService.this);
//            }
//            else{
//                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, LocationService.this);
//            }
//
//        }
//
//    }

    private boolean isGpsConnected() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isThereConnectivity(){
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    //region GOOGLE API CLIENT CALLBACKS
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        userLogged = ApplicationController.getInstance().getUserLogged();
        if(locationTimerPoller != null) locationTimerPoller.cancel();

        locationTimerPoller = new CountDownTimer(userLogged.isDriver() ? DRIVER_LOCATION_FREQUENCY : PASSENGER_LOCATION_FREQUENCY,10000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                locationTimerPoller.start();
                getLastLocation();
            }
        };

        getLastLocation();
        locationTimerPoller.start();
//        if(userLogged != null){
//            setLocationRequest(userLogged.isDriver() ? DRIVER_LOCATION_FREQUENCY: PASSENGER_LOCATION_FREQUENCY);
//        }else{
//            // TODO: 11/12/16 utente non loggato sentire cosa fare
//        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    public void onLocationChanged(Location location) {
        if (ZegoConstants.DEBUG) {
            Log.d("onLocationChanged:", "" + location);
        }
        userLogged = ApplicationController.getInstance().getUserLogged();
        if(location != null && userLogged != null){
            lastLocation = location;
            isLocationAvailable(true);
            ApplicationController.getInstance().saveLastPositionSent(new LatLng(location.getLatitude(),location.getLongitude()));
            Position pos = new Position(location,userLogged.getId());
            sendPosition(pos);
        }else if(location == null){
            sendGpsConnectionStatus(isGpsConnected());
            isLocationAvailable(false);
        }

    }

    private void getLastLocation(){
        onLocationChanged(LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient));
    }
    private void isLocationAvailable(boolean available) {
        BusResponseMessage responseMessage = new BusResponseMessage();
        responseMessage.setCode(ZegoConstants.OttoBusConstants.IS_LOCATION_AVAILABLE);
        responseMessage.setMessage(available);
        ApplicationController.getInstance().getOttoBus().post(responseMessage);
    }

    //endregion


    //region BROADCAST RECEIVER

    private void registerLocationProviderBrdReceiver() {

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.location.PROVIDERS_CHANGED");
        filter.addCategory("android.intent.category.DEFAULT");

        locationProviderBrdReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
                    ContentResolver contentResolver = getContentResolver();
                    // Find out what the settings say about which providers are enabled
                    int mode = Settings.Secure.getInt(contentResolver, Settings.Secure.LOCATION_MODE, Settings.Secure.LOCATION_MODE_OFF);

                    boolean isGpsOn = true;
                    if (mode == Settings.Secure.LOCATION_MODE_OFF) {
                        isGpsOn =  false;
                        // Position is turned OFF!
                        if (ZegoConstants.DEBUG)
                            Log.d("GPS_RECEIVER:", "LOCATION_MODE_OFF");
                    } else {
                        // Position is turned ON!
                        isGpsOn =  true;
                        if (ZegoConstants.DEBUG)
                            Log.d("GPS_RECEIVER:", "LOCATION_MODE_ON");

                    }

                    sendGpsConnectionStatus(isGpsOn);

                }
            }
        };

        registerReceiver(locationProviderBrdReceiver, filter);

    }

    //endregion

    private void sendPosition(Position position){
        Call<Position> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postLocation(userLogged.getZegotoken(),position);
        call.enqueue(new Callback<Position>() {
            @Override
            public void onResponse(Call<Position> call, Response<Position> response) {
                if(response.isSuccessful()){
                    if(ZegoConstants.DEBUG){
                        Log.d("POSITION_SENT:", "" + response.body().toString());
                    }

                    //salvo la data dell'ultima posizione inviata
                  //  ApplicationController.getInstance().saveLastPositionSent(new LatLng(response.body().getLat(),response.body().getLng()));
                    sendGpsConnectionStatus(isGpsConnected());

                }else if(ZegoConstants.DEBUG){
                    Log.d("POSITION_SENT:", "Failed");
                }
            }

            @Override
            public void onFailure(Call<Position> call, Throwable t) {
                //se non c'è connessione invio comunque info riguardanti il gps per eventualmente sbloccare la view modale gps non abilitato
                sendGpsConnectionStatus(isGpsConnected());
                if(ZegoConstants.DEBUG){
                    Log.d("POSITION_SENT:", "Failed");
                }
            }
        });

    }

    private void sendGpsConnectionStatus(boolean isGpsOn){
        BusResponseMessage busResponseMessage = new BusResponseMessage();
        busResponseMessage.setCode(ZegoConstants.OttoBusConstants.GPS_AVAILABLE_RESPONSE);
        busResponseMessage.setMessage(isGpsOn);
        ApplicationController.getInstance().getOttoBus().post(busResponseMessage);
    }


    @Subscribe
    public void ottoReceiver(BusRequestMessage reqMessage){
        switch (reqMessage.getCode()){
            case ZegoConstants.OttoBusConstants.KILL_POSITION_SERVICE :
                userLogged = null;
                if(locationTimerPoller != null)
                    locationTimerPoller.cancel();
                stopSelf();
                break;
            case ZegoConstants.OttoBusConstants.MY_POSITION :
                Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

                if(loc != null){
                    if(ZegoConstants.DEBUG){
                        Log.d("MY_POSITION:", "" + loc != null ? (loc.getLatitude() + "," + loc.getLongitude()) : "null");
                    }
                    BusResponseMessage responseMessage = new BusResponseMessage();
                    responseMessage.setCode(ZegoConstants.OttoBusConstants.MY_POSITION);
                    responseMessage.setMessage(new LatLng(loc.getLatitude(),loc.getLongitude()));
                    ApplicationController.getInstance().getOttoBus().post(responseMessage);
                }else{
                    isLocationAvailable(false);
                }
                break;

        }
    }


}
