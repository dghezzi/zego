package it.sharethecity.mobile.letzgo.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.albinmathew.photocrop.cropoverlay.edge.Edge;
import com.albinmathew.photocrop.cropoverlay.utils.PaintUtil;
import com.albinmathew.photocrop.photoview.PhotoViewAttacher;

import it.sharethecity.mobile.letzgo.R;
import it.sharethecity.mobile.letzgo.application.ApplicationController;


/**
 * Created by lucabellaroba on 26/02/16.
 */


/**
 * @author albin
 *
 *  Modified/stripped down Code from cropper library : https://github.com/edmodo/cropper
 */
public class MyCropOverlayView extends View implements PhotoViewAttacher.IGetImageBounds {

    private static final float CORNER_RADIUS = 0;
    private boolean DEFAULT_GUIDELINES = false;
    private boolean DEFAULT_DRAW_CIRCLE = false;
    private int DEFAULT_MARGINTOP = 100;
    private int DEFAULT_MARGINSIDE = 50;
    private int DEFAULT_BOARDER_COLOR = 0xFFFFFFFF;
    private int DEFAULT_BACKGROUND_COLOR = 0xB029303F;
    // we are croping square image so width and height will always be equal
    private int DEFAULT_CROPWIDTH = 600;
    // The Paint used to darken the surrounding areas outside the crop area.
    private Paint mBackgroundPaint;

    // The Paint used to draw the white rectangle around the crop area.
    private Paint mBorderPaint;

    // The Paint used to draw the guidelines within the crop area.
    private Paint mGuidelinePaint;

    // The bounding box around the Bitmap that we are cropping.
    private Rect mBitmapRect;

    private int cropHeight = DEFAULT_CROPWIDTH;
    private int cropWidth = DEFAULT_CROPWIDTH;


    private boolean mGuidelines;
    private boolean mDrawCircle;
    private int mMarginTop;
    private int mMarginSide;
    private int mBorderPaintColor;
    private int mBackgroundColor;
    private Context mContext;
    private Path clipPath;
    private RectF rectF;

    public MyCropOverlayView(Context context) {
        super(context);
        init(context);
        this.mContext = context;
    }

    public MyCropOverlayView(Context context, int side, boolean isProfileImage) {
        super(context);
        mMarginSide = side;
        mMarginTop = ApplicationController.getInstance().getScreenDimension().heightPixels/2 - this.cropHeight/2;
        mGuidelines = false;
        mDrawCircle = isProfileImage;
        mBorderPaintColor = getResources().getColor(R.color.gray_text);
        mBackgroundColor = getResources().getColor(R.color.transparent_black_app);
        init(context);
        this.mContext = context;
    }

