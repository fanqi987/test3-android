package com.test.helloworld.test3android;

import android.app.AlarmManager;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.telecom.ConnectionService;
import android.view.View;

import androidx.annotation.Nullable;

import com.test.helloworld.test3android.service.MyTimingService;
import com.test.helloworld.test3android.service.TestService;


public class MainActivity extends BaseActivity {

    private TestService.DownloadBinder binder;
    private MyTimingService.MyTimingBinder timingBinder;

    private ServiceConnection serviceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder= (TestService.DownloadBinder) service;
            binder.onStartDownload();
            binder.onProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private ServiceConnection timingServiceConnection=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            timingBinder= (MyTimingService.MyTimingBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        findViewById(R.id.start_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TestService.class);
                startService(i);
            }
        });

        findViewById(R.id.stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TestService.class);
                stopService(i);
            }
        });

        findViewById(R.id.bind_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this, TestService.class);
                bindService(i, serviceConnection, Service.BIND_AUTO_CREATE);
            }
        });

        findViewById(R.id.unbind_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService(serviceConnection);
            }
        });

        findViewById(R.id.timing_task_service_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyTimingService.class);
                bindService(i,timingServiceConnection,Service.BIND_AUTO_CREATE);
                startService(i);
            }
        });

        findViewById(R.id.timing_task_service_stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,MyTimingService.class);
                timingBinder.stopAlarmTask();
                unbindService(timingServiceConnection);
                stopService(i);
            }
        });

        findViewById(R.id.network_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NetworkActivity.class));
            }
        });
    }
}
