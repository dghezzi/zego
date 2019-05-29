package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.dao.CompactUser;
import it.sharethecity.mobile.letzgo.dao.StripeCard;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 15/12/16.
 */

public class PassengerInfoView extends PercentRelativeLayout {

    public static final float PERCENT_DIMEN_IMAGES = 0.23f;
    public static final float PERCENT_DIMEN_BACKGROUND = 0.20f;

    @Nullable
    @BindView(R.id.backgound_view)
    RelativeLayout backGround;

    @Nullable
    @BindView(R.id.passenger_profile_image_layout)
    RelativeLayout passengerProfileImageLayout;


    @Nullable
    @BindView(R.id.passenger_fullname_text_view)
    MyFontTextView passengerFullName;


    @Nullable
    @BindView(R.id.passenger_rating_image_view)
    ImageView passengerRating;

    @Nullable
    @BindView(R.id.tel_image_profile)
    ImageView telImage;


    @Nullable
    @BindView(R.id.passenger_profile_image_view)
    ImageView passengerProfileImageView;

    @Nullable
    @BindView(R.id.passenger_number_image_view)
    ImageView passengerNumberImageView;


    @Nullable
    @BindView(R.id.driver_eta_text_view)
    MyFontTextView driverEtaTextView;

    @Nullable
    @BindView(R.id.tipoCorsaView)
    View tipoCorsaView;

    private PassengerInfoListener listener;


    public PassengerInfoView(Context context) {
        super(context);
        init(true);
    }

    public PassengerInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public PassengerInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(false);
    }


    private void init(boolean b) {

        inflate(getContext(), R.layout.passenger_info_layout,this);
        if(b){
            ButterKnife.bind(this);
            setUi();
        }

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUi();
    }


    @Optional
    @OnClick(R.id.tel_image_profile)
    public void OnProfileImageClick(){
        if(listener != null){
            listener.onCallPassenger();
        }
    }

    private void setUi(){
        DisplayMetrics dm  = getResources().getDisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.height = (int) (dm.widthPixels * PERCENT_DIMEN_IMAGES);
        setLayoutParams(param);
        //setBackgroundColor(ContextCompat.getColor(getContext(),R.color.red_error));
        backGround.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_BACKGROUND);
        passengerProfileImageLayout.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_IMAGES);
       // passengerRatingLayout.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_IMAGES);
        passengerProfileImageView.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_IMAGES);

    }

    public void setPassengerProfileImage(String urlImage) {
        if(urlImage != null && !urlImage.isEmpty()){
            Picasso.with(getContext())
                    .load(urlImage)
                    .placeholder(R.drawable.user_placeholder)
                    .transform(new CircleTransform())
                    .resize(passengerProfileImageLayout.getLayoutParams().width,passengerProfileImageLayout.getLayoutParams().height)
                    .into(passengerProfileImageView);
        }else{
            Picasso.with(getContext())
                    .load(R.drawable.user_placeholder)
                    .transform(new CircleTransform())
                    .resize(passengerProfileImageLayout.getLayoutParams().width,passengerProfileImageLayout.getLayoutParams().height)
                    .into(passengerProfileImageView);
        }
    }

    public void setPassengerInfo(CompactUser rider) {
        if (rider == null) return;

        setPassengerProfileImage(rider.getUserimg());
        passengerFullName.setText(rider.getName());
        if(ZegoConstants.DEBUG){
            Log.d("PASSENGER_RATING:","" + rider.getRating().floatValue());
            Log.d("PASSENGER_RATING:","rounded:" + Math.round(rider.getRating().floatValue()));
        }
        passengerRating.setImageResource(ZegoConstants.ArrayImages.RATING_ARRAY[Math.round(rider.getRating().floatValue())]);
    }

    public void setPassengerNumber(Integer numpass) {
        numpass = (numpass < 0 || numpass > ZegoConstants.MAX_PASSENGER) ? 0 : numpass;
        passengerNumberImageView.setImageResource(ZegoConstants.ArrayImages.PASSENGERS_DRIVER_ARRAY[numpass]);
    }


    public void setTipoCorsa(Integer level){
        if(level != null && (level == 2 || level == 4)){
            tipoCorsaView.setVisibility(VISIBLE);
            tipoCorsaView.setBackgroundColor(ContextCompat.getColor(getContext(),level == 2 ? R.color.pink_simbolo : R.color.orange_simbolo));
        }else {
            tipoCorsaView.setVisibility(GONE);
        }
    }

    public void setEta(Integer eta) {
        driverEtaTextView.setText(etaFormatter(eta));
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

    public void setListener(PassengerInfoListener listener) {
        this.listener = listener;
    }

    @Nullable
    public ImageView getTelImage() {
        return telImage;
    }


    public interface PassengerInfoListener{
        void onCallPassenger();
    }
}
