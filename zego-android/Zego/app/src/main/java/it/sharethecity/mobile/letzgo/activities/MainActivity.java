package it.sharethecity.mobile.letzgo.activities;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.os.Bundle;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.broadcastreceiver.NetworkChangeReceiver;
import it.sharethecity.mobile.letzgo.bus.BusRequestMessage;
import it.sharethecity.mobile.letzgo.bus.BusResponseMessage;
import it.sharethecity.mobile.letzgo.customviews.AbortRideView;
import it.sharethecity.mobile.letzgo.customviews.FeedbackStatusView;
import it.sharethecity.mobile.letzgo.customviews.FindLiftStatusView;
import it.sharethecity.mobile.letzgo.customviews.MotivationDialog;
import it.sharethecity.mobile.letzgo.customviews.MyFakeToastView;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;
import it.sharethecity.mobile.letzgo.customviews.NoGpsView;
import it.sharethecity.mobile.letzgo.customviews.PassengerOnRideStatusView;
import it.sharethecity.mobile.letzgo.customviews.PickUpStatusView;
import it.sharethecity.mobile.letzgo.customviews.PopUpDialog;
import it.sharethecity.mobile.letzgo.customviews.PopUpThreeActionDialog;
import it.sharethecity.mobile.letzgo.customviews.RideRequestSentView;
import it.sharethecity.mobile.letzgo.customviews.StartStopView;
import it.sharethecity.mobile.letzgo.dao.Address;
import it.sharethecity.mobile.letzgo.dao.CompactDriver;
import it.sharethecity.mobile.letzgo.dao.Feedback;
import it.sharethecity.mobile.letzgo.dao.Service;
import it.sharethecity.mobile.letzgo.dao.StripeCard;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.gcm.GcmMessageHandler;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.PassengerRESTInterface;
import it.sharethecity.mobile.letzgo.network.interfaces.RideRESTInterface;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.request.EtaRequest;
import it.sharethecity.mobile.letzgo.network.request.PriceRequest;
import it.sharethecity.mobile.letzgo.network.request.RideRequestAction;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.network.response.EtaResponse;
import it.sharethecity.mobile.letzgo.network.response.PriceResponse;
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
public class MainActivity extends ZegoNavBaseActivity implements OnMapReadyCallback {

    private static final int PICK_UP_ADDRESS_REQUEST = 3425;
    private static final int DROP_OFF_ADDRESS_REQUEST = 3225;
    public static final int TRACKING_FREQUENCY = 5000;
    public static final String PICK_UP_ADDRESS = "puaddr";
    public static final String DROP_OFF_ADDRESS = "doaddr";
    private static final int PICKUP_STATUS = 0;
    private static final int DROP_OFF_STATUS = 1;
    private static final int FIND_LIFT_STATUS = 2;
    private static final int PAYMENT_REQUEST = 54;
    private static final int MY_CARDS_REQUEST = 55;
    private static final String RIDER_ABORT_REASONS = "rider.abort.reason";
    private static final String RIDER_FEEDBACK_REASONS = "rider.feedback.reason";
    protected int lastRtstatus;
    boolean isFromZoom = false;
    public static final int NUMBER_OF_FEEDBACKS = 4;
    public static final int NUMBER_OF_ABORT_MOTIVATIONS = 4;
    public boolean isFromCreation = false;

    @Nullable
    @BindView(R.id.nav_drawer_button)
    ImageView navBarDrawer;


    @Nullable
    @BindView(R.id.pointer)
    ImageView pinter;

    @Nullable
    @BindView(R.id.helperView)
    View mHelperView;


    @Nullable
    @BindView(R.id.connectivity_status)
    MyFakeToastView connectionStatusView;

    @Nullable
    @BindView(R.id.start)
    StartStopView startStopView;

    @Nullable
    @BindView(R.id.activity_main)
    PercentRelativeLayout main;

    @Nullable
    @BindView(R.id.pickup_view)
    PickUpStatusView pickUpView;

    @Nullable
    @BindView(R.id.find_lift)
    FindLiftStatusView findLiftStatusView;

    @Nullable
    @BindView(R.id.cancel_ride_view)
    RideRequestSentView cancelRideView;

    @Nullable
    @BindView(R.id.abort_ride_view)
    AbortRideView abortRideView;

    @Nullable
    @BindView(R.id.feedback_status_view)
    FeedbackStatusView feedBackStatusView;

    @Nullable
    @BindView(R.id.onride_status_view)
    PassengerOnRideStatusView onRideStatusView;

    @Nullable
    @BindView(R.id.error_toast)
    MyFontTextView errorTextView;

    @Nullable
    @BindView(R.id.no_gps_view)
    NoGpsView noGpsView;


    private View mapView;

    private int rideRequestState = PICKUP_STATUS;

    private List<StripeCard> cardList;



    private int typeOfRideSelected = 0;

    boolean isactivityCreatedAgain = false;



//    private CountDownTimer trackTimer = new CountDownTimer(TRACKING_FREQUENCY,TRACKING_FREQUENCY) {
//        @Override
//        public void onTick(long l) {
//
//        }
//
//        @Override
//        public void onFinish() {
//            trackTimer.start();
//            if(ZegoConstants.DEBUG) {
//                Log.d("trackTimer:", "eta:" + currentRide.getDrivereta());
//            }
//            abortRideView.setEta(currentRide.getDrivereta());
//            trackDriverOnMap(new LatLng(currentRide.getDriver().getLat(),currentRide.getDriver().getLng()));
//        }
//    };

