package com.example.edgar.blog_app.models;

import com.example.edgar.blog_app.models.Comment;

import java.util.ArrayList;

/**
 * Created by edgar on 2/26/18.
 */

public class Post {
    private String title;
    private String description;
    private int image;
    private ArrayList<Comment> comments = new ArrayList<>();
    private int likes;

    public Post(String title, String description, int image) {
        this.title = title;
        this.description = description;
        this.image = image;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public void setComments(Comment comment) {
        comments.add(comment);
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImage() {
        return image;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getLikes() {
        return likes;
    }
}