    public MyCropOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, com.albinmathew.photocrop.R.styleable.CropOverlayView, 0, 0);
        try {
            mGuidelines = ta.getBoolean(com.albinmathew.photocrop.R.styleable.CropOverlayView_guideLines, DEFAULT_GUIDELINES);
            mDrawCircle = ta.getBoolean(com.albinmathew.photocrop.R.styleable.CropOverlayView_drawCircle, DEFAULT_DRAW_CIRCLE);
            mMarginTop = ta.getDimensionPixelSize(com.albinmathew.photocrop.R.styleable.CropOverlayView_marginTop, DEFAULT_MARGINTOP);
            mMarginSide = ta.getDimensionPixelSize(com.albinmathew.photocrop.R.styleable.CropOverlayView_marginSide, DEFAULT_MARGINSIDE);
            mBorderPaintColor = ta.getColor(com.albinmathew.photocrop.R.styleable.CropOverlayView_borderColor, DEFAULT_BOARDER_COLOR);
            mBackgroundColor = ta.getColor(com.albinmathew.photocrop.R.styleable.CropOverlayView_overlayColor, DEFAULT_BACKGROUND_COLOR);
        } finally {
            ta.recycle();
        }

        init(context);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        if(mDrawCircle) {
            float cx = (Edge.LEFT.getCoordinate() + Edge.RIGHT.getCoordinate()) / 2;
            float cy = (Edge.TOP.getCoordinate() + Edge.BOTTOM.getCoordinate()) / 2;
            float radius2 = (Edge.RIGHT.getCoordinate() - Edge.LEFT.getCoordinate()) / 2;
            clipPath.addCircle(cx, cy, radius2, Path.Direction.CW);
            canvas.clipPath(clipPath, Region.Op.DIFFERENCE);
            canvas.drawColor(mBackgroundColor);
            if(Build.VERSION.SDK_INT>=23){
            }else{
                canvas.restore();
            }
            canvas.drawCircle(cx, cy, radius2, mBorderPaint);
        }else {
            final float radius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CORNER_RADIUS, mContext.getResources().getDisplayMetrics());
            clipPath.addRoundRect(rectF, radius, radius, Path.Direction.CW);
            canvas.clipPath(clipPath, Region.Op.DIFFERENCE);
            canvas.drawColor(mBackgroundColor);
            if(Build.VERSION.SDK_INT>=23){
            }else{
                canvas.restore();
            }
            canvas.drawRoundRect(rectF, radius, radius, mBorderPaint);
        }
        if(mGuidelines) {
            drawRuleOfThirdsGuidelines(canvas);
        }
    }

    @Override
    public Rect getImageBounds() {
        return new Rect((int) Edge.LEFT.getCoordinate(), (int) Edge.TOP.getCoordinate(), (int) Edge.RIGHT.getCoordinate(), (int) Edge.BOTTOM.getCoordinate());
    }


    private void init(Context context) {
        clipPath = new Path();
        int w = context.getResources().getDisplayMetrics().widthPixels;
        cropWidth = w - 2 * mMarginSide;
        cropHeight = cropWidth;
        int edgeT = mMarginTop;
        int edgeB = mMarginTop + cropHeight;
        int edgeL = mMarginSide;
        int edgeR = mMarginSide + cropWidth;
        mBackgroundPaint = PaintUtil.newBackgroundPaint(context);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBorderPaint = PaintUtil.newBorderPaint(context);
        mBorderPaint.setColor(mBorderPaintColor);
        mGuidelinePaint = PaintUtil.newGuidelinePaint();
        Edge.TOP.setCoordinate(edgeT);
        Edge.BOTTOM.setCoordinate(edgeB);
        Edge.LEFT.setCoordinate(edgeL);
        Edge.RIGHT.setCoordinate(edgeR);
        new Rect(edgeL, edgeT, edgeR, edgeB);
        mBitmapRect = new Rect(0, 0, w, w);
        rectF = new RectF(Edge.LEFT.getCoordinate(),
                Edge.TOP.getCoordinate(),
                Edge.RIGHT.getCoordinate(),
                Edge.BOTTOM.getCoordinate());
    }


    private void drawRuleOfThirdsGuidelines(Canvas canvas) {

        final float left = Edge.LEFT.getCoordinate();
        final float top = Edge.TOP.getCoordinate();
        final float right = Edge.RIGHT.getCoordinate();
        final float bottom = Edge.BOTTOM.getCoordinate();

        // Draw vertical guidelines.
        final float oneThirdCropWidth = Edge.getWidth() / 3;

        final float x1 = left + oneThirdCropWidth;
        canvas.drawLine(x1, top, x1, bottom, mGuidelinePaint);
        final float x2 = right - oneThirdCropWidth;
        canvas.drawLine(x2, top, x2, bottom, mGuidelinePaint);

        // Draw horizontal guidelines.
        final float oneThirdCropHeight = Edge.getHeight() / 3;

        final float y1 = top + oneThirdCropHeight;
        canvas.drawLine(left, y1, right, y1, mGuidelinePaint);
        final float y2 = bottom - oneThirdCropHeight;
        canvas.drawLine(left, y2, right, y2, mGuidelinePaint);
    }

    public int getmMarginTop() {
        return mMarginTop;
    }

    public void setmMarginTop(int mMarginTop) {
        this.mMarginTop = mMarginTop;
    }

    public int getmMarginSide() {
        return mMarginSide;
    }

    public void setmMarginSide(int mMarginSide) {
        this.mMarginSide = mMarginSide;
    }
}
