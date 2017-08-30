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
    private float moveX = 0;
    private float moveY = 0;
    private float offset = 0;
    private boolean state;

    public DrawLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DrawLineView(Context context, float beginX, float beginY, float stopX, float stopY, float moveX, float moveY, boolean state) {
        super(context);
        this.beginX = beginX;
        this.beginY = beginY;
        this.stopX = stopX;
        this.stopY = stopY;
        this.moveX = moveX;
        this.moveY = moveY;
        this.state = state;
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

        mPath.moveTo(moveX, moveY);

        // 45.0 < x <= 135.0 && -135.0 <= nodeAngle < -45.0 인 경우ㅡ
        if(state){
            mPath.cubicTo(beginX, beginY, beginX, stopY, stopX, stopY);
        } else {
            mPath.cubicTo(beginX, beginY, stopX, beginY, stopX, stopY);
        }
        canvas.drawPath(mPath, redPaint);
    }
}
