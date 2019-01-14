package com.example.boo.lab2android.repositories;

import android.arch.lifecycle.LiveData;
import android.util.Log;

import com.example.boo.lab2android.api.ContactResource;
import com.example.boo.lab2android.daos.ContactDAO;
import com.example.boo.lab2android.db.UserDB;
import com.example.boo.lab2android.models.Contact;
import com.example.boo.lab2android.models.Etag;
import com.example.boo.lab2android.models.User;
import com.example.boo.lab2android.viewobject.ContactRequest;
import com.example.boo.lab2android.viewobject.ContactVO;
import com.example.boo.lab2android.viewobject.Page;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ContactRepository {
    private static final String CLASSNAME = "CONTACT REPOSITORY";
    private static ContactDAO contactDAO;
    private static ContactRepository repositoryInstance;
    Executor executor;

    private ContactRepository(ContactDAO contactDAO)
    {
        this.contactDAO =contactDAO;
        executor = Executors.newSingleThreadExecutor();
    }

    public static  ContactRepository getInstance(ContactDAO contactDAO)
    {
        if(repositoryInstance==null)
        {
            repositoryInstance=new ContactRepository(contactDAO);
        }
        return repositoryInstance;
    }

    private static ContactResource getAPIService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ContactResource.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ContactResource api = retrofit.create(ContactResource.class);
        return api;
    }


    public void getContacts(String token) {
        Etag etag = UserDB.databaseInstance.getEtagDao().getEtag();
        final String etagValue;
        if (etag == null)
            etagValue = "";
        else
            etagValue = etag.getValue();

        getAPIService()
                .getContacts("Bearer " + token, etagValue)
                .enqueue(new Callback<Page>() {
                    @Override
                    public void onResponse(Call<Page> call, final Response<Page> response) {
                        Log.d(CLASSNAME, "CALL GET TASKS SUCCESS");

                        if (response.code() == 304) {
                            Log.d(CLASSNAME, "ETAG UNCHANGED");
                            return;
                        }

                        Log.d(CLASSNAME, "ETAG DIFFERENT");

                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                List<ContactVO> contacts = response.body().getContactVOS();
                                String etag = response.headers().get("etag").toString();
                                UserDB.databaseInstance.getEtagDao().deleteAll();
                                UserDB.databaseInstance.getEtagDao().insert(new Etag(0, etag));
                                contactDAO.deleteAll();
                                if (contacts != null) {
                                    for (ContactVO contact : contacts) {
                                        Contact contactDB = new Contact(contact.getId(), contact.getFirstname(), contact.getLastname(), contact.getPhoneNumber(), contact.getWorkNumber());
                                        contactDAO.insert(contactDB);
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Page> call, Throwable t) {
                        Log.d(CLASSNAME, "CALL GET TASKS FAILED", t);
                    }


                });

    }

    public void update(final ContactRequest contactRequest, String token)
    {
        getAPIService()
                .update(contactRequest,"Bearer "+token)
                .enqueue(new Callback<ContactVO>() {
                    @Override
                    public void onResponse(Call<ContactVO> call, final Response<ContactVO> response) {
                        Log.d(CLASSNAME,"CALL UPDATE SUCCESS");
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                ContactVO contact=response.body();

                                if(contact!=null) {
                                    Contact contactDB = new Contact(contact.getId(), contact.getFirstname(),contact.getLastname(),contact.getPhoneNumber(),contact.getWorkNumber());
                                    contactDAO.update(contactDB);
                                }
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                        Log.d(CLASSNAME,"CALL UPDATE FAILED",t);
                        Contact newContact = new Contact(contactRequest.getId(),contactRequest.getFirstName(),contactRequest.getLastName(),contactRequest.getPhoneNumber(),contactRequest.getWorkNumber());
                        contactDAO.update(newContact);
                    }
                });

    }

    public LiveData<List<Contact>> getCacheContacts()
    {
        return contactDAO.getLiveContacts();
    }

    public static void updateContacts()
    {
        List<Contact> contacts=  contactDAO.getContacts();
        User user = UserDB.databaseInstance.getUserDao().getCurrentUser();
        if(user == null)
            return;
        String token = user.getToken();

        for (Contact c : contacts){
            ContactRequest cr = new ContactRequest(c.getId(),c.getFirstname(),c.getLastname(),c.getPhoneNumber(),c.getWorkNumber());
            getAPIService()
                    .update(cr,"Bearer "+token)
                    .enqueue(new Callback<ContactVO>() {
                        @Override
                        public void onResponse(Call<ContactVO> call, final Response<ContactVO> response) {
                            Log.d(CLASSNAME,"CALL UPDATE SUCCESS");
                        }
                        @Override
                        public void onFailure(Call call, Throwable t) {
                            Log.d(CLASSNAME,"CALL UPDATE FAILED",t);
                        }
                    });
        }

    }

}
