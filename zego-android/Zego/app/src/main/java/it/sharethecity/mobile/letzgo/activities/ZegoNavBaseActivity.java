package it.sharethecity.mobile.letzgo.activities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.adapters.NavDrawerAdapter;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.broadcastreceiver.NetworkChangeReceiver;
import it.sharethecity.mobile.letzgo.customviews.BoldRelewayTextView;
import it.sharethecity.mobile.letzgo.customviews.LightRelewayTextView;
import it.sharethecity.mobile.letzgo.customviews.MyFontTextView;
import it.sharethecity.mobile.letzgo.customviews.RegularRelewayTextView;
import it.sharethecity.mobile.letzgo.dao.CompactDriver;
import it.sharethecity.mobile.letzgo.dao.User;
import it.sharethecity.mobile.letzgo.network.NetworkManager;
import it.sharethecity.mobile.letzgo.network.interfaces.UserRESTInterface;
import it.sharethecity.mobile.letzgo.network.request.ErrorObject;
import it.sharethecity.mobile.letzgo.network.utils.NetworkErrorHandler;
import it.sharethecity.mobile.letzgo.services.LocationService;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ZegoNavBaseActivity extends ZegoBaseActivity {

    public static final float LEFT_ICON_PERCENT_DIMEN = 0.092f;
    protected static final int MAP_PADDING_BOUNDS = 200;

    @Nullable
    @BindView(R.id.container)
    FrameLayout container;

    @Nullable
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;

    @Nullable
    @BindView(R.id.profile_image)
    ImageView profileImageView;

    @Nullable
    @BindView(R.id.user_full_name_text_view)
    BoldRelewayTextView fullNameTextView;

    @Nullable
    @BindView(R.id.rating_nav_bar_text_view)
    LightRelewayTextView ratingTextView;

    @Nullable
    @BindView(R.id.promo_code_text_view)
    BoldRelewayTextView promoCodeTextView;

    @Nullable
    @BindView(R.id.tap_here_text_view)
    RegularRelewayTextView tapHereTextView;

    @Nullable
    @BindView(R.id.promo_layout)
    LinearLayout promoButton;


    @Nullable
    @BindView(R.id.become_driver_button)
    RelativeLayout becomeDriverButton;

    @Nullable
    @BindView(R.id.header_nav)
    RelativeLayout navHeader;

    @Nullable
    @BindView(R.id.nav_list_view)
    RecyclerView recyclerView;

    @Nullable
    @BindView(R.id.become_driver_icon)
    ImageView becomeDriverButtonIcon;


    @Nullable
    @BindView(R.id.become_driver_text_view)
    MyFontTextView becomeDriverTextView;

    private Intent activityToOpenFromDrawerIntent;
    private boolean isLogOut;
    protected Bitmap pickUpMarkerResized;
    protected Bitmap dropOffMarkerResized;
    private Bitmap greenCarMarkerResized;
    private Bitmap bluCarMarkerResized;
    protected List<CompactDriver> driversList = new ArrayList<>();
    private boolean unRegBus;
    private HashMap<Integer,Marker> driversMarkers = new HashMap();
    protected Marker driverMarker;
    protected GoogleMap googleMap;
    protected View mapView;
    protected LatLngBounds.Builder builder = new LatLngBounds.Builder();
    protected boolean isFromResume;
    protected NetworkChangeReceiver networkChangeReceiver;
    protected boolean centerOnMe;
    protected int lastRtstatus;
    protected boolean isAddressFromGoogle = false;
    protected boolean isChangingUmode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zego_nav_base);
        ButterKnife.bind(this);

        unRegBus = true;
        setUI();
        setListeners();
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext(),LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(new NavDrawerAdapter(getBaseContext(), new NavDrawerAdapter.NavBarListener() {
            @Override
            public void positionSelected(int position) {
                activityToOpenFromDrawerIntent = null;
                isLogOut = false;

                switch (position){
                    case NavDrawerAdapter.PROFILE:
                        activityToOpenFromDrawerIntent = new Intent(ZegoNavBaseActivity.this,ProfileActivity.class);
                        break;
                    case NavDrawerAdapter.PAYMENT:
                        activityToOpenFromDrawerIntent = new Intent(ZegoNavBaseActivity.this,MyCardsActivity.class);
                        break;
                    case NavDrawerAdapter.MY_LIFTS:
                        activityToOpenFromDrawerIntent = new Intent(ZegoNavBaseActivity.this,RideHistoryActivity.class);
                        break;
                    case NavDrawerAdapter.PROMO:
                        activityToOpenFromDrawerIntent = new Intent(ZegoNavBaseActivity.this,MyPromoActivity.class);
                        break;
                    case NavDrawerAdapter.INFO:
                        activityToOpenFromDrawerIntent = new Intent(ZegoNavBaseActivity.this,InfoActivity.class);
                        break;
                    case NavDrawerAdapter.GIVE_LIFTS:
//                        activityToOpenFromDrawerIntent = new Intent(ZegoNavBaseActivity.this,.class);
                        break;
                }
                if(activityToOpenFromDrawerIntent != null)
                     startActivity(activityToOpenFromDrawerIntent);

                drawerLayout.closeDrawer(Gravity.LEFT);
            }

            @Override
            public void onchangeMode(CheckBox checkBox) {
                if(!isChangingUmode && (user.getRtstatus() == User.REALTIME_STATUS_DRIVER_IDLE || user.getRtstatus() == User.REALTIME_STATUS_PASSENGER_IDLE)){
                    postChangeMode(checkBox.isChecked() ? User.UMODE_DRIVER : User.UMODE_RIDER);
                }else{
                    checkBox.setChecked(!checkBox.isChecked());
                }

            }
        }));
    }

    private void postChangeMode(String umode) {
        isChangingUmode = true;
        killPollingRideService();
        killPositionService();
        unregitserOttoBusSafetly();
       // ApplicationController.getInstance().getOttoBus().unregister(this);
        Call<User> call = NetworkManager.getInstance().getRetrofit().create(UserRESTInterface.class).postChamgeMode(user.getZegotoken(),umode,user);
        showOrDismissProgressWheel(SHOW);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                showOrDismissProgressWheel(DISMISS);
                if(response.isSuccessful()){
                    ApplicationController.getInstance().saveUser(response.body());
                    Intent i = new Intent(ZegoNavBaseActivity.this,response.body().getUmode().equalsIgnoreCase(User.UMODE_DRIVER) ? DriverMainActivity.class : MainActivity.class);
                    startActivity(i);
                    finish();

                }else {
                    if(response.code() == ZegoConstants.ApiRestConstants.ERROR_500){
                        ErrorObject errorObject = NetworkErrorHandler.getInstance().decodeError(response);
                        Toast.makeText(getBaseContext(),(errorObject != null ? errorObject.getMsg() : getString(R.string.unknown_error)),Toast.LENGTH_SHORT).show();
                    }else{
                        NetworkErrorHandler.getInstance().errorHandler(response,ZegoNavBaseActivity.this);
                    }
                    ApplicationController.getInstance().getOttoBus().register(ZegoNavBaseActivity.this);
                    user = user == null ? ApplicationController.getInstance().getUserLogged() : user;
                    startService(new Intent(ZegoNavBaseActivity.this, LocationService.class));
                    startPollingService(user);

                }
                isChangingUmode = false;
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                showOrDismissProgressWheel(DISMISS);
                ApplicationController.getInstance().getOttoBus().register(ZegoNavBaseActivity.this);
                user = user == null ? ApplicationController.getInstance().getUserLogged() : user;
                startService(new Intent(ZegoNavBaseActivity.this, LocationService.class));
                startPollingService(user);
                isChangingUmode = false;
                showInfoDialog(getString(R.string.warning),getString(R.string.impossible_to_connetc_to_server));
            }
        });

    }

    private void setUI() {
        becomeDriverButton.getLayoutParams().height = (int) (0.0833 * ApplicationController.getInstance().getScreenDimension().heightPixels);
      //  navHeader.getLayoutParams().height = (int) (0.1927 * ApplicationController.getInstance().getScreenDimension().heightPixels);
        becomeDriverButtonIcon.getLayoutParams().width = (int) (LEFT_ICON_PERCENT_DIMEN* ApplicationController.getInstance().getScreenDimension().widthPixels);
        becomeDriverButtonIcon.getLayoutParams().height = (int) (LEFT_ICON_PERCENT_DIMEN* ApplicationController.getInstance().getScreenDimension().widthPixels);
    }

    @Override
    protected void onResume() {
        super.onResume();
        user = ApplicationController.getInstance().getUserLogged();

        if(user != null){
            updateNavigaTionDrawer(user);
        }

    }


    private void updateNavigaTionDrawer(User user){
        if(user != null){
            fullNameTextView.setText(user.getFname() + " " + user.getLname());

            ratingTextView.setVisibility(user.getRatingByCanDriveStatus() == 0  ? View.GONE : View.VISIBLE);
            if(user.getRatingByCanDriveStatus() > 0d){
                ratingTextView.setText(String.format("Rating: %.2f",user.getRatingByCanDriveStatus()));
            }

            tapHereTextView.setText(user.getRefcode());
//        becomeDriverButton.setVisibility(user.getCandrive() == 0 ? View.VISIBLE  : View.GONE);
            becomeDriverTextView.setText(getString(user.getCandrive() == 0 ? R.string.become_driver : R.string.driver_area));

            if(user.hasImage()){
                Picasso.with(getBaseContext())
                        .load(user.getImg())
                        .transform(new CircleTransform())
                        .placeholder(R.drawable.user_placeholder)
                        .into(profileImageView);
            }
        }
    }

    private void unregitserOttoBusSafetly(){
        try{
            ApplicationController.getInstance().getOttoBus().unregister(this);
        }catch (Exception ex){
            //cattura l'eccezione generata da otto se si prova a unregistrare quando non era registrato

        }
    }
    @Override
    protected void onStop() {
        super.onStop();

        unregitserOttoBusSafetly();

    }

    @Override
    protected void onStart() {
        super.onStart();
        ApplicationController.getInstance().getOttoBus().register(this);
    }

