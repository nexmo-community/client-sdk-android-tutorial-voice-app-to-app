package com.vonage.tutorial.voice;

public class User {

    String name;
    public String jwt;

    public User(String name, String jwt) {
        this.name = name;
        this.jwt = jwt;
    }
    public String getName() {
        return name;
    }
    public String getId() {
        return jwt;
    }
}