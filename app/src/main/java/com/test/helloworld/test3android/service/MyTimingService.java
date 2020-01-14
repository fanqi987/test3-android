package com.test.helloworld.test3android.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.test.helloworld.test3android.broadcast.TimingTaskReceiver;

public class MyTimingService extends Service {

    private PendingIntent pi;

    public class MyTimingBinder extends Binder{
        public void stopAlarmTask(){
            AlarmManager am= (AlarmManager) getSystemService(ALARM_SERVICE);
            am.cancel(pi);
        }
    }
    private MyTimingBinder binder=new MyTimingBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.e("定时服务已销毁","done");

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("定时服务已启动","done");
//                stopSelf();
            }
        }).start();



        AlarmManager am= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent i=new Intent(this, TimingTaskReceiver.class);
         pi=PendingIntent.getBroadcast(this,0,i,0);
        am.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+10*1000,pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