//    protected void loadDriversOnMap(GoogleMap googleMap, List<CompactDriver> drivers, boolean amIdriver){
//
//        if(googleMap != null){
//           // googleMap.clear();
//            if(drivers ==  null ) return;
//            for( CompactDriver driver : drivers) {
//                if (driver != null && driver.getLat() != null && driver.getLng() != null){
//                    if(ZegoConstants.DEBUG){
//                        Log.d("DRIVER:", "" + driver.getLat() + "," + driver.getLng());
//                        Log.d("USER:", "" + user.toString());
//                    }
//
//                    MarkerOptions markerOptions = getCarMarker(amIdriver && user.getId().equals(driver.getDid()),driver.getLat(),driver.getLng());
//                    googleMap.addMarker(markerOptions);
//                }
//            }
//        }
//    }

    private void setListeners(){


        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                activityToOpenFromDrawerIntent = null;
            }

            @Override
            public void onDrawerClosed(View drawerView) {

//                if(isLogOut){
//
//                }else{
//                    if (activityToOpenFromDrawerIntent != null){
//                        activityToOpenFromDrawerIntent.putExtra(FROM_ACTIVITY,ZegoNavBaseActivity.class.getSimpleName());
//                        startActivity(activityToOpenFromDrawerIntent);
//                    }
//
//
//                    activityToOpenFromDrawerIntent = null;
//                }


            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }


    @Optional
    @OnClick(R.id.become_driver_button)
    public void onBecomeDriverClick(){
        if(user.getCandrive() == 1){
            onpenUrlLoginGateway(user.getZegotoken());
        }else{
            Intent i = new Intent(ZegoNavBaseActivity.this,BecomeADriverActivity.class);
            startActivity(i);
        }

    }



    @Optional
    @OnClick({R.id.promo_code_text_view,R.id.tap_here_text_view})
    public void onPromoCodeClick(){
       if(user != null && user.getRefcode() != null && !user.getRefcode().isEmpty()) {
           Intent sendIntent = new Intent();
           sendIntent.setAction(Intent.ACTION_SEND);
           sendIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_promo_code).replace("{XX}",user.getRefcode()));
           sendIntent.setType("text/plain");
           startActivity(Intent.createChooser(sendIntent,""));
       }

    }

    protected void resizePickUpAndDroppOffMarkers(){
//112 150
        pickUpMarkerResized = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icona_pickup),getResources().getDimensionPixelSize(R.dimen.marker_pickup_width),getResources().getDimensionPixelSize(R.dimen.marker_pickup_height),true);//UtilityFunctions.getResizedBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icona_pickup), 112, 150);
        dropOffMarkerResized = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.icona_dropoff),getResources().getDimensionPixelSize(R.dimen.marker_dropoff_width),getResources().getDimensionPixelSize(R.dimen.marker_dropoff_height),true);//UtilityFunctions.getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.icona_dropoff),127,150);
        bluCarMarkerResized = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.driver_blu),getResources().getDimensionPixelSize(R.dimen.car_dimen),getResources().getDimensionPixelSize(R.dimen.car_dimenh),true);//UtilityFunctions.getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.driver_blu),90,90);
        greenCarMarkerResized = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.standard_icon),getResources().getDimensionPixelSize(R.dimen.car_dimen),getResources().getDimensionPixelSize(R.dimen.car_dimenh),true);//UtilityFunctions.getResizedBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.standard_icon),90,90);
    }

    protected MarkerOptions getPickUpMarker(double lat,double lng){
        return  new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(pickUpMarkerResized))
                .position(new LatLng(lat,lng));
    }


    protected MarkerOptions getDropOffpMarker(double lat,double lng){
        return  new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(dropOffMarkerResized))
                .position(new LatLng(lat,lng));
    }

    protected MarkerOptions getCarMarker(boolean greenCar,double lat,double lng){
        return  new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromBitmap(greenCar ? greenCarMarkerResized : bluCarMarkerResized))
                .position(new LatLng(lat,lng));
    }

    protected void showViewAnimated(final View viewToShow, final View viewToRemove, int starty, int stopy){
        ObjectAnimator exitAnim = ObjectAnimator.ofFloat(viewToRemove,"alpha",1f,0);
        exitAnim.setDuration(200); // duration 0.2 seconds

        final ObjectAnimator entryAnim = ObjectAnimator.ofFloat(viewToShow, "y",starty, stopy);
        entryAnim.setDuration(400);

        exitAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                viewToRemove.setVisibility(View.GONE);
                viewToRemove.setAlpha(1);
                viewToShow.setVisibility(View.VISIBLE);
                entryAnim.start();

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        exitAnim.start();

    }

    protected void showViewAnimated(View viewToShow, int starty, int stopy){
        ObjectAnimator entryAnim = ObjectAnimator.ofFloat(viewToShow, "y",starty, stopy);
        entryAnim.setDuration(400);
        viewToShow.setVisibility(View.VISIBLE);
        entryAnim.start();
    }

    protected LatLng getFakePositionToCenterMap(){
        double latDelta = Math.abs(currentRide.getPulat() - currentRide.getDolat());
        double min =  Math.min(currentRide.getPulat(),currentRide.getDolat());
        float k = latDelta < 0.001d ? 9f : 1.8f;
        LatLng latLng = new LatLng(min - k * latDelta,currentRide.getPulng());
        return latLng;

    }

    protected void setDriverList(List<CompactDriver> drivers){
        driversList.clear();
        driversList.addAll(drivers);
    }

    protected List<String> getDialogReasons(String baseTag,int number){
        String tag = baseTag;
        String lang = "." + (ApplicationController.getInstance().getsDefSystemLanguage().equalsIgnoreCase("it") ? "it" : "en");
        List<String> reasons = new ArrayList<>();
        for(int i = 1; i <= number; i++){
            reasons.add(ApplicationController.getInstance().getGlobalConfParam(tag + i + lang));
        }

        return reasons;

    }


    protected void loadDriversOnMap(GoogleMap googleMap,List<CompactDriver> drivers,boolean amIdriver){
        if(googleMap != null){
            if(drivers == null) return;
            for(Iterator<Map.Entry<Integer,Marker>> it = driversMarkers.entrySet().iterator(); it.hasNext();){
                Map.Entry<Integer,Marker> entry = it.next();
                boolean isMarkerPresent = false;
                for(CompactDriver driver : drivers){
                    if(driver.getDid().equals(entry.getKey())){
                        isMarkerPresent = true;
                        break;
                    }
                }
                if(!isMarkerPresent){
                    entry.getValue().remove();//rimuove il marker dalla mappa
                    it.remove();  // rimove il riferimento del driver  dall'hashMap
                }
            }
            for (CompactDriver driver : drivers) {
                if (driver != null && driver.getLat() != null && driver.getLng() != null){
                    if (ZegoConstants.DEBUG){
                        Log.d("DRIVER:", "" + driver.getLat() + "," + driver.getLng());
                        Log.d("USER:", "" + user.toString());
                    }
                    boolean isItMe = amIdriver && user.getId().equals(driver.getDid());
                    if (driversMarkers.containsKey(driver.getDid())){
                        Marker marker = driversMarkers.get(driver.getDid());
                        marker.setPosition(new LatLng(isItMe ? user.getLlat() : driver.getLat(), isItMe ? user.getLlon() : driver.getLng()));
                        driversMarkers.put(driver.getDid(), marker);
                    }else {
                        MarkerOptions markerOptions = getCarMarker(isItMe,isItMe ? user.getLlat() : driver.getLat(),isItMe ? user.getLlon() : driver.getLng());
                        driversMarkers.put(driver.getDid(), googleMap.addMarker(markerOptions));
                    }
                }
            }
        }
    }

    protected void resetMap(){
        if(googleMap != null){
            googleMap.clear();
            driverMarker = null;
            driversMarkers.clear();
        }
    }



//    protected void makeSound(int sound){
//        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + sound);
//        if(ringtone != null){
//            if(ringtone.isPlaying()){
//                ringtone.stop();
//            }
//            ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
//        }
//        else{
//            ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
//        }
//        ringtone.play();
//    }
//
//    protected void stopSound(){
//        if(ringtone != null) {
//            if (ringtone.isPlaying()) {
//                ringtone.stop();
//            }
//        }
//    }

}
