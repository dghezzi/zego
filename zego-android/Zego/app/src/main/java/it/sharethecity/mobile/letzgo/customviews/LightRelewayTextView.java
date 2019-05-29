package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lucabellaroba on 14/11/16.
 */

public class LightRelewayTextView extends TextView {
    public LightRelewayTextView(Context context) {
        super(context);
        init(null);
    }


    public LightRelewayTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public LightRelewayTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LightRelewayTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        String font = "fonts/Raleway-Light.ttf";
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), font);
        setTypeface(tf);
    }
}
