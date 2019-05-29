package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 14/12/16.
 */

public class PassengerOnRideStatusView extends PercentRelativeLayout {

    @Nullable
    @BindView(R.id.alert_button)
    public Button alertButton;

    @Nullable
    @BindView(R.id.share_button)
    public Button shareButton;

    @Nullable
    @BindView(R.id.pichup_dropoff_view)
    public StartStopView pudoView;

    @Nullable
    @BindView(R.id.driver_my_position_button)
    ImageButton myPositionButton;

    private PassengerOnRideListener listener;


    public PassengerOnRideStatusView(Context context) {
        super(context);
        init(true);
    }

    public PassengerOnRideStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public PassengerOnRideStatusView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(false);
    }



    private void init(boolean b) {

        inflate(getContext(), R.layout.passenger_onride_status_layout,this);
        if(b){
            ButterKnife.bind(this);
            setUi();
        }
    }


    @Optional
    @OnClick(R.id.share_button)
    public void onShareClick(){
        if(listener != null){
            listener.onShare();
        }
    }

    @Optional
    @OnClick(R.id.alert_button)
    public void onAlertClick(){
        if(listener != null){
            listener.onAlert();
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

    public void setListener(PassengerOnRideListener listener) {
        this.listener = listener;
    }


    public void setContent(String puaddr, String doaddr) {
        pudoView.setDropOffaddr(doaddr);
        pudoView.setPickUpaddr(puaddr);
        setUi();

    }

    public interface PassengerOnRideListener{
        void onAlert();
        void onShare();
        void onCenterMap();
    }
}
