package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;

/**
 * Created by lucabellaroba on 12/12/16.
 */

public class FeedbackStatusView extends RelativeLayout {

    @Nullable
    @BindView(R.id.feedBack_view)
    FeedbackView feedbackView;

    @Nullable
    @BindView(R.id.send_feedback_button)
    Button sendFeedbackButton;

    private FeedbackStatusViewListener listener;

    public FeedbackStatusView(Context context) {
        super(context);
        init(true);
    }

    public FeedbackStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public FeedbackStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FeedbackStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(false);
    }


    private void  init(boolean b){
        inflate(getContext(), R.layout.feedback_status_view,this);
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
    @OnClick(R.id.send_feedback_button)
    public void onSendFeedbackButtonClick(){
        if(listener!= null){
            listener.onSendFeedback(feedbackView.getRatingBar());
        }
    }

    public FeedbackStatusViewListener getListener() {
        return listener;
    }

    public void setListener(FeedbackStatusViewListener listener) {
        this.listener = listener;
    }

    public interface FeedbackStatusViewListener{
        void onSendFeedback(int rate);
    }

    public void resetView(){
        feedbackView.resetView();
    }

    public void setContent(String sender,String urlImage,boolean isPassenger){
        feedbackView.setPersonProfileImage(urlImage);
    }
}
