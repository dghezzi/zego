package it.sharethecity.mobile.letzgo.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lucabellaroba on 22/11/16.
 */

public class FontManager {
    private static FontManager instance;

    private AssetManager mgr;
    private Context ctx;

    private Map<String, Typeface> fonts;

    private FontManager(Context context) {
        ctx = context;
        fonts = new HashMap<String, Typeface>();
    }

    public static void init(Context ctx) {
        instance = new FontManager(ctx);
    }

    public static FontManager getInstance(Context context) {
        if(instance == null){
            instance = new FontManager(context);
        }
        return instance;
    }

    public Typeface getFont(String asset) {
        if (fonts.containsKey(asset))
            return fonts.get(asset);

        Typeface font = null;

        try {
            font = Typeface.createFromAsset(ctx.getAssets(), "fonts/" +  asset);
            fonts.put(asset, font);
        } catch (Exception e) {

        }

        if (font == null) {
            try {
                String fixedAsset = "fonts/" + fixAssetFilename(asset);
                font = Typeface.createFromAsset(ctx.getAssets(), fixedAsset);
                fonts.put(asset, font);
                //fonts.put(fixedAsset, font);
            } catch (Exception e) {
                Log.d("FONT:",e.getMessage());
            }
        }

        return font;
    }

    private String fixAssetFilename(String asset) {
        // Empty font filename?
        // Just return it. We can't help.
        if (asset.isEmpty())
            return asset;

        // Make sure that the font ends in '.ttf' or '.ttc' or '.otc'
        if ((!asset.endsWith(".ttf")) && (!asset.endsWith(".ttc") && !asset.endsWith(".otc")))
            asset = String.format("%s.ttf", asset);

        return asset;
    }
}
