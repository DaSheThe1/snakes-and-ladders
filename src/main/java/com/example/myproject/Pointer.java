package com.example.myproject;

public class Pointer {
    private int x; // the line
    private int y; // the row

    public Pointer() { // regular constructor. the player always start at 0,0 but i changed it to 0,9 to rotate it from right to left to left to right
        this.x = 0;
        this.y = 9;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
