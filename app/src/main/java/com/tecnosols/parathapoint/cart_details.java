package com.tecnosols.parathapoint;

public class cart_details {
    String item_img_url,itemname,itemprice,quantity,userid,data_key;

    public cart_details(String item_img_url, String itemname, String itemprice, String quantity, String userid, String data_key) {
        this.item_img_url = item_img_url;
        this.itemname = itemname;
        this.itemprice = itemprice;
        this.quantity = quantity;
        this.userid = userid;
        this.data_key = data_key;
    }

    public cart_details(String item_img_url, String itemname, String itemprice, String userid, String data_key) {
        this.item_img_url = item_img_url;
        this.itemname = itemname;
        this.itemprice = itemprice;
        this.userid = userid;
        this.data_key = data_key;
    }

    public cart_details(String item_img_url, String itemname, String itemprice, String data_key) {
        this.item_img_url = item_img_url;
        this.itemname = itemname;
        this.itemprice = itemprice;
        this.data_key = data_key;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public cart_details() {
    }

    public String getData_key() {
        return data_key;
    }

    public void setData_key(String data_key) {
        this.data_key = data_key;
    }

    public String getItem_img_url() {
        return item_img_url;
    }

    public void setItem_img_url(String item_img_url) {
        this.item_img_url = item_img_url;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getItemprice() {
        return itemprice;
    }

    public void setItemprice(String itemprice) {
        this.itemprice = itemprice;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
