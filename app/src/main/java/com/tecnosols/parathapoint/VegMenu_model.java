package com.tecnosols.parathapoint;

public class VegMenu_model {
    String name,price_1piece,price_2piece,item_id;

    public VegMenu_model(String name, String price_1piece, String price_2piece, String item_id) {
        this.name = name;
        this.price_1piece = price_1piece;
        this.price_2piece = price_2piece;
        this.item_id = item_id;
    }

    public VegMenu_model(String name, String price_1piece, String price_2piece) {
        this.name = name;
        this.price_1piece = price_1piece;
        this.price_2piece = price_2piece;
    }

    public VegMenu_model() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice_1piece() {
        return price_1piece;
    }

    public void setPrice_1piece(String price_1piece) {
        this.price_1piece = price_1piece;
    }

    public String getPrice_2piece() {
        return price_2piece;
    }

    public void setPrice_2piece(String price_2piece) {
        this.price_2piece = price_2piece;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }
}
