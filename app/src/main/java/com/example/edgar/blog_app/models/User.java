package com.example.edgar.blog_app.models;

import java.util.ArrayList;

/**
 * Created by edgar on 3/18/18.
 */

public class User {
    private String name;
    private String surName;
    private String image;
    private AboutUser mAboutUser;
    private ArrayList<Post> posts;


    public User(String name, String surName, String image, AboutUser mAboutUser) {
        this.name = name;
        this.surName = surName;
        this.image = image;
        this.mAboutUser = mAboutUser;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setmAboutUser(AboutUser mAboutUser) {
        this.mAboutUser = mAboutUser;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public String getName() {
        return name;
    }

    public String getSurName() {
        return surName;
    }

    public String getImage() {
        return image;
    }

    public AboutUser getmAboutUser() {
        return mAboutUser;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }
}
