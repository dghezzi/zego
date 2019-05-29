package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;
import it.sharethecity.mobile.letzgo.utilities.CircleTransform;

/**
 * Created by lucabellaroba on 10/12/16.
 */

public class FeedbackView extends PercentRelativeLayout {


//    @Nullable
//    @BindView(R.id.rating_bar)
//    AppCompatRatingBar ratingBar;

    @Nullable
    @BindView(R.id.new_rating_bar)
    SimpleRatingBar ratingBar;

    @Nullable
    @BindView(R.id.backgound_view)
    RelativeLayout backGround;

    @Nullable
    @BindView(R.id.person_profile_image_layout)
    RelativeLayout personProfileImageLayout;


    @Nullable
    @BindView(R.id.person_profile_image_view)
    ImageView personProfileImageView;


    int imageDimen;


    public FeedbackView(Context context) {
        super(context);
        init(true);
    }

    public FeedbackView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }


    public FeedbackView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(false);

    }



    private void init(boolean b) {

        inflate(getContext(), R.layout.feedback_layout,this);
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
        backGround.getLayoutParams().height = (int) (dm.widthPixels * .23f);
        personProfileImageLayout.getLayoutParams().height = (int) (dm.widthPixels * .26f);


        imageDimen = (int) (dm.widthPixels * .26f);
        personProfileImageView.getLayoutParams().height = imageDimen;
        personProfileImageView.getLayoutParams().width = imageDimen;

        ratingBar.setRating(5);

//        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
//                if(b){
//                    if(ratingBar.getRating() < 1){
//                        ratingBar.setRating(1);
//                    }
//                }
//            }
//        });
        ratingBar.setOnRatingBarChangeListener(new SimpleRatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(SimpleRatingBar simpleRatingBar, float rating, boolean fromUser) {
                if(fromUser){
                    if(ratingBar.getRating() < 1){
                        ratingBar.setRating(1);
                    }
                }
            }
        });
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        setUi();
    }




    public int getRatingBar() {
        return (int) ratingBar.getRating();
    }

    public void setRating(Integer rating) {
        ratingBar.setRating(rating.floatValue());
    }




    public void setPersonProfileImage(String urlImage) {
        if(urlImage != null && !urlImage.isEmpty()){
            Picasso.with(getContext())
                    .load(urlImage)
                    .transform(new CircleTransform())
                    .placeholder(R.drawable.user_placeholder)
                    .resize(imageDimen,imageDimen)
                    .into(personProfileImageView);
        }else{
            Picasso.with(getContext())
                    .load(R.drawable.user_placeholder)
                    .transform(new CircleTransform())
                    .resize(imageDimen,imageDimen)
                    .into(personProfileImageView);
        }
    }



    public void resetView() {
        ratingBar.setRating(5);
    }
}
