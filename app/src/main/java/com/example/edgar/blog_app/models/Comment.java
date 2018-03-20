package com.example.edgar.blog_app.models;

/**
 * Created by edgar on 3/1/18.
 */

public class Comment {
    private String author;
    private String comment;

    public Comment(String author, String comment) {
        this.author = author;
        this.comment = comment;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAuthor() {
        return author;
    }

    public String getComment() {
        return comment;
    }
}
