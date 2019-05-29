package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.utilities.FontManager;
import me.grantland.widget.*;


/**
 * Created by lucabellaroba on 22/11/16.
 */

public class MyFontTextView extends me.grantland.widget.AutofitTextView {
    public MyFontTextView(Context context) {
        this(context, null);
    }

    public MyFontTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (isInEditMode())
            return;

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.Font);

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
