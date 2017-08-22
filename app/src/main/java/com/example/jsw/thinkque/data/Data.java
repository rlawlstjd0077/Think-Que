package com.example.jsw.thinkque.data;

        import java.util.ArrayList;

/**
 * Created by JSW on 2017-08-21.
 */

public class Data {
    public static Data root = new Data("", 1, null, 0, 0, 100);
    public static ArrayList<Data> dataList = new ArrayList<>();

    private String text;
    private int level;
    private float cx;
    private float cy;
    private float radius;
    private Data parent;
    private ArrayList<Data> childrenList;
    private boolean seletedState;

    public Data(String text, int level, Data parent, int cx, int cy, int radius){
        this.text = text;
        this.level = level;
        this.parent = parent;
        this.cx = cx;
        this.cy = cy;
        this.radius = radius;
        childrenList = new ArrayList<>();
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

    public Data getParent() {
        return parent;
    }

    public void setParent(Data parent) {
        this.parent = parent;
    }

    public ArrayList<Data> getChildrenList() {
        return childrenList;
    }

    public void setChildrenList(ArrayList<Data> childrenList) {
        this.childrenList = childrenList;
    }

    public void addChildren(Data children){
        this.childrenList.add(children);
    }

    public float getCx() {
        return cx;
    }

    public void setCx(float cx) {
        this.cx = cx;
    }

    public float getCy() {
        return cy;
    }

    public void setCy(float cy) {
        this.cy = cy;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public int getNumChildren(){
        return childrenList.size();
    }

    public void setSeletedState(boolean state){
        this.seletedState = state;
    }

    public boolean isSelected(){
        return this.seletedState;
    }
}