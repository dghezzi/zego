package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;

/**
 * Created by lucabellaroba on 15/12/16.
 */

public class DriverStartRideStatusView extends RelativeLayout {

    @Nullable
    @BindView(R.id.passenger_info_view)
    PassengerInfoView passengerInfoView;

    @Nullable
    @BindView(R.id.info_ride)
    DriverInfoRideView driverInfoRideView;

    @Nullable
    @BindView(R.id.cancel_ride_button)
    Button cancelRideButton;


    @Nullable
    @BindView(R.id.start_ride_button)
    Button startRideButton;

    @Nullable
    @BindView(R.id.driver_my_position_button)
    ImageButton myPositionButton;


//    @Nullable
//    @BindView(R.id.pu_add_text_view)
//    MyFontTextView puTexrView;

    @Nullable
    @BindView(R.id.pudo_layout)
    StartStopView pudoView;

    private DriverStartRideListener listener;


    public DriverStartRideStatusView(Context context) {
        super(context);
        init(true);
    }

    public DriverStartRideStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public DriverStartRideStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DriverStartRideStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(false);
    }


    private void  init(boolean b){

        inflate(getContext(), R.layout.driver_start_ride_view,this);
        if(b){
            ButterKnife.bind(this);
            setUI();
        }
    }

    private void setUI() {
        passengerInfoView.getTelImage().setVisibility(VISIBLE);
        pudoView.pickUpCancelButton.setVisibility(VISIBLE);
        pudoView.pickUpCancelButton.setImageResource(R.drawable.nav);
        pudoView.dropOffCancelButton.setVisibility(GONE);

    }

//    @Optional
//    @OnClick(R.id.nav_img_button)
//    public void onNavigation(){
//        if(listener != null){
//            listener.onNavigation();
//        }
//    }


    @Optional
    @OnClick(R.id.cancel_ride_button)
    public void onCancelRide(){
        if(listener != null){
            listener.onCancelRide();
        }
    }

    @Optional
    @OnClick(R.id.start_ride_button)
    public void onStartRide(){
        if(listener != null){
            listener.onStartRide();
        }
    }

    @Optional
    @OnClick(R.id.driver_my_position_button)
    public void onCenterMap(){
        if(listener != null){
            listener.onCenterMap();
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUI();
    }

    public void setListener(final DriverStartRideListener listener) {
        this.listener = listener;
        passengerInfoView.setListener(listener);
        pudoView.setListeners(new StartStopView.PickUpDropoffListeners() {
            @Override
            public void onPickUpCancel() {
                if(listener!= null){
                    listener.onNavigation();
                }
            }

            @Override
            public void onDropOffpCancel() {
//                if(listener!= null){
//                    listener.onNavigation();
//                }
            }

            @Override
            public void onPickUpClick() {

            }

            @Override
            public void onDropOffClick() {

            }
        });
    }

    public void setContent(Riderequest currentRide) {
//        puTexrView.setText(currentRide.getPuaddr());
        pudoView.setPickUpaddr(currentRide.getPuaddr());
        pudoView.setSimpleDoAddress(currentRide.getDoaddr());
        passengerInfoView.setPassengerInfo(currentRide.getRider());
        passengerInfoView.setPassengerNumber(currentRide.getNumpass());
        passengerInfoView.setTipoCorsa(currentRide.getReqlevel());
//        passengerInfoView.setEta(currentRide.getDrivereta());
        driverInfoRideView.setContent(currentRide);
    }


    public interface DriverStartRideListener extends PassengerInfoView.PassengerInfoListener{
        void onNavigation();
        void onCancelRide();
        void onStartRide();
        void onCenterMap();
    }
}
