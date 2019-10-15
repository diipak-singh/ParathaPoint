package com.tecnosols.parathapoint;

public class myOrderModel {
    String image_resource, item_name, item_price, quantity, order_time, order_date, username, userphone, useraddress, useremail,total_amount, userid,payment_status;

    public myOrderModel(String item_name, String item_price, String quantity, String order_time, String order_date, String username, String userphone, String useraddress, String useremail, String total_amount, String userid, String payment_status) {
        this.item_name = item_name;
        this.item_price = item_price;
        this.quantity = quantity;
        this.order_time = order_time;
        this.order_date = order_date;
        this.username = username;
        this.userphone = userphone;
        this.useraddress = useraddress;
        this.useremail = useremail;
        this.total_amount = total_amount;
        this.userid = userid;
        this.payment_status = payment_status;
    }

    public myOrderModel() {
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getImage_resource() {
        return image_resource;
    }

    public void setImage_resource(String image_resource) {
        this.image_resource = image_resource;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}

