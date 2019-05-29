package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;
import me.grantland.widget.*;

/**
 * Created by lucabellaroba on 10/12/16.
 */

public class PickUpView extends PercentRelativeLayout {

    public static final float PERCENT_DIMEN_TYPE_RIDE_IMAGE = 0.19f;
    public static final float PERCENT_DIMEN_BACKGROUND = 0.16f;
    public static final float PERCENT_DIMEN_CANCEL = 0.083f;
    public static final int MAX_ETA = 30;

    @Nullable
    @BindView(R.id.backgound_view)
    RelativeLayout backGround;

    @Nullable
    @BindView(R.id.type_of_ride_layout)
    RelativeLayout typeRideLayout;

    @Nullable
    @BindView(R.id.pick_up_address_text_view)
    MyFontTextView  pickUpaddressTextView;

    @Nullable
    @BindView(R.id.eta_text_view)
    MyFontTextView etaTextView;


    @Nullable
    @BindView(R.id.type_of_ride_image_view)
    ImageView typeRideImageView;

//    @Nullable
//    @BindView(R.id.cancel_image_view)
//    ImageView cancelImageView;

    private PickUpViewListener listener;


    public PickUpView(Context context) {
        super(context);
        init(true);
    }

    public PickUpView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }


    public PickUpView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(false);

    }



    private void init(boolean b) {

        inflate(getContext(), R.layout.pick_up_layout,this);
        if(b){
            ButterKnife.bind(this);
            setUi();
        }

    }


    private void setUi(){
        DisplayMetrics dm  = getResources().getDisplayMetrics();
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);

        ViewGroup.LayoutParams param = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.height = (int) (dm.widthPixels * .26f);
        setLayoutParams(param);
        //setBackgroundColor(ContextCompat.getColor(getContext(),R.color.red_error));
        backGround.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_BACKGROUND);
        typeRideLayout.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_TYPE_RIDE_IMAGE);
      //  cancelImageView.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_CANCEL);

//        typeRideImageView.getDrawable().setLevel(1);
      //  cancelImageView.setEnabled(false);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUi();
    }

    @Optional
    @OnClick({/*R.id.cancel_image_view,*/R.id.type_of_ride_image_view,R.id.pick_up_address_text_view})
    public void onClick(View v){
        switch (v.getId()){
//            case R.id.cancel_image_view:
//                onCancelPickUpAddrClickAction();
//                break;
            case R.id.type_of_ride_image_view:
                onTypeRideClickAction();
                break;
            case R.id.pick_up_address_text_view:
                onSearchAddressClickAction();
                break;
        }
    }

    private void onSearchAddressClickAction() {
        if(listener != null){
            listener.onSearchAddress(pickUpaddressTextView.getText().toString());
        }
    }

    private void onTypeRideClickAction() {
        if(listener != null){
            listener.onTypeRideClick();
        }
    }


    public String getPickUpAddr(){
        return pickUpaddressTextView.getText().toString();
    }

    public void setPickUpAddress(String puaddr){
        pickUpaddressTextView.setText(puaddr);
    }

    private void onCancelPickUpAddrClickAction() {
        resetView();
        if(listener != null){
            listener.onCancelPickUpAddress();
        }
    }



    public void setTypeRideImage(int typeRide) {
        typeRideImageView.setImageResource(typeRide);
    }


    public void resetView(){
        pickUpaddressTextView.setText("");
        etaTextView.setText("");
        typeRideImageView.setImageResource(R.drawable.tondo_standard);
      //  enableCancelButton(false);
    }

    public void setEta(Integer eta){
        String etaFormatted = (eta/60 + 1) > MAX_ETA ? " 30+ m" : (String.format("%d min",(eta/60) + 1));
        etaTextView.setText(etaFormatted);
    }

    public void setListener(PickUpViewListener listener) {
        this.listener = listener;
    }

//    public void enableCancelButton(boolean enable){
//        cancelImageView.setEnabled(enable);
//        cancelImageView.setVisibility(enable ? VISIBLE : INVISIBLE);
//    }

//    @Nullable
//    public ImageView getCancelImageView() {
//        return cancelImageView;
//    }

    public interface PickUpViewListener{
        void onCancelPickUpAddress();
        void onTypeRideClick();
        void onSearchAddress(String currentAddress);
    }

}
