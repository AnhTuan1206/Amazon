package com.tuan.amazon.models;

import java.util.Date;

public class Comment {
    private String name, imgAvatar, comment, dateTime, idUser, id, idPost;
    private Date dateObject;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgAvatar() {
        return imgAvatar;
    }

    public void setImgAvatar(String imgAvatar) {
        this.imgAvatar = imgAvatar;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public Comment(String name, String imgAvatar, String comment, String dateTime, String idUser, String img, String id, String idPost, Date dateObject) {
        this.name = name;
        this.imgAvatar = imgAvatar;
        this.comment = comment;
        this.dateTime = dateTime;
        this.idUser = idUser;
        this.id = id;
        this.idPost = idPost;
        this.dateObject = dateObject;
    }

    public Comment() {
    }

    public String getIdPost() {
        return idPost;
    }

    public void setIdPost(String idPost) {
        this.idPost = idPost;
    }
}
