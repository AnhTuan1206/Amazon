package com.tuan.amazon.models;

import java.util.Date;

public class Posts {
    private String postId, creatorId,caption, image, dateTime, likeNumber;
    private Date dateObject;

    public Posts(String postId, String creatorId,String caption,String image, String dateTime, String likeNumber, Date dateObject) {
        this.postId = postId;
        this.creatorId = creatorId;
        this.caption = caption;
        this.image = image;
        this.dateTime = dateTime;
        this.likeNumber = likeNumber;
        this.dateObject = dateObject;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Posts() {
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLikeNumber() {
        return likeNumber;
    }

    public void setLikeNumber(String likeNumber) {
        this.likeNumber = likeNumber;
    }

    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }
}
