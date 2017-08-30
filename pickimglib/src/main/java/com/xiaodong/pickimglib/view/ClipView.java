package com.xiaodong.pickimglib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 */
public class ClipView extends View {
    private Paint paint = new Paint();
    private Paint borderPaint = new Paint();
    private float mHorizontalPadding;
    private int clipBorderWidth;
    private int clipRadiusWidth;
    private int clipWidth;
    private ClipType clipType = ClipType.CIRCLE;
    private Xfermode xfermode;

    public ClipView(Context context) {
        this(context, null);
    }

    public ClipView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint.setAntiAlias(true);
        borderPaint.setStyle(Style.STROKE);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setStrokeWidth(clipBorderWidth);
        borderPaint.setAntiAlias(true);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        canvas.saveLayer(0, 0, this.getWidth(), this.getHeight(), null, LAYER_FLAGS);
        canvas.drawColor(Color.parseColor("#a8000000"));
        paint.setXfermode(xfermode);
        if (clipType == ClipType.CIRCLE) {
            canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, clipRadiusWidth, paint);
            canvas.drawCircle(this.getWidth() / 2, this.getHeight() / 2, clipRadiusWidth, borderPaint);
        } else if (clipType == ClipType.RECTANGLE) {
            canvas.drawRect(mHorizontalPadding, this.getHeight() / 2 - clipWidth / 2,
                    this.getWidth() - mHorizontalPadding, this.getHeight() / 2 + clipWidth / 2, paint);
            canvas.drawRect(mHorizontalPadding, this.getHeight() / 2 - clipWidth / 2,
                    this.getWidth() - mHorizontalPadding, this.getHeight() / 2 + clipWidth / 2, borderPaint);
        }
        canvas.restore();
    }

    /**
     *
     * @return
     */
    public Rect getClipRect() {
        Rect rect = new Rect();
        rect.left = (this.getWidth() / 2 - clipRadiusWidth);
        rect.right = (this.getWidth() / 2 + clipRadiusWidth);
        rect.top = (this.getHeight() / 2 - clipRadiusWidth);
        rect.bottom = (this.getHeight() / 2 + clipRadiusWidth);
        return rect;
    }

    /**
     *
     * @param clipBorderWidth
     */
    public void setClipBorderWidth(int clipBorderWidth) {
        this.clipBorderWidth = clipBorderWidth;
        borderPaint.setStrokeWidth(clipBorderWidth);
        invalidate();
    }

    /**
     *
     * @param mHorizontalPadding
     */
    public void setmHorizontalPadding(float mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
        this.clipRadiusWidth = (int) (getScreenWidth(getContext()) - 2 * mHorizontalPadding) / 2;
        this.clipWidth = clipRadiusWidth * 2;
    }

    /**
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }


    /**
     *
     * @param clipType
     */
    public void setClipType(ClipType clipType) {
        this.clipType = clipType;
    }

    /**
     */
    public enum ClipType {
        CIRCLE, RECTANGLE
    }
}
