package com.example.listofgoods;

public class Product {
    private int image;
    private String name;
    private String store;
    private String date;

    public Product(String name, String store, int image, String date){

        this.name = name;
        this.store = store;
        this.image = image;
        this.date = date;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore() {
        return this.store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
