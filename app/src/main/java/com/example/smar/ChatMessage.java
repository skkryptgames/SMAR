package com.example.smar;

public class ChatMessage {

    private String messageText;
    private String sender;

    public String getuserType() {
        return userType;
    }

    public void setuserType(String userType) {
        this.userType = userType;
    }

    String userType;


        public ChatMessage() {

        }

        public ChatMessage(String messageText, String sender,String userType) {
            this.messageText = messageText;
            this.sender = sender;
            this.userType=userType;
        }

        public String getSender() {
            return sender;
        }


        public String getMessageText() {
            return messageText;
        }
    }

