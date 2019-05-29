package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sharethecity.mobile.letzgo.R;

/**
 * Created by lucabellaroba on 11/12/16.
 */

public class MyFakeToastView extends RelativeLayout {


    @Nullable
    @BindView(R.id.fake_toast_image)
    public ImageView fakeToastImg;

    @Nullable
    @BindView(R.id.msg_text_view)
    public MyFontTextView fakeToastmsg;


    public MyFakeToastView(Context context) {
        super(context);
        init(true);
    }


    public MyFakeToastView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(false);
    }

    public MyFakeToastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyFakeToastView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(false);
    }


    private void init(boolean b) {
        inflate(getContext(), R.layout.fake_toast_layout,this);
        if(b){
            ButterKnife.bind(this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    public void setFakeToastImg(int resource){
        fakeToastImg.setImageResource(resource);
    }

    public void hideImage(){
        fakeToastImg.setVisibility(GONE);
    }

    public String getMsg(){
        return fakeToastmsg.getText().toString();
    }

    public void setFakeMsg(String msg){
        msg = msg == null ? "" : msg;
        fakeToastmsg.setText(msg);
    }
}
