package com.happicouch.livestory.models;

import java.io.Serializable;

public class User implements Serializable {
    private String username;
    private String email;
    private String bio;
    private String fullName;
    private String imageUri;
    private boolean privacy;
    private int followers;
    private int following;

    public User(){
        //Empty
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        privacy = false;
        followers = 0;
        following = 0;
        bio = null;
        fullName = null;
        imageUri = null;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }
}
