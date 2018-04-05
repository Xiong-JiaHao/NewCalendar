package com.gin.xjh.newcalendar;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by Gin on 2018/4/4.
 */

public class Calendar_day_textView extends android.support.v7.widget.AppCompatTextView {

    public boolean isToday = false;
    private Paint mPaint = new Paint();

    public Calendar_day_textView(Context context) {
        super(context);
        init();
    }

    public Calendar_day_textView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public Calendar_day_textView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(isToday){
            canvas.translate(getWidth()/2,getHeight()/2);
            canvas.drawCircle(0,0,getWidth()/2,mPaint);
        }

    }
}
