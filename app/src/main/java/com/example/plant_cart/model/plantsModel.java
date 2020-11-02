package com.example.plant_cart.model;

public class plantsModel {
    private String name;
    private long price;
    private String pic;

    private String quantity;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public plantsModel(){}

    private plantsModel(String name, long price , String pic, String quantity)
    {
        this.name = name;
        this.price = price;
        this.pic = pic;
        this.quantity = quantity;
    }

    public long getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getPic() { return pic; }

    public String getQuantity() { return quantity; }
}
