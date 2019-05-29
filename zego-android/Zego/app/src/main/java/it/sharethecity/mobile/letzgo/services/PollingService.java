package it.sharethecity.mobile.letzgo.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.AppLaunchChecker;
import android.util.Log;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.bus.BusRequestMessage;
import it.sharethecity.mobile.letzgo.bus.BusResponseMessage;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.RideRESTInterface;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PollingService extends Service {


    public static final String USER = "user";
    public static final int POLLING_FREQUENCY = 5000;  //period in ms


    private User currentUser;
    private Riderequest currentRequest;
    private boolean isPollingRide = false;

    private boolean isPollingOff;


    private CountDownTimer pollingTimer = new CountDownTimer(POLLING_FREQUENCY,1000) {
        @Override
        public void onTick(long l) {

        }

        @Override
        public void onFinish() {


            if(!isPollingOff){
                currentUser = ApplicationController.getInstance().getUserLogged();

                pollingTimer.start();

                if(currentUser != null){
                    if(ZegoConstants.DEBUG){
                        Log.d("Polling:","Polling service");
                        Log.d("currentUser:","" + currentUser.toString());
                    }
                    polling(currentUser);
                }
            }


        }
    };


    public PollingService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.getInstance().getOttoBus().register(this);
        if(ZegoConstants.DEBUG){
            Log.d("OnCreate:","Polling service created");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        pollingTimer.cancel();
        ApplicationController.getInstance().getOttoBus().unregister(this);
        if(ZegoConstants.DEBUG){
            Log.d("POLLING RIDE SERVICE:","KILLED");
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        pollingTimer.cancel();
        currentUser = (User) intent.getExtras().getSerializable(USER);
        pollingTimer.start();
        isPollingOff = false;
        if(currentUser != null){
            polling(currentUser);
        }

        return Service.START_NOT_STICKY;
    }


    private void polling(User currentUser){

        if(ZegoConstants.DEBUG){
            Log.d("RTSTATUS POLLING F:", "" + currentUser.getRtstatus());
        }
        if(currentUser != null && !isPollingOff){
            if(!(currentUser.getRtstatus() == User.REALTIME_STATUS_DRIVER_IDLE || currentUser.getRtstatus() == User.REALTIME_STATUS_PASSENGER_IDLE)){
                getCurrentRideRequest(currentUser);
            }else{
                getUserById(currentUser);
            }
        }
    }

    private void getUserById(User user) {
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getUserById(user.getZegotoken(),user.getId());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful() && !isPollingOff){
                    if(ZegoConstants.DEBUG){
                        Log.d("PollingService:",response.body().toString());
                    }
                     ApplicationController.getInstance().saveUser(response.body());


                    if(response.body().isDriver() && response.body().getRtstatus() == User.REALTIME_STATUS_DRIVER_ANSWERING &&
                            currentUser.getRtstatus() == User.REALTIME_STATUS_DRIVER_IDLE){
                        currentUser = response.body();
                       // ApplicationController.getInstance().saveUser(response.body());
                        ApplicationController.getInstance().makeSound(R.raw.simple_alarm);
                        polling(currentUser);
                    }else{
                        currentUser = response.body();
                        BusResponseMessage busResponseMessage = new BusResponseMessage();
                        busResponseMessage.setCode(ZegoConstants.OttoBusConstants.USER_UPDATE_RESPONSE);
                        busResponseMessage.setMessage(response.body());
                        ApplicationController.getInstance().getOttoBus().post(busResponseMessage);
                    }
//                    if(currentUser == null || currentUser.getRtstatus() != response.body().getRtstatus()){
//                        // TODO: 13/12/16 vibrare
//                    }


                }else{
                    // TODO: 13/12/16 che fare se fallisce
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    private void getCurrentRideRequest(final User user){
        if(ZegoConstants.DEBUG){
            Log.d("Polling:","getting CurrrentRideRequest \n userCurrent:" + user.getCurrent());
            Log.d("Polling RTSTATUS:","" + user.getRtstatus());
        }
        Call<Riderequest> call = NetworkManager.getInstance().getRetrofit().create(RideRESTInterface.class).getRideRequest(user.getZegotoken(),user.getCurrent());
        call.enqueue(new Callback<Riderequest>() {
            @Override
            public void onResponse(Call<Riderequest> call, Response<Riderequest> response) {
                if(response.isSuccessful() && !isPollingOff){

                    if(user.isDriver()){
                        if(user.getRtstatus() != User.REALTIME_STATUS_DRIVER_IDLE && (response.body().getStatus() == Riderequest.REQUEST_STATUS_PASSENGER_CANCELED ||
                                response.body().getStatus() == Riderequest.REQUEST_STATUS_PASSENGER_ABORTED)){
                            ApplicationController.getInstance().makeSound(R.raw.short_simple_alarm);
                            user.updateDriverFromCompactDriver(response.body().getDriver());
                        }else if(user.getRtstatus() != User.REALTIME_STATUS_DRIVER_IDLE && response.body().getStatus() == Riderequest.REQUEST_TIME_OUT){
                            user.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                        }else if(user.getRtstatus() != User.REALTIME_STATUS_DRIVER_IDLE && response.body().getStatus() == Riderequest.REQUEST_STATUS_DRIVER_ANSWERED && !response.body().getDid().equals(user.getId())){
                            user.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                        }
                    }else{
                        if(user.getRtstatus() != User.REALTIME_STATUS_DRIVER_IDLE && response.body().getStatus() == Riderequest.REQUEST_STATUS_DRIVER_ABORTED){
                            ApplicationController.getInstance().makeSound(R.raw.short_simple_alarm);
                            user.updateUserFromCompactUser(response.body().getRider());
                        }
                    }

                    ApplicationController.getInstance().saveUser(user);
                    BusResponseMessage busResponseMessage = new BusResponseMessage();
                    busResponseMessage.setCode(ZegoConstants.OttoBusConstants.RIDE_UPDATE_RESPONSE);
                    busResponseMessage.setMessage(response.body());
                    ApplicationController.getInstance().getOttoBus().post(busResponseMessage);
                    if(ZegoConstants.DEBUG){
                        Log.d("RideReqResp:","" + response.body().getStatus());
                    }
                }else{
                    if(ZegoConstants.DEBUG){
                        Log.d("getCurrentRide:","NOT SUCCESSFUL");
                    }
                    // TODO: 13/12/16 decidere cosa fare
                }
            }

            @Override
            public void onFailure(Call<Riderequest> call, Throwable t) {
                if(ZegoConstants.DEBUG){
                    Log.d("getCurrentRide:","FAILED");
                }
            }
        });
    }

    @Subscribe
    public void ottoReceiver(BusRequestMessage reqMessage){
        switch (reqMessage.getCode()) {
            case ZegoConstants.OttoBusConstants.KILL_POLLING_RIDE_SERVICE:
                isPollingOff = true;
                if(pollingTimer != null){
                    pollingTimer.cancel();
                }

                currentUser = null;
                if(ZegoConstants.DEBUG){
                    Log.d("Polling Ride service:", "Killed");
                }
                stopSelf();
                break;
            case ZegoConstants.OttoBusConstants.START_STOP_POLLING:
                isPollingOff = true;
                if(pollingTimer != null){
                    pollingTimer.cancel();
                }
                if((boolean)reqMessage.getPayLoad()){
                    isPollingOff = false;
                    pollingTimer.start();
                    if(currentUser != null && currentUser.getRtstatus().equals(User.REALTIME_STATUS_DRIVER_ANSWERING)) {
                        getCurrentRideRequest(currentUser);
                    }
                }
                break;
            case ZegoConstants.OttoBusConstants.UPDATE_USER:
                if(ZegoConstants.DEBUG){
                    Log.d("USER_UPDATE:", "UPDATED:" + ((User) reqMessage.getPayLoad()).toString());
                }
                currentUser = (User) reqMessage.getPayLoad();
                ApplicationController.getInstance().saveUser(currentUser);
                break;
            case ZegoConstants.OttoBusConstants.SYNCH_STATUS:
                if(ZegoConstants.DEBUG){
                    Log.d("SYNCH_STATUS:", "SYNCH_STATUS:" + ((User) reqMessage.getPayLoad()).toString());
                }
                currentUser = (User) reqMessage.getPayLoad();
                polling(currentUser);
                break;

        }
    }

}
