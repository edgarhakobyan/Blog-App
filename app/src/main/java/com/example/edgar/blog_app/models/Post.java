package com.example.edgar.blog_app.models;

import java.util.Date;

/**
 * Created by edgar on 2/26/18.
 */

public class Post extends PostId {

    private String userId;
    private String description;
    private String imageUrl;
    private Date timestamp;

    public Post() {}

    public Post(String userId, String imageUrl, String description, Date timestamp) {
        this.userId = userId;
        this.imageUrl = imageUrl;
        this.description = description;
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


    public Date getTimestamp() {
        return timestamp;
    }
}
