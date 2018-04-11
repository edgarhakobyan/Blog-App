package com.example.edgar.blog_app.models;

import com.example.edgar.blog_app.models.Comment;
import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by edgar on 2/26/18.
 */

public class Post {

    //private String title;
    private String userId;
    private String description;
    private String imageUrl;
    private String imageThumb;
    private Date timestamp;

    public Post() {}

    public Post(String userId, String imageUrl, String description, String imageThumb, Date timestamp) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.description = description;
        this.imageThumb = imageThumb;
        this.timestamp = timestamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setImageThumb(String imageThumb) {
        this.imageThumb = imageThumb;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getImageThumb() {
        return imageThumb;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    //    private int image;
//    private ArrayList<Comment> comments = new ArrayList<>();
//    private int likes;
//
//    public Post(String userId, String description, int image) {
//        this.userId = userId;
//        this.description = description;
//        this.image = image;
//    }
//
////    public void setTitle(String title) {
////        this.title = title;
////    }
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }
//
//    public void setComments(Comment comment) {
//        comments.add(comment);
//    }
//
//    public void setLikes(int likes) {
//        this.likes = likes;
//    }
//
////    public String getTitle() {
////        return title;
////    }
//    public String getUserId() {
//        return userId;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public int getImage() {
//        return image;
//    }
//
//    public ArrayList<Comment> getComments() {
//        return comments;
//    }
//
//    public int getLikes() {
//        return likes;
//    }
}
