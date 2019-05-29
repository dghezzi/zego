package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.dao.Conf;
import it.sharethecity.mobile.letzgo.dao.StripeCard;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;
import it.sharethecity.mobile.letzgo.network.response.PriceResponse;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 12/12/16.
 */

public class FindLiftStatusView extends PercentRelativeLayout implements InfoRideView.InfoRideViewListener,TipSelectorView.TipSelectorListener{


    @Nullable
    @BindView(R.id.info_ride_view)
    InfoRideView infoRideView;

    @Nullable
    @BindView(R.id.tip_selector_view)
    TipSelectorView tipSelectorView;

    @Nullable
    @BindView(R.id.pickup_dropoff_view)
    StartStopView pickupDropOffView;

    @Nullable
    @BindView(R.id.confirm_button)
    Button confirmButton;

    @Nullable
    @BindView(R.id.toast_view_msg)
    MyFakeToastView myToast;

    private FindLiftStatusViewListener listener;
    Integer rideMinCost;
    Integer rideMaxCost;
    private int suggestedPrice;
    private int driverfee;
    private Riderequest rideReq;


    public FindLiftStatusView(Context context) {
        super(context);
        init(true);
    }

    public FindLiftStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public FindLiftStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    public FindLiftStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init(false);
//    }


    private void  init(boolean b){
        inflate(getContext(), R.layout.find_lift_status_view,this);
        if(b){
            ButterKnife.bind(this);
            setListeners();
            setUI();
        }
    }

    private void setUI(){
        infoRideView.originalPriceTextView.setText(getResources().getString(R.string.edit_price));
        infoRideView.showTextPriceModified(true);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setListeners();
        setUI();
    }

    private void setListeners() {
        infoRideView.setListener(this);
        tipSelectorView.setListener(this);
    }


    public void resetView(){
        infoRideView.resetView();
        showInfoRide(false);
        pickupDropOffView.resetView();
        resetDropOff();
        tipSelectorView.resetView();
        myToast.setVisibility(GONE);

    }




    public void setPickUpAddr(String pickUpAddr) {
        pickupDropOffView.setPickUpaddr(pickUpAddr);
    }


    @Optional
    @OnClick(R.id.confirm_button)
    public void onConfirmButtonClick(){
        if(listener != null){
            listener.onCofirmButton();
        }

    }



    public void setListener(FindLiftStatusViewListener listener) {
        this.listener = listener;
        pickupDropOffView.setListeners(listener);

    }


    //region INFO RIDE CALLBACKS
    @Override
    public void onPassengerClick(int passengerNumber) {
        if(listener != null){
            listener.onPassengerNumberChanged(passengerNumber);
        }
    }

    @Override
    public void onCcClick() {
        if(listener != null){
            listener.onCreditCardClick();
        }
    }

    @Override
    public void onEstimatedPriceClick() {
        boolean isVisible = tipSelectorView.getVisibility() == VISIBLE;
        tipSelectorView.setVisibility(isVisible ? GONE : VISIBLE);
    }

    @Override
    public void onPriceChanged(SeekBar seekBar) {

        int newPrice =  computePrice(seekBar);
        if(ZegoConstants.DEBUG){
            Log.d("price:","onPriceChanged:" + newPrice);
        }
        tipSelectorView.setPriceTextView(newPrice,driverfee);

        infoRideView.setEstimatedPrice(newPrice);


    }

    @Override
    public void onStopPriceChanged(SeekBar seekBar) {
        int newPrice = computePrice(seekBar);
        if(ZegoConstants.DEBUG){
            Log.d("price:","onStopPriceChanged:" + newPrice);
        }
//        tipSelectorView.setPriceTextView(newPrice);
//        infoRideView.setEstimatedPrice(newPrice);

        if(listener != null){
            listener.onPriceChanged(newPrice);
        }
    }

    public void updateSliderAndInfoPrice(Riderequest riderequest){
        driverfee = riderequest.getDriverfee();
        tipSelectorView.setSuggestedPrice(!riderequest.getOriginalPrice().equals(riderequest.getPassprice()) ? riderequest.getPassprice() : riderequest.getOriginalPrice(),readMaximunFee(), riderequest.getDriverfee());
        infoRideView.setEstimatedPrice(!riderequest.getOriginalPrice().equals(riderequest.getPassprice()) ? riderequest.getPassprice() : riderequest.getOriginalPrice());
    }

    private int computePrice(SeekBar seekBar){
        if(rideMinCost == null || rideMinCost == 0){
            readMinimunPrice();
        }

        int newPrice = suggestedPrice;
        if(seekBar.getProgress() < rideMinCost){
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
            //  Toast.makeText(getContext(),getResources().getString(R.string.min_price_alert),Toast.LENGTH_SHORT);
        }
        newPrice = 100*((seekBar.getProgress() - 25)/100) + 100;
        return newPrice;

    }

    public void hideRidePrice(boolean hide){
        infoRideView.estimatedPriceTextView.setVisibility(hide ? INVISIBLE : VISIBLE);
    }



