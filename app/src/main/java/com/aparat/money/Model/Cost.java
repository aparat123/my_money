package com.aparat.money.Model;

public class Cost {
    private String name;
    private int price;

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Cost(){

    }

    public Cost (String name, int price){
        this.name = name;
        this.price = price;
    }
}
