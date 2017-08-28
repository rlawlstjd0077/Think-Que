package com.example.jsw.thinkque;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.example.jsw.thinkque.data.Node;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by JSW on 2017-08-21.
 */

public class DrawLineView extends View {
    private float beginX = 0;
    private float beginY = 0;
    private float stopX = 100;
    private float stopY = 100;
    private float offset = 0;

    public DrawLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawLineView(Context context, float beginX, float beginY, float stopX, float stopY) {
        super(context);
        this.beginX = beginX;
        this.beginY = beginY;
        this.stopX = stopX;
        this.stopY = stopY;
    }


    @Override
    protected void onDraw(Canvas canvas) { // 화면을 그려주는 작업
        super.onDraw(canvas);
        Paint redPaint = new Paint();
        redPaint.setAntiAlias(true);
        redPaint.setColor(Color.WHITE);
        redPaint.setStrokeWidth(5.0f);
        redPaint.setStyle(Paint.Style.STROKE);
        redPaint.setTextSize(50);

        Path mPath = new Path();
        mPath.reset();

        mPath.moveTo(beginX, beginY);
        mPath.cubicTo(beginX + 100, beginY, beginX, stopY, stopX, stopY);
        canvas.drawPath(mPath, redPaint);
    }
}
