package com.example.boo.lab2android.viewobject;

public class AccountResponse {
    //private String Username;
    //private String Password;
    private String Token;

//    private boolean IsSuccess;

//    public void setSuccess(boolean success) {
//        IsSuccess = success;
//    }

//    public boolean isSuccess() {
//        return IsSuccess;
//    }

//    public String getUsername() {
//        return Username;
//    }
//
//    public String getPassword() {
//        return Password;
//    }
//
//    public void setUsername(String username) {
//        Username = username;
//    }
//
//    public void setPassword(String password) {
//        Password = password;
//    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public AccountResponse(String token) {
//        Username = username;
//        Password = password;
        Token = token;
//        IsSuccess = isSuccess;
    }

    public AccountResponse()
    {

    }
}
