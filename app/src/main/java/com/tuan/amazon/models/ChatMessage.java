package com.tuan.amazon.models;

import java.util.Date;

public class ChatMessage {
    private String senderId, receiverID, message, dateTime;
    private Date dateObject;
    private String lastMessage;
    private String conversionName, conversionImage, conservationId;

    public String getConservationId() {
        return conservationId;
    }

    public void setConservationId(String conservationId) {
        this.conservationId = conservationId;
    }

    public String getConversionName() {
        return conversionName;
    }

    public void setConversionName(String conversionName) {
        this.conversionName = conversionName;
    }

    public String getConversionImage() {
        return conversionImage;
    }

    public void setConversionImage(String conversionImage) {
        this.conversionImage = conversionImage;
    }

    public ChatMessage(String senderId, String receiverID, String message, String dateTime, Date dateObject, String lastMessage, String conversionImage, String conservationId) {
        this.senderId = senderId;
        this.receiverID = receiverID;
        this.message = message;
        this.dateTime = dateTime;
        this.dateObject = dateObject;
        this.lastMessage = lastMessage;
        this.conversionImage = conversionImage;
        this.conservationId = conservationId;
    }

    public ChatMessage() {
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public void setReceiverID(String receiverID) {
        this.receiverID = receiverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
