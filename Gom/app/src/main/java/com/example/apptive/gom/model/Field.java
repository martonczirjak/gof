package com.example.apptive.gom.model;

import android.graphics.Point;

import java.io.Serializable;
import java.util.List;

/**
 * Created by apptive on 2017. 02. 08..
 */

public class Field implements Serializable {
    private int x;
    private int y;
    private String m;

    public Field() {
    }

    public Field(int x, int y, String m) {
        this.x = x;
        this.y = y;
        this.m = m;
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

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }


}
