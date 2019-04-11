package com.zlalini.rattle.Login;

public class UserContentPojo {
    String name,id,email;
    int key;
    public UserContentPojo(){

    }
    public UserContentPojo(String name,String id,String email,int key){
        this.name=name;
        this.id=id;
        this.email=email;
        this.key=key;

    }
    public String getName(){
        return name;
    }
    public String getId(){return id;}
    public String getEmail(){return email;}
    public int getKey(){return key;}
    public void setName(String name){
        this.name=name;
    }
    public void setId(String id){this.id=id;}
    public void setEmail(String email){this.email=email;}
    public void setKey(int key){this.key=key;}
}
