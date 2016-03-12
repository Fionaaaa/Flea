package com.fiona.tiaozao.fragment.myself.edit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.fiona.tiaozao.PurchaseActivity;

/**
 * 添加便签的按钮
 * Created by fiona on 15-12-22.
 */
public class AddEmptionView extends View {

    float h = 0;      //视图高
    float w = 0;      //视图宽
    float x;        //按钮中点X
    float y;        //按钮中点Y
    Paint p;        //画笔

    float len;      //圆的半径

    float textH = 20;        //  文字的高

    Context context;        //  活动上下文

    boolean isPress = false;  //是否被按下

    public AddEmptionView(Context context) {
        this(context, null);
    }

    public AddEmptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 按钮坐标初始化
         */
        if (w == 0) {
            w = getWidth();
            h = getHeight();
            x = w / 2;
            y = h / 2 - textH;
            len = h / 4;
        }

        p.setStyle(Paint.Style.STROKE);
        p.setColor(Color.rgb(0x80, 0x80, 0x80));

        //画分割线
        p.setStrokeWidth(1);
        canvas.drawLine(0, 0, w, 0, p);

        //画圆
        if (isPress) {
            p.setStyle(Paint.Style.FILL);
            p.setColor(Color.parseColor("#6699FF"));
        }
        p.setStrokeWidth(2);
        canvas.drawCircle(x, y, len, p);

        //画十字架
        if (isPress) {
            p.setColor(Color.WHITE);
        }
        p.setStrokeWidth(4);
        canvas.drawLine(x - len / 2, y, x + len / 2, y, p);
        canvas.drawLine(x, y - len / 2, x, y + len / 2, p);

        //写字
        p.setColor(Color.rgb(0xaa, 0xaa, 0xaa));
        if (isPress) {
            p.setColor(Color.parseColor("#6699FF"));
            isPress = false;
        }
        p.setStrokeWidth(0);
        p.setTextAlign(Paint.Align.CENTER);     //文本位置
//        p.setTextSkewX(-0.25f);                 //文本倾斜
        p.setTextSize(len / 3 * 2);
        p.setStyle(Paint.Style.FILL);
        canvas.drawText("求购物品", x, (h - y - len) / 2 + y + len + len / 5, p);
    }

    /**
     * 点击监听
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float ex = event.getX();
        float ey = event.getY();
        if (ex > x - len && ex < x + len && ey > y - len && ey < y + len) {
            isPress = true;       //按下则按钮不一样
            invalidate();

            //开启编辑便签的活动：EditActivity
            context.startActivity(new Intent(context, PurchaseActivity.class));
        }
        return super.onTouchEvent(event);
    }
}
