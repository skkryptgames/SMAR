package com.example.smar;

public class ChatMessage {

    private String messageText;
    private String sender;
    private String receiver;


        public ChatMessage() {

        }

        public ChatMessage(String messageText, String sender) {
            this.messageText = messageText;
            this.sender = sender;
        }

        public String getSender() {
            return sender;
        }

        public String getReceiver() {
            return receiver;
        }

        public String getMessageText() {
            return messageText;
        }
    }

