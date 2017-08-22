package com.example.jsw.thinkque;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import com.example.jsw.thinkque.data.Data;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by JSW on 2017-08-21.
 */

public class DrawActivity extends View {
    public DrawActivity(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) { // 화면을 그려주는 작업
        Paint normalPaint = new Paint(); // 화면에 그려줄 도구를 셋팅하는 객체
        normalPaint.setColor(Color.WHITE); // 색상을 지정
        normalPaint.setStyle(Paint.Style.STROKE);
        normalPaint.setStrokeWidth(6);
        Paint selectedPaint = new Paint();
        selectedPaint.setColor(Color.BLACK);
        selectedPaint.setStyle(Paint.Style.STROKE);
        selectedPaint.setStrokeWidth(6);

        Queue<Data> nodeStack = new LinkedList<>();
        nodeStack.add(Data.root);

        while(!nodeStack.isEmpty()) {
            Data curNode = nodeStack.poll();
            canvas.drawCircle(curNode.getCx(), curNode.getCy(), curNode.getRadius(), curNode.isSelected() ? selectedPaint : normalPaint);

            if(curNode.getNumChildren() > 0) { // has child
                Data [] children = new Data[curNode.getNumChildren()];
                for(int i = children.length - 1; i >= 0; i--) { // expand node
                    children[i] = curNode.getChildrenList().get(i);
                    nodeStack.add(children[i]);
                }
            }
        }
    }
}
