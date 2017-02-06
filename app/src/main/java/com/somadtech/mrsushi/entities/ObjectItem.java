package com.somadtech.mrsushi.entities;

/**
 * Created by smt on 28/01/17.
 * Project Name: Mrsushi
 */

public class ObjectItem {

    private int itemId;
    private String itemName;
    private int itemImage;

    // constructor
    public ObjectItem(int itemId, String itemName, int itemImage) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
    }

    public String getName(){
        return this.itemName;
    }

    public int getItemImage(){
        return this.itemImage;
    }

}