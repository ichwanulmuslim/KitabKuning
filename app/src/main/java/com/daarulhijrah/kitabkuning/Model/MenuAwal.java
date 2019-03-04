package com.daarulhijrah.kitabkuning.Model;

import android.graphics.drawable.Drawable;

public class MenuAwal {
    String title;
    Drawable image;

    // Empty Constructor
    public MenuAwal() {

    }

    // Constructor
    public MenuAwal(String title, Drawable image) {
        super();
        this.title = title;
        this.image = image;
    }

    // Getter and Setter Method
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }


}