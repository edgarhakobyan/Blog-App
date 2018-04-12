package com.example.edgar.blog_app.models;

import java.util.Date;

/**
 * Created by edgar on 3/1/18.
 */

public class Comment {
    private String message;
    private String userId;
    private Date timestamp;

    public Comment() {};

    public Comment(String message, String userId, Date timestamp) {
        this.message = message;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
