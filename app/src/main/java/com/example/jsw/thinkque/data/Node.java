package com.example.jsw.thinkque.data;

        import android.graphics.drawable.Drawable;
        import android.widget.Button;
        import android.widget.RelativeLayout;

        import com.example.jsw.thinkque.DrawLineView;

        import java.util.ArrayList;

/**
 * Created by JSW on 2017-08-21.
 */

public class Node {
    public static Node root = new Node("Root", 1, null, 0, 0, 200, 200, ButtonShape.CIRCLE);
    public static ArrayList<Node> nodeList = new ArrayList<>();

    public enum ButtonShape {
        CIRCLE(1),
        ROUNDRECT(2),
        SHARPRECT(3);

        private int style;

        ButtonShape(int style){
            this.style = style;
        }

        public static ButtonShape getStyleFromNum(int num){
            switch (num){
                case 1 :
                    return CIRCLE;
                case 2 :
                     return ROUNDRECT;
                default:
                    return SHARPRECT;
            }
        }
    }

    private Button button;
    private String text;
    private int level;
    private float x;
    private float y;
    private float cX;
    private float cY;
    private double width;
    private double height;
    private Node parent;
    private ArrayList<Node> childrenList;
    private boolean selectedState;
    private ButtonShape buttonShape;
    private int buttonColor;
    private int buttonStroke;
    private DrawLineView line;


    public Node(String text, int level, Node parent, float x, float y, int width, int height, ButtonShape buttonShape){
        this.text = text;
        this.level = level;
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.cX = x + width / 2;
        this.cY = y + height / 2;
        this.width = width;
        this.height = height;
        childrenList = new ArrayList<>();
        this.buttonShape = buttonShape;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(ArrayList<Node> childrenList) {
        this.childrenList = childrenList;
    }

    public void addChildren(Node children){
        this.childrenList.add(children);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        setcX((float) (x + width / 2));
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        setcY((float) (y + height / 2));
        this.y = y;
    }

    public int getNumChildren(){
        return childrenList.size();
    }

    public void setSelectedState(boolean state){
        this.selectedState = state;
    }

    public boolean isSelected(){
        return this.selectedState;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double heigth) {
        this.height = heigth;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public ButtonShape getButtonShape() {
        return buttonShape;
    }

    public void setButtonShape(ButtonShape buttonShape) {
        this.buttonShape = buttonShape;
    }

    public float getcX() {
        return cX;
    }

    public void setcX(float cX) {
        this.cX = cX;
    }

    public float getcY() {
        return cY;
    }

    public void setcY(float cY) {
        this.cY = cY;
    }

    public void setButtonColor(int buttonColor) {
        this.buttonColor = buttonColor;
    }

    public int getButtonColor(){
        return buttonColor;
    }

    public int getButtonStroke() {
        return buttonStroke;
    }

    public void setButtonStroke(int buttonStroke) {
        this.buttonStroke = buttonStroke;
    }

    public DrawLineView getLine() {
        return line;
    }

    public void setLine(DrawLineView line) {
        this.line = line;
    }
}