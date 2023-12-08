package com.tuan.amazon.models;


import java.io.Serializable;

public class User implements Serializable {

    private String name;
    private String id;
    private String image;
    private boolean inviteAddFriend;


    public User() {
    }
    public User(String name, String id, String image, boolean inviteAddFriend) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.inviteAddFriend = inviteAddFriend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getInviteAddFriend(){
        return inviteAddFriend;
    }
    public void setInviteAddFriend(boolean inviteAddFriend){
         this.inviteAddFriend = inviteAddFriend;
    }
}
