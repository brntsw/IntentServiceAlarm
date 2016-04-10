package br.com.bruno.intentservicealarm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Bruno on 10/04/2016.
 */
public class MyAlarmReceiver extends BroadcastReceiver {
    public static final int REQUEST_CODE = 1;
    public static final String ACTION = "br.com.bruno.intentservicealarm";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, MyTestService.class);
        i.putExtra("foo", "bar");
        context.startService(i);
    }
}
