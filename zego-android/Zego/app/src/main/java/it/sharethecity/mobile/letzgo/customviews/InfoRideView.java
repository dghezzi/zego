package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLngBounds;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.utilities.UtilityFunctions;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 11/12/16.
 */

public class InfoRideView extends LinearLayout {




    @Nullable
    @BindView(R.id.estimated_price_layout)
    PercentRelativeLayout estimatedPriceButton;

    @Nullable
    @BindView(R.id.passengers_layout)
    PercentRelativeLayout passengersButton;

    @Nullable
    @BindView(R.id.cc_layout)
    PercentRelativeLayout ccButton;

    @Nullable
    @BindView(R.id.cc_number_text_view)
    MyFontTextView ccNumberTextView;

    @Nullable
    @BindView(R.id.estimated_price_text_view)
    MyFontTextView estimatedPriceTextView;

    @Nullable
    @BindView(R.id.original_price_text_view)
    MyFontTextView originalPriceTextView;

    @Nullable
    @BindView(R.id.passengers_image_view)
    ImageView passengersImgView;

    @Nullable
    @BindView(R.id.cc_image_view)
    ImageView ccImgView;

    @Nullable
    @BindView(R.id.money_image_view)
    ImageView cashImg;

    private int passengerNumber = 0;
    private InfoRideViewListener listener;


    public InfoRideView(Context context) {
        super(context);
        init(true);
    }

    public InfoRideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public InfoRideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public InfoRideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(false);
    }


    private void init(boolean b) {
        setWeightSum(3);
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.WHITE);
        inflate(getContext(), R.layout.info_ride_layout,this);
        if(b){
            ButterKnife.bind(this);
            setUI();
        }

    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUI();
    }

    private void setUI(){
        passengersImgView.setImageResource(ZegoConstants.ArrayImages.PASSENGERS_ARRAY[passengerNumber]);
    }




    @Optional
    @OnClick({R.id.cc_layout,R.id.passengers_layout,R.id.estimated_price_layout})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.cc_layout:
                onCcClickAction();
                break;
            case R.id.passengers_layout:
                onPassengersClickAction();
                break;
            case R.id.estimated_price_layout:
                onEstimatedPriceClickAction();
                break;
        }
    }

    private void onCcClickAction() {
        if(listener != null){
            listener.onCcClick();
        }
    }

    private void onPassengersClickAction() {
        setPassengerNumber(passengerNumber + 1);
        if(listener != null){
            listener.onPassengerClick(passengerNumber);
        }
    }

    private void onEstimatedPriceClickAction() {
        if(listener != null){
            listener.onEstimatedPriceClick();
        }
    }



    public void setPassengerNumber(int number){
        passengerNumber = (number < 0 || number > ZegoConstants.MAX_PASSENGER) ? 0 : number;
        passengersImgView.setImageResource(ZegoConstants.ArrayImages.PASSENGERS_ARRAY[passengerNumber]);
    }

    public void setCcImage(String cardBrand){
        ccImgView.setImageResource(UtilityFunctions.getCardImageByBrand(cardBrand));
    }


    public void setEstimatedPrice(Integer estimatePrice){
        estimatedPriceTextView.setText(priceFormatter(estimatePrice));
    }

    private SpannableString priceFormatter(Integer priceInteger){
        if(priceInteger == null) priceInteger = 0;

        String s = String.format("%.2f",(priceInteger)/100f) + " €";
        s = s.replace(".",",");
        SpannableString spannableString = new SpannableString(s);
        int decimalIndex = s.indexOf(",");
        int currencyIndex = s.indexOf("€");

        spannableString.setSpan(new RelativeSizeSpan(1.7f), 0, decimalIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, decimalIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), currencyIndex, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }

    private SpannableString originalPriceFormat(Integer value){
        if(value == null) return new SpannableString("");

        SpannableString spannableString = priceFormatter(value);
        spannableString.setSpan(new StrikethroughSpan(), 0, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    public void setListener(InfoRideViewListener listener) {
        this.listener = listener;
    }

    public void resetView() {
        passengerNumber = 0;
        setPassengerNumber(passengerNumber);
        estimatedPriceTextView.setText(priceFormatter(0));
//        setOriginalPrice(0);
    }

    public void resetEstimatedPrice(){
        estimatedPriceTextView.setText(priceFormatter(0));
    }

    public void setCredicardNumber(String lastdigit) {
        ccNumberTextView.setText(lastdigit == null ? "-" : "****" + lastdigit);
    }

    public void setOriginalPrice(Integer value){
        originalPriceTextView.setVisibility(value == null || value == 0 ? GONE : VISIBLE);
        originalPriceTextView.setText(originalPriceFormat(value));
    }

    public void showTextPriceModified(boolean show){
        originalPriceTextView.setVisibility(show ? VISIBLE : GONE);
    }

    public interface InfoRideViewListener{
        void onPassengerClick(int passengerNumber);
        void onCcClick();
        void onEstimatedPriceClick();
    }

}
