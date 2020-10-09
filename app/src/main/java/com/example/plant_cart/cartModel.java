package com.example.plant_cart;

public class cartModel {

    String name;
    String price;
    String image;
    String quantity;

    cartModel(){}

    public cartModel(String name, String price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImage() { return image; }

    public String getPrice() { return price; }

    public String getQuantity() { return quantity; }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

}
