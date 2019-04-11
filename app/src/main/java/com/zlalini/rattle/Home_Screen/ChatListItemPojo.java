package com.zlalini.rattle.Home_Screen;

    public class ChatListItemPojo {
        String name,id,password,email;
        public ChatListItemPojo(){

        }
        public ChatListItemPojo(String name,String id,String email){
            this.name=name;
            this.id=id;
            this.email=email;

        }
        public String getName(){
            return name;
        }
        public String getId(){return id;}
        public String getEmail(){return email;}
        public void setName(String name){
            this.name=name;
        }
        public void setId(String id){this.id=id;}
        public void setPhoto(String email){this.email=email;}
    }
