package com.somadtech.mrsushi.entities;

import android.annotation.SuppressLint;

/**
 * Created by smt on 1/02/17.
 * Project Name: Mrsushi
 */

public class Album {
    private String name;
    private double numOfSongs;
    private int thumbnail;

    public Album() {
    }

    public Album(String name, int numOfSongs, int thumbnail) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    @SuppressLint("DefaultLocale")
    public String getNumOfSongs() {
        //DecimalFormat df2 = new DecimalFormat(".##");
        return String.format("%.2f", numOfSongs);
        //return df2.format(numOfSongs);
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

}
