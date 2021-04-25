package com.example.myproject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Snakesandladders {
    private Map< Integer , Integer > snakenladders = new HashMap< Integer , Integer >();

    public Snakesandladders(){

        //snakes
        this.snakenladders.put(31, 13);
        this.snakenladders.put(35, 6);
        this.snakenladders.put(41, 16);
        this.snakenladders.put(51, 33);
        this.snakenladders.put(56, 32);
        this.snakenladders.put(71, 44);
        this.snakenladders.put(86, 68);
        this.snakenladders.put(94, 63);
        this.snakenladders.put(98, 61);


        //ladders
        this.snakenladders.put(1, 37);
        this.snakenladders.put(8, 12);
        this.snakenladders.put(11, 29);
        this.snakenladders.put(15, 45);
        this.snakenladders.put(18, 20);
        this.snakenladders.put(54, 64);
        this.snakenladders.put(57, 78);
        this.snakenladders.put(66, 92);
        this.snakenladders.put(70, 88);
        this.snakenladders.put(77, 96);
    }

    public Map<Integer, Integer> getSnakenladders() {
        return this.snakenladders;
    }

    public void setSnakenladders(Map<Integer, Integer> snakenladders) {
        this.snakenladders = snakenladders;
    }
}
