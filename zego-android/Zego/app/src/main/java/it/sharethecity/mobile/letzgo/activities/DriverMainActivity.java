package it.sharethecity.mobile.letzgo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.broadcastreceiver.NetworkChangeReceiver;
import it.sharethecity.mobile.letzgo.bus.BusResponseMessage;
import it.sharethecity.mobile.letzgo.customviews.DriverEndTripStatusView;
import it.sharethecity.mobile.letzgo.customviews.DriverStartRideStatusView;
import it.sharethecity.mobile.letzgo.customviews.FeedbackStatusView;
import it.sharethecity.mobile.letzgo.customviews.MotivationDialog;
import it.sharethecity.mobile.letzgo.customviews.MyFakeToastView;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;
import it.sharethecity.mobile.letzgo.customviews.NoGpsView;
import it.sharethecity.mobile.letzgo.customviews.PopUpDialog;
import it.sharethecity.mobile.letzgo.customviews.PopUpThreeActionDialog;
import it.sharethecity.mobile.letzgo.customviews.RideRequestReceivedView;
import it.sharethecity.mobile.letzgo.customviews.StartStopView;
import it.sharethecity.mobile.letzgo.dao.CompactDriver;
import it.sharethecity.mobile.letzgo.dao.Feedback;
import it.sharethecity.mobile.letzgo.dao.StripeCard;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.gcm.GcmMessageHandler;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.DriverRESTInterface;
import it.sharethecity.mobile.letzgo.network.interfaces.RideRESTInterface;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;

import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.request.EtaRequest;
import it.sharethecity.mobile.letzgo.network.request.RideRequestAction;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.network.response.EtaResponse;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.services.LocationService;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@RuntimePermissions
public class DriverMainActivity extends ZegoNavBaseActivity implements OnMapReadyCallback {


    private static final int POLLING_REQUEST_STATUS = 0;
    private static final String DRIVER_ABORT_REASONS = "driver.abort.reason";


    @Nullable
    @BindView(R.id.nav_drawer_button)
    ImageView navBarDrawer;

    @Nullable
    @BindView(R.id.driver_my_position_button)
    ImageButton myPositionButton;


    @Nullable
    @BindView(R.id.connectivity_status)
    MyFakeToastView connectionStatusView;


    @Nullable
    @BindView(R.id.start)
    StartStopView startStopView;


    @Nullable
    @BindView(R.id.ride_req_received_view)
    RideRequestReceivedView rideRequestReceivedView;

    @Nullable
    @BindView(R.id.start_ride_view)
    DriverStartRideStatusView startRideStatusView;

    @Nullable
    @BindView(R.id.end_ride_view)
    DriverEndTripStatusView endRideView;

    @Nullable
    @BindView(R.id.no_gps_view)
    NoGpsView noGpsView;


    @Nullable
    @BindView(R.id.feedback_status_view)
    FeedbackStatusView feedBackStatusView;

    @Nullable
    @BindView(R.id.driver_error_toast)
    MyFontTextView errorTextView;

    @Nullable
    @BindView(R.id.helperView)
    View mHelperView;


    private int rideRequestState = POLLING_REQUEST_STATUS;
    private Integer driverPrice;

