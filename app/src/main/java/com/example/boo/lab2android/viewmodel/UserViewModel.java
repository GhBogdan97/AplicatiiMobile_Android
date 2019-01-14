package com.example.boo.lab2android.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.boo.lab2android.db.UserDB;
import com.example.boo.lab2android.models.User;
import com.example.boo.lab2android.repositories.UserRepository;

public class UserViewModel extends ViewModel {
    private LiveData<User> userLiveData;
    private UserRepository userRepository;

    public UserViewModel(Context context)
    {
        userRepository=UserRepository.getInstance(UserDB.getDatabase(context).getUserDao());
        userLiveData = userRepository.getUser();
    }

    public LiveData<User> getUser() {
        return userLiveData;
    }


    public boolean login(String username, String password)
    {
        return userRepository.login(username, password);
    }


    public static class Factory implements ViewModelProvider.Factory {
        private final Context ctxt;

        public Factory(Context ctxt) {
            this.ctxt=ctxt.getApplicationContext();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return((T)new UserViewModel(ctxt));
        }
    }
}
