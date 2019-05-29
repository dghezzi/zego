package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;
import it.sharethecity.mobile.letzgo.R;

import static android.support.v7.appcompat.R.styleable.View;

/**
 * Created by lucabellaroba on 14/11/16.
 */

public class CustomNumberKeyBoard extends LinearLayout {

    @Nullable
    @BindView(R.id.one_text_view)
    RegularRelewayTextView oneButton;

    @Nullable
    @BindView(R.id.two_text_view)
    RegularRelewayTextView twoButton;

    @Nullable
    @BindView(R.id.three_text_view)
    RegularRelewayTextView threeButton;

    @Nullable
    @BindView(R.id.four_text_view)
    RegularRelewayTextView fourButton;

    @Nullable
    @BindView(R.id.five_text_view)
    RegularRelewayTextView fiveButton;

    @Nullable
    @BindView(R.id.siz_text_view)
    RegularRelewayTextView sixButton;

    @Nullable
    @BindView(R.id.seven_text_view)
    RegularRelewayTextView sevenButton;

    @Nullable
    @BindView(R.id.eight_text_view)
    RegularRelewayTextView eightButton;

    @Nullable
    @BindView(R.id.nine_text_view)
    RegularRelewayTextView nineButton;

    @Nullable
    @BindView(R.id.zero_text_view)
    RegularRelewayTextView zeroButton;

    @Nullable
    @BindView(R.id.cancel_button)
    RelativeLayout cancelButton;

    private onKeyBoardListener listener;

    public CustomNumberKeyBoard(Context context) {
        super(context);
        init();
    }

    public CustomNumberKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomNumberKeyBoard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomNumberKeyBoard(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setWeightSum(4);
        inflate(getContext(), R.layout.number_keyboard_layout,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
    }

    @Optional
    @OnClick({R.id.one_text_view,R.id.two_text_view,R.id.three_text_view,R.id.four_text_view,R.id.five_text_view,R.id.siz_text_view,R.id.seven_text_view,R.id.eight_text_view,R.id.nine_text_view,R.id.zero_text_view,R.id.cancel_button})
    public void onClick(View v){
        String s = "";
        switch (v.getId()){
            case R.id.one_text_view:
                s = oneButton.getText().toString();
                break;
            case R.id.two_text_view:
                s = twoButton.getText().toString();
                break;
            case R.id.three_text_view:
                s = threeButton.getText().toString();
                break;
            case R.id.four_text_view:
                s = fourButton.getText().toString();
                break;
            case R.id.five_text_view:
                s = fiveButton.getText().toString();
                break;
            case R.id.siz_text_view:
                s = sixButton.getText().toString();
                break;
            case R.id.seven_text_view:
                s = sevenButton.getText().toString();
                break;
            case R.id.eight_text_view:
                s = eightButton.getText().toString();
                break;
            case R.id.nine_text_view:
                s = nineButton.getText().toString();
                break;
            case R.id.zero_text_view:
                s = zeroButton.getText().toString();
                break;
            case R.id.cancel_button:
                break;
            default:
                break;
        }

        if(listener != null){
            if(!s.isEmpty()){
                listener.onKeyBoardClick(s);
            }else{
                listener.onCancelClick();
            }
        }
    }

    public void setListener(onKeyBoardListener listener) {
        this.listener = listener;
    }


    public interface onKeyBoardListener{
        void onKeyBoardClick(String buttonClick);
        void onCancelClick();
    }
}
