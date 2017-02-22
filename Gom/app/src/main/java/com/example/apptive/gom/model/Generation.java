package com.example.apptive.gom.model;

import android.graphics.Point;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apptive on 2017. 02. 08..
 */

public class Generation implements Serializable {
    private List<Point> cells;
    private int x;
    private int y;

    public Generation() {
    }

    public Generation(List<Point> points, int x, int y) {
        this.cells = points;
        this.x = x;
        this.y = y;
    }

    public void addPoint(Point p)
    {
        cells.add(p);
    }



    public List<Point> getPoints() {
        return cells;
    }

    public void setPoints(List<Point> points) {
        this.cells = points;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
