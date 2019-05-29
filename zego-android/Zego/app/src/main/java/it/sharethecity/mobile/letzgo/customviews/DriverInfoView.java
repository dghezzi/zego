package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.dao.CompactDriver;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;
import it.sharethecity.mobile.letzgo.utilities.ZegoConstants;

/**
 * Created by lucabellaroba on 10/12/16.
 */

public class DriverInfoView extends PercentRelativeLayout {


    public static final float PERCENT_DIMEN_IMAGES = 0.23f;
    public static final float PERCENT_DIMEN_BACKGROUND = 0.20f;

    @Nullable
    @BindView(R.id.backgound_view)
    RelativeLayout backGround;

    @Nullable
    @BindView(R.id.driver_profile_image_layout)
    RelativeLayout driverProfileImageLayout;

    @Nullable
    @BindView(R.id.driver_car_image_layout)
    RelativeLayout driverCarImageLayout;

    @Nullable
    @BindView(R.id.car_info_text_view)
    MyFontTextView drivercarInfoTextView;

    @Nullable
    @BindView(R.id.driver_rating_image_view)
    ImageView driverRating;


    @Nullable
    @BindView(R.id.driver_profile_image_view)
    ImageView driverProfileImageView;

    @Nullable
    @BindView(R.id.driver_car_image_view)
    ImageView driverCarImageView;


    public DriverInfoView(Context context) {
        super(context);
        init(true);
    }

    public DriverInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }


    public DriverInfoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(false);

    }



    private void init(boolean b) {

        inflate(getContext(), R.layout.driver_info_layout,this);
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
        param.height = (int) (dm.widthPixels * PERCENT_DIMEN_IMAGES);
        setLayoutParams(param);
        //setBackgroundColor(ContextCompat.getColor(getContext(),R.color.red_error));
        backGround.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_BACKGROUND);
        driverProfileImageLayout.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_IMAGES);
        driverCarImageLayout.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_IMAGES);
        driverProfileImageView.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_IMAGES);
        driverCarImageView.getLayoutParams().height = (int) (dm.widthPixels * PERCENT_DIMEN_IMAGES);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUi();
    }



    public void setRating(Integer rating) {
        if(ZegoConstants.DEBUG){
            Log.d("DRIVER_RATING:","rating:" + rating + " intRating:" + rating.intValue());
        }

        driverRating.setImageResource(ZegoConstants.ArrayImages.RATING_ARRAY[rating]);
        //driverRating.getDrawable().setLevel(rating.intValue());
    }



    public void setCarInfo(String carInfo) {drivercarInfoTextView.setText(carInfo);
    }


    public void setCarProfileImage(String urlImage) {
        if(urlImage != null && !urlImage.isEmpty()){
            Picasso.with(getContext())
                    .load(urlImage)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.car_placeholder)
                    .resize(driverCarImageView.getLayoutParams().height,driverCarImageView.getLayoutParams().height)
                    .into(driverCarImageView);
        }else{
            Picasso.with(getContext())
                    .load(R.drawable.car_placeholder)
                    .transform(new CircleTransform())
                    .resize(driverCarImageView.getLayoutParams().height,driverCarImageView.getLayoutParams().height)
                    .into(driverCarImageView);
        }
    }

    public void setDriverProfileImage(String urlImage) {
        if(urlImage != null && !urlImage.isEmpty()){
            Picasso.with(getContext())
                    .load(urlImage)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.user_placeholder)
                    .resize(driverProfileImageLayout.getLayoutParams().height ,driverProfileImageLayout.getLayoutParams().height )
                    .into(driverProfileImageView);
        }else{
            Picasso.with(getContext())
                    .load(R.drawable.user_placeholder)
                    .transform(new CircleTransform())
                    .resize(driverProfileImageLayout.getLayoutParams().height ,driverProfileImageLayout.getLayoutParams().height )
                    .into(driverProfileImageView);
        }
    }


    public void setDriverInfo(CompactDriver driver) {
        if(ZegoConstants.DEBUG){
            Log.d("RATING:",""+ driver.getRating().intValue());
            Log.d("RATING:","rounded:"+ Math.round(driver.getRating().floatValue()));
        }
        setRating(driver.getRating() > 5d ? 5 :Math.round(driver.getRating().floatValue()));
        setCarProfileImage(driver.getCarimg());
        setDriverProfileImage(driver.getUserimg());
        drivercarInfoTextView.setText(driver.getModel() + " " + driver.getColor());

    }
}
