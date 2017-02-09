package com.somadtech.mrsushi.entities;

/**
 * Created by smt on 28/01/17.
 * Project Name: Mrsushi
 */

public class Category {


    private int itemId;
    private String itemName;
    private String itemImage;

    // constructor
    public Category() {

    }
    // constructor
    public Category(int itemId, String itemName, String itemImage) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemImage = itemImage;
    }
    public String getItemImage(){
        return this.itemImage;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }


}