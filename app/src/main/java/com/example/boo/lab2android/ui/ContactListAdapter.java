package com.example.boo.lab2android.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;


import com.example.boo.lab2android.R;
import com.example.boo.lab2android.models.Contact;

public class ContactListAdapter extends ArrayAdapter<Contact> {

    public ContactListAdapter(Context context, List<Contact> contacts) {
        super(context, 0, contacts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Contact contact = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_layout, parent, false);
        }
        // Lookup view for data population
        TextView text_firstName = (TextView) convertView.findViewById(R.id.firstName);
        TextView text_lastName = (TextView) convertView.findViewById(R.id.lastName);
        TextView text_phoneNumber = (TextView) convertView.findViewById(R.id.phoneNumber);
        TextView text_workNumber = (TextView) convertView.findViewById(R.id.workNumber);

        // Populate the data into the template view using the data object
        text_firstName.setText(contact.getFirstname());
        text_lastName.setText(contact.getLastname());
        text_phoneNumber.setText(contact.getPhoneNumber());
        text_workNumber.setText(contact.getWorkNumber());

        // Return the completed view to render on screen
        return convertView;
    }
}