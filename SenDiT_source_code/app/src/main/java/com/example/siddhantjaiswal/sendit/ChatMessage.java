package com.example.siddhantjaiswal.sendit;

import java.util.Date;
public class ChatMessage {
    private  String MessageText;
    private  String MessageUser;
    private long messageTime;

    public ChatMessage(String messageText, String messageUser) {
        MessageText = messageText;
        MessageUser = messageUser;
        messageTime = new Date().getTime();

    }

    public ChatMessage(){

    }
    public String getMessageText() {

        return MessageText;
    }

    public void setMessageText(String messageText) {
        this.MessageText = messageText;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageUser() {
        String[] separated = MessageUser.split("@");
        String qwerty=separated[0];
        return qwerty;
    }

    public void setMessageUser(String messageUser) {
        this.MessageUser = messageUser;
    }
}

