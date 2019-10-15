package com.tecnosols.parathapoint;

public class item_details {
    String item_desc,item_id,item_name,item_picURL,item_price;

    public item_details(String item_desc, String item_id, String item_name, String item_picURL, String item_price) {
        this.item_desc = item_desc;
        this.item_id = item_id;
        this.item_name = item_name;
        this.item_picURL = item_picURL;
        this.item_price = item_price;
    }

    public item_details() {
    }

    public String getItem_desc() {
        return item_desc;
    }

    public void setItem_desc(String item_desc) {
        this.item_desc = item_desc;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_picURL() {
        return item_picURL;
    }

    public void setItem_picURL(String item_picURL) {
        this.item_picURL = item_picURL;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }
}
