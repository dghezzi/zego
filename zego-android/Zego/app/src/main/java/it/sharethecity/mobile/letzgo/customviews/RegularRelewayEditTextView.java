package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by lucabellaroba on 14/11/16.
 */

public class RegularRelewayEditTextView extends EditText {
    public RegularRelewayEditTextView(Context context) {
        super(context);
        init(null);
    }


    public RegularRelewayEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RegularRelewayEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RegularRelewayEditTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        String font = "fonts/Raleway-Regular.ttf";
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), font);
        setTypeface(tf);
    }
}
