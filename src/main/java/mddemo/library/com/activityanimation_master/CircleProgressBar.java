package mddemo.library.com.activityanimation_master;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.net.VpnService;
import android.util.AttributeSet;
import android.view.View;

import java.util.Properties;

/**
 * Author:  梁铖城
 * Email:   1038127753@qq.com
 * Date:    2015年11月6日15:45:38
 * Description:  自定义控件圆形进度条
 */

public class CircleProgressBar extends View {
    private Paint paint;
    private int circleColor;
    private int circleProgressColor;
    private int textColor;
    private float textSize;
    private float roundWidth;
    private int max;
    private int progress;
    private boolean textIsDisplayable;
    private int style;
    public static final int STROKE = 0;
    public static final int FILL = 1;
    public CircleProgressBar(Context context) {
        this(context, null);
    }
    public CircleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CircleProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint();
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.CircleProgressBar);
        //获取自定义属性和默认值
        circleColor = mTypedArray.getColor(R.styleable.CircleProgressBar_circleColor, 0xff47d8af);
        circleProgressColor = mTypedArray.getColor(R.styleable.CircleProgressBar_circleProgressColor, Color.WHITE);
        textColor = mTypedArray.getColor(R.styleable.CircleProgressBar_textColor, Color.WHITE);
        textSize = mTypedArray.getDimension(R.styleable.CircleProgressBar_textSize, 40);
        roundWidth = mTypedArray.getDimension(R.styleable.CircleProgressBar_circleWidth, 3);
        max = mTypedArray.getInteger(R.styleable.CircleProgressBar_max, 100);
        textIsDisplayable = mTypedArray.getBoolean(R.styleable.CircleProgressBar_textIsDisplayable, true);
        style = mTypedArray.getInt(R.styleable.CircleProgressBar_style, 0);
        mTypedArray.recycle();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       //画最外层的大圆环
        int centre = getWidth()/2;
        //圆环的半径
        int radius = (int) (centre - roundWidth/2);
        //设置圆环的颜色
        paint.setColor(circleColor);
        //设置空心
        paint.setStyle(Paint.Style.STROKE);
        //设置圆环的宽度
        paint.setStrokeWidth(roundWidth);
        //消除锯齿
        paint.setAntiAlias(true);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        //画出圆环
        canvas.drawCircle(centre, centre, radius, paint);
        // 画进度百分比
        paint.setStrokeWidth(0);
        paint.setColor(textColor);
        paint.setTextSize(textSize);
        //设置字体
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        //中间的进度百分比，先转换成float在进行除法运算，不然都为0
        int percent = (int)(((float)progress / (float)max) * 100);
        //测量字体宽度，我们需要根据字体的宽度设置在圆环中间
        float textWidth = paint.measureText(percent + "%");
        if(textIsDisplayable && percent != 0 && style == STROKE){
            //画出进度百分比
            canvas.drawText(percent + "%", centre - textWidth / 2, centre + textSize/2, paint);
        }

        //设置进度是实心还是空心
        radius = (int) (centre - roundWidth*2);
        //设置圆环的宽度
        paint.setStrokeWidth(roundWidth*2);
        //设置进度的颜色
        paint.setColor(circleProgressColor);
        //用于定义的圆弧的形状和大小的界限
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);
        switch (style) {
            case STROKE:{
                paint.setStyle(Paint.Style.STROKE);
                //根据进度画圆弧
                canvas.drawArc(oval, 0, 360 * progress / max, false, paint);
                break;
            }
            case FILL:{
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if(progress !=0)
                    //根据进度画圆弧
                    canvas.drawArc(oval, 0, 360 * progress / max, true, paint);
                break;
            }
        }
    }
    public synchronized int getMax() {
        return max;
    }
   //设置进度的最大值
    public synchronized void setMax(int max) {
        if(max < 0){
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    //获取进度需要同步
    public synchronized int getProgress() {
        return progress;
    }

    //设置当前的进度
    public synchronized void setProgress(int progress) {
        if(progress < 0){
            throw new IllegalArgumentException("progress not less than 0");
        }
        if(progress > max){
            progress = max;
        }
        if(progress <= max){
            this.progress = progress;
            postInvalidate();
        }
    }
    public int getCricleColor() {
        return circleColor;
    }
    public void setCricleColor(int cricleColor) {
        this.circleColor = cricleColor;
    }
    public int getCricleProgressColor() {
        return circleProgressColor;
    }
    public void setCricleProgressColor(int cricleProgressColor) {
        this.circleProgressColor = cricleProgressColor;
    }
    public int getTextColor() {
        return textColor;
    }
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }
    public float getTextSize() {
        return textSize;
    }
    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }
    public float getRoundWidth() {
        return roundWidth;
    }
    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }
}