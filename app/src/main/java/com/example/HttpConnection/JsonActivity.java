package com.example.HttpConnection;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.HttpConnection.Gson.Info;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonActivity extends AppCompatActivity implements View.OnClickListener {
    private final String Tag = "JsonActivity";

    private Button JsonParse_Button, GsonParse_Button, JsonArray_Button;

    private TextView tv1, tv2;

    private ListView listView;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_json);


        tv1 = (TextView) findViewById(R.id.status);
        tv2 = (TextView) findViewById(R.id.msg);

        JsonParse_Button = (Button) findViewById(R.id.Json_Parse);
        JsonParse_Button.setOnClickListener(this);
        GsonParse_Button = (Button) findViewById(R.id.GSON_Parse);
        GsonParse_Button.setOnClickListener(this);
        JsonArray_Button = (Button) findViewById(R.id.Json_Array);
        JsonArray_Button.setOnClickListener(this);

        listView = (ListView) findViewById(R.id.list_view);


    }


    private String get(String url) {
        try {
//      实例化管理网络地址的对象
            URL Url = new URL(url);
//      实例化管理网络连接的对象,设置timeout和get方法
            HttpURLConnection conn = (HttpURLConnection) Url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//          处理流数据，保存为String类型
                InputStream in = conn.getInputStream();

                byte[] bytes = new byte[1024];
                int len = 0;

                ByteArrayOutputStream out = new ByteArrayOutputStream();
                while ((len = in.read(bytes)) != -1) {
                    out.write(bytes, 0, len);
                }

                return out.toString();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void parseByJsonObject() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "https://www.imooc.com/api/teacher?type=3&cid=1";
                try {
                    String res = get(url);
                    JSONObject obj = new JSONObject(res);
                    final int status = obj.getInt("status");
                    final String message = obj.getString("msg");

                    Log.d(Tag, obj.getJSONObject("data") + "");
                    Log.d(Tag, status + "");
                    Log.d(Tag, message);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv1.setText(String.valueOf(status));
                            tv2.setText(message);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void parseJsonArray() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String res = get("https://www.imooc.com/api/teacher?type=2&cid=1");
                    JSONObject obj = new JSONObject(res);
                    JSONArray arr = obj.getJSONArray("data");

                    List<Map<String, String>> list = new ArrayList<>();

                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject temp = arr.getJSONObject(i);

                        String name = temp.getString("name");
                        String id = temp.getInt("id") + "";

                        Map<String, String> map = new HashMap<>();
                        map.put("name", name);
                        map.put("id", id);

                        list.add(map);
                    }

//                    创建SimpleAdapter(数据源 List<Map<String,Object>>, 布局资源 R.layout.item.xml, from, to)
                    String[] from = {"id", "name"};
                    int[] to = {R.id.item_id, R.id.item_name};

                    final SimpleAdapter adapter = new SimpleAdapter(JsonActivity.this, list, R.layout.item, from, to);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listView.setAdapter(adapter);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();


    }

    private void parseGson() {

////        实例化GSON对象（工具对象）
//        Gson gson = new Gson();
////        toJson:将对象变为json字符串
//        String res = gson.toJson(new Book("title","author","Content"));
//        Log.d(Tag,res);
//
////        fromJson：将字符串转换为实体类
//        Book b2 = gson.fromJson(res,Book.class);
//        Log.d(Tag,b2.toString());


        new Thread(new Runnable() {
            @Override
            public void run() {
                Gson gson = new Gson();
                String response = get("https://www.imooc.com/api/teacher?type=3&cid=1");
                if (response != null) {
                    Info info = gson.fromJson(response, Info.class);
                    Log.d(Tag, info.toString());
                }
            }
        }).start();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            Log.d
            case R.id.GSON_Parse:
                parseGson();
                break;
//                Log.d + textview
            case R.id.Json_Parse:
                parseByJsonObject();
                break;
//                listView
            case R.id.Json_Array:
                parseJsonArray();
                break;
            default:
                break;
        }
    }
}
