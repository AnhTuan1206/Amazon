package com.tuan.amazon.models;



public class User  {

    private String name;
    private String id;
    private String image;
    private boolean inviteAddFriend;
    private String FCM;

    public boolean isInviteAddFriend() {
        return inviteAddFriend;
    }

    public String getFCM() {
        return FCM;
    }

    public void setFCM(String FCM) {
        this.FCM = FCM;
    }

    public User() {
    }
    public User(String name, String id, String image, boolean inviteAddFriend, String fcm) {
        this.name = name;
        this.id = id;
        this.image = image;
        this.inviteAddFriend = inviteAddFriend;
        FCM = fcm;
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
