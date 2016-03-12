package com.fiona.tiaozao.fragment.myself.edit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.fiona.tiaozao.R;

/**
 * Created by fiona on 15-12-24.
 */
public class DeleteView extends View {

    Paint p;
    float w = 0;
    float h = 0;
    Bitmap bitmap;
    float x;
    float y;

    public DeleteView(Context context) {
        this(context, null);
    }

    public DeleteView(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_delete_white_36dp);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (w == 0) {
            w = getWidth();
            h = getHeight();
            x = bitmap.getWidth();
            y = bitmap.getHeight();
        }
        canvas.drawBitmap(bitmap, w / 2 - x / 2, h / 2 - y / 2, p);
    }
}
