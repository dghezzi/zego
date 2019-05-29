package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sharethecity.mobile.letzgo.R;

/**
 * Created by lucabellaroba on 16/12/16.
 */

public class NoGpsView extends RelativeLayout {

    @Nullable
    @BindView(R.id.no_gps_toast)
    MyFakeToastView noGpsToast;

    public NoGpsView(Context context) {
        super(context);
        init(true);
    }

    public NoGpsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public NoGpsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NoGpsView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(false);
    }

    private void init(boolean b) {

        inflate(getContext(), R.layout.no_gps_layout,this);
        setBackgroundColor(ContextCompat.getColor(getContext(),R.color.black_no_gps_background));
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
        noGpsToast.setFakeToastImg(R.drawable.nogps);
        noGpsToast.setFakeMsg(getResources().getString(R.string.no_gps));
    }

    public void show(boolean isGpsAvailable) {
        setVisibility(isGpsAvailable ? GONE : VISIBLE);
    }
}
