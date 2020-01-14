package com.test.helloworld.test3android.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

public class TestIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public TestIntentService() {
        super("TestIntentService");
    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("启动服务","done");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("销毁服务","done");
    }
}
