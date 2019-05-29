package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.network.request.Riderequest;

/**
 * Created by lucabellaroba on 15/12/16.
 */

public class DriverInfoRideView extends LinearLayout {


    @Nullable
    @BindView(R.id.ride_price_text_view)
    MyFontTextView priceTextView;

    @Nullable
    @BindView(R.id.trip_length_text_view)
    MyFontTextView tripLengthTextView;

    @Nullable
    @BindView(R.id.eta_text_view)
    MyFontTextView etaTextView;

    public DriverInfoRideView(Context context) {
        super(context);
        init(true);
    }

    public DriverInfoRideView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public DriverInfoRideView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DriverInfoRideView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(false);
    }


    private void  init(boolean b){
        setWeightSum(3);
        setOrientation(HORIZONTAL);
        setBackgroundColor(Color.WHITE);
        inflate(getContext(), R.layout.driver_info_ride_view,this);
        if(b){
            ButterKnife.bind(this);
        }
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    private SpannableString etaFormatter(Integer etaInteger){
        if(etaInteger == null) etaInteger = 1;

        String s = String.format("%d",((etaInteger)/60 + 1)) + " min";
        SpannableString spannableString = new SpannableString(s);

        int minuteIndex = s.indexOf(" ");

        spannableString.setSpan(new RelativeSizeSpan(1.7f), 0, minuteIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, minuteIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), minuteIndex, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
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


    public void setEta(Integer eta){
        etaTextView.setText(etaFormatter(eta));
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
//        setEta(riderequest.getExtsecond());
        setEta(riderequest.getDrivereta());
    }
}
