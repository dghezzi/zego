package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.percent.PercentRelativeLayout;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;

/**
 * Created by lucabellaroba on 13/12/16.
 */

public class RideRequestSentView extends PercentRelativeLayout {

    @Nullable
    @BindView(R.id.cancel_request_button)
    RelativeLayout cancelRequestButton;


    private CancelRequestListener listener;

    public RideRequestSentView(Context context) {
        super(context);
        init(true);
    }


    public RideRequestSentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public RideRequestSentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }



    private void init(boolean b) {

        inflate(getContext(), R.layout.ride_request_sent_layout,this);
        if(b){
            ButterKnife.bind(this);
           // setUi();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
      //  setUi();
    }


    @Optional
    @OnClick(R.id.cancel_request_button)
    public void onCancelRequestClick(){
        if(listener != null){
            listener.onCancelRequestClick();
        }
    }

    public void setListener(CancelRequestListener listener) {
        this.listener = listener;
    }

    public interface CancelRequestListener{
        void onCancelRequestClick();
    }

}
