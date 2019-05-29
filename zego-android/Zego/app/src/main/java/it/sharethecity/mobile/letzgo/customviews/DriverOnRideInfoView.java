package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;

/**
 * Created by lucabellaroba on 28/12/16.
 */

public class DriverOnRideInfoView extends LinearLayout {

    @Nullable
    @BindView(R.id.ride_price_text_view)
    MyFontTextView priceTextView;

    @Nullable
    @BindView(R.id.trip_length_text_view)
    MyFontTextView tripLengthTextView;

    @Nullable
    @BindView(R.id.ride_price_label_text_view)
    MyFontTextView priceRideLabelTextView;

    @Nullable
    @BindView(R.id.accepted_price_layout)
    LinearLayout accpetedPriceButton;

    @Nullable
    @BindView(R.id.paymentMethodImg)
    ImageView paymentMethodImg;

    private DriverOnRideInfoRideListener listener;

    public DriverOnRideInfoView(Context context) {
        super(context);
        init(true);
    }

    public DriverOnRideInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public DriverOnRideInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DriverOnRideInfoView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(false);
    }


    private void  init(boolean b){
        setWeightSum(3);
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.WHITE);
        inflate(getContext(), R.layout.driver_on_ride_info_view,this);
        if(b){
            ButterKnife.bind(this);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }


    @Optional
    @OnClick(R.id.accepted_price_layout)
    public void onAcceptedPriceClick(){
        if(listener != null){
            listener.onAcceptedPriceClick();
        }
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

    private SpannableString lengthFormatter(Integer lengthInteger){
        if(lengthInteger == null) lengthInteger = 0;

        String s = String.format("%.1f",(lengthInteger)/1000f) + " km";
        s = s.replace(".",",");
        SpannableString spannableString = new SpannableString(s);
        int decimalIndex = s.indexOf(",");
        int currencyIndex = s.indexOf("km");

        spannableString.setSpan(new RelativeSizeSpan(1.7f), 0, decimalIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, decimalIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), decimalIndex, currencyIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }



    public void setPriceTextView(Integer price){
        priceTextView.setText(priceFormatter(price));
    }

    public void setTripLength(Integer length){
        tripLengthTextView.setText(lengthFormatter(length));
    }

    public void setContent(Riderequest riderequest) {
        setTripLength(riderequest.getExtmeters());
        setPriceTextView(riderequest.getDriverfee() + riderequest.getZegofee());
        paymentMethodImg.setImageResource(riderequest.isCard() ? R.drawable.cardgreen : R.drawable.cashgreen);
    }

    public void setListener(DriverOnRideInfoRideListener listener) {
        this.listener = listener;
    }

    public void setPriceLabel(String text) {
        priceRideLabelTextView.setText(text);
    }

    public void resetView() {
        priceRideLabelTextView.setText(getResources().getString(R.string.price_accepted));
        priceTextView.setText("");
        tripLengthTextView.setText("");
    }


    public interface DriverOnRideInfoRideListener{
        void onAcceptedPriceClick();
    }
}
