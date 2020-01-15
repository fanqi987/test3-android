package com.test.helloworld.test3android.network;

import android.util.Log;

import com.test.helloworld.test3android.RequestLinstener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtils {

    public static void requestHttp(final String urlString, final RequestLinstener linstener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url;
                HttpURLConnection connection = null;
                BufferedReader bf = null;
                StringBuilder sb = new StringBuilder();
                try {
                    Log.e("urlString", urlString);
                    url = new URL(urlString);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(5000);
                    InputStream is = connection.getInputStream();
                    bf = new BufferedReader(new InputStreamReader(is));
                    String s;
                    while ((s = bf.readLine()) != null) {
                        sb.append(s);
                    }
                    linstener.success(sb.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                    linstener.fail(e);
                } finally {
                    if (bf != null) {
                        try {
                            bf.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
