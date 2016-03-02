package com.fiona.tiaozao.myself;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * 画一条直线
 * Created by fiona on 16-2-23.
 */
public class Line extends View {

    Paint p;
    float h;
    float w=0;

    public Line(Context context) {
        super(context, null);
    }

    public Line(Context context, AttributeSet attrs) {
        super(context, attrs);
        p=new Paint();
        p.setColor(0xffa0a0a0);
        p.setStrokeWidth(1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(w==0){
            h=canvas.getHeight();
            w=canvas.getWidth();
        }
        canvas.drawLine(0,h/2,w,h/2,p);
    }
}
