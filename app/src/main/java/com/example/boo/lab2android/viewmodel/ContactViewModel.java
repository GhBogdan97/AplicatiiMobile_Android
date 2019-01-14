package com.example.boo.lab2android.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.support.annotation.NonNull;

import com.example.boo.lab2android.db.UserDB;
import com.example.boo.lab2android.models.Contact;
import com.example.boo.lab2android.repositories.ContactRepository;
import com.example.boo.lab2android.viewobject.ContactRequest;

import java.util.List;

public class ContactViewModel extends ViewModel {
private LiveData<List<Contact>> contactLiveData;
    private ContactRepository contactRepository;

    public ContactViewModel(Context context)
    {
        contactRepository =ContactRepository.getInstance(UserDB.getDatabase(context).getContactDao());
        contactLiveData = contactRepository.getCacheContacts();
    }

    public LiveData<List<Contact>> getContactsLiveData() {
        return contactLiveData;
    }

    public void getContacts(String token)
    {
        contactRepository.getContacts(token);
    }

    public void editContact(int id, String firstName, String lastName, String phoneNumber, String workNumber, String token) {
        ContactRequest contactRequest=new ContactRequest(id,firstName,lastName,phoneNumber,workNumber);
        contactRepository.update(contactRequest,token);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context ctxt;

        public Factory(Context ctxt) {
            this.ctxt=ctxt.getApplicationContext();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return((T)new ContactViewModel(ctxt));
        }
    }
}