    private boolean isFromZoom = false;
    private boolean isFromCreation = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.driver_activity_main,container);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        ButterKnife.bind(this);
        centerOnMe = true;
        resizePickUpAndDroppOffMarkers();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);

        //  targeting Android 7.0 (API level 24) do not receive CONNECTIVITY_ACTION broadcasts  so I have to do this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkChangeReceiver = new NetworkChangeReceiver();
            registerReceiver(networkChangeReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
        user = ApplicationController.getInstance().getUserLogged();
        lastRtstatus = user.getRtstatus();
//        Log.d("driver Creation:", user.toString());
        setDriverListeners();
        isFromCreation = true;
        isFromResume =  true;
        termConditionsDialog();
    }

    private void setDriverListeners() {

        rideRequestReceivedView.setListener(new RideRequestReceivedView.RideRequestReceivedListener() {
            @Override
            public void onRejectRide() {
                ApplicationController.getInstance().stopSound();
                sendRideRequestAction(RideRequestAction.DRIVER_REJECT_ACTION,null);
            }

            @Override
            public void onAcceptRide() {
                ApplicationController.getInstance().stopSound();
                sendRideRequestAction(RideRequestAction.DRIVER_ACCEPT_ACTION, null);
            }

            @Override
            public void onCenterMap() {
                if(ZegoConstants.DEBUG){
                    Log.d("OnMyPositionClick:","rideRequestReceivedView");
                }
                centerOnMe = true;
                setMapBystate(user.getRtstatus(),true);
            }
        });

        startRideStatusView.setListener(new DriverStartRideStatusView.DriverStartRideListener() {
            @Override
            public void onNavigation() {
                if(currentRide != null){
                    startNavigation(new LatLng(currentRide.getPulat(),currentRide.getPulng()));
                }
            }

            @Override
            public void onCancelRide() {
                MotivationDialog.showMotivationDialog(DriverMainActivity.this,
                        getString(R.string.motivation_title),
                        getString(R.string.motivation_abort_question),
                        getString(R.string.close),
                        Color.RED,
                        0,
                        getDialogReasons(DRIVER_ABORT_REASONS,4),
                        //Arrays.asList(getResources().getStringArray(R.array.driver_motivations_for_abort)),
                        new MotivationDialog.MotivationListener() {
                            @Override
                            public void onAnswerSelected(Pair<Integer,String> s) {
                                if(ZegoConstants.DEBUG){
                                    Log.d("ABORT:","" + s.first);
                                }
                                sendRideRequestAction(RideRequestAction.DRIVER_ABORT_ACTION, s);
                            }
                        });

            }

            @Override
            public void onStartRide() {
                PopUpDialog.showConfirmPopUpDialog(DriverMainActivity.this, "", getString(R.string.confirm_start_msg).replace("{pname}",currentRide.getRider().getName()), getString(R.string.confirm), getString(R.string.annulla), 0, null, new PopUpDialog.DialogActionListener() {
                    @Override
                    public void actionListener() {
                        sendRideRequestAction(RideRequestAction.DRIVER_START_ACTION,null);
                    }

                    @Override
                    public void negativeAction() {

                    }
                });

            }

            @Override
            public void onCenterMap() {
                if(ZegoConstants.DEBUG){
                    Log.d("OnMyPositionClick:","startRideStatusView");
                }
                centerOnMe = true;
                setMapBystate(user.getRtstatus(),false);
            }

            @Override
            public void onCallPassenger() {
                String msg = getString(R.string.msg_call) + " " + currentRide.getRider().getName() + "?";
                PopUpThreeActionDialog.ThreeActionPopUpBuilder v = new PopUpThreeActionDialog.ThreeActionPopUpBuilder(DriverMainActivity.this);
                v.setMsg(msg)
                        .setFirstTextButton(getString(R.string.call))
                        .setSecondTextButton(getString(R.string.send_notification))
                        .setThirdTextButton(getString(R.string.annulla))
                        .setImageToLoad(R.drawable.tel300)
                        .setThirdButtonBackGroundColor(R.drawable.red_button_selector)
                        .setListener(new PopUpThreeActionDialog.DialogThreeActionListener() {
                            @Override
                            public void firstActionListener() {
                                callPhoneNumber(getString(R.string.zego_telephone_number));
                            }

                            @Override
                            public void secondActionListener() {
                                sendRideRequestAction(RideRequestAction.DRIVER_IAMHERE_ACTION,null);
                            }

                            @Override
                            public void thirdActionListener() {

                            }
                        });
                v.createDialog();

//                PopUpDialog.showConfirmPopUpDialog(DriverMainActivity.this, "", getString(R.string.msg_call) + " " + currentRide.getRider().getName() + "?", getString(R.string.call), getString(R.string.annulla), 0, null, new PopUpDialog.DialogActionListener() {
//                    @Override
//                    public void actionListener() {
//                        callPhoneNumber(getString(R.string.zego_telephone_number));
//                    }
//
//                    @Override
//                    public void negativeAction() {
//
//                    }
//                });

            }
        });

        endRideView.setListener(new DriverEndTripStatusView.EndTripListener() {
            @Override
            public void onNavigation() {
                if(currentRide != null){
                    startNavigation(new LatLng(currentRide.getDolat(),currentRide.getDolng()));
                }
            }

            @Override
            public void onEndRide() {
                PopUpDialog.showConfirmPopUpDialog(DriverMainActivity.this, getString(R.string.termina_title_msg), getString(R.string.termina_corsa_confirm_msg),
                        getString(R.string.ok), getString(R.string.no), 0, null, new PopUpDialog.DialogActionListener() {
                            @Override
                            public void actionListener() {
                                sendRideRequestAction(RideRequestAction.DRIVER_END_ACTION,null);
                            }

                            @Override
                            public void negativeAction() {

                            }
                        });

            }

            @Override
            public void onPriceChanged(int price) {

                if(ZegoConstants.DEBUG){
                    Log.d("DRIVER_PRICE:","" + price);
                }
                driverPrice = price;

            }

            @Override
            public void onCenterMap() {
                if(ZegoConstants.DEBUG){
                    Log.d("OnMyPositionClick:","endRideView");
                }
                centerOnMe = true;
                setMapBystate(user.getRtstatus(),false);
            }
        });

        feedBackStatusView.setListener(new FeedbackStatusView.FeedbackStatusViewListener() {
            @Override
            public void onSendFeedback(final int rate) {
//                if(rate <= LOW_RATE_THRESHOULD){
//                        MotivationDialog.showMotivationDialog(DriverMainActivity.this,
//                                getString(R.string.motivation_title),
//                                getString(R.string.motivation_feedback_question), Color.RED,
//                                Arrays.asList(getResources().getStringArray(R.array.driver_motivations_for_feedback)),
//                                new MotivationDialog.MotivationListener() {
//                                    @Override
//                                    public void onAnswerSelected(String s) {
//                                        sendFeedback(user,rate,s);
//                                    }
//                                });
//
//                }else{
                sendFeedback(user,rate,"");
//                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        ApplicationController.getInstance().removePushNotificationById(GcmMessageHandler.NOTIFICATION_ID);
        if(ZegoConstants.DEBUG){
            Log.d("DriverActivity onStart:","");
        }
        user = ApplicationController.getInstance().getUserLogged();

        setUI();
        isFromResume =  true;
        if(!isFromCreation){
            startStopPolling(false);
            startPollingService(user);
        }

        isFromCreation = false;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(networkChangeReceiver != null)
            unregisterReceiver(networkChangeReceiver);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        setMapListener();
        centerMapToPosition(user.getMyPositionLatLng(),true);

        DriverMainActivityPermissionsDispatcher.onStartServiceWithCheck(this);
    }


    private void setMapListener() {
        if(googleMap != null){
            mHelperView.setOnTouchListener(new View.OnTouchListener() {
                private float scaleFactor = 1f;

                @Override
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
//                    if(user.getRtstatus() != User.REALTIME_STATUS_DRIVER_IDLE){
//                        centerOnMe = false;
//                    }

                    centerOnMe = false;

                    if (simpleGestureDetector.onTouchEvent(motionEvent)) { // Double tap
                        isFromZoom = false;
                        googleMap.animateCamera(CameraUpdateFactory.zoomIn()); // Fixed zoom in
                    } else if (motionEvent.getPointerCount() == 1) { // Single tap
                        isFromZoom = false;
                        mapView.dispatchTouchEvent(motionEvent); // Propagate the event to the map (Pan)
                    } else if (scaleGestureDetector.onTouchEvent(motionEvent)) { // Pinch zoom
                        isFromZoom = true;
                        googleMap.moveCamera(CameraUpdateFactory.zoomBy( // Zoom the map without panning it
                                (googleMap.getCameraPosition().zoom * scaleFactor
                                        - googleMap.getCameraPosition().zoom) / 4));
                    }

                    return true; // Consume all the gestures
                }

                // Gesture detector to manage double tap gestures
                private GestureDetector simpleGestureDetector = new GestureDetector(
                        DriverMainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        isFromZoom = false;
                        return true;
                    }
                });

                // Gesture detector to manage scale gestures
                private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(
                        DriverMainActivity.this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                    @Override
                    public boolean onScale(ScaleGestureDetector detector) {
                        scaleFactor = detector.getScaleFactor();
                        return true;
                    }
                });
            });
            this.googleMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                    if(ZegoConstants.DEBUG){
                        Log.d("CameraPosition:", googleMap.getCameraPosition().target.toString());
                    }
                }
            });
        }
    }



    private void termConditionsDialog() {
        if(user.getTcok() == 0){
            user.setTcok(1);
            ApplicationController.getInstance().saveUser(user);
            PopUpDialog.showConfirmPopUpDialog(DriverMainActivity.this, getString(R.string.warning),getString(R.string.term_condition_changed),
                    getString(R.string.read_now),getString(android.R.string.yes), 0, null, new PopUpDialog.DialogActionListener() {
                        @Override
                        public void actionListener() {
                            postUser(user,true);
                        }

                        @Override
                        public void negativeAction() {
                            postUser(user,false);
                        }
                    });
        }else{
            startPollingService(user);
        }
    }


    private void postUser(User u,final boolean showTermCond){
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postUser(u.getZegotoken(),u);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    ApplicationController.getInstance().saveUser(response.body());
                    user = response.body();
                    if(showTermCond){
//                        openUrl(getString(R.string.url_term_cond));
                        openUrl(getTCurlByLanguage());
                    }

                }else{
                    if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        // TODO: 02/12/16 chiedere cosa fare

                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response,DriverMainActivity.this);
                    }
                }
                startPollingService(user);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
                startPollingService(user);
            }
        });
    }

    @Optional
    @OnClick(R.id.nav_drawer_button)
    public void onNavDrawButtonClick(){
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            drawerLayout.openDrawer(Gravity.LEFT);
        }

    }