    private void trackDriverOnMap(LatLng latLng) {
        if(googleMap != null){
        //    googleMap.clear();
            if(latLng ==  null ) return;
            setMapBystate(user.getRtstatus());

//            MarkerOptions markerOptions = new MarkerOptions()
//                    .position(latLng)
//                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.standard_icon));
//
//            googleMap.addMarker(markerOptions);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_main,container);
        ButterKnife.bind(this);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapView = mapFragment.getView();
        mapFragment.getMapAsync(this);
        resizePickUpAndDroppOffMarkers();

        //  targeting Android 7.0 (API level 24) do not receive CONNECTIVITY_ACTION broadcasts  so I have to do this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            networkChangeReceiver = new NetworkChangeReceiver();
            registerReceiver(networkChangeReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

        currentRide = new Riderequest();
        cardList = new ArrayList<>();
        centerOnMe = true;
        user = ApplicationController.getInstance().getUserLogged();


        lastRtstatus = user.getRtstatus();
        if(user.getRtstatus() == User.REALTIME_STATUS_PASSENGER_IDLE){
            pickUpView.setVisibility(View.VISIBLE);
        }
        setListeners();
        isFromCreation = true;
        isFromResume =  true;
        termConditionsDialog();

    }


    @Override
    protected void onStart() {
        super.onStart();
        ApplicationController.getInstance().removePushNotificationById(GcmMessageHandler.NOTIFICATION_ID);
        if(ZegoConstants.DEBUG){
            Log.d("MainActivity onStart:","on start main activity");
        }
        user = ApplicationController.getInstance().getUserLogged();


        getUserCreditCards();
        setUI();
        isFromResume = true;

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

    private void setListeners(){

        startStopView.setListeners(new StartStopView.PickUpDropoffListeners() {
            @Override
            public void onPickUpCancel() {

            }

            @Override
            public void onDropOffpCancel() {

            }

            @Override
            public void onPickUpClick() {
                Intent i = new Intent(MainActivity.this,PlaceSelectorActivity.class);
                startActivity(i);
            }

            @Override
            public void onDropOffClick() {

            }
        });

        cancelRideView.setListener(new RideRequestSentView.CancelRequestListener() {
            @Override
            public void onCancelRequestClick() {
                sendRideRequestAction(RideRequestAction.RIDER_CANCEL_ACTION, "");
            }
        });


        abortRideView.setListener(new AbortRideView.AbortRequestListener() {
            @Override
            public void onAbortRequestClick() {

                MotivationDialog.showMotivationDialog(MainActivity.this,
                        getString(R.string.motivation_title),
                        getString(currentRide.isFreeCancelTimeElapsed() ? R.string.pay_to_cancel : R.string.motivation_abort_question),
                        getString(R.string.close),
                        Color.RED,
                        MotivationDialog.DEFAULT,
                        getDialogReasons(RIDER_ABORT_REASONS,NUMBER_OF_ABORT_MOTIVATIONS), new MotivationDialog.MotivationListener() {
                    @Override
                    public void onAnswerSelected(Pair<Integer,String> s) {
                        sendRideRequestAction(RideRequestAction.RIDER_ABORT_ACTION, s.second);
                    }
                });

            }

            @Override
            public void onCall() {
                PopUpDialog.showConfirmPopUpDialog(MainActivity.this, "", getString(R.string.msg_call) + " " + currentRide.getDriver().getName() + "?", getString(R.string.call), getString(R.string.annulla), 0, null, new PopUpDialog.DialogActionListener() {
                    @Override
                    public void actionListener() {
                        callPhoneNumber(getString(R.string.zego_telephone_number));
                    }

                    @Override
                    public void negativeAction() {

                    }
                });
            }

            @Override
            public void onCenterMap() {
                centerOnMe = true;
                setMapBystate(user.getRtstatus());
            }
        });


        onRideStatusView.setListener(new PassengerOnRideStatusView.PassengerOnRideListener() {
            @Override
            public void onAlert() {
                PopUpDialog.showConfirmPopUpDialog(MainActivity.this, getString(R.string.end_ride_title), getString(R.string.alert_msg), getString(R.string.end), getString(R.string.close_alert_dialog), 0, null, new PopUpDialog.DialogActionListener() {
                    @Override
                    public void actionListener() {
                        sendRideRequestAction(RideRequestAction.RIDER_TERMINATE_ACTION,"");
                    }

                    @Override
                    public void negativeAction() {

                    }
                });

            }

            @Override
            public void onShare() {
                openShareTextChooser(getString(R.string.msg_share_ride)+ " " + getString(R.string.url_id_opaco) + currentRide.getShid());
            }

            @Override
            public void onCenterMap() {
                centerOnMe = true;
                setMapBystate(user.getRtstatus());
            }
        });


        feedBackStatusView.setListener(new FeedbackStatusView.FeedbackStatusViewListener() {
            @Override
            public void onSendFeedback(final int rate) {
                if(rate <= LOW_RATE_THRESHOULD){
                    MotivationDialog.showMotivationDialog(MainActivity.this,
                            "",
                            getString(R.string.motivation_feedback_question),
                            getString(R.string.back),
                            MotivationDialog.DEFAULT,
                            MotivationDialog.DEFAULT,
                            getDialogReasons(RIDER_FEEDBACK_REASONS,NUMBER_OF_FEEDBACKS), new MotivationDialog.MotivationListener() {
                        @Override
                        public void onAnswerSelected(Pair<Integer,String> s) {
                            sendFeedback(user,rate,s.second);
                        }
                    });
                }else{
                    sendFeedback(user,rate,"");
                }
            }
        });


        pickUpView.setListener(new PickUpStatusView.PickUpViewStatusListener() {
            @Override
            public void onCofirmStartingPoint() {
                if(currentRide.isPuAddressValid()){
                    pickUpView.resetView();
                    makeRideRequestUiBystate(DROP_OFF_STATUS);
                    MarkerOptions puMarker = getPickUpMarker(currentRide.getPulat(),currentRide.getPulng());
                    googleMap.addMarker(puMarker);
                }

            }

            @Override
            public void onMyPositionClick() {
                centerOnMe = true;
                askPositionToService();
            }

            @Override
            public void onRideType(Integer level) {
                typeOfRideSelected = level;
//                currentRide.setReqlevel(level);
                getEta(currentRide.getPuLatLng());
            }

            @Override
            public void onCancelPickUpAddress() {
                currentRide.setPuaddr("");
                resetMap();
            }

            @Override
            public void onTypeRideClick() {
                pickUpView.toggleTypeOfRideSelector();
            }

            @Override
            public void onSearchAddress(String currentAddress) {
                Intent i = new Intent(MainActivity.this,PlaceSelectorActivity.class);
                i.putExtra(PlaceSelectorActivity.PICKUP,true);
                startActivityForResult(i,PICK_UP_ADDRESS_REQUEST);
            }
        });

        findLiftStatusView.setListener(new FindLiftStatusView.FindLiftStatusViewListener() {
            @Override
            public void onCofirmButton() {
                if(rideRequestState == FIND_LIFT_STATUS){
                    currentRide.setMethod(ApplicationController.getInstance().isCashMethod() ? Riderequest.CASH : Riderequest.CARD);
                   if(user.hasImage()){
                       postRideRequestWithDiscountCheck();
//                       if(currentRide.getDiscount() <= 0){
//                           postRiderRequest(false);
//                       }else{
//                           String amount = String.format("%.2f",currentRide.getDiscountedPrice()/100f);
//                           PopUpDialog.showConfirmPopUpDialog(MainActivity.this, getString(R.string.active_promo),getString(R.string.amount_to_pay).replace("{XX}",amount),
//                                   getString(R.string.ok),getString(R.string.annulla), 0, null, new PopUpDialog.DialogActionListener() {
//                                       @Override
//                                       public void actionListener() {
//                                           postRiderRequest(false);
//                                       }
//
//                                       @Override
//                                       public void negativeAction() {
//
//                                       }
//                                   });
//                       }
                   }else{
                       PopUpDialog.showConfirmPopUpDialog(MainActivity.this, getString(R.string.warning),getString(R.string.no_profile_image_msg),
                               getString(R.string.ok),getString(R.string.annulla), 0, null, new PopUpDialog.DialogActionListener() {
                                   @Override
                                   public void actionListener() {
                                       postRideRequestWithDiscountCheck();
                                   }

                                   @Override
                                   public void negativeAction() {

                                   }
                               });
                   }


                }else if(rideRequestState == DROP_OFF_STATUS){
                    if(currentRide.isDoAddressValid()){
//                        MarkerOptions doMarker = getDropOffpMarker(currentRide.getDolat(),currentRide.getDolng());
//                        googleMap.addMarker(doMarker);
                        makeRideRequestUiBystate(FIND_LIFT_STATUS);
                        findLiftStatusView.showInfoRide(true);
                    }

                }

            }

            @Override
            public void onPassengerNumberChanged(int numPassenger) {
                currentRide.setNumpass(numPassenger);
            }

            @Override
            public void onCreditCardClick() {
                openMyCards();
//                if(cardList.isEmpty()){
//                    goToPayment();
//                }else{
//                    Intent i = new Intent(MainActivity.this,MyCardsActivity.class);
//                    startActivityForResult(i,MY_CARDS_REQUEST);
//                }

            }

            @Override
            public void onPriceChanged(int newPrice) {
                if(ZegoConstants.DEBUG){
                    Log.d("PASS_PRICE:","" + newPrice);
                }
                postPassPrice(newPrice);



            }

            @Override
            public void onPickUpCancel() {
                pickUpView.getPickUpConsole().setPickUpAddress("");
                makeRideRequestUiBystate(PICKUP_STATUS);
                resetMap();
            }

            @Override
            public void onDropOffpCancel() {
                cancelDropOff();

            }

            @Override
            public void onPickUpClick() {

            }

            @Override
            public void onDropOffClick() {
                if(rideRequestState != DROP_OFF_STATUS){
                    cancelDropOff();
                }else{
                    Intent i = new Intent(MainActivity.this,PlaceSelectorActivity.class);
                    i.putExtra(PlaceSelectorActivity.DROPOFF,true);
                    startActivityForResult(i,DROP_OFF_ADDRESS_REQUEST);
                }

            }
        });
    }


    private void openMyCards(){
        Intent i = new Intent(MainActivity.this,MyCardsActivity.class);
        startActivityForResult(i,MY_CARDS_REQUEST);
    }

    private void cancelDropOff(){
        currentRide.setDoaddr("");
        currentRide.setPassprice(0);
        currentRide.setDolng(0d);
        currentRide.setDolat(0d);
        rideRequestState = DROP_OFF_STATUS;
        findLiftStatusView.resetDropOff();
        resetMap();
        MarkerOptions puMarker = getPickUpMarker(currentRide.getPulat(),currentRide.getPulng());
        googleMap.addMarker(puMarker);
    }

    private void postRideRequestWithDiscountCheck(){

        if(currentRide.isCard()){
            if(currentRide.getDiscount() <= 0){
                postRiderRequest(false);
            }else{
                String amount = String.format("%.2f",currentRide.getDiscountedPrice()/100f);
                PopUpDialog.showConfirmPopUpDialog(MainActivity.this, getString(R.string.active_promo),getString(R.string.amount_to_pay).replace("{XX}",amount),
                        getString(R.string.ok),getString(R.string.annulla), 0, null, new PopUpDialog.DialogActionListener() {
                            @Override
                            public void actionListener() {
                                postRiderRequest(false);
                            }

                            @Override
                            public void negativeAction() {

                            }
                        });
            }
        }else{
            if(user.isFirstCashUse()){
                PopUpThreeActionDialog.ThreeActionPopUpBuilder v = new PopUpThreeActionDialog.ThreeActionPopUpBuilder(MainActivity.this);
                v.setMsg(getString(R.string.paga_in_contanti_msg))
                        .setFirstTextButton(getString(R.string.inserisci_carta))
                        .setSecondTextButton(getString(R.string.ahead))
                        .setThirdTextButton(getString(R.string.annulla))
                        .setImageToLoad(null)
                        .setThirdButtonBackGroundColor(null)
                        .setListener(new PopUpThreeActionDialog.DialogThreeActionListener() {
                            @Override
                            public void firstActionListener() {
                                openMyCards();
                            }

                            @Override
                            public void secondActionListener() {

                                if(currentRide.getDiscount() <= 0){
                                    postRiderRequest(false);
                                }else{
                                    showPromoContantiDialog();
                                }

                            }

                            @Override
                            public void thirdActionListener() {

                            }
                        });
                v.createDialog();

            }else{
                if(currentRide.getDiscount() <= 0){
                    postRiderRequest(false);
                }else{
                    showPromoContantiDialog();
                }
            }
        }


    }



    private void showPromoContantiDialog(){
        String amount = String.format("%.2f",currentRide.getDiscount()/100f);
        PopUpThreeActionDialog.ThreeActionPopUpBuilder v = new PopUpThreeActionDialog.ThreeActionPopUpBuilder(MainActivity.this);
        v.setMsg(getString(R.string.promo_msg_contanti).replace("{c}",amount))
                .setFirstTextButton(getString(R.string.inserisci_carta))
                .setSecondTextButton(getString(R.string.ahead))
                .setThirdTextButton(getString(R.string.annulla))
                .setImageToLoad(null)
                .setThirdButtonBackGroundColor(null)
                .setListener(new PopUpThreeActionDialog.DialogThreeActionListener() {
                    @Override
                    public void firstActionListener() {
                        openMyCards();
                    }

                    @Override
                    public void secondActionListener() {
                        postRiderRequest(false);
                    }

                    @Override
                    public void thirdActionListener() {

                    }
                });
        v.createDialog();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && (requestCode == PICK_UP_ADDRESS_REQUEST || requestCode == DROP_OFF_ADDRESS_REQUEST) ){
            if(data != null){
                if(requestCode == PICK_UP_ADDRESS_REQUEST ) {
                    Address puAdd = (Address) data.getExtras().getSerializable(PICK_UP_ADDRESS);
                    if(puAdd != null){
                        currentRide.setPuaddr(puAdd.getAddress());
                        currentRide.setPulat(puAdd.getLat());
                        currentRide.setPulng(puAdd.getLng());
                        isAddressFromGoogle = true;
                        centerMapToPosition(new LatLng(puAdd.getLat(),puAdd.getLng()),true);
                    }

                }else if(requestCode == DROP_OFF_ADDRESS_REQUEST){
                    Address doAdd = (Address) data.getExtras().getSerializable(DROP_OFF_ADDRESS);
                    if(doAdd != null){
                        currentRide.setDoaddr(doAdd.getAddress());
                        currentRide.setDolat(doAdd.getLat());
                        currentRide.setDolng(doAdd.getLng());
                        findLiftStatusView.getPickupDropOffView().setDropOffaddr(doAdd.getAddress());
                        isAddressFromGoogle = true;
                        centerMapToPosition(new LatLng(doAdd.getLat(),doAdd.getLng()),true);
                    }

                }
            }

        }
//        else if(requestCode == PAYMENT_REQUEST && resultCode == RESULT_OK){
//            getUserCreditCards();
//        }
//        else if(requestCode == MY_CARDS_REQUEST){
//            getUserCreditCards();
//        }
        else{
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        setMapListener();

        if(ZegoConstants.DEBUG){
            Log.d("OnMapReady:","");
            Log.d("isactivityCreatedAgain:","" +  isactivityCreatedAgain);
        }

        centerMapToPosition(user.getMyPositionLatLng(),true);
        MainActivityPermissionsDispatcher.onStartServiceWithCheck(this);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(!isactivityCreatedAgain){
                    LatLng lastLatLng = ApplicationController.getInstance().getLastPosition();
                    if(user.getRtstatus().equals(User.REALTIME_STATUS_PASSENGER_IDLE)) {
                        centerMapToPosition(lastLatLng != null ? lastLatLng : user.getMyPositionLatLng(),true);
                    }

                }
            }
        },600);
    }

