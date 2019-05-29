package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.utilities.FontManager;


/**
 * Created by lucabellaroba on 22/11/16.
 */

public class MyFontEditText extends EditText {

    public MyFontEditText(Context context) {
        super(context);
    }

    public MyFontEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MyFontEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public MyFontEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs){


        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.Font);

        if (ta != null) {
            String fontAsset = ta.getString(R.styleable.Font_customfont);

            if (!fontAsset.isEmpty()) {
                Typeface tf = FontManager.getInstance(getContext()).getFont(fontAsset);
                int style = Typeface.NORMAL;
                float size = getTextSize();

                if (getTypeface() != null)
                    style = getTypeface().getStyle();

                if (tf != null)
                    setTypeface(tf, style);
                else
                    Log.d("FontText", String.format("Could not create a font from asset: %s", fontAsset));
            }
        }
    }
}
