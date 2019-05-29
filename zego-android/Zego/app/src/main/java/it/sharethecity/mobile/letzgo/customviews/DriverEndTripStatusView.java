package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.dao.Conf;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 15/12/16.
 */

public class DriverEndTripStatusView extends PercentRelativeLayout implements DriverOnRideInfoView.DriverOnRideInfoRideListener, TipSelectorView.TipSelectorListener {

    @Nullable
    @BindView(R.id.info_ride)
    DriverOnRideInfoView driverInfoRideView;

    @Nullable
    @BindView(R.id.pudo_layout)
    StartStopView pudoView;

    @Nullable
    @BindView(R.id.price_slider)
    TipSelectorView priceSlider;

    @Nullable
    @BindView(R.id.end_trip_button)
    Button endTripButton;

    @Nullable
    @BindView(R.id.toast_view_msg)
    MyFakeToastView myToast;

    @Nullable
    @BindView(R.id.driver_my_position_button)
    ImageButton myPositionButton;


    private EndTripListener listener;
    private Integer priceAccepted;
    private Integer suggestedPrice;
    Integer rideMinCost;

    public DriverEndTripStatusView(Context context) {
        super(context);
        init(true);
    }

    public DriverEndTripStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public DriverEndTripStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public DriverEndTripStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(false);
//    }

    private void  init(boolean b){

        inflate(getContext(), R.layout.end_ride_view,this);
        if(b){
            ButterKnife.bind(this);
            setUI();
        }
    }

    private void setUI() {
        pudoView.pickUpCancelButton.setVisibility(GONE);
//        pudoView.dropOffCancelButton.setVisibility(VISIBLE);
        pudoView.dropOffCancelButton.setImageResource(R.drawable.nav);
        pudoView.enableDoAddrCancelButton(true);

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUI();
    }

    @Optional
    @OnClick(R.id.end_trip_button)
    public void onEdnRide(){
        if(listener != null){
            listener.onEndRide();
        }
    }

    @Optional
    @OnClick(R.id.driver_my_position_button)
    public void onCenterMap(){
        if(listener != null){
            listener.onCenterMap();
        }
    }

    private void readMinimunPrice(){
        String rideMinCostString = ApplicationController.getInstance().getGlobalConfParam(Conf.RIDE_MIN_COST);
        try{
            rideMinCost = Integer.valueOf(rideMinCostString);
        }catch (NumberFormatException ex){
            Log.d("SETUI:", "rideMinCostString:" + rideMinCostString );
        }
    }

    @Override
    public void onPriceChanged(SeekBar seekBar) {
        if(rideMinCost == null || rideMinCost == 0){
            readMinimunPrice();
        }
        int newPrice = suggestedPrice;
        if(seekBar.getProgress() <= rideMinCost){
            newPrice = rideMinCost;
            seekBar.setProgress(rideMinCost);
            if(myToast.getVisibility() == GONE){
                myToast.setFakeMsg(getResources().getString(R.string.min_price_alert));
                myToast.hideImage();
                myToast.setVisibility(VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        myToast.setVisibility(GONE);
                    }
                },1500);
            }

        }else if(seekBar.getProgress() >= suggestedPrice){
            seekBar.setProgress(suggestedPrice);
        }else {
            newPrice = 100*((seekBar.getProgress())/100);
        }


        if(ZegoConstants.DEBUG){
            Log.d("price:","" + newPrice);
        }

        priceSlider.setSimplePriceTextView(priceFormatted(newPrice));

        driverInfoRideView.setPriceTextView(newPrice);
//        driverInfoRideView.setOriginalPrice(getResources().getString(R.string.price));
        if(listener != null){
            listener.onPriceChanged(newPrice);
        }

    }

    @Override
    public void onStopPriceChanged(SeekBar seekBar) {

    }

    private SpannableString priceFormatted(Integer value){
        if(value == null) value = 0;

        String s = String.format("%.2f",(value)/100f) + " €";
        s = s.replace(".",",");
        SpannableString spannableString = new SpannableString(s);
        int decimalIndex = s.indexOf(",");
        int currencyIndex = s.indexOf("€");

        spannableString.setSpan(new RelativeSizeSpan(1.7f), 0, decimalIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, currencyIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), currencyIndex, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void setListener(final EndTripListener listener) {
        this.listener = listener;
        pudoView.setListeners(new StartStopView.PickUpDropoffListeners() {
            @Override
            public void onPickUpCancel() {

            }

            @Override
            public void onDropOffpCancel() {
                if(listener!= null){
                    listener.onNavigation();
                }
            }

            @Override
            public void onPickUpClick() {

            }

            @Override
            public void onDropOffClick() {

            }
        });

        driverInfoRideView.setListener(this);
        priceSlider.setListener(this);

    }


    public void setContent(Riderequest currentRide) {
        pudoView.setDropOffaddr(currentRide.getDoaddr());
        pudoView.setPickUpaddr(currentRide.getPuaddr());
        pudoView.enableDoAddrCancelButton(true);
        driverInfoRideView.setContent(currentRide);
        priceSlider.setSuggestedPrice(currentRide.getDriverfee() + currentRide.getZegofee(),currentRide.getDriverfee() + currentRide.getZegofee(),currentRide.getDriverfee() + currentRide.getZegofee());
        priceSlider.setSimplePriceTextView(priceFormatted(currentRide.getDriverfee() + currentRide.getZegofee()));
        suggestedPrice = currentRide.getDriverfee() + currentRide.getZegofee();
    }

    @Override
    public void onAcceptedPriceClick() {
        boolean isVisible = priceSlider.getVisibility() == VISIBLE;
        priceSlider.setVisibility(isVisible ? GONE : VISIBLE);
    }


    public void resetView(){
        suggestedPrice = 0;
        priceSlider.resetView();
        pudoView.resetView();
        priceSlider.setVisibility(GONE);
        driverInfoRideView.resetView();
    }


    public interface EndTripListener{
        void onNavigation();
        void onEndRide();
        void onPriceChanged(int price);
        void onCenterMap();
    }

}
