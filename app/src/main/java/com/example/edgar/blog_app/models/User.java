package com.example.edgar.blog_app.models;

/**
 * Created by edgar on 3/18/18.
 */

public class User {
    private String name;
    private String image;

    public User() {

    }

    public User(String name, String image) {
        this.name = name;
        this.image = image;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }
}
