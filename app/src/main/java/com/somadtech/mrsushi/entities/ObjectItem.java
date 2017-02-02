package com.somadtech.mrsushi.entities;

/**
 * Created by smt on 28/01/17.
 * Project Name: Mrsushi
 */

public class ObjectItem {

    public int itemId;
    public String itemName;

    // constructor
    public ObjectItem(int itemId, String itemName) {
        this.itemId = itemId;
        this.itemName = itemName;
    }

    public String getName(){
        return this.itemName;
    }

}