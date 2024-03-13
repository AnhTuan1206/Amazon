package com.tuan.amazon.models;

import java.util.Date;

public class Post {
    private String postId, creatorId,caption, imagePost, dateTime,  creatorName, imgCreator;
    private Boolean isLike;

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }

    public String getImgCreator() {
        return imgCreator;
    }

    public void setImgCreator(String imgCreator) {
        this.imgCreator = imgCreator;
    }

    private int countComment, countLike;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getCountComment() {
        return countComment;
    }

    public void setCountComment(int countComment) {
        this.countComment = countComment;
    }

    public int getCountLike() {
        return countLike;
    }

    public void setCountLike(int countLike) {
        this.countLike = countLike;
    }

    private Date dateObject;

    public Post(String postId, String creatorId, String caption, String imagePost, String dateTime, String creatorName, String imgCreator, Boolean isLike, Date dateObject, int countComment, int countLike) {
        this.postId = postId;
        this.creatorId = creatorId;
        this.caption = caption;
        this.imagePost = imagePost;
        this.dateTime = dateTime;
        this.creatorName = creatorName;
        this.imgCreator = imgCreator;
        this.isLike = isLike;
        this.dateObject = dateObject;
        this.countComment = countComment;
        this.countLike = countLike;
    }

    public String getImagePost() {
        return imagePost;
    }

    public void setImagePost(String imagePost) {
        this.imagePost = imagePost;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Post() {
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

    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }
}
