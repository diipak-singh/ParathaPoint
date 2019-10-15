package com.tecnosols.parathapoint;

public class HorizontalModel {
    String imageResource;
    String itemName;
    String itemPrice;

    public HorizontalModel(String imageResource, String itemName, String itemPrice) {
        this.imageResource = imageResource;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public HorizontalModel() {
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(String itemPrice) {
        this.itemPrice = itemPrice;
    }
}
