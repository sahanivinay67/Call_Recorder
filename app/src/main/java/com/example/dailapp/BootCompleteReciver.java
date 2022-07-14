package com.example.dailapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReciver extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {


        Intent serviceIntent = new Intent(context, CallRecorder.class);
        context.startService(serviceIntent);
    }

}
