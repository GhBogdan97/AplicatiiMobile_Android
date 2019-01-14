package com.example.boo.lab2android.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.boo.lab2android.R;
import com.example.boo.lab2android.api.ContactResource;
import com.example.boo.lab2android.models.Contact;
import com.example.boo.lab2android.models.User;
import com.example.boo.lab2android.network.NetworkReceiver;
import com.example.boo.lab2android.viewmodel.ContactViewModel;
import com.example.boo.lab2android.viewmodel.HomeViewModel;
import com.example.boo.lab2android.viewobject.ContactRequest;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private TextView text_welcome;
    private HomeViewModel homeViewModel;
    private ContactViewModel contactViewModel;
    private Button button_logout;
    private ListView listView;
    private static final String CLASSNAME = "HOME ACTIVITY";
    private static String token;
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity
    private List<Contact> currentContacts;
    private ContactListAdapter currentAdapter;
    private final Context context = this;
    private HubConnection hubConnection;

    private void initControls() {
        button_logout=(Button)findViewById(R.id.logout);
        listView = (ListView) findViewById(R.id.listview_tasks);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initControls();
        hubConnection = HubConnectionBuilder.create(ContactResource.BASE_URL+"broadcaster")
                .build();

        hubConnection.on("SendMessage", (message) -> {
            homeViewModel.getUser().observe(this, new Observer<User>() {
                @Override
                public void onChanged(@Nullable User user) {
                    if (user != null && user.getToken() != null) {
                        Log.d(CLASSNAME, "GET USER ");
                        token = user.getToken();
                        contactViewModel.getContacts(user.getToken());
                    } else {
                        Log.d(CLASSNAME, "VALUE USER IS NULL");
                        logoutUser();
                    }
                }
            });
        },Object.class);
        hubConnection.start();
        while(hubConnection.getConnectionState()!=HubConnectionState.CONNECTED){}
        hubConnection.send("Subscribe","User");

        NetworkReceiver networkListener=new NetworkReceiver();
        startService(new Intent(this, AlwaysOnService.class));

        initSensorManager();
        homeViewModel = ViewModelProviders.of(this, new HomeViewModel.Factory(getApplicationContext())).get(HomeViewModel.class);
        contactViewModel = ViewModelProviders.of(this, new ContactViewModel.Factory(getApplicationContext())).get(ContactViewModel.class);

        homeViewModel.getUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                if (user != null && user.getToken() != null) {
                    Log.d(CLASSNAME, "GET USER ");
                    token = user.getToken();
                    contactViewModel.getContacts(user.getToken());
                } else {
                    Log.d(CLASSNAME, "VALUE USER IS NULL");
                    logoutUser();
                }
            }
        });
        final Context context=this;



        contactViewModel.getContactsLiveData().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                currentContacts = contacts;
                ContactListAdapter adapter = new ContactListAdapter(context, contacts);
                currentAdapter = adapter;
                listView.setAdapter(adapter);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Contact item = (Contact) parent.getItemAtPosition(position);
                ContactRequest contactEdit=new ContactRequest(item.getId(),item.getFirstname(),item.getLastname(),item.getPhoneNumber(),item.getWorkNumber());
                Intent intent = new Intent(context, EditActivity.class);
                intent.putExtra("ContactEdit", contactEdit);
                intent.putExtra("Token", token);
                startActivity(intent);
            }
        });

        button_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void initSensorManager()
    {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;
    }

    private final SensorEventListener mSensorListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter
            if (mAccel > 8 && currentContacts!=null && currentAdapter!=null) {
                Collections.sort(currentContacts, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact c1, Contact c2) {
                        if (c1.getLastname().compareTo(c2.getLastname()) == 0) {
                            return c1.getFirstname().compareTo(c2.getFirstname());
                        }
                        else
                            return c1.getLastname().compareTo(c2.getLastname());
                    }
                });
                ContactListAdapter newAdapter = new ContactListAdapter(context,currentContacts);
                listView.setAdapter(newAdapter);
            }
            else if (mAccel > 6 && currentContacts!=null && currentAdapter!=null) {
                Collections.sort(currentContacts, new Comparator<Contact>() {
                    @Override
                    public int compare(Contact c1, Contact c2) {
                        if (c2.getLastname().compareTo(c1.getLastname()) == 0) {
                            return c2.getFirstname().compareTo(c1.getFirstname());
                        }
                        else
                            return c2.getLastname().compareTo(c1.getLastname());
                    }
                });
                ContactListAdapter newAdapter = new ContactListAdapter(context,currentContacts);
                listView.setAdapter(newAdapter);
            }



        }
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    private void logoutUser() {
        hubConnection.send("Unsubscribe","User");
        hubConnection.stop();
        homeViewModel.clearUserData();
        Log.d(CLASSNAME,"LOG OUT OK");
        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    class AlwaysOnService extends Service {

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, new Intent(this, NetworkReceiver.class), 0);

            /*Handle Android O Notifs as they need channel when targeting 28th SDK*/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                NotificationChannel notificationChannel = new NotificationChannel(
                        "download_check_channel_id",
                        "Channel name",
                        NotificationManager.IMPORTANCE_LOW);

                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                Notification.Builder builder = new Notification.Builder(this.getBaseContext(), notificationChannel.getId())
                        .setContentTitle("Hi! I'm service")
                        .setContentIntent(pendingIntent)
                        .setOngoing(true);

                Notification notification = builder.build();
                startForeground("StackOverflow".length(), notification);
            }

            return START_STICKY;
        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onLowMemory() {  // rem this if you want it always----
            stopSelf();
            super.onLowMemory();
        }


    }
}