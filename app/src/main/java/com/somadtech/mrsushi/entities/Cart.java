package com.somadtech.mrsushi.entities;

/**
 * Created by smt on 2/13/17.
 * Project: mrsushi-android
 */

public class Cart {

    private int id;
    private Product product;
    private Variant variant;
    private int order_id;
    private String observations;
    private int state;
    private int quantity;

    public Cart(){

    }

    public Cart(Product product, Variant variant, String observations){
        this.product = product;
        this.variant = variant;
        this.observations = observations;
        this.order_id = 0;
        this.state = 1;
        this.quantity = 1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Variant getVariant() {
        return variant;
    }

    public void setVariant(Variant variant) {
        this.variant = variant;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}
