package com.test.helloworld.test3android.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import androidx.annotation.Nullable;

import com.test.helloworld.test3android.R;

public class TestService extends Service {


    //todo 是继承Binder，它是IBinder的一个实现类，应该是基础的任务绑定类
     public  class DownloadBinder extends  Binder {
        public void onStartDownload(){
            Log.e("开始下载执行","done");
        }

        public void onProgress(){
            Log.e("下载进度执行","done");
        }
     }
    private DownloadBinder binder=new DownloadBinder();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.e("绑定服务","done");
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e("解绑服务","done");
        return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("创建服务","done");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("启动服务","done");

        NotificationChannel channel=new NotificationChannel("service_test","service test",
                NotificationManager.IMPORTANCE_HIGH);
        NotificationManager nm= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.createNotificationChannel(channel);
        Notification.Builder builder=new Notification.Builder(this,"service_test");
        builder.setTicker("一条消息");
        builder.setContentTitle("服务消息标题");
        builder.setContentText("服务消息内容");
        builder.setSmallIcon(Icon.createWithResource(this,R.mipmap.ic_launcher));
        builder.setWhen(SystemClock.elapsedRealtime());
        builder.setShowWhen(true);
//        builder.setTimeoutAfter(System.currentTimeMillis());
        builder.setLargeIcon(Icon.createWithResource(this,R.mipmap.ic_launcher));
        Notification notification=builder.build();
        //todo 需要foreground service 权限
        startForeground(1,notification);
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("销毁服务","done");
    }
}
