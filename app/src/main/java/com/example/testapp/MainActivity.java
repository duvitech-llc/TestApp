package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Messenger myService = null;
    boolean isBound;

    Intent intent = null;

    private ServiceConnection myConnection =
            new ServiceConnection() {
                public void onServiceConnected(
                        ComponentName className,
                        IBinder service) {
                    Log.d(TAG,"Service Connected");
                    myService = new Messenger(service);
                    isBound = true;
                }

                public void onServiceDisconnected(
                        ComponentName className) {
                    Log.d(TAG,"Service Disconnected");
                    myService = null;
                    isBound = false;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    protected void onStart() {
        Log.d(TAG,"onStart");
        super.onStart();
    }


    @Override
    protected void onResume() {
        Log.d(TAG,"onResume");
        super.onResume();
        intent = new Intent(getApplicationContext(),
                RemoteService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        Log.d(TAG,"onPause");
        unbindService(myConnection);
        super.onPause();

    }

    @Override
    protected void onStop() {
        Log.d(TAG,"onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG,"onDestroy");
        super.onDestroy();
    }

    public void sendMessage(View view)
    {
        if (!isBound) return;

        Message msg = Message.obtain();

        Bundle bundle = new Bundle();
        bundle.putString("MyString", "Message Received");

        msg.setData(bundle);

        try {
            myService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
