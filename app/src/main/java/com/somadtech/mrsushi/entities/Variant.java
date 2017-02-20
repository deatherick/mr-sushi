package com.somadtech.mrsushi.entities;

import android.annotation.SuppressLint;

/**
 * Created by smt on 2/6/17.
 * Project: mrsushi-android
 */

public class Variant {

    private int id;
    private int product_id;
    private String name;
    private String slug;
    private double price;
    private String image;

    public Variant(){
        this.id = 0;
        this.product_id = 0;
        this.name = "";
        this.price = 0;
        this.slug = "";
    }

    public Variant(int id, int product_id, String name, double price, String image){
        this.id = id;
        this.product_id =  product_id;
        this.name = name;
        this.price = price;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressLint("DefaultLocale")
    public String getPrice() {
        return String.format("%.2f", price);
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
