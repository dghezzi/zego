package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import java.util.Calendar;
import java.util.TimeZone;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.util.Log;
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
 * Created by lucabellaroba on 13/12/16.
 */

public class AbortRideView extends PercentRelativeLayout {


    @Nullable
    @BindView(R.id.abort_req_text_view)
    MyFontTextView abortButton;


    @Nullable
    @BindView(R.id.eta_value_text_view)
    MyFontTextView etaTextView;

    @Nullable
    @BindView(R.id.puaddr_req_text_view)
    MyFontTextView puAddrTextView;

    @Nullable
    @BindView(R.id.driver_arrives_in_label)
    MyFontTextView driverArrivesTextView;

    @Nullable
    @BindView(R.id.cancel_term)
    MyFontTextView cancelTimeTextView;


    @Nullable
    @BindView(R.id.info_rider_view)
    DriverInfoView driverInfoView;


    @Nullable
    @BindView(R.id.call_button)
    RelativeLayout callButton;

    @Nullable
    @BindView(R.id.driver_my_position_button)
    ImageButton myPositionButton;

    private AbortRequestListener listener;

    private CountDownTimer countDownTimer ;


    public AbortRideView(Context context) {
        super(context);
        init(true);
    }

    public AbortRideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public AbortRideView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(false);
    }


    private void init(boolean b) {

        inflate(getContext(), R.layout.passenger_abort_request_layout,this);
        if(b){
            ButterKnife.bind(this);
            // setUi();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        //  setUi();
    }


    @Optional
    @OnClick(R.id.abort_req_text_view)
    public void onAbortRequestClick(){
        if(listener != null){
            listener.onAbortRequestClick();
        }
    }

    @Optional
    @OnClick(R.id.call_button)
    public void onCallClick(){
        if(listener != null){
            listener.onCall();
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

    public void setListener(AbortRequestListener listener) {
        this.listener = listener;
    }

    public void setEta(Integer drivereta) {
        etaTextView.setText(String.format("%d min",drivereta/60 + 1));
    }

    public interface AbortRequestListener{
        void onAbortRequestClick();
        void onCall();
        void onCenterMap();
    }

    public void setContent(Riderequest riderequest){
        driverArrivesTextView.setText(riderequest.getDriver().getName()+ " " + getResources().getString(R.string.driver_arrives_in));
        driverInfoView.setDriverInfo(riderequest.getDriver());
        puAddrTextView.setText(riderequest.getPuaddr());
        setEta(riderequest.getDrivereta());
        cancelTimeTextView.setVisibility(VISIBLE);
        cancelTimeTextView.setText(getResources().getString(R.string.two_min_to_cancel) + " 02:00 ");
        Calendar cal = Calendar.getInstance(java.util.TimeZone.getTimeZone("CET"));
        Calendar now = Calendar.getInstance(java.util.TimeZone.getTimeZone("CET"));
        cal.setTimeInMillis((Long.valueOf(riderequest.getAssigndate()) * 1000));
        cal.add(Calendar.MINUTE,2);
        long twoMinutesAheadMs = cal.getTimeInMillis();
        long nowMs = now.getTimeInMillis();
        long diffTime = twoMinutesAheadMs - nowMs;

        startTimer(diffTime);


// etaTextView.setTypeface(etaTextView.getTypeface(), Typeface.BOLD);
    }

    public void startTimer(long time){
        if(countDownTimer != null) countDownTimer.cancel();
        if(time <= 0){
            cancelTimeTextView.setVisibility(GONE);
            return;
        }

        countDownTimer = new CountDownTimer(time,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished/1000;
                cancelTimeTextView.setText(getResources().getString(R.string.two_min_to_cancel) + " " + String.format("%02d",seconds/60) + ":" + String.format("%02d",seconds%60));
            }

            @Override
            public void onFinish() {
                cancelTimeTextView.setVisibility(GONE);
            }
        };
        countDownTimer.start();
    }
}
