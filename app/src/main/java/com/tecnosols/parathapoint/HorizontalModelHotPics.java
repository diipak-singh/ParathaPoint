package com.tecnosols.parathapoint;

public class HorizontalModelHotPics {
    String imageResoure;
    String imageTitle;
    String imagePrice;

    public HorizontalModelHotPics(String imageResoure, String imageTitle, String imagePrice) {
        this.imageResoure = imageResoure;
        this.imageTitle = imageTitle;
        this.imagePrice = imagePrice;
    }

    public HorizontalModelHotPics() {
    }

    public String getImageResoure() {
        return imageResoure;
    }

    public void setImageResoure(String imageResoure) {
        this.imageResoure = imageResoure;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    public String getImagePrice() {
        return imagePrice;
    }

    public void setImagePrice(String imagePrice) {
        this.imagePrice = imagePrice;
    }
}
