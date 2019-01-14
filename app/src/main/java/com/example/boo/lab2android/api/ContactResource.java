package com.example.boo.lab2android.api;

import com.example.boo.lab2android.viewobject.AccountRequest;
import com.example.boo.lab2android.viewobject.AccountResponse;
import com.example.boo.lab2android.viewobject.ContactRequest;
import com.example.boo.lab2android.viewobject.ContactVO;
import com.example.boo.lab2android.viewobject.Page;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface ContactResource {
    String BASE_URL = "http://192.168.43.117:5000/";

    @POST("user/authenticate")
    Call<AccountResponse> login(@Body AccountRequest post);

    @GET("contacts")
    Call<Page> getContacts(@Header("Authorization") String token, @Header("If-None-Match") String etag);

    @POST("contacts")
    Call<ContactVO> update(@Body ContactRequest taskRequest,@Header("Authorization") String token);
}
