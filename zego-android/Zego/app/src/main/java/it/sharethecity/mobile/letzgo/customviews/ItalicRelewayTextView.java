package it.sharethecity.mobile.letzgo.customviews;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lucabellaroba on 14/11/16.
 */

public class ItalicRelewayTextView extends TextView {
    public ItalicRelewayTextView(Context context) {
        super(context);
        init(null);
    }


    public ItalicRelewayTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public ItalicRelewayTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ItalicRelewayTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }


    private void init(AttributeSet attrs) {
        String font = "fonts/Raleway-Italic.ttf";
        Typeface tf = Typeface.createFromAsset(getResources().getAssets(), font);
        setTypeface(tf);
    }
}
