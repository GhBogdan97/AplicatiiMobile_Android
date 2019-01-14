package com.example.boo.lab2android.ui;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.boo.lab2android.R;
import com.example.boo.lab2android.viewmodel.ContactViewModel;
import com.example.boo.lab2android.viewobject.ContactRequest;

public class EditActivity   extends AppCompatActivity {
    private EditText text_firstName;
    private EditText text_lastName;
    private EditText text_phoneNumber;
    private EditText text_workNumber;
    private Button button_edit;
    private TextView text_id,text_error;
    private ContactViewModel contactViewModel;

    private void initControls() {
        text_firstName = (EditText) findViewById(R.id.firstName);
        text_lastName = (EditText) findViewById(R.id.lastName);
        text_phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        text_workNumber = (EditText) findViewById(R.id.workNumber);
        text_id = (TextView) findViewById(R.id.id_task);
        text_error = (TextView) findViewById(R.id.error);
        button_edit = (Button) findViewById(R.id.edit);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit);
        initControls();
        ContactRequest contactRequest = (ContactRequest) getIntent().getSerializableExtra("ContactEdit");
        final String token = (String) getIntent().getSerializableExtra("Token");

        contactViewModel =ViewModelProviders.of(this, new ContactViewModel.Factory(getApplicationContext())).get(ContactViewModel.class);
        text_id.setText(String.valueOf(contactRequest.getId()));
        text_firstName.setText(contactRequest.getFirstName());
        text_lastName.setText(contactRequest.getLastName());
        text_phoneNumber.setText(contactRequest.getPhoneNumber());
        text_workNumber.setText(contactRequest.getWorkNumber());

        final Context context=this;
        button_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editContact(token);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(context, HomeActivity.class);
                finish();
                startActivity(intent);
            }
        });
    }

    public void editContact(String token)
    {
        int id=Integer.parseInt(text_id.getText().toString());
        String newFirstName= text_firstName.getText().toString();
        String newLastName= text_lastName.getText().toString();
        String newPhoneNumber= text_phoneNumber.getText().toString();
        String newWorkNumber= text_workNumber.getText().toString();
        contactViewModel.editContact(id,newFirstName,newLastName,newPhoneNumber,newWorkNumber, token);
    }
}