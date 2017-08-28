package com.example.jsw.thinkque;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.jsw.thinkque.data.Node;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addFloatingButton;
    private FloatingActionButton editFloatingButton;
    private FloatingActionsMenu menuFloatingMenu;
    private ZoomView zoomView;
    private DrawLineView drawLineView;
    private RelativeLayout nodeMap;
    private Node selectedNode;
    private ArrayList<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void drawNode(final Node node){
        final Button button = new Button(this);
        button.setBackgroundResource(R.drawable.circle);
        button.setTextColor(Color.BLACK);
        button.setText(node.getText());
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new BounceInterpolator());
        animation.setStartOffset(50);// 动画秒数。
        animation.setFillAfter(true);
        animation.setDuration(700);
        button.startAnimation(animation);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)node.getWidth(), (int)node.getHeight());
        
        layoutParams.topMargin = (int)node.getY();
        layoutParams.leftMargin = (int)node.getX();
        nodeMap.addView(button, layoutParams);
        buttonList.add(button);
        node.setButton(button);

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(button.getBackground() == getResources().getDrawable(R.drawable.button_circle) &&
                        (Math.pow(event.getX() - node.getX() + (node.getWidth() / 2), 2.0) +
                                Math.pow(event.getY() - node.getY() + (node.getWidth() / 2), 2)) < Math.pow(node.getWidth(), 2)){
                    return false;
                }
                clearAllButtonsBacgkround();
                selectedNode = findNodeFromButton(button);
                button.setBackgroundResource(R.drawable.button_circle_clicked);
                return false;
            }
        });

        if(node.getLevel() > 1){
            double nodeAngle = getAngle(node.getParent().getcX(), node.getParent().getcY(), node.getcX(), node.getcY());
            if( -45.0 <= nodeAngle && nodeAngle < 45.0){

            } else if( 45.0 <= nodeAngle && nodeAngle < 135.0 ){
                drawLineView = new DrawLineView(this, node.getParent().getcX(),node.getParent().getcY(), node.getX(), node.getcY());
            } else if( 135.0 <= nodeAngle && nodeAngle < -135.0 ){

            } else if( -135.0 <= nodeAngle && nodeAngle < 45.0){

            }

//            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(Math.abs((int)(node.getcX() - node.getParent().getcX())), Math.abs((int)(node.getcY() - node.getParent().getcY())));
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(2000, 2000);
            nodeMap.addView(drawLineView, nodeMap.getChildCount(), layoutParam);
        }
    }

    private double getAngle(double x1,double y1, double x2,double y2){
        double dx = x2 - x1;
        double dy = y2 - y1;

        double rad= Math.atan2(dx, dy);
        double degree = (rad * 180)/Math.PI ;

        return degree;
    }

    private Node findNodeFromButton(Button button){
        for(Node node : Node.nodeList){
            if(node.getButton() == button){
                return node;
            }
        }
        return null;
    }

    private void clearAllButtonsBacgkround(){
        selectedNode = null;
        for(Button button : buttonList){
            button.setBackgroundResource(R.drawable.button_circle);
        }
    }

    private void init() {
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initRootNode(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = zoomView.getHeight();
        int width = displayMetrics.widthPixels;

        Node root = Node.root;
        Node.nodeList.add(root);
        Node.root.setX(width / 2 - (float)root.getWidth()/2);
        Node.root.setY(height / 2 - (float)root.getHeight()/2);
        drawNode(Node.root);
    }

    private void initView(){
        addFloatingButton = (FloatingActionButton) findViewById(R.id.fab_add);
        editFloatingButton = (FloatingActionButton) findViewById(R.id.fab_edit);
        menuFloatingMenu = (FloatingActionsMenu)findViewById(R.id.multiple_actions);
        nodeMap = (RelativeLayout) findViewById(R.id.node_map);

        zoomView = (ZoomView) findViewById(R.id.zoom_view);
        zoomView.setMiniMapEnabled(true); // 좌측 상단 검은색 미니맵 설정
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        zoomView.setMiniMapCaption("Mini Map Test"); //미니 맵 내용
        zoomView.setMiniMapCaptionSize(20); // 미니 맵 내용 글씨 크기 설정
        zoomView.post(new Runnable() {
            @Override
            public void run() {
                initRootNode();
            }
        });
        buttonList = new ArrayList<>();
    }

    private void initListener(){
//        zoomView.setOnTouchListener(zoomViewOnTouchListener);
        addFloatingButton.setOnClickListener(fabAddOnClickListener);
        editFloatingButton.setOnClickListener(fabEditOnClickListener);
    }

//    View.OnTouchListener zoomViewOnTouchListener = new View.OnTouchListener() {
//        @Override
//        public boolean onTouch(View v, MotionEvent event) {
//            if(event.getAction() == MotionEvent.ACTION_DOWN) {
//                Node selectedNode = findNodeFromButton(event.getX(), event.getY());
//                if (selectedNode != null) {
//                    clearAllButtonsBacgkround();
//                    selectedNode.setSelectedState(true);
//                    drawLineView.invalidate();
//                    MainActivity.this.selectedNode = selectedNode;
//                } else {
//                    MainActivity.this.selectedNode = null;
//                }
//            }
//            return false;
//        }
//    };

    View.OnClickListener fabAddOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(selectedNode != null){
                Toast.makeText(getApplicationContext(), "Node Added", Toast.LENGTH_SHORT).show();
                Node parent = selectedNode;
                Node newNode = new Node("", parent.getLevel() + 1, parent, parent.getX() + 300,
                        parent.getY() - 200, 150, 150, Node.ButtonShape.getStyleFromNum(parent.getLevel() + 1));
                Node.nodeList.add(newNode);
                selectedNode.getChildrenList().add(newNode);
                drawNode(newNode);
            }
        }
    };

    View.OnClickListener fabEditOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "dasdas", Toast.LENGTH_SHORT).show();
        }
    };
}
