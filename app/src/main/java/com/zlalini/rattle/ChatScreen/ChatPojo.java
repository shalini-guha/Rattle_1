package com.zlalini.rattle.ChatScreen;

public class ChatPojo {
        String message,name;
        public ChatPojo(){

        }
        public ChatPojo(String message,String name){
            this.message=message;
            this.name=name;
        }
        public String getName(){
            return name;
        }
        public String getMessage() {
            return message;
        }

        public void setMessage(String message){
            this.message=message;
        }
        public void setName(String name){
            this.name=name;
        }

    }

