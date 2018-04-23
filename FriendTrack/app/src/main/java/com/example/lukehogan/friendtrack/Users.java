package com.example.lukehogan.friendtrack;

public class Users {

    public String name;
    public String url;
    public String email;


    public Users(String name,String url, String email) {
        this.name = name;
        this.url = url;
        this.email = email;


    }
    public Users(){

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getUrl() { return url; }

    public void setEmail(String email){ this.email = email;}

    public String getEmail(){ return email;}
}
