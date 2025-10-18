package com.example.termproject_game;

import java.util.ArrayList;

public class LinePosition {
    public float[] s = new float[2];
    public float[] e = new float[2];
    public boolean d;

    static ArrayList<LinePosition> AllLine = new ArrayList<LinePosition>();


    public LinePosition(float x1, float y1, float x2, float y2) {
        this.s[0] = x1;
        this.s[1] = y1;
        this.e[0] = x2;
        this.e[1] = y2;
        this.d = (s[0] > e[0]);
        AllLine.add(this);
    }

    public void LineDownMove(float Down) {
        this.s[1] += Down;
        this.e[1] += Down;
    }
}