    public void setDropOff(PriceResponse presp){
        if(presp != null && presp.getPrice() != null){
//            pickupDropOffView.setDropOffaddr(presp.getDropoff());
            infoRideView.setEstimatedPrice(presp.getOriginalPrice());
            suggestedPrice = presp.getOriginalPrice();
            tipSelectorView.setSuggestedPrice(presp.getOriginalPrice(),readMaximunFee(), presp.getDriverfee());

            driverfee = presp.getDriverfee();
//            tipSelectorView.setSuggestedPrice(presp.getPrice());
//            onPriceChanged(presp.getPrice());
        }

    }

    public void setContent(Riderequest riderequest){
        rideReq = riderequest;
        pickupDropOffView.setPickUpaddr(riderequest.getPuaddr());
        pickupDropOffView.setDropOffaddr(riderequest.getDoaddr());
        infoRideView.setEstimatedPrice(!riderequest.getOriginalPrice().equals(riderequest.getPassprice()) ? riderequest.getPassprice() : riderequest.getOriginalPrice());
        infoRideView.setPassengerNumber(riderequest.getNumpass());
        showInfoRide(true);
        hideRidePrice(false);
        suggestedPrice = !riderequest.getOriginalPrice().equals(riderequest.getPassprice()) ? riderequest.getPassprice() : riderequest.getOriginalPrice();
        tipSelectorView.setSuggestedPrice(!riderequest.getOriginalPrice().equals(riderequest.getPassprice()) ? riderequest.getPassprice() : riderequest.getOriginalPrice(),readMaximunFee(),riderequest.getDriverfee());
        }

    public void resetDropOff() {
        infoRideView.resetEstimatedPrice();
        showInfoRide(false);
        tipSelectorView.resetView();
        tipSelectorView.setVisibility(GONE);
        confirmButton.setText(getResources().getString(R.string.enter_destination));
        suggestedPrice = 0;
        pickupDropOffView.resetDropOff();

    }

    private void readMinimunPrice(){
        String rideMinCostString = ApplicationController.getInstance().getGlobalConfParam(Conf.RIDE_MIN_COST);
        try{
            rideMinCost = Integer.valueOf(rideMinCostString);
        }catch (NumberFormatException ex){
            Log.d("SETUI:", "rideMinCostString:" + rideMinCostString );
        }
    }

    private int readMaximunFee(){
        String rideMaxCostString = ApplicationController.getInstance().getGlobalConfParam(Conf.PRICING_MAXIMUM_FEE);
        try{
            rideMaxCost = Math.min(Integer.valueOf(rideMaxCostString),2*suggestedPrice);
        }catch (NumberFormatException ex){
            Log.d("SETUI:", "rideMaxCostString:" + rideMaxCostString );

        }

        return rideMaxCost;
    }


    public void showTipSelector(Integer extPrice) {
        if(extPrice != null){
            // tipSelectorView.setVisibility(VISIBLE);
            confirmButton.setText(getResources().getString(R.string.find_lift));
            //  tipSelectorView.tipSeekBar.setProgress(extPrice);
        }

    }

    public void showDropOffCancelButton(boolean show){
        pickupDropOffView.enableDoAddrCancelButton(show);
    }

    //endregion

    public void setCard(StripeCard preferredCard) {
        infoRideView.setCcImage(preferredCard == null ? "" : preferredCard.getBrand());
        infoRideView.setCredicardNumber(preferredCard == null ? null : preferredCard.getLastdigit());

    }

    public void enableButton(boolean enable){
        confirmButton.setEnabled(enable);
        confirmButton.setBackground(ContextCompat.getDrawable(getContext(), enable ? R.drawable.green_button_selector :  R.color.gray_button));
    }

    public void showInfoRide(boolean show) {
        setPaymentMethod(!ApplicationController.getInstance().isCashMethod());
        infoRideView.setVisibility(show ? VISIBLE : GONE);
    }

    @Nullable
    public StartStopView getPickupDropOffView() {
        return pickupDropOffView;
    }

    public void setPickupDropOffView(@Nullable StartStopView pickupDropOffView) {
        this.pickupDropOffView = pickupDropOffView;
    }

    public void setPaymentMethod(boolean isCard) {
        infoRideView.cashImg.setImageResource(isCard ? R.drawable.cashgrey :  R.drawable.cashgreen);

        if(!isCard){
            infoRideView.ccImgView.setImageResource(R.drawable.cardgrey);
        }

//        if(isCard){
//            infoRideView.ccImgView.clearColorFilter();
//        }else{
//            infoRideView.ccImgView.setColorFilter(ContextCompat.getColor(getContext(),R.color.gray_button));
//        }

    }


    public interface FindLiftStatusViewListener extends StartStopView.PickUpDropoffListeners
    {
        void onCofirmButton();
        void onPassengerNumberChanged(int numPassenger);
        void onCreditCardClick();
        void onPriceChanged(int newPrice);
    }


}
