package com.example.edgar.blog_app;

/**
 * Created by edgar on 2/26/18.
 */

public class Card {
    private String title;
    private String description;
    private int image;
    private String comment;
    private int likes;

    public Card(String title, String description, int image, String comment, int likes) {
        this.title = title;
        this.description = description;
        this.image = image;
        this.comment = comment;
        this.likes = likes;
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

    public void setComment(String comment) {
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public int getLikes() {
        return likes;
    }
}
