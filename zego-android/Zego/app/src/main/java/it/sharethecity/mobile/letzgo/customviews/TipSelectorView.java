package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.SeekBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sharethecity.mobile.letzgo.R;

/**
 * Created by lucabellaroba on 10/12/16.
 */

public class TipSelectorView extends PercentRelativeLayout implements SeekBar.OnSeekBarChangeListener {


    @Nullable
    @BindView(R.id.tip_seek_bar)
    SeekBar tipSeekBar;

    @Nullable
    @BindView(R.id.price_text_view)
    MyFontTextView priceTextView;

    private int suggestedPrice;
    Integer rideMinCost;

    private TipSelectorListener listener;

    public TipSelectorView(Context context) {
        super(context);
        init(true);
    }

    public TipSelectorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public TipSelectorView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(false);
    }

    private void init(boolean b) {

        inflate(getContext(), R.layout.tip_selector_layout,this);
        setBackground(ContextCompat.getDrawable(getContext(),R.drawable.gray_rounded_corners_rect_with_stroke));
        if(b){
            ButterKnife.bind(this);
            setUi();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        tipSeekBar.setOnSeekBarChangeListener(this);
    }



    private void setUi() {

    }



    public void resetView(){

        try{
            tipSeekBar.setProgress(0);
        }catch (NumberFormatException ex){
        }

    }


    private SpannableString priceFormatter(Integer priceInteger, Integer driverfee){
        if(priceInteger == null) priceInteger = 0;

        String s = String.format("%.2f",(priceInteger)/100f) + " €";
        s = s.replace(".",",");
        s += " " + getResources().getString(R.string.fee_zego_included);//.replace("{XX}",String.format("%.2f",(driverfee)/100f));
        SpannableString spannableString = new SpannableString(s);
        int decimalIndex = s.indexOf(",");
        int currencyIndex = s.indexOf("€");

        spannableString.setSpan(new RelativeSizeSpan(1.7f), 0, decimalIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new StyleSpan(Typeface.BOLD), 0, currencyIndex + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(1.2f), currencyIndex, s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.black_text)), currencyIndex + 1,s.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new RelativeSizeSpan(0.7f), currencyIndex + 1,s.length() , Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return spannableString;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

//        if(rideMinCost == null || rideMinCost == 0){
//            readMinimunPrice();
//        }

        if(b){
//            int newPrice = suggestedPrice;
//            if(seekBar.getProgress() < rideMinCost){
//                seekBar.setProgress(rideMinCost);
//                Toast.makeText(getContext(),getResources().getString(R.string.min_price_alert),Toast.LENGTH_SHORT).show();
//            }
//            newPrice = 50*((seekBar.getProgress() - 25)/50) + 50;
//            if(ZegoConstants.DEBUG){
//                Log.d("price:","" + newPrice);
//            }
          //  priceTextView.setText(priceFormatter(newPrice));
            if(listener != null){
                listener.onPriceChanged(tipSeekBar);
            }
        }

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if(listener != null){
            listener.onStopPriceChanged(tipSeekBar);
        }

    }

    public void setListener(TipSelectorListener listener) {
        this.listener = listener;
    }

    public int getSuggestedPrice() {
        return suggestedPrice;
    }

    public void setSuggestedPrice(int suggestedPrice,int maxValueSlider,int driverfee) {
        this.suggestedPrice = suggestedPrice;
        tipSeekBar.setMax(maxValueSlider);
        priceTextView.setText(priceFormatter(suggestedPrice,driverfee));
        tipSeekBar.setProgress(suggestedPrice);
    }

    public void setPriceTextView(Integer value, Integer driverfee){
        priceTextView.setText(priceFormatter(value, driverfee));
    }

    public void setSimplePriceTextView(SpannableString s){
        priceTextView.setText(s);
    }

    public interface TipSelectorListener{
        void onPriceChanged(SeekBar seekBar);
        void onStopPriceChanged(SeekBar seekBar);
    }
}
