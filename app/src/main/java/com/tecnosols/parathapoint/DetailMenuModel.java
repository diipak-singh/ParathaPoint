package com.tecnosols.parathapoint;

public class DetailMenuModel {
    String name,price,description,item_id;

    public DetailMenuModel(String name, String price, String description, String item_id) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.item_id = item_id;
    }

    public DetailMenuModel(String name, String price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public DetailMenuModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
