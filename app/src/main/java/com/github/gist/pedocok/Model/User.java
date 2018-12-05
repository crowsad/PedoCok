package com.github.gist.pedocok.Model;

public class User {

    private String username;
    private String password;
    private String email;
    private Integer currentScore;
    private Integer highScore;

    public User() {

    }

    public User(String username, String password, String email, Integer currentScore, Integer highScore) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.currentScore = currentScore;
        this.highScore = highScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getCurrentScore() {
        return currentScore;
    }

    public void setCurrentScore(Integer currentScore) {
        this.currentScore = currentScore;
    }

    public Integer getHighScore() {
        return highScore;
    }

    public void setHighScore(Integer highScore) {
        this.highScore = highScore;
    }
}
