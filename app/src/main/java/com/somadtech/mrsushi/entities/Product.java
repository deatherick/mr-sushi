package com.somadtech.mrsushi.entities;

import android.annotation.SuppressLint;
import java.util.List;

/**
 * Created by smt on 1/02/17.
 * Project Name: Mrsushi
 */

public class Product {

    private int id;
    private String name;
    private String description;
    private double originalPrice;
    private String thumbnail;
    private List<Variant> variants;
    private Category category;

    public Product() {
    }

    public Product(String name, int originalPrice, String thumbnail) {
        this.name = name;
        this.originalPrice = originalPrice;
        this.thumbnail = thumbnail;
    }

    public Product(int id, String name, int originalPrice, String thumbnail, Category category) {
        this.id = id;
        this.name = name;
        this.originalPrice = originalPrice;
        this.thumbnail = thumbnail;
        this.category = category;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    @SuppressLint("DefaultLocale")
    public String getOriginalPrice() {
        //DecimalFormat df2 = new DecimalFormat(".##");
        return String.format("%.2f", originalPrice);
        //return df2.format(originalPrice);
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public List<Variant> getVariants() {
        return variants;
    }

    public void setVariants(List<Variant> variants) {
        this.variants = variants;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
