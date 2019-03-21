package com.xiaodong.pickimglib.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.xiaodong.pickimglib.R;

public class ClipViewFreedom extends View implements ClipViewInterface{
    private String TAG = getClass().getName();

    private Paint paint = new Paint();
    private Paint borderPaint = new Paint();
    private float mHorizontalPadding;//内边距
    private int clipBorderWidth;//边框宽度
    private Rect rect = new Rect();//裁剪框形状
    private int clipRadiusWidth;//裁剪框宽半径
    private int clipRadiusHeight;//裁剪框高半径
    private int clipWidth;//裁剪框kuan
    private int lineMoveMode = 0;//0不可以移动,1可以移动左边，2可以移动上边 3可以移动右边 4可以移动下边
    private Xfermode xfermode;


    public ClipViewFreedom(Context context) {
        this(context,null);
    }

    public ClipViewFreedom(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ClipViewFreedom(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ClipViewFreedom);
        array.recycle();
        init();
        setClipRect();
    }

    private void init(){
        paint.setAntiAlias(true);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(Color.WHITE);
        borderPaint.setAntiAlias(true);
        borderPaint.setStrokeWidth(clipBorderWidth);
        xfermode = new PorterDuffXfermode(PorterDuff.Mode.DST_OUT);
        mHorizontalPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
        Log.d(TAG, "setClipRect-mHorizontalPadding: mHorizontalPadding");
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    boolean isFirstDraw = true;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(isFirstDraw){
            //第一次绘制是初始化rect，因为此时的getWidth()大小才是正确，在onDraw之前获取到的getWith都是0
            isFirstDraw = false;
            setClipRect();
            return;
        }
        if(lineMoveMode!=0){
            borderPaint.setColor(Color.GREEN);
        }else {
            borderPaint.setColor(Color.WHITE);
        }
        int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG | Canvas.CLIP_SAVE_FLAG
                | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
                | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
        //canvas调用saveLayer之后，开启了一个新的透明图层。绘制完成后再合并到上一个图层上。
        canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);
        paint.setXfermode(xfermode);
        canvas.drawColor(Color.parseColor("#a8000000"));

        canvas.drawRect(rect,borderPaint);
        canvas.drawRect(rect,paint);
        canvas.restore();
    }

    float downX;
    float downY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()& MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                if((Math.abs(event.getX()-rect.left)<10&&event.getY()>rect.top&&event.getY()<rect.bottom)){
                    lineMoveMode = 1;

                }else if((Math.abs(event.getY()-rect.top)<10&&event.getX()>rect.left&&event.getX()<rect.right)){
                    lineMoveMode = 2;

                }else if((Math.abs(event.getX()-rect.right)<10&&event.getY()>rect.top&&event.getY()<rect.bottom)){
                    lineMoveMode = 3;

                }else if((Math.abs(event.getY()-rect.bottom)<10&&event.getX()>rect.left&&event.getX()<rect.right)){
                    lineMoveMode = 4;

                }else {
                    lineMoveMode = 0;
                }
                if(lineMoveMode != 0){
                    invalidate();
                    return true;
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:

                break;
            case MotionEvent.ACTION_UP:
                lineMoveMode = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:

                break;
            case MotionEvent.ACTION_MOVE:
                if(lineMoveMode==1){
                    if(rect.left+event.getX()-downX<mHorizontalPadding){
                        rect.left = (int) mHorizontalPadding;
                    }else if(rect.left+event.getX()-downX>rect.right-100){
                        rect.left = rect.right-100;
                    }else {
                        rect.left += event.getX()-downX;
                    }

                }
                if(lineMoveMode==2){
                    if(rect.top<10){
                        rect.top = 10;
                    }else if(rect.top+event.getY()-downY>rect.bottom-100){
                        rect.top = rect.bottom-100;
                    }else {
                        rect.top += event.getY()-downY;
                    }

                }
                if(lineMoveMode==3){
                    if(rect.right+event.getX()-downX<rect.left+100){
                        rect.right = rect.left+100;
                    }else if(rect.right+event.getX()-downX>getWidth()-mHorizontalPadding){
                        rect.right = (int) (getWidth()-mHorizontalPadding);
                    }else {
                        rect.right += event.getX()-downX;
                    }

                }
                if(lineMoveMode==4){
                    if(rect.bottom+event.getY()-downY<rect.top+100){
                        rect.bottom = rect.top+100;
                    }else if(rect.bottom+event.getY()-downY>getHeight()-mHorizontalPadding){
                        rect.bottom = (int) (getHeight()-mHorizontalPadding);
                    }else {
                        rect.bottom += event.getY()-downY;
                    }

                }
                if(lineMoveMode != 0){
                    downX = event.getX();
                    downY = event.getY();
                    invalidate();
                    return true;
                }
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    /**
     *
     * @return
     */
    @Override
    public Rect getClipRect() {
        return rect;
    }

    public void setClipRect(){
        clipRadiusWidth = (int) (getScreenWidth(getContext()) - 2 * mHorizontalPadding) / 2;
        clipRadiusHeight = clipRadiusWidth;
        rect.left = (int) mHorizontalPadding;
        rect.right = (int) (getWidth() - mHorizontalPadding);
        rect.top = (getHeight() / 2 - clipRadiusHeight);
        rect.bottom = (getHeight() / 2 + clipRadiusHeight);
        invalidate();
    }

    /**
     *
     * @param clipBorderWidth
     */
    @Override
    public void setClipBorderWidth(int clipBorderWidth) {
        this.clipBorderWidth = clipBorderWidth;
        borderPaint.setStrokeWidth(clipBorderWidth);
        invalidate();
    }

    /**
     *
     * @param mHorizontalPadding
     */
    @Override
    public void setmHorizontalPadding(float mHorizontalPadding) {
        this.mHorizontalPadding = mHorizontalPadding;
        this.clipRadiusWidth = (int) (getScreenWidth(getContext()) - 2 * mHorizontalPadding) / 2;
        this.clipRadiusHeight = this.clipRadiusWidth;
        this.clipWidth = clipRadiusWidth * 2;
//        setClipRect();
    }

    @Override
    public void setClipType(ClipType type) {

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
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}
