package com.example.jsw.thinkque;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
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
import java.util.Random;

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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void drawNode(final Node node){
        final Button button = new Button(this);
        button.setBackground(generateButtonColor(node));

        button.setTextColor(Color.BLACK);
        button.setText(node.getText());
        ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setInterpolator(new BounceInterpolator());
        animation.setStartOffset(50);// 动画秒数。
        animation.setFillAfter(true);
        animation.setDuration(700);
        button.startAnimation(animation);
        final RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams((int)node.getWidth(), (int)node.getHeight());

        layoutParams.topMargin = (int)node.getY();
        layoutParams.leftMargin = (int)node.getX();
        button.setLayoutParams(layoutParams);

        nodeMap.addView(button, layoutParams);
        buttonList.add(button);
        node.setButton(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(button.getBackground() == getResources().getDrawable(R.drawable.button_circle) &&
//                        (Math.pow(event.getX() - node.getX() + (node.getWidth() / 2), 2.0) +
//                                Math.pow(event.getY() - node.getY() + (node.getWidth() / 2), 2)) < Math.pow(node.getWidth(), 2)){
//                }
                if(selectedNode != null){
                    backButtonStyle(selectedNode);
                    if(findNodeFromButton(button) == selectedNode){
                        selectedNode = null;
                        menuFloatingMenu.setVisibility(View.INVISIBLE);
                        return;
                    }
                }
                selectedNode = findNodeFromButton(button);
                changeButtonStyleSelected(button);
                menuFloatingMenu.setVisibility(View.VISIBLE);
            }
        });

        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(findNodeFromButton(button).getLevel() != 1) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_MOVE:
                            int topMargin = (int) (button.getY() - (button.getHeight() / 2 - event.getY()));
                            int leftMargin = (int) (button.getX() - (button.getWidth() / 2 - event.getX()));
                            node.setX(leftMargin);
                            node.setY(topMargin);
                            layoutParams.topMargin= topMargin;
                            layoutParams.leftMargin = leftMargin;
                            System.out.println("topMargin :" + topMargin + "leftMargin : " + leftMargin);
                            button.setLayoutParams(layoutParams);

                            clearConnectLineNode(node);
                            DrawLineView toParentLine = getLineBetweenNodes(node.getParent(), node);
                            node.setLine(toParentLine);
                            ArrayList<DrawLineView> toChildLineList = new ArrayList<DrawLineView>();
                            if(node.getChildrenList().size() > 0) {
                                for (Node temp : node.getChildrenList()){
                                    DrawLineView toChildLine = getLineBetweenNodes(node, temp);
                                    toChildLineList.add(toChildLine);
                                    temp.setLine(toChildLine);
                                }
                            }

                            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(2000, 2000);
                            nodeMap.addView(toParentLine, layoutParam);

                            for(DrawLineView drawLineView : toChildLineList){
                                layoutParam = new RelativeLayout.LayoutParams(2000, 2000);
                                nodeMap.addView(drawLineView, layoutParam);
                            }
                    }
                }
                return false;
            }
        });

        if(node.getLevel() > 1){
            drawLineView = getLineBetweenNodes(node.getParent(), node);

            //layoutParams 조정 필요
            RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(2000, 2000);
            nodeMap.addView(drawLineView, layoutParam);
            node.setLine(drawLineView);
        }
    }

    private DrawLineView getLineBetweenNodes(Node parent, Node child){
        DrawLineView drawLineView = null;
        double nodeAngle = getAngle(parent.getcX(), parent.getcY(), child.getcX(), child.getcY());
        if( -45.0 <= nodeAngle && nodeAngle < 45.0){
            drawLineView = new DrawLineView(this, child.getcX(), (float)(child.getY() - 100), parent.getcX(), parent.getcY(), child.getcX(), child.getY(), false);
        } else if( 45.0 <= nodeAngle && nodeAngle < 135.0 ){
            drawLineView = new DrawLineView(this, parent.getcX() + 100, parent.getcY(), child.getX(), child.getcY(), parent.getcX(), parent.getcY(), true);
        } else if( 135.0 <= nodeAngle || nodeAngle < -135.0 ){
            drawLineView = new DrawLineView(this, child.getcX(), (float)(child.getY() + child.getHeight() + 100), parent.getcX(), parent.getcY(), child.getcX(), (float)(child.getY() + child.getHeight()), false);
        } else if( -135.0 <= nodeAngle && nodeAngle < -45.0){
            drawLineView = new DrawLineView(this, parent.getcX() - 100, parent.getcY(), (float)(child.getX() + child.getWidth()), child.getcY(), parent.getcX(), child.getParent().getcY(), true);
        }
        return drawLineView;
    }

    private void clearConnectLineNode(Node node){
        nodeMap.removeView(node.getLine());
        for(Node child : node.getChildrenList()){
            nodeMap.removeView(child.getLine());
        }
    }

    private GradientDrawable generateButtonColor(Node node){
        int[] background_list = getResources().getIntArray(R.array.background_list);
        int[] stroke_list = getResources().getIntArray(R.array.stroke_list);

        Button button = new Button(this);
        button.setBackgroundResource(R.drawable.circle);

        GradientDrawable drawable = (GradientDrawable) button.getBackground().getCurrent();

        int random = new Random().nextInt(background_list.length);
        drawable.setColor(background_list[random]);
        drawable.setStroke(6, stroke_list[random]);

        node.setButtonColor(background_list[random]);
        node.setButtonStroke(stroke_list[random]);

        return drawable;
    }

    private void changeButtonStyleSelected(Button button){
        GradientDrawable drawable = (GradientDrawable) button.getBackground().getCurrent();
        drawable.setStroke(10, Color.BLACK);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void backButtonStyle(Node node){
        GradientDrawable drawable = (GradientDrawable) node.getButton().getBackground().getCurrent();
        drawable.setColor(node.getButtonColor());
        drawable.setStroke(6, node.getButtonStroke());
        node.getButton().setBackground(drawable);
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

    private void init() {
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
//                    clearAllButtonsBackground();
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
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onClick(View v) {
            if(selectedNode != null){
                Toast.makeText(getApplicationContext(), "Node Added", Toast.LENGTH_SHORT).show();
                Node parent = selectedNode;
                Node newNode = new Node("", parent.getLevel() + 1, parent, parent.getX() + 200,
                        parent.getY() +  300 , 150, 150, Node.ButtonShape.getStyleFromNum(parent.getLevel() + 1));
                Node.nodeList.add(newNode);
                selectedNode.addChildren(newNode);
                drawNode(newNode);
            } else {
                Toast.makeText(getApplicationContext(), "Please, select node", Toast.LENGTH_SHORT).show();
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