//    @Optional
//    @OnClick({R.id.cancel,R.id.accept})
//    public void onwButtonClick(View v){
//        switch (v.getId()){
//            case R.id.accept:
//                if( user.getRtstatus() == User.REALTIME_STATUS_DRIVER_ANSWERING){
//                    sendRideRequestAction(RideRequestAction.DRIVER_ACCEPT_ACTION);
//                }else if(user.getRtstatus() == User.REALTIME_STATUS_DRIVER_PICKINGUP){
//                    sendRideRequestAction(RideRequestAction.DRIVER_START_ACTION);
//                }else if(user.getRtstatus() == User.REALTIME_STATUS_DRIVER_ONRIDE){
//                    sendRideRequestAction(RideRequestAction.DRIVER_END_ACTION);
//                }
//
//                break;
//            case R.id.cancel:
//                if( user.getRtstatus() == User.REALTIME_STATUS_DRIVER_ANSWERING){
//                    sendRideRequestAction(RideRequestAction.DRIVER_REJECT_ACTION);
//                }else if(user.getRtstatus() == User.REALTIME_STATUS_DRIVER_PICKINGUP){
//                    sendRideRequestAction(RideRequestAction.DRIVER_ABORT_ACTION);
//                }
//
//                break;
//        }
//    }

    @Optional
    @OnClick(R.id.driver_my_position_button)
    public void OnMyPositionClick(){
        centerOnMe = true;
        centerMapToPosition(user.getMyPositionLatLng(),false);
    }



    private void setUI(){
        if(user != null && user.hasImage()) {
            Picasso.with(getBaseContext())
                    .load(user.getImg())
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.user_placeholder)
                    .into(navBarDrawer);
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void loadRespondersOnMap(List<CompactDriver> drivers){

        if(googleMap != null){
            googleMap.clear();
            if(drivers ==  null ) return;
            for( CompactDriver driver : drivers) {
                if (driver != null && driver.getLat() != null && driver.getLng() != null){
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(driver.getLat(),driver.getLng()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.standard_icon));
                    googleMap.addMarker(markerOptions);
                }
            }
        }
    }


    //region REST REQUEST

    private void sendRideRequestAction(final String driverction, Pair<Integer,String> s) {
        startStopPolling(false);
        final RideRequestAction reqA = new RideRequestAction();
        reqA.setUid(user.getId());
        if(ZegoConstants.DEBUG){
            Log.d("ABORT:","" + (s == null ? "null" : s.first));
            Log.d("Action:",driverction);
        }
        reqA.setText(s == null ? "" : s.second);
        reqA.setCapture(s == null ? 0 : (s.first == 3 ? 1 : 0));
        reqA.setRid(user.getCurrent());
        reqA.setPriceupdate(driverction.equalsIgnoreCase(RideRequestAction.DRIVER_END_ACTION) ? driverPrice : null);
        reqA.setAction(driverction);
        Call<Riderequest> call = NetworkManager.getInstance().getRetrofit().create(RideRESTInterface.class).postRideRequestAction(user.getZegotoken(),reqA);
        showOrDismissProgressWheel(SHOW);

        call.enqueue(new Callback<Riderequest>() {
            @Override
            public void onResponse(Call<Riderequest> call, Response<Riderequest> response) {

                if(response.isSuccessful()){
                    if(driverction.equalsIgnoreCase(RideRequestAction.DRIVER_IAMHERE_ACTION)){
                        Toast.makeText(getBaseContext(),getString(R.string.notification_sent),Toast.LENGTH_SHORT).show();
                        startStopPolling(true);
                    }else{
                        currentRide = response.body();
                        if(ZegoConstants.DEBUG) {
                            Log.d("SEND ACTION::", "ride:" + (currentRide != null ? currentRide.getDriver().getRtstatus() : currentRide));
                        }
                        user.updateDriverFromCompactDriver(response.body().getDriver());
                        updateUserPolling(user);
//                    ApplicationController.getInstance().saveUser(user);
//                        startPollingService(user);
                        setUiBystate(response.body(),false);
                        setMapBystate(user.getRtstatus(),true);

                    }

                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    showErrorToast(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                    setUiBystate(user.getRtstatus(),false);
                }
                showOrDismissProgressWheel(DISMISS);

            }

            @Override
            public void onFailure(Call<Riderequest> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                setUiBystate(user.getRtstatus(),false);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    private void sendFeedback(User u,int rating,String reason) {
        Feedback feedback = new Feedback();
        feedback.setRating(rating == 0 ? 5 : rating);
        feedback.setSender(u.getId());
        feedback.setReason(reason);
        feedback.setRid(u.getCurrent());
        feedback.setUid(currentRide.getPid());
        Call<Feedback> call = NetworkManager.getInstance().getRetrofit().create(RideRESTInterface.class).postFeedback(u.getZegotoken(),feedback);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    user.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                    ApplicationController.getInstance().saveUser(user);
//                    currentRide = new Riderequest();
//                    driverPrice = null;
                    setUiBystate(user,false);
                    setMapBystate(User.REALTIME_STATUS_DRIVER_IDLE,true);
                    centerMapToPosition(user.getMyPositionLatLng(),true);
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    showErrorToast(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                }
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    private void addDrivers(final User user) {
        EtaRequest etaReq = new EtaRequest(user.getMyPositionLatLng());
        Call<EtaResponse> call = NetworkManager.getInstance().getRetrofit().create(DriverRESTInterface.class).addDriversRequest(user.getZegotoken(),etaReq);
        call.enqueue(new Callback<EtaResponse>() {
            @Override
            public void onResponse(Call<EtaResponse> call, Response<EtaResponse> response) {
                if(response.isSuccessful()){
                    setDriverList(response.body().getDrivers());
                    // setMapBystate(user.getRtstatus());
                    loadDriversOnMap(googleMap,driversList,true);
                }else{
                    // TODO: 15/12/16 gestire il caso
                }
            }

            @Override
            public void onFailure(Call<EtaResponse> call, Throwable t) {
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    //endregion

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onStartService(){
        //start location service
        googleMap.setMyLocationEnabled(true);
        myPositionButton.setVisibility(View.VISIBLE);
        startService(new Intent(DriverMainActivity.this, LocationService.class));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        DriverMainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void showDeniedLocation() {
        DriverMainActivityPermissionsDispatcher.onStartServiceWithCheck(this);
    }


    //region OTTOBUS RECEIVER

    @Subscribe
    public void ottoReceiver(BusResponseMessage respMessage){
        if(respMessage == null) return;

        switch (respMessage.getCode()){
            case ZegoConstants.OttoBusConstants.MY_POSITION :
                centerMapToPosition(user.setMyLastPosition((LatLng)respMessage.getMessage()),false);
                break;

            case ZegoConstants.OttoBusConstants.USER_UPDATE_RESPONSE:
//                if(user.getRtstatus() == User.REALTIME_STATUS_DRIVER_IDLE){
//
//                }

                if(ZegoConstants.DEBUG){
                    Log.d("USER_UPDATE_RESPONSE:", "ride:" + (currentRide != null ? currentRide.toString() : currentRide));
                    Log.d("USER_UPDATE_RESPONSE:", "user:" + user.toString());
                }


                if(isFromResume){
                    setUiBystate((User) respMessage.getMessage(),isFromResume);
                    setMapBystate(((User)respMessage.getMessage()).getRtstatus(),true);
                }else if(((User)respMessage.getMessage()).getRtstatus() != lastRtstatus) {
                    setUiBystate((User) respMessage.getMessage(),false);
                    setMapBystate(((User)respMessage.getMessage()).getRtstatus(),true);
                }else if(user.getRtstatus() == User.REALTIME_STATUS_DRIVER_IDLE){
                    setUiBystate((User) respMessage.getMessage(),false);
                    setMapBystate(((User)respMessage.getMessage()).getRtstatus(),false);
                }

                isFromResume = false;
                break;

            case ZegoConstants.OttoBusConstants.RIDE_UPDATE_RESPONSE:
                currentRide = ((Riderequest)respMessage.getMessage());

                if(ZegoConstants.DEBUG){
                    Log.d("CURRENT_RIDE:", "" + currentRide.toString());
                }

                if(currentRide != null && currentRide.getStatus().equals(Riderequest.REQUEST_TIME_OUT)){
                    PopUpDialog.showPopUpDialog(DriverMainActivity.this,"",getString(R.string.ride_request_time_out),getString(R.string.ok),0,null,null);
                }else if(currentRide != null && currentRide.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_TERMINATED)){
                    PopUpDialog.showPopUpDialog(DriverMainActivity.this,"",getString(R.string.passenger_terminated_ride),getString(R.string.ok),0,null,null);
                }else if(currentRide != null && currentRide.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ANSWERED) &&  !user.getId().equals(currentRide.getDriver().getDid())){
                    PopUpDialog.showPopUpDialog(DriverMainActivity.this,"",getString(R.string.ride_answered_by_others),getString(R.string.ok),0,null,null);
                }

                if(currentRide != null && currentRide.getDriver() == null){
                    user.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                    setMapBystate(User.REALTIME_STATUS_DRIVER_IDLE,true);
                    setUiBystate(user,false);
                }
                else if(currentRide.getDriver().getRtstatus() != lastRtstatus|| isFromResume){

                    if(currentRide.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_CANCELED)){
                        PopUpDialog.showPopUpDialog(DriverMainActivity.this, getString(R.string.warning), getString(R.string.passenger_canceled_the_ride), getString(R.string.ok), 0, null, new PopUpDialog.DialogActionListener() {
                            @Override
                            public void actionListener() {
                                ApplicationController.getInstance().removePushNotificationById(GcmMessageHandler.NOTIFICATION_ID);
                            }

                            @Override
                            public void negativeAction() {

                            }
                        });
                    }else if(currentRide.getStatus().equals(Riderequest.REQUEST_STATUS_PASSENGER_ABORTED)){
                        PopUpDialog.showPopUpDialog(DriverMainActivity.this, getString(R.string.warning), getString(R.string.passenger_aborted_the_ride), getString(R.string.ok), 0, null, new PopUpDialog.DialogActionListener() {
                            @Override
                            public void actionListener() {
                                ApplicationController.getInstance().removePushNotificationById(GcmMessageHandler.NOTIFICATION_ID);
                            }

                            @Override
                            public void negativeAction() {

                            }
                        });
                    }

                    if(!user.getId().equals(currentRide.getDriver().getDid())) {
                        user.setRtstatus(User.REALTIME_STATUS_DRIVER_IDLE);
                        setMapBystate(User.REALTIME_STATUS_DRIVER_IDLE,true);
                        setUiBystate(user,false);
                    }else {
                        setMapBystate(currentRide.getDriver().getRtstatus(),true);
                        setUiBystate(((Riderequest)respMessage.getMessage()),isFromResume);
                    }


                }else if(currentRide.getDriver().getRtstatus().equals(User.REALTIME_STATUS_DRIVER_ONRIDE) || currentRide.getDriver().getRtstatus().equals(User.REALTIME_STATUS_DRIVER_PICKINGUP)){
                    setMapBystate(currentRide.getDriver().getRtstatus(),false);
                }

                isFromResume = false;
                break;
            case ZegoConstants.OttoBusConstants.GPS_AVAILABLE_RESPONSE:
                boolean isGpsAvailable = (boolean)respMessage.getMessage();
                if(ZegoConstants.DEBUG){
                    Log.d("GPS_AVAILABLE:", "" + isGpsAvailable);
                }
                noGpsView.show(isGpsAvailable);
                break;
            case ZegoConstants.OttoBusConstants.CONNECTION_STATUS :
                // se non ho connessione mostro la view con msg connessione assente
                connectionStatusView.setFakeToastImg(R.drawable.connessione_icon);
                connectionStatusView.setFakeMsg(getString(R.string.no_connectivity));
                connectionStatusView.setVisibility(((Boolean)respMessage.getMessage()) ? View.VISIBLE : View.GONE);
                break;
            case ZegoConstants.OttoBusConstants.IS_LOCATION_AVAILABLE:
                boolean isLocationAvailable = (boolean)respMessage.getMessage();
                if(ZegoConstants.DEBUG){
                    Log.d("LOCATION_AVAILABLE:", "" + isLocationAvailable);
                }
                showNoLocationAvailableToast(isLocationAvailable);
                break;

        }
    }


    private void centerMapToPosition(LatLng latLng,boolean zoom) {
        if(googleMap != null){
            if(latLng != null){
                if(zoom){
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,ZegoConstants.DEFAULT_ZOOM));

                }else{
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }
        }
    }
    //endregion




    private void setUiBystate(Object obj,boolean isRefresh) {

        if (obj != null && (obj instanceof User)) {
            user = (User) obj;
            ApplicationController.getInstance().saveUser(user);
        }else if (obj != null && (obj instanceof Riderequest)) {
            user.updateDriverFromCompactDriver((CompactDriver)((Riderequest) obj).getDriver());
            ApplicationController.getInstance().saveUser(user);
        }
        lastRtstatus = user.getRtstatus();
        switch (user.getRtstatus()) {
            case User.REALTIME_STATUS_DRIVER_IDLE:
                startStopPolling(true);
//                addDrivers(user);
                currentRide = new Riderequest();
                driverPrice = null;
                rideRequestReceivedView.setVisibility(View.GONE);
                feedBackStatusView.setVisibility(View.GONE);
                startRideStatusView.setVisibility(View.GONE);
                endRideView.setVisibility(View.GONE);
                if(ZegoConstants.DEBUG){
                    Toast.makeText(getBaseContext(), "DRIVER_IDLE", Toast.LENGTH_SHORT).show();
                }

                break;
            case User.REALTIME_STATUS_DRIVER_ANSWERING:
                startStopPolling(true);
                rideRequestReceivedView.setContent(currentRide);
//                setMapBystate(user.getRtstatus());
//                setPickUpAndDropOffMarker(currentRide);
                // rideRequestReceivedView.setVisibility(View.VISIBLE);
                if(isRefresh){
                    rideRequestReceivedView.setVisibility(View.VISIBLE);
                }else{
                    showViewAnimated(rideRequestReceivedView,dm.heightPixels,0);
                }

                if(ZegoConstants.DEBUG) {
                    Toast.makeText(getBaseContext(), "DRIVER_ANSWERING", Toast.LENGTH_SHORT).show();
                }
                break;
            case User.REALTIME_STATUS_DRIVER_PICKINGUP:
                startStopPolling(true);
                endRideView.setVisibility(View.GONE);
                feedBackStatusView.setVisibility(View.GONE);
                // rideRequestReceivedView.setVisibility(View.GONE);
                // startRideStatusView.setVisibility(View.VISIBLE);
                startRideStatusView.setContent(currentRide);
//                setMapBystate(user.getRtstatus());
                if(isRefresh){
                    rideRequestReceivedView.setVisibility(View.GONE);
                    startRideStatusView.setVisibility(View.VISIBLE);
                }else {
                    showViewAnimated(startRideStatusView,rideRequestReceivedView,dm.heightPixels,0);
                }

//                acceptRejectLayout.setVisibility(View.VISIBLE);
                if(ZegoConstants.DEBUG) {
                    Toast.makeText(getBaseContext(), "DRIVER_PICKINGUP", Toast.LENGTH_SHORT).show();
                }
                break;
            case User.REALTIME_STATUS_DRIVER_ONRIDE:
                startStopPolling(true);
//                startRideStatusView.setVisibility(View.GONE);
                rideRequestReceivedView.setVisibility(View.GONE);
                feedBackStatusView.setVisibility(View.GONE);
                // endRideView.setVisibility(View.VISIBLE);
                endRideView.resetView();
                endRideView.setContent(currentRide);
//                setMapBystate(user.getRtstatus());
                if(isRefresh){
                    startRideStatusView.setVisibility(View.GONE);
                    endRideView.setVisibility(View.VISIBLE);
                }else {
                    showViewAnimated(endRideView,startRideStatusView,dm.heightPixels,0);
                }

                if(ZegoConstants.DEBUG) {
                    Toast.makeText(getBaseContext(), "DRIVER_ONRIDE", Toast.LENGTH_SHORT).show();
                }
                break;
            case User.REALTIME_STATUS_DRIVER_FEEDBACKDUE:
                startStopPolling(false);
                rideRequestReceivedView.setVisibility(View.GONE);
                startRideStatusView.setVisibility(View.GONE);
//                endRideView.setVisibility(View.GONE);
                // feedBackStatusView.setVisibility(View.VISIBLE);
                feedBackStatusView.resetView();
                feedBackStatusView.setContent(currentRide.getRider().getName(),currentRide.getRider().getUserimg(),false);
//                setMapBystate(user.getRtstatus());
                if(isRefresh){
                    endRideView.setVisibility(View.GONE);
                    feedBackStatusView.setVisibility(View.VISIBLE);
                }else{
                    showViewAnimated(feedBackStatusView,endRideView,dm.heightPixels,0);
                }

                break;
        }

    }

    private void setPickUpAndDropOffMarker(Riderequest riderequest){
        googleMap.clear();
        googleMap.addMarker(getPickUpMarker(riderequest.getPulat(),riderequest.getPulng()));
        googleMap.addMarker(getDropOffpMarker(riderequest.getDolat(),riderequest.getDolng()));
        addDrivers(user);
        builder.include(new LatLng(riderequest.getPulat(),riderequest.getPulng()));
        builder.include(new LatLng(riderequest.getDolat(),riderequest.getDolng()));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300)); //zoom to fit la mappa

    }

    private void showErrorToast(String msg){
        if(errorTextView.getVisibility() != View.VISIBLE){
            errorTextView.setVisibility(View.VISIBLE);
            errorTextView.setText(msg);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    errorTextView.setVisibility(View.GONE);
                }
            },ZegoConstants.ERROR_MESSAGE_LIFE_TIME);
        }
    }

    private void showNoLocationAvailableToast(boolean isLocationAvailable){

        if(!isThereConnectivity()){
            connectionStatusView.setFakeToastImg(R.drawable.connessione_icon);
            connectionStatusView.setFakeMsg(getString(R.string.no_connectivity));
            connectionStatusView.setVisibility(View.VISIBLE);
        }else {
            if(!isLocationAvailable){
                connectionStatusView.setFakeToastImg(R.drawable.nogps);
                connectionStatusView.setFakeMsg(getString(R.string.no_location_available));
            }
            connectionStatusView.setVisibility(isLocationAvailable ? View.GONE : View.VISIBLE);
        }
    }


    private void setMapBystate(int requestState, boolean refresh) {
        if(googleMap == null){
            return;
        }

        builder = new LatLngBounds.Builder();

        if (requestState == User.REALTIME_STATUS_DRIVER_IDLE){
            driverMarker = null;
            if(ZegoConstants.DEBUG){
                Log.d("LOADMAP_IDLE:", "" + driversList.toString());
            }
            if(refresh){
                resetMap();
                centerOnMe = true;
                centerMapToPosition(user.getMyPositionLatLng(),true);
            }
            else if (centerOnMe) {
                centerMapToPosition(user.getMyPositionLatLng(),false);
            }

            addDrivers(user);
            return;
        }

        else if(requestState == User.REALTIME_STATUS_DRIVER_ANSWERING) {

            if(currentRide == null || currentRide.getPulat() == null || currentRide.getPulng() == null || currentRide.getDolat() == null || currentRide.getDolng() == null) return;

            if(refresh){
                resetMap();
                googleMap.addMarker(getPickUpMarker(currentRide.getPulat(), currentRide.getPulng()));
                googleMap.addMarker(getDropOffpMarker(currentRide.getDolat(), currentRide.getDolng()));
                LatLng latLng = ApplicationController.getInstance().getLastPosition();
                if(latLng != null){
                    driverMarker = googleMap.addMarker(getCarMarker(true,latLng.latitude,latLng.longitude));
                    builder.include(latLng);
                }


                if(centerOnMe){
                    builder.include(currentRide.getPuLatLng());
                    builder.include(currentRide.getDoLatLng());
                    builder.include(getFakePositionToCenterMap());
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), MAP_PADDING_BOUNDS));
                }

            }else{
                LatLng latLng = ApplicationController.getInstance().getLastPosition();
                if(driverMarker == null && latLng != null){
                    driverMarker = googleMap.addMarker(getCarMarker(true,latLng.latitude,latLng.longitude));
                }else if(latLng != null) {
                    driverMarker.setPosition(latLng);
                }
            }


        }
        else if(requestState == User.REALTIME_STATUS_DRIVER_ONRIDE || requestState == User.REALTIME_STATUS_DRIVER_PICKINGUP) {
            if(currentRide == null || currentRide.getPulat() == null || currentRide.getPulng() == null || currentRide.getDolat() == null || currentRide.getDolng() == null) return;
            LatLng latLng = ApplicationController.getInstance().getLastPosition();
            LatLng driverLatLng = new LatLng(latLng != null ? latLng.latitude : currentRide.getDriver().getLat(),latLng != null ? latLng.longitude : currentRide.getDriver().getLng());
            if(refresh){
                resetMap();
                googleMap.addMarker(getPickUpMarker(currentRide.getPulat(), currentRide.getPulng()));
                googleMap.addMarker(getDropOffpMarker(currentRide.getDolat(), currentRide.getDolng()));
                driverMarker = googleMap.addMarker(getCarMarker(true,driverLatLng.latitude,driverLatLng.longitude));
            }else{

                if(driverMarker == null && latLng != null){
                    driverMarker = googleMap.addMarker(getCarMarker(true,driverLatLng.latitude,driverLatLng.longitude));
                }else if(latLng != null) {
                    driverMarker.setPosition(driverLatLng);
                }
            }

            if(centerOnMe){
                builder.include(new LatLng(driverLatLng.latitude,driverLatLng.longitude));
                builder.include(new LatLng(driverLatLng.latitude - 0.01d,driverLatLng.longitude));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), MAP_PADDING_BOUNDS));
            }

        }
        else{
            if(currentRide == null || currentRide.getPulat() == null || currentRide.getPulng() == null || currentRide.getDolat() == null || currentRide.getDolng() == null) return;
            LatLng latLng = ApplicationController.getInstance().getLastPosition();
            LatLng driverLatLng = new LatLng(latLng != null ? latLng.latitude : currentRide.getDriver().getLat(),latLng != null ? latLng.longitude : currentRide.getDriver().getLng());

            if(refresh){
                resetMap();
                googleMap.addMarker(getPickUpMarker(currentRide.getPulat(), currentRide.getPulng()));
                googleMap.addMarker(getDropOffpMarker(currentRide.getDolat(), currentRide.getDolng()));
                driverMarker = googleMap.addMarker(getCarMarker(true,driverLatLng.latitude,driverLatLng.longitude));
                //   driverMarker = googleMap.addMarker(new MarkerOptions().position(new LatLng(currentRide.getDriver().getLat(),currentRide.getDriver().getLng())).icon(BitmapDescriptorFactory.fromResource(R.drawable.standard_icon)));

            }else{
                if(driverMarker == null){
                    driverMarker = googleMap.addMarker(getCarMarker(true,driverLatLng.latitude,driverLatLng.longitude));
                    //  driverMarker =  googleMap.addMarker(new MarkerOptions().position(new LatLng(currentRide.getDriver().getLat(),currentRide.getDriver().getLng())).icon(BitmapDescriptorFactory.fromResource(R.drawable.standard_icon)));
                }else {
                    driverMarker.setPosition(new LatLng(driverLatLng.latitude,driverLatLng.longitude));
                }
            }

            if(centerOnMe){
                builder.include(currentRide.getPuLatLng());
                builder.include(currentRide.getDoLatLng());
                builder.include(new LatLng(driverLatLng.latitude,driverLatLng.longitude));
                builder.include(getFakePositionToCenterMap());
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), MAP_PADDING_BOUNDS));
            }

        }

    }


}
