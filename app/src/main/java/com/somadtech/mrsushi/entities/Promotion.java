package com.somadtech.mrsushi.entities;

import java.util.List;

/**
 * Created by smt on 2/15/17.
 * Project: mrsushi-android
 */

public class Promotion {

    private int id;
    private String name;
    private String slug;
    private String description;
    private String image_small;
    private String image_large;
    private String type;
    private List<Product> trigger;
    private List<Product> target;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage_small() {
        return image_small;
    }

    public void setImage_small(String image_small) {
        this.image_small = image_small;
    }

    public String getImage_large() {
        return image_large;
    }

    public void setImage_large(String image_large) {
        this.image_large = image_large;
    }

    public List<Product> getTrigger() {
        return trigger;
    }

    public void setTrigger(List<Product> trigger) {
        this.trigger = trigger;
    }

    public List<Product> getTarget() {
        return target;
    }

    public void setTarget(List<Product> target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
