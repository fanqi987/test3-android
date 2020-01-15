package com.test.helloworld.test3android;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.helloworld.test3android.network.HttpUtils;
import com.test.helloworld.test3android.service.MyTimingService;
import com.test.helloworld.test3android.service.TestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;


public class MainActivity extends BaseActivity {

    private TestService.DownloadBinder binder;
    private MyTimingService.MyTimingBinder timingBinder;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binder = (TestService.DownloadBinder) service;
            binder.onStartDownload();
            binder.onProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    private ServiceConnection timingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            timingBinder = (MyTimingService.MyTimingBinder) service;
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
                bindService(i, timingServiceConnection, Service.BIND_AUTO_CREATE);
                startService(i);
            }
        });

        findViewById(R.id.timing_task_service_stop_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, MyTimingService.class);
                timingBinder.stopAlarmTask();
                unbindService(timingServiceConnection);
                stopService(i);
            }
        });

        findViewById(R.id.network_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NetworkActivity.class));
            }
        });

        findViewById(R.id.pull_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        findViewById(R.id.pull_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo 本地服务器使用的是10.0.2.2
                HttpUtils.requestHttp("http://10.0.2.2/get_data.xml", new RequestLinstener() {
                    @Override
                    public void success(String response) {
                        XMLPullParse(response);
                    }

                    private void XMLPullParse(String response) {
//                        Log.e("response", response);
                        try {
                            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                            XmlPullParser parser = factory.newPullParser();
                            parser.setInput(new StringReader(response));
                            int type = parser.getEventType();
                            String id = "";
                            String name = "";
                            String version = "";
                            while (type != XmlPullParser.END_DOCUMENT) {
                                String tagName = parser.getName();
                                switch (type) {
                                    case XmlPullParser.START_TAG: {
                                        if ("id".equals(tagName)) {
                                            id = parser.nextText();
                                        } else if ("name".equals(tagName)) {
                                            name = parser.nextText();
                                        } else if ("version".equals(tagName)) {
                                            version = parser.nextText();
                                        }
                                        break;
                                    }

                                    case XmlPullParser.END_TAG: {
                                        if ("app".equals(tagName)) {
                                            Log.e("PULL app:", "---------");
                                            Log.e("PULL id:", id);
                                            Log.e("PULL name:", name);
                                            Log.e("PULL version:", version);
                                        }
                                        break;
                                    }
                                }
                                //todo next应该是寻找下一任意元素，而nextTag应该是寻找下一个标签
//                                type = parser.nextTag();
                                type = parser.next();
                            }
                        } catch (XmlPullParserException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void fail(Exception e) {

                    }
                });
            }
        });
        findViewById(R.id.sax_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtils.requestHttp("http://10.0.2.2/get_data.xml", new RequestLinstener() {
                    @Override
                    public void success(String response) {
                        XMLSaxParse(response);
                    }

                    private void XMLSaxParse(String response) {
                        try {
                            SAXParserFactory factory = SAXParserFactory.newInstance();
                            XMLReader reader = factory.newSAXParser().getXMLReader();
                            ContentHandler handler = new ContentHandler();
                            reader.setContentHandler(handler);
                            reader.parse(new InputSource(new StringReader(response)));
                        } catch (SAXException | ParserConfigurationException | IOException e) {
                            e.printStackTrace();
                        }
                    }

                    class ContentHandler extends DefaultHandler {

                        private StringBuilder id;
                        private StringBuilder name;
                        private StringBuilder version;
                        private String tagName = "";

                        @Override
                        public void startDocument() throws SAXException {

                            //todo 使用了StringBuilder来构建，因为保存数据时，增加的是char数组
                            id = new StringBuilder();
                            name = new StringBuilder();
                            version = new StringBuilder();
                        }

                        @Override
                        public void startElement(String uri, String localName, String qName,
                                                 Attributes attributes) throws SAXException {
                            tagName = localName;
                        }

                        @Override
                        public void endElement(String uri, String localName, String qName) throws SAXException {
                            //todo 这里使用localname获取当前tag的名字，自己的tagName保存的是之前的tag的名字
//                            if ("app".equals(tagName)) {
                            if ("app".equals(localName)) {
                                Log.e("SAX app", "-------------");
                                Log.e("SAX id", id.toString().trim());
                                Log.e("SAX name", name.toString().trim());
                                Log.e("SAX version", version.toString().trim());
                                //todo sb是字符串连接类，在用于保存时，每次使用setLength来清空
                                id.setLength(0);
                                name.setLength(0);
                                version.setLength(0);
                            }
                        }

                        @Override
                        public void characters(char[] ch, int start, int length) throws SAXException {
                            switch (tagName) {
                                case "id":
                                    //todo  使用sb来增加char数组
                                    id.append(ch, start, length);
                                    break;
                                case "name":
                                    name.append(ch, start, length);
                                    break;
                                case "version":
                                    version.append(ch, start, length);
                                    break;
                            }
                        }
                    }

                    @Override
                    public void fail(Exception e) {

                    }
                });
            }
        });

        findViewById(R.id.json_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtils.requestHttp("http://10.0.2.2/get_data.json", new RequestLinstener() {
                    @Override
                    public void success(String response) {
                        try {
                            JSONArray array = new JSONArray(response);
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                Log.e("JSON app", "----------");
                                Log.e("id", object.getString("id"));
                                Log.e("name", object.getString("name"));
                                Log.e("version", object.getString("version"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void fail(Exception e) {

                    }
                });
            }
        });

        findViewById(R.id.gson_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtils.requestHttp("http://10.0.2.2/get_data.json", new RequestLinstener() {
                    @Override
                    public void success(String response) {
                        Gson gson = new Gson();
                        List<App> apps = gson.fromJson(response, new TypeToken<List<App>>() {
                        }.getType());
                        for (App app : apps) {
                            Log.e("GSON app", "---------");
                            Log.e("GSON id", app.getId());
                            Log.e("GSON name", app.getName());
                            Log.e("GSON version", app.getVersion());
                        }
                    }

                    @Override
                    public void fail(Exception e) {

                    }

                    class App {
                        private String id;
                        private String name;
                        private String version;

                        public String getId() {
                            return id;
                        }

                        public void setId(String id) {
                            this.id = id;
                        }

                        public String getName() {
                            return name;
                        }

                        public void setName(String name) {
                            this.name = name;
                        }

                        public String getVersion() {
                            return version;
                        }

                        public void setVersion(String version) {
                            this.version = version;
                        }
                    }
                });


            }
        });

    }

}
