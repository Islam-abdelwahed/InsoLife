package com.example.insulife;

public class User {
    private String imageUrl = "";
    private String phone = "";
    private String username = "";
    private String email = "";
    private  String type="";

    public String getType() {
        return type;
    }

    private String id = "";

    public User() {
    }

    public User(String imageUrl, String phone, String username, String type) {
        this.imageUrl = imageUrl;
        this.phone = phone;
        this.username = username;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }
}
