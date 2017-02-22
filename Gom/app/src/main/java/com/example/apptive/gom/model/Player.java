package com.example.apptive.gom.model;

import android.graphics.Point;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apptive on 2017. 02. 08..
 */

public class Player implements Serializable {
    private List<Point> points;
    private int x;
    private int y;

    public Player() {
    }

    public Player(List<Point> points, int x, int y) {
        this.points = points;
        this.x = x;
        this.y = y;
    }

    public void addPoint(Point p)
    {
        points.add(p);
    }



    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
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
