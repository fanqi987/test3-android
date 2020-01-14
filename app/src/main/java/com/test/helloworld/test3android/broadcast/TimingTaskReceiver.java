package com.test.helloworld.test3android.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.test.helloworld.test3android.service.MyTimingService;

public class TimingTaskReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i =new Intent(context, MyTimingService.class);
        context.startService(i);
    }
}
