package com.example.boo.lab2android.viewobject;

public class AccountRequest {
    private String Username;
    private String Password;
    private  String Token;

    public String getUsername() {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public AccountRequest(String username, String password, String token) {
        Username = username;
        Password = password;
        Token = token;

    }

    public AccountRequest()
    {

    }
}
