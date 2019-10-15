package com.tecnosols.parathapoint;

public class user_details {

    String user_pic,user_name,user_mobile,user_email,user_city,user_address,user_id;

    public user_details() {
    }

    public user_details(String user_pic, String user_name, String user_mobile, String user_email, String user_city, String user_address, String user_id) {
        this.user_pic = user_pic;
        this.user_name = user_name;
        this.user_mobile = user_mobile;
        this.user_email = user_email;
        this.user_city = user_city;
        this.user_address = user_address;
        this.user_id = user_id;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_mobile() {
        return user_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        this.user_mobile = user_mobile;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_city() {
        return user_city;
    }

    public void setUser_city(String user_city) {
        this.user_city = user_city;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
