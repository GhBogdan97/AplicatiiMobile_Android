package com.example.boo.lab2android.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.boo.lab2android.api.ContactResource;
import com.example.boo.lab2android.daos.UserDAO;
import com.example.boo.lab2android.db.UserDB;
import com.example.boo.lab2android.models.User;
import com.example.boo.lab2android.viewobject.AccountRequest;
import com.example.boo.lab2android.viewobject.AccountResponse;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserRepository  {
    private static final String CLASSNAME = "USER REPOSITORY";
    private UserDAO userDao;
    private static UserRepository repositoryInstance;
    Executor executor;


    private UserRepository(UserDAO userDao)
    {

        this.userDao=userDao;
        executor = Executors.newSingleThreadExecutor();
    }

    public static  UserRepository getInstance(UserDAO userDao)
    {
        if(repositoryInstance==null)
        {
            repositoryInstance=new UserRepository(userDao);
        }
        return repositoryInstance;
    }

    private ContactResource getAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ContactResource api = retrofit.create(ContactResource.class);
        return api;
    }

    public boolean login(final String username, final String password)
    {
        final Boolean[] message = new Boolean[]{false};

        getAPIService()
                .login(new AccountRequest(username,password,""))
                .enqueue(new Callback<AccountResponse>() {
                    @Override
                    public void onResponse(Call<AccountResponse> call, final Response<AccountResponse> response) {
                        Log.d(CLASSNAME,"CALL LOGIN SUCCESS");
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                if(response.code()==200) {
//                                    String username = response.body().getUsername();
//                                    String password = response.body().getPassword();
                                    String token = response.body().getToken();
                                    User user = new User(username,password,token);
                                    userDao.insert(user);
                                    message[0] = true;
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<AccountResponse> call, Throwable t) {
                        Log.d(CLASSNAME,"CALL LOGIN FAILED",t);
                    }
                });

        return message[0];
    }

    public boolean userExists(String username, String password)
    {
        LiveData<User> user=userDao.getUser(username);
        if(user==null)
        {
            return false;
        }
        return user.getValue().getPassword().equals(password);
    }

    public LiveData<User> getUser() {
        return userDao.getLiveCurrentUser();
    }

    public void clearUserCached() {
        executor.execute(new Runnable() {
            @Override
            public void run() {

                UserDB.databaseInstance.getEtagDao().deleteAll();
                UserDB.databaseInstance.getContactDao().deleteAll();
                userDao.deleteAll();
            }
        });
    }
}
