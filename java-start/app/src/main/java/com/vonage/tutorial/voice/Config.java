package com.vonage.tutorial.voice;

public class Config {

    public static User getAlice() {
        return new User(
                "Alice",
                "" // TODO: "set Alice JWT token"
        );
    }

    public static User getBob() {
        return new User(
                "Bob",
                "" // TODO: "set Bob JWT token"
        );
    }

    public static String getOtherUserName(String userName) {
        if (userName.equals(getAlice().getName())) {
            return getBob().getName();
        } else {
            return getAlice().getName();
        }
    }
}
