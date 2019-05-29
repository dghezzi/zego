package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 15/12/16.
 */

public class RideRequestReceivedView extends RelativeLayout {


    @Nullable
    @BindView(R.id.pudo_layout)
    StartStopView pudoView;

    @Nullable
    @BindView(R.id.passenger_info_view)
    PassengerInfoView passengerInfoView;

    @Nullable
    @BindView(R.id.info_ride)
    DriverInfoRideView driverInfoRideView;

    @Nullable
    @BindView(R.id.reject_button)
    Button rejectButton;


    @Nullable
    @BindView(R.id.accept_button)
    Button acceptButton;

    @Nullable
    @BindView(R.id.driver_my_position_button)
    ImageButton myPositionButton;

    private RideRequestReceivedListener listener;


    public RideRequestReceivedView(Context context) {
        super(context);
        init(true);
    }

    public RideRequestReceivedView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public RideRequestReceivedView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RideRequestReceivedView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(false);
    }


    private void  init(boolean b){
        inflate(getContext(), R.layout.ride_request_received_status_view,this);
        if(b){
            ButterKnife.bind(this);
            setUi();
        }
    }

    @Optional
    @OnClick({R.id.reject_button,R.id.accept_button})
    public void onButtonsClick(View v){
        switch (v.getId()){
            case R.id.reject_button:
                if(listener != null){
                    listener.onRejectRide();
                }
                break;

            case R.id.accept_button:
                if(listener != null){
                    listener.onAcceptRide();
                }
                break;
        }
    }

    @Optional
    @OnClick(R.id.driver_my_position_button)
    public void OnMyPositionClick(){
        if(listener != null){
            if(ZegoConstants.DEBUG){
                Log.d("OnMyPositionClick:","OnMyPositionClick");
            }
            listener.onCenterMap();
        }
    }

    private void setUi() {
        pudoView.pickUpCancelButton.setVisibility(GONE);
        pudoView.dropOffCancelButton.setVisibility(GONE);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUi();
    }

    public void setContent(Riderequest riderequest){
        pudoView.setPickUpaddr(riderequest.getPuaddr());
        pudoView.setSimpleDoAddress(riderequest.getDoaddr());
        passengerInfoView.setPassengerInfo(riderequest.getRider());
        passengerInfoView.setTipoCorsa(riderequest.getReqlevel());
        passengerInfoView.setPassengerNumber(riderequest.getNumpass());
//        passengerInfoView.setEta(riderequest.getDrivereta());
        driverInfoRideView.setContent(riderequest);
    }

    public void setListener(RideRequestReceivedListener listener) {
        this.listener = listener;
    }


    public interface RideRequestReceivedListener{
        void onRejectRide();
        void onAcceptRide();
        void onCenterMap();
    }
}
