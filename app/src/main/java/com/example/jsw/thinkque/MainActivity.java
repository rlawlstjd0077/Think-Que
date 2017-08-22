package com.example.jsw.thinkque;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jsw.thinkque.data.Data;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.LinkedList;
import java.util.Queue;

import pl.polidea.view.ZoomView;

public class MainActivity extends AppCompatActivity {
    View mainView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
//        zoomView.setListner(new ZoomView.ZoomViewListener() {
//            @Override
//            public void onZoomStarted(float zoom, float zoomx, float zoomy) {
//
//            }
//
//            @Override
//            public void onZooming(float zoom, float zoomx, float zoomy) {
//                System.out.println("onZooming: ");
//            }
//
//            @Override
//            public void onZoomEnded(float zoom, float zoomx, float zoomy) {
//
//            }
//        });
    }

    public Data findDataIncludePoint(float x, float y){
        Queue<Data> nodeStack = new LinkedList<>();
        nodeStack.add(Data.root);
        Data accordData = null;

        while(!nodeStack.isEmpty()) {
            Data curNode = nodeStack.poll();
            if((Math.pow(x - curNode.getCx(), 2.0) + Math.pow(y - curNode.getCy(), 2)) < Math.pow(curNode.getRadius(), 2)){
                return curNode;
            }

            if(curNode.getNumChildren() > 0) { // has child
                Data [] children = new Data[curNode.getNumChildren()];
                for(int i = children.length - 1; i >= 0; i--) { // expand node
                    children[i] = curNode.getChildrenList().get(i);
                    nodeStack.add(children[i]);
                }
            }
        }
        return accordData;
    }

    private void clearDatasSelectedState(){
        for(Data data : Data.dataList){
            data.setSeletedState(false);
        }
    }

    public void init() {
        setContentView(R.layout.activity_main);
        mainView = findViewById(R.id.mainView);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        Data.dataList.add(Data.root);
        Data.root.setCx(width / 2);
        Data.root.setCy(height / 2);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        final ZoomView zoomView = new ZoomView(this);
        final DrawActivity drawActivity = new DrawActivity(getApplicationContext());
        zoomView.addView(drawActivity);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(true); // 좌측 상단 검은색 미니맵 설정
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        zoomView.setMiniMapCaption("Mini Map Test"); //미니 맵 내용
        zoomView.setMiniMapCaptionSize(20); // 미니 맵 내용 글씨 크기 설정

        RelativeLayout container = (RelativeLayout) findViewById(R.id.mainView);
        container.addView(zoomView);

        final View actionB = findViewById(R.id.action_b);

        FloatingActionButton actionC = new FloatingActionButton(getBaseContext());
        actionC.setTitle("Hide/Show Action above");
        actionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionB.setVisibility(actionB.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });

        zoomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    Data selectedNode = findDataIncludePoint(event.getX(), event.getY());
                    if (selectedNode != null) {
                        clearDatasSelectedState();
                        selectedNode.setSeletedState(true);
                        drawActivity.invalidate();
                    }
                }
                return false;
            }
        });
        FloatingActionsMenu fam_menu = (FloatingActionsMenu)findViewById(R.id.multiple_actions);
        fam_menu.bringToFront();
    }
}
