package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;

/**
 * Created by lucabellaroba on 14/11/16.
 */

public class CellNumber extends LinearLayout {

    @Nullable
    @BindView(R.id.number_text_view)
    RegularRelewayTextView numberTextView;

    @Nullable
    @BindView(R.id.cell_relative)
    RelativeLayout cellRelative;

    public CellNumber(Context context) {
        super(context);
        init();
    }

    public CellNumber(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CellNumber(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CellNumber(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        setOrientation(VERTICAL);
        inflate(getContext(), R.layout.cell_number_layout,this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        ButterKnife.bind(this);
        cellRelative.getLayoutParams().width = (int) (0.092f * ApplicationController.getInstance().getScreenDimension().widthPixels);
       // cellRelative.getLayoutParams().height = (int) (0.070f * ApplicationController.getInstance().getScreenDimension().heightPixels);
    }


    public void setText(String text){
        numberTextView.setText(text==null ? "" : text);
    }

    public String getText(){
        return numberTextView.getText().toString();
    }

    public void setTextColor(int color){
        numberTextView.setTextColor(color);
    }
}
