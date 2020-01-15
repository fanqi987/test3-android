package com.test.helloworld.test3android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkActivity extends BaseActivity {


    private static final int NETWORK_COMPLETE = 0;
    private URL urlUrl;
    private URL urlClient;
    private HttpURLConnection connection;
    private HttpClient httpClient;

    private TextView networkTv;
    private Button clearBtn;

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case NETWORK_COMPLETE:
                    networkTv.setText((String) msg.obj);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.network);

        clearBtn = findViewById(R.id.clear_btn);
        networkTv = findViewById(R.id.net_test_tv);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkTv.setText("");
            }
        });
        findViewById(R.id.url_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BufferedReader br = null;
                        BufferedWriter bw = null;
                        try {
                            urlUrl = new URL("https://www.baidu.com");
                            connection = (HttpURLConnection) urlUrl.openConnection();
                            connection.setReadTimeout(5000);
                            connection.setConnectTimeout(5000);
                            connection.setRequestMethod("GET");
                            //todo  要提交的post表单，在获取输入流前写出即可
//                            connection.setRequestMethod("POST");
//                            OutputStream os=connection.getOutputStream();
//                            bw=new BufferedWriter(new OutputStreamWriter(os));
//                            bw.write("name=admin&password=123456");
                            //todo  书中使用的是DataOutputstream
//                    DataOutputStream dos=new DataOutputStream(os);
//                    dos.writeBytes("name=admin&password=123456");
                            InputStream is = connection.getInputStream();
                            br = new BufferedReader(new InputStreamReader(is));
                            StringBuilder sb = new StringBuilder();
                            String s = new String();
                            while ((s = br.readLine()) != null) {
                                sb.append(s);
                            }
                            //todo  必须要关闭连接
//                    networkTv.setText(sb);
                            Message m = new Message();
                            m.what = NETWORK_COMPLETE;
                            m.obj = sb.toString();
                            handler.sendMessage(m);
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            if (br != null) {
                                try {
                                    br.close();
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
        });

        findViewById(R.id.client_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //todo HTTPClient不使用URL,而是把网页地址传入到HttpPost或HttpGet中
//                            urlClient=new URL("http://www.baidu.com");
                            httpClient = new DefaultHttpClient();
                            HttpGet hg = new HttpGet("https://www.baidu.com");
                            //todo HTTPClient的post相关
//                            HttpPost hp=new HttpPost("https://www.baidu.com");

//                            List<NameValuePair> params=new ArrayList<NameValuePair>();
//                            params.add(new BasicNameValuePair("name","admin"));
//                            params.add(new BasicNameValuePair("password","123456"));
//                            UrlEncodedFormEntity entity=new UrlEncodedFormEntity(params,"UTF-8");
//                            hp.setEntity(entity);
//                            HttpResponse response=httpClient.execute(hp);
                            HttpResponse response = httpClient.execute(hg);
                            if (response.getStatusLine().getStatusCode() == 200) {
                                HttpEntity entity1 = response.getEntity();
                                //todo entity结果，使用Entity工具类EntityUtils直接转换为String即可
                                String s = EntityUtils.toString(entity1, "UTF-8");
//                        InputStream is=entity1.getContent();
//                        DataInputStream ds=new DataInputStream(is);
//                        StringBuilder sb=new StringBuilder();
//                        String s="";
//                        if((s=ds.readLine())!=null){
//                            sb.append(s);
//                        }
//                                networkTv.setText(s);
                                Message m = new Message();
                                m.what = NETWORK_COMPLETE;
                                m.obj = s;
                                handler.sendMessage(m);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                        }
                    }
                }).start();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