    private void setMapListener() {
        if(googleMap != null){

            mHelperView.setOnTouchListener(new View.OnTouchListener() {
                private float scaleFactor = 1f;

                @Override
                public boolean onTouch(final View view, final MotionEvent motionEvent) {
                    if(user.getRtstatus() > User.REALTIME_STATUS_PASSENGER_IDLE){
                        centerOnMe = false;
                    }

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
                                        - googleMap.getCameraPosition().zoom) / 5));
                    }

                    return true; // Consume all the gestures
                }

                // Gesture detector to manage double tap gestures
                private GestureDetector simpleGestureDetector = new GestureDetector(
                        MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        isFromZoom = false;
                        return true;
                    }
                });

                // Gesture detector to manage scale gestures
                private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(
                        MainActivity.this, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
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

//                    if(isFromZoom){  // se zummo la mappa non chiamo ne l'eta ne il prezzo stimato, altrimenti verrebbe chiamata mille volte
//                        isFromZoom = false;
//                        return;
//                    }

                    LatLng cameraPosition = googleMap.getCameraPosition().target;
                    if(rideRequestState == PICKUP_STATUS && user.getRtstatus() == User.REALTIME_STATUS_PASSENGER_IDLE){
                        if(!isFromZoom){
                            getEta(cameraPosition);
                        }

                    }else if(rideRequestState == DROP_OFF_STATUS && user.getRtstatus() == User.REALTIME_STATUS_PASSENGER_IDLE){
                        if(!isAddressFromGoogle){
                            currentRide.setDolat(cameraPosition.latitude);
                            currentRide.setDolng(cameraPosition.longitude);
                        }
                        findLiftStatusView.enableButton(false);
                        findLiftStatusView.hideRidePrice(true);
                        if(!isFromZoom) {
                            getEstimatedPrice(currentRide);
                        }
                    }

                }
            });
        }
    }




    private void termConditionsDialog() {
        if(user.getTcok() == 0){
            user.setTcok(1);
            ApplicationController.getInstance().saveUser(user);
            PopUpDialog.showConfirmPopUpDialog(MainActivity.this, getString(R.string.warning),getString(R.string.term_condition_changed),
                    getString(R.string.read_now),getString(R.string.ok), 0, null, new PopUpDialog.DialogActionListener() {
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

    @Optional
    @OnClick(R.id.nav_drawer_button)
    public void onNavDrawButtonClick(){
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawer(Gravity.LEFT);
        }else{
            drawerLayout.openDrawer(Gravity.LEFT);
        }

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

    private void goToPayment() {
        Intent i = new Intent(MainActivity.this,PaymentMethodActivity.class);
        i.putExtra(FROM_ACTIVITY,MainActivity.class.getSimpleName());
        startActivityForResult(i,PAYMENT_REQUEST);
    }

    private void showDriverOnMap(CompactDriver driver,boolean clearMap){
        if(googleMap != null){
            if(clearMap) googleMap.clear();
            if(driver ==  null || driver.getLat() == null || driver.getLng() == null) return;

            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(driver.getLat(),driver.getLng()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.standard_icon));
            googleMap.addMarker(markerOptions);
        }
    }


    //region REST REQUEST


    private void getUserCreditCards() {
        Call<List<StripeCard>> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).getUserCards(user.getZegotoken(),user.getId());
        call.enqueue(new Callback<List<StripeCard>>() {
            @Override
            public void onResponse(Call<List<StripeCard>> call, Response<List<StripeCard>> response) {
                if(response.isSuccessful()){
                    cardList.clear();
                    cardList.addAll(response.body());
                    findLiftStatusView.setCard(getPreferredCard());
                    if(response.body().isEmpty() || user.getPayok() != 1){
                        ApplicationController.getInstance().saveMethodCash(1);
                    }
                    findLiftStatusView.setPaymentMethod(!ApplicationController.getInstance().isCashMethod());
                }else{
                    // TODO: 15/12/16 vedere cosa fare
                }
            }

            @Override
            public void onFailure(Call<List<StripeCard>> call, Throwable t) {
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    private boolean isServiceAvailable(Integer level, List<Service> services){
        for (Service s : services){
            if(s == null) continue;
            if(level.equals(s.getLevel())){
                return true;
            }
        }

        return false;
    }

    private void getEta(final LatLng latLng){
        EtaRequest etaReq = new EtaRequest(latLng);
        etaReq.setLevel(typeOfRideSelected);

        Call<EtaResponse> call = NetworkManager.getInstance().getRetrofit().create(PassengerRESTInterface.class).etaRequest(user.getZegotoken(),etaReq);
        call.enqueue(new Callback<EtaResponse>() {
            @Override
            public void onResponse(Call<EtaResponse> call, Response<EtaResponse> response) {
                if(response.isSuccessful()){
//                    Log.d("user eta:", user.toString());
//                    Log.d("isAddressFromGoogle:", "" + isAddressFromGoogle);
//                    Log.d("eta Address:", ""+ response.body().getAddress());
                    if(!isAddressFromGoogle){
                        currentRide.setPuaddr(response.body().getAddress());
                        currentRide.setPulat(latLng.latitude);
                        currentRide.setPulng(latLng.longitude);
                    }


                    if(response.body() != null && response.body().getAddress()!= null && !response.body().getAddress().isEmpty()){
                        pickUpView.getPickUpConsole().setEta(response.body().getEta());
                        pickUpView.getPickUpConsole().setPickUpAddress(isAddressFromGoogle ? currentRide.getPuaddr() : response.body().getAddress());
                        //  pickUpConsole.enableCancelButton(true);
                        pickUpView.updateTypeOfRide(typeOfRideSelected,response.body().getServices());
                        pickUpView.enableButton(true);
                        typeOfRideSelected = isServiceAvailable(typeOfRideSelected,response.body().getServices()) ? typeOfRideSelected : 0;
                        currentRide.setReqlevel(typeOfRideSelected);
                    }

                    if(ZegoConstants.DEBUG){
                        Log.d("RID_REQ->EtaREsp",currentRide.toString());
                    }
//                    pickUpView.updateData(response.body());
                    setDriverList(response.body().getDrivers());
                    loadDriversOnMap(googleMap,response.body().getDrivers(),false);
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    pickUpView.resetView();
                    currentRide.setReqlevel(0);
                    typeOfRideSelected = 0;
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    showErrorToast(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                }
                isAddressFromGoogle = false;
            }

            @Override
            public void onFailure(Call<EtaResponse> call, Throwable t) {
                isAddressFromGoogle = false;
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }

    private void getEstimatedPrice(Riderequest riderequest) {
        PriceRequest preq = new PriceRequest(new LatLng(riderequest.getDolat(),riderequest.getDolng()),new LatLng(riderequest.getPulat(),riderequest.getPulng()));
        preq.setLevel(riderequest.getReqlevel());
        Call<PriceResponse> call = NetworkManager.getInstance().getRetrofit().create(PassengerRESTInterface.class).estimatedPriceRequest(user.getZegotoken(),preq);
        call.enqueue(new Callback<PriceResponse>() {
            @Override
            public void onResponse(Call<PriceResponse> call, Response<PriceResponse> response) {
                if(response.isSuccessful()){
                    currentRide.setExtprice(response.body().getPrice());
                    currentRide.setDriverfee(response.body().getDriverfee());
                    currentRide.setDoaddr(response.body().getDropoff());
                    currentRide.setZegofee(response.body().getZegofee());
                    currentRide.setPassprice(response.body().getPrice());
                    currentRide.setDiscount(response.body().getDiscount());
                    findLiftStatusView.setDropOff(response.body());
                    if(!isAddressFromGoogle)
                        findLiftStatusView.getPickupDropOffView().setDropOffaddr(response.body().getDropoff());

                    findLiftStatusView.setCard(getPreferredCard());
                    findLiftStatusView.setPaymentMethod(currentRide.isCard());
                    findLiftStatusView.enableButton(true);
                    findLiftStatusView.hideRidePrice(false);
                    if(ZegoConstants.DEBUG){
                        Log.d("RID_REQ->EstiPrice",currentRide.toString());
                    }
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    showErrorToast(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                }
                isAddressFromGoogle =false;
            }

            @Override
            public void onFailure(Call<PriceResponse> call, Throwable t) {
                isAddressFromGoogle = false;
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }
    private void postPassPrice(int newPrice) {
        if(currentRide == null || !currentRide.isDoAddressValid() || !currentRide.isPuAddressValid()) return;

        if(ZegoConstants.DEBUG){
            Log.d("ZEGO_TOKEN:",user.getZegotoken());
        }
        currentRide.setPassprice(newPrice);
        currentRide.setPid(user.getId());
        Call<Riderequest> call = NetworkManager.getInstance().getRetrofit().create(PassengerRESTInterface.class).postPassPriceRideRequest(user.getZegotoken(),currentRide);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<Riderequest>() {
            @Override
            public void onResponse(Call<Riderequest> call, Response<Riderequest> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    if(ZegoConstants.DEBUG){
                        Log.d("DISCOUNT:","" + response.body().getDiscount());
                    }
                    currentRide.setDiscount(response.body().getDiscount());
                    currentRide.setExtprice(response.body().getExtprice());
                    currentRide.setPassprice(response.body().getPassprice());
                    currentRide.setDriverprice(response.body().getDriverprice());
                    currentRide.setZegofee(response.body().getZegofee());
                    currentRide.setDriverfee(response.body().getDriverfee());
                    findLiftStatusView.updateSliderAndInfoPrice(currentRide);
                }else{
                    if(ZegoConstants.DEBUG){
                        Log.d("POST_PASS:","ERROR");
                    }
                    NetworkErrorHandler.getInstance().errorHandler(response,MainActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Riderequest> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
                if(ZegoConstants.DEBUG){
                    Log.d("POST_PASS:","Failure");
                }
            }
        });
    }

    private void postRiderRequest(boolean isForceRideRequest) {

        currentRide.setPid(user.getId());
        if(ZegoConstants.DEBUG){
            Log.d("RID_REQ->R",currentRide.toString());
        }

        PassengerRESTInterface  passengerRESTInterface = NetworkManager.getInstance().getRetrofit().create(PassengerRESTInterface.class);
        Call<Riderequest> call = isForceRideRequest ? passengerRESTInterface.postForcedRideRequest(user.getZegotoken(),currentRide) : passengerRESTInterface.postRideRequest(user.getZegotoken(),currentRide);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<Riderequest>() {
            @Override
            public void onResponse(Call<Riderequest> call, Response<Riderequest> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    if(!currentRide.isCard()){
                        ApplicationController.getInstance().setFirstCash(false);
                    }
                    ApplicationController.getInstance().saveMethodCash(user.getPayok() == 1 ? 0 : 1);
                    currentRide = response.body();
                    user.updateUserFromCompactUser((response.body().getRider()));
                    lastRtstatus = user.getRtstatus();
                    ApplicationController.getInstance().saveUser(user);
                    setUIByRideRequestStatus(response.body().getRider().getRtstatus(),false);
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    if(errorObject != null && errorObject.getCode() == ErrorObject.ERROR_CODE_MISSING_PAYMENT_METHOD){
                        PopUpDialog.showConfirmPopUpDialog(MainActivity.this, getString(R.string.warning), errorObject.getMsg(), getString(R.string.enter_one), getString(R.string.close), 0, null, new PopUpDialog.DialogActionListener() {
                            @Override
                            public void actionListener() {
                                goToPayment();
                            }

                            @Override
                            public void negativeAction() {

                            }
                        });
                    }
                    else if(errorObject != null &&  errorObject.getCode() == ErrorObject.ERROR_CODE_RIDE_NOT_PAID){
                        PopUpDialog.showPopUpDialog(MainActivity.this, getString(R.string.warning), errorObject.getMsg(),getString(R.string.ok), 0, null, new PopUpDialog.DialogActionListener() {
                            @Override
                            public void actionListener() {
                                Intent i = new Intent(MainActivity.this,MyCardsActivity.class);
                                startActivity(i);
                            }

                            @Override
                            public void negativeAction() {

                            }
                        });
                    }
                    else if(errorObject != null && (errorObject.getCode() == ErrorObject.ERROR_CODE_MISSING_RIDE_TOO_SHORT ||  errorObject.getCode() == ErrorObject.ERROR_CODE_MISSING_RIDE_TOO_FAR_FROM_PICKUP)){
                        PopUpDialog.showConfirmPopUpDialog(MainActivity.this, getString(R.string.warning), errorObject.getMsg(), getString(R.string.confirm), getString(R.string.annulla), 0, null, new PopUpDialog.DialogActionListener() {
                            @Override
                            public void actionListener() {
                                postRiderRequest(true);
                            }

                            @Override
                            public void negativeAction() {

                            }
                        });
                    }
                    else{
                        showErrorToast(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                    }
                }else{
                    NetworkErrorHandler.getInstance().errorHandler(response,MainActivity.this);
                }
            }

            @Override
            public void onFailure(Call<Riderequest> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    private void sendRideRequestAction(String driverction,String reason) {
        startStopPolling(false);
        final RideRequestAction reqA = new RideRequestAction();
        reqA.setUid(user.getId());
        reqA.setRid(user.getCurrent());
        reqA.setText(reason);
        reqA.setAction(driverction);
        Call<Riderequest> call = NetworkManager.getInstance().getRetrofit().create(RideRESTInterface.class).postRideRequestAction(user.getZegotoken(),reqA);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<Riderequest>() {
            @Override
            public void onResponse(Call<Riderequest> call, Response<Riderequest> response) {
                if(response.isSuccessful()){
                    currentRide = response.body();
                    user.updateUserFromCompactUser(response.body().getRider());
                    lastRtstatus = user.getRtstatus();
                    updateUserPolling(user);
                    ApplicationController.getInstance().saveUser(user);
                    setUIByRideRequestStatus(response.body().getRider().getRtstatus(),false);
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    showErrorToast(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                    setUIByRideRequestStatus(user.getRtstatus(),false);
                }
                showOrDismissProgressWheel(DISMISS);
            }

            @Override
            public void onFailure(Call<Riderequest> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                setUIByRideRequestStatus(user.getRtstatus(),false);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));

            }
        });
    }


    private void sendFeedback(User u,int rating,String reason) {
        Feedback feedback = new Feedback();
        feedback.setRating(rating <= 0 ? 5 : rating);
        feedback.setSender(u.getId());
        feedback.setReason(reason);
        feedback.setRid(u.getCurrent());
        feedback.setUid(currentRide.getDriver().getDid());
        Call<Feedback> call = NetworkManager.getInstance().getRetrofit().create(RideRESTInterface.class).postFeedback(u.getZegotoken(),feedback);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<Feedback>() {
            @Override
            public void onResponse(Call<Feedback> call, Response<Feedback> response) {

                if(response.isSuccessful()){
                    user.setRtstatus(User.REALTIME_STATUS_PASSENGER_IDLE);
                    lastRtstatus = User.REALTIME_STATUS_PASSENGER_IDLE;
                    ApplicationController.getInstance().saveUser(user);
                    currentRide = new Riderequest();
                    setUIByRideRequestStatus(User.REALTIME_STATUS_PASSENGER_IDLE,false);
                }else if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                    ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                    showErrorToast(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error));
                }
                showOrDismissProgressWheel(DISMISS);
            }

            @Override
            public void onFailure(Call<Feedback> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });
    }


    private StripeCard getPreferredCard(){
        for(StripeCard stripeCard : cardList){
            if(stripeCard.getPreferred() == 1){
                return stripeCard;
            }
        }

        return null;
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
                    lastRtstatus = response.body().getRtstatus();
                    if(showTermCond){
//                        openUrl(getString(R.string.url_term_cond));
                        openUrl(getTCurlByLanguage());
                    }

                }else{
                    if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        // TODO: 02/12/16 chiedere cosa fare

                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response,MainActivity.this);
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


    //endregion

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    public void onStartService(){
        //start location service

        googleMap.setMyLocationEnabled(true);

        startService(new Intent(MainActivity.this, LocationService.class));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void showDeniedLocation() {
        MainActivityPermissionsDispatcher.onStartServiceWithCheck(this);
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
                if(ZegoConstants.DEBUG){
                    Log.d("USER_UPDATE_RESPONSE:", "" + user.toString());
                }
                 if(((User)respMessage.getMessage()).getRtstatus() != lastRtstatus ||
                        (isFromResume && lastRtstatus != User.REALTIME_STATUS_PASSENGER_IDLE)){

                    if(ZegoConstants.DEBUG){
                        Log.d("Primo IF:", "" + ((User)respMessage.getMessage()).toString());
                    }
                     user =  (User)respMessage.getMessage();
                     lastRtstatus = user.getRtstatus();
                     //aggiunto eer evitare il crash dovuto al click simultaneo driver rider del termina
                     ApplicationController.getInstance().saveUser(user);
                     if(currentRide != null && currentRide.getId() == null && user.getRtstatus() > User.REALTIME_STATUS_PASSENGER_IDLE){
                         synchWithRightStatus(user);
                         return;
                     }

                    setUIByRideRequestStatus(user.getRtstatus(),isFromResume);

                }else if(isactivityCreatedAgain && lastRtstatus == User.REALTIME_STATUS_PASSENGER_IDLE){
                    startStopPolling(false);
                    if(ZegoConstants.DEBUG){
                        Log.d("Secondo IF:", "" + ((User)respMessage.getMessage()).toString());
                        Log.d("rideRState Secondo IF:", "" + rideRequestState);
                    }
                     if(rideRequestState > FIND_LIFT_STATUS){
                         rideRequestState = PICKUP_STATUS;
                     }
                    if(currentRide  != null){
                        if(rideRequestState == PICKUP_STATUS){
                            makeRideRequestUiBystate(rideRequestState);
                        }
                        else if(rideRequestState == DROP_OFF_STATUS && currentRide.isPuAddressValid()){
                            makeRideRequestUiBystate(rideRequestState);
                        }else if(rideRequestState == FIND_LIFT_STATUS && currentRide.isDoAddressValid() && currentRide.isPuAddressValid()){
                            pickUpView.setVisibility(View.GONE);
                            if(currentRide != null){
                                findLiftStatusView.setContent(currentRide);
                                findLiftStatusView.setCard(getPreferredCard());
                                findLiftStatusView.setPaymentMethod(currentRide.isCard());
                                findLiftStatusView.enableButton(true);
                                findLiftStatusView.setVisibility(View.VISIBLE);
                                makeRideRequestUiBystate(rideRequestState);
                            }

                        }
                        if(rideRequestState != PICKUP_STATUS){
                            setMapBystate(rideRequestState);
//                            setPickUpAndDropOffMarker(currentRide,false);
//                            loadDriversOnMap(googleMap,driversList,false);
                            // centerMapToPosition(new LatLng(currentRide.getPulat(),currentRide.getPulng()));
                        }

                    }

                }else if(lastRtstatus == User.REALTIME_STATUS_PASSENGER_IDLE){
                     startStopPolling(false);
                }

                isactivityCreatedAgain = false;
                isFromResume = false;
                break;
            case ZegoConstants.OttoBusConstants.RIDE_UPDATE_RESPONSE:
                currentRide  = ((Riderequest)respMessage.getMessage());
                if(ZegoConstants.DEBUG){
                    Log.d("CURRENT_RIDE:", "" + currentRide.toString());
                }
                if(currentRide.getRider().getRtstatus() != lastRtstatus){

                    if(currentRide.getStatus().equals(Riderequest.REQUEST_STATUS_DRIVER_ABORTED)){
                       // makeSound(R.raw.short_simple_alarm);
                        PopUpDialog.showPopUpDialog(MainActivity.this,getString(R.string.warning),getString(R.string.driver_aborted_your_request),getString(R.string.ok),0,null,null);
                    }
                    else if(currentRide.getStatus().equals(Riderequest.REQUEST_TIME_OUT)){
                        // makeSound(R.raw.short_simple_alarm);
                        PopUpDialog.showPopUpDialog(MainActivity.this,"",getString(R.string.no_driver_answered_your_request),getString(R.string.ok),0,null,null);
                    }

                    user.updateUserFromCompactUser((((Riderequest)respMessage.getMessage()).getRider()));
                    lastRtstatus = user.getRtstatus();
                    if(ZegoConstants.DEBUG){
                        Log.d("USER:", "" + user.toString());
                    }
                    ApplicationController.getInstance().saveUser(user);
                    setUIByRideRequestStatus(user.getRtstatus(),false);
                }else if(isFromResume){
                    user.updateUserFromCompactUser((((Riderequest)respMessage.getMessage()).getRider()));
                    lastRtstatus = user.getRtstatus();
                    ApplicationController.getInstance().saveUser(user);
                    setUIByRideRequestStatus(user.getRtstatus(),true);
                }else{
                    lastRtstatus = user.getRtstatus();
                    refreshContentByState(user.getRtstatus());
                }

                isFromResume = false;
                break;
            case ZegoConstants.OttoBusConstants.CONNECTION_STATUS :
                // se non ho connessione mostro la view con msg connessione assente
                connectionStatusView.setFakeToastImg(R.drawable.connessione_icon);
                connectionStatusView.setFakeMsg(getString(R.string.no_connectivity));
                connectionStatusView.setVisibility(((Boolean)respMessage.getMessage()) ? View.VISIBLE : View.GONE);
                break;
            case ZegoConstants.OttoBusConstants.GPS_AVAILABLE_RESPONSE:
                boolean isGpsAvailable = (boolean)respMessage.getMessage();
                if(ZegoConstants.DEBUG){
                    Log.d("GPS_AVAILABLE:", "" + isGpsAvailable);
                }
                showNoGpsMessage(isGpsAvailable);
//                noGpsView.show(isGpsAvailable);
                break;

        }

    }



    private void refreshContentByState(int rtstatus) {
        switch (rtstatus){
            case User.REALTIME_STATUS_PASSENGER_IDLE:
                break;
            case User.REALTIME_STATUS_PASSENGER_REQUEST_SENT:
                break;
            case User.REALTIME_STATUS_PASSENGER_WAITING_DRIVER:
                if(ZegoConstants.DEBUG) {
                    Log.d("ETA_REFRESH:", "eta:" + currentRide.getDrivereta());
                }
                abortRideView.setEta(currentRide.getDrivereta());
                trackDriverOnMap(new LatLng(currentRide.getDriver().getLat(),currentRide.getDriver().getLng()));
                break;
            case User.REALTIME_STATUS_PASSENGER_ONRIDE:
                trackDriverOnMap(new LatLng(currentRide.getDriver().getLat(),currentRide.getDriver().getLng()));
                break;
            case User.REALTIME_STATUS_PASSENGER_FEEDBACK_DUE:
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


    private void makeRideRequestUiBystate(int state){
        switch (state){
            case PICKUP_STATUS:
             //   builder = new LatLngBounds.Builder();
                rideRequestState = PICKUP_STATUS;
                findLiftStatusView.resetView();
                currentRide = new Riderequest();
                pickUpView.resetView();
                findLiftStatusView.setVisibility(View.GONE);
                if(pickUpView.getVisibility() != View.VISIBLE){
                    pickUpView.setVisibility(View.VISIBLE);
                    showViewAnimated(pickUpView,dm.heightPixels,0);
                }
                centerMapToPosition(user.getMyPositionLatLng(),true);
                startStopPolling(false);
                break;
            case DROP_OFF_STATUS:
                rideRequestState = DROP_OFF_STATUS;
                pickUpView.setVisibility(View.GONE);
                findLiftStatusView.setPickUpAddr(currentRide.getPuaddr());
                findLiftStatusView.showDropOffCancelButton(false);
                if(pickUpView.getVisibility() != View.VISIBLE){
                    findLiftStatusView.setVisibility(View.VISIBLE);
                    showViewAnimated(findLiftStatusView,dm.heightPixels,0);
                }
                startStopPolling(false);
                break;
            case FIND_LIFT_STATUS:
                rideRequestState = FIND_LIFT_STATUS;
                findLiftStatusView.showDropOffCancelButton(true);
                findLiftStatusView.showTipSelector(currentRide.getExtprice());
                startStopPolling(false);
                break;
        }

        setMapBystate(state);
    }


    private void setUIByRideRequestStatus(int rtStatus,boolean isRefresh){

        switch (rtStatus){
            case User.REALTIME_STATUS_PASSENGER_IDLE:
                setUIForIdlestate(isRefresh);
                break;
            case User.REALTIME_STATUS_PASSENGER_REQUEST_SENT:
                setUIForRideRequesSent(isRefresh);
                break;
            case User.REALTIME_STATUS_PASSENGER_WAITING_DRIVER:
                setUIForWaitingDriver(isRefresh);
                break;
            case User.REALTIME_STATUS_PASSENGER_ONRIDE:
                setUIForOnRide(isRefresh);
                break;
            case User.REALTIME_STATUS_PASSENGER_FEEDBACK_DUE:
                setUIForFeedBack(isRefresh);
                break;
        }

        setMapBystate(rtStatus);
    }


    private void setUIForIdlestate(boolean isRefresh){

//        trackTimer.cancel();
        centerOnMe = true;
        pinter.setVisibility(View.VISIBLE);
        cancelRideView.setVisibility(View.GONE);
        abortRideView.setVisibility(View.GONE);
        feedBackStatusView.setVisibility(View.GONE);
        onRideStatusView.setVisibility(View.GONE);
        pickUpView.setVisibility(View.VISIBLE);
        findLiftStatusView.resetView();
        rideRequestState = PICKUP_STATUS;
        makeRideRequestUiBystate(rideRequestState);
        startStopPolling(false);
    }

    private void setUIForRideRequesSent(boolean isRefresh){

        pinter.setVisibility(View.GONE);
        rideRequestState = User.REALTIME_STATUS_PASSENGER_REQUEST_SENT;
        pickUpView.setVisibility(View.GONE);
        feedBackStatusView.setVisibility(View.GONE);
        onRideStatusView.setVisibility(View.GONE);
        if(isRefresh) {
            cancelRideView.setVisibility(View.VISIBLE);
            findLiftStatusView.setVisibility(View.GONE);
        }else {
            showViewAnimated(cancelRideView,findLiftStatusView,dm.heightPixels,0);
        }

        startStopPolling(true);
    }

    private void setUIForWaitingDriver(boolean isRefresh){
        startStopPolling(true);
//        trackTimer.start();
        pinter.setVisibility(View.GONE);
        pickUpView.setVisibility(View.GONE);
        feedBackStatusView.setVisibility(View.GONE);
        onRideStatusView.setVisibility(View.GONE);
        if(ZegoConstants.DEBUG){
            Log.d("currentRideWaiting:",currentRide.toString());
        }
        abortRideView.setContent(currentRide);
        if(isRefresh){
            abortRideView.setVisibility(View.VISIBLE);
            cancelRideView.setVisibility(View.GONE);
        }else{
            showViewAnimated(abortRideView,cancelRideView,dm.heightPixels,0);
        }


    }

    private void setUIForOnRide(boolean isRefresh){
        startStopPolling(true);
//        trackTimer.cancel();
        pinter.setVisibility(View.GONE);
        pickUpView.setVisibility(View.GONE);
        cancelRideView.setVisibility(View.GONE);
        feedBackStatusView.setVisibility(View.GONE);
        onRideStatusView.setContent(currentRide.getPuaddr(),currentRide.getDoaddr());
        if(isRefresh) {
            abortRideView.setVisibility(View.GONE);
            onRideStatusView.setVisibility(View.VISIBLE);
        }else{
            showViewAnimated(onRideStatusView,abortRideView,dm.heightPixels,0);
        }

    }

    private void setUIForFeedBack(boolean isRefresh){
        startStopPolling(false);
        PopUpDialog.removeDialog(MainActivity.this);
        pinter.setVisibility(View.GONE);
        pickUpView.setVisibility(View.GONE);
        cancelRideView.setVisibility(View.GONE);
        abortRideView.setVisibility(View.GONE);
        feedBackStatusView.resetView();
        feedBackStatusView.setContent(currentRide.getDriver().getName(),currentRide.getDriver().getUserimg(),true);
        if(isRefresh) {
            onRideStatusView.setVisibility(View.GONE);
            feedBackStatusView.setVisibility(View.VISIBLE);
        }else{
            showViewAnimated(feedBackStatusView,onRideStatusView,dm.heightPixels,0);
        }

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

    private void showNoGpsMessage(boolean isGpsAvailable){

        if(!isThereConnectivity()){
            connectionStatusView.setFakeToastImg(R.drawable.connessione_icon);
            connectionStatusView.setFakeMsg(getString(R.string.no_connectivity));
            connectionStatusView.setVisibility(View.VISIBLE);
        }else{
            if(!isGpsAvailable){
                connectionStatusView.setFakeToastImg(R.drawable.nogps);
                connectionStatusView.setFakeMsg(getString(R.string.no_gps));
            }
            connectionStatusView.setVisibility(isGpsAvailable ? View.GONE : View.VISIBLE);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState)  {
        outState.putInt("rideRequestState",rideRequestState);
        outState.putSerializable("currentRide",currentRide);
        outState.putBoolean("centerOnMe",centerOnMe);
        if(ZegoConstants.DEBUG){
            Log.d("onSaveInstanceState:", "rideRequestState:" + rideRequestState);
            Log.d("onSaveInstanceState:", "currentRide:" + currentRide.toString());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        rideRequestState = savedInstanceState.getInt("rideRequestState");
        centerOnMe = savedInstanceState.getBoolean("centerOnMe");
        currentRide = (Riderequest) savedInstanceState.getSerializable("currentRide");
        if(ZegoConstants.DEBUG){
            Log.d("onRestoreInstanceState:", "rideRequestState:" + rideRequestState);
            Log.d("onRestoreInstanceState:", "currentRide:" + currentRide.toString());
        }

        isactivityCreatedAgain = true;

    }


    private void setPickUpAndDropOffMarker(Riderequest riderequest,boolean addDriver){
//        googleMap.clear();
//        builder = new LatLngBounds.Builder();
        if(currentRide.isPuAddressValid()){
            googleMap.addMarker(getPickUpMarker(riderequest.getPulat(),riderequest.getPulng()));
            builder.include(new LatLng(riderequest.getPulat(),riderequest.getPulng()));
        }

        if(currentRide.isDoAddressValid()){
            googleMap.addMarker(getDropOffpMarker(riderequest.getDolat(),riderequest.getDolng()));
            builder.include(new LatLng(riderequest.getDolat(),riderequest.getDolng()));
        }

        if(addDriver){
            builder.include(new LatLng(riderequest.getDriver().getLat(),riderequest.getDriver().getLng()));
        }

//        builder.include(getFakePositionToCenterMap());
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), MAP_PADDING_BOUNDS)); //zoom to fit la mappa

    }



    private void setMapBystate(int requestState) {
        if(googleMap == null) return;


        resetMap();


        builder = new LatLngBounds.Builder();

        if (requestState == PICKUP_STATUS) {
            loadDriversOnMap(googleMap,driversList,false);
            return;

        } else if (requestState == DROP_OFF_STATUS) {
            loadDriversOnMap(googleMap,driversList,false);
            if(currentRide != null && currentRide.getPulat() != null && currentRide.getPulng() != null){
                googleMap.addMarker(getPickUpMarker(currentRide.getPulat(),currentRide.getPulng()));
            }

            return;
        }else if (requestState == User.REALTIME_STATUS_PASSENGER_IDLE){
            return;
        }
        else if (requestState == FIND_LIFT_STATUS || requestState == User.REALTIME_STATUS_PASSENGER_REQUEST_SENT) {
            loadDriversOnMap(googleMap, driversList, false);
            if(currentRide != null && currentRide.getDolng() != null && currentRide.getDolat() != null && currentRide.getPulat() != null && currentRide.getPulng() != null){

                googleMap.addMarker(getPickUpMarker(currentRide.getPulat(), currentRide.getPulng()));
                googleMap.addMarker(getDropOffpMarker(currentRide.getDolat(), currentRide.getDolng()));

                builder.include(currentRide.getPuLatLng());
                builder.include(currentRide.getDoLatLng());
                builder.include(getFakePositionToCenterMap());
            }else{
                return;
            }

        }
        else {
            if(currentRide != null && currentRide.getDolng() != null && currentRide.getDolat() != null && currentRide.getPulat() != null && currentRide.getPulng() != null){
                if(ZegoConstants.DEBUG) {
                    Log.d("currentRide MAIN:", currentRide.toString());
                }

                googleMap.addMarker(getPickUpMarker(currentRide.getPulat(),currentRide.getPulng()));
                googleMap.addMarker(getDropOffpMarker(currentRide.getDolat(),currentRide.getDolng()));
                // googleMap.addMarker(new MarkerOptions().position(new LatLng(currentRide.getDriver().getLat(),currentRide.getDriver().getLng())).icon(BitmapDescriptorFactory.fromResource(R.drawable.standard_icon)));
                driverMarker = googleMap.addMarker(getCarMarker(true,currentRide.getDriver().getLat(),currentRide.getDriver().getLng()));
                if(ZegoConstants.DEBUG) {
                    Log.d("driverMarker:", "lat:" + currentRide.getDriver().getLat() + " Lng:" + currentRide.getDriver().getLng());
                }
                builder.include(currentRide.getPuLatLng());
                builder.include(currentRide.getDoLatLng());
                builder.include(driverMarker.getPosition());
                builder.include(getFakePositionToCenterMap());
            }else{
                return;
            }

        }

        if(centerOnMe){
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), MAP_PADDING_BOUNDS));
        }

    }


    @Nullable
    public PickUpStatusView getPickUpView() {
        return pickUpView;
    }

    public void setPickUpView(@Nullable PickUpStatusView pickUpView) {
        this.pickUpView = pickUpView;
    }




    private void askPositionToService(){
        BusRequestMessage busRequestMessage = new BusRequestMessage();
        busRequestMessage.setCode(ZegoConstants.OttoBusConstants.MY_POSITION);
        ApplicationController.getInstance().getOttoBus().post(busRequestMessage);
    }
}
