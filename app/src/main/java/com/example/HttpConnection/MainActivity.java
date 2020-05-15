package com.example.HttpConnection;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String Tag = "MainActivity";

    private Button Get_Button, Post_Button, JsonParse_Button;


    private EditText acc_Edit, Pwd_Edit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Get_Button = (Button) findViewById(R.id.get);
        Get_Button.setOnClickListener(this);
        Post_Button = (Button) findViewById(R.id.post);
        Post_Button.setOnClickListener(this);
        JsonParse_Button = (Button) findViewById(R.id.Json_Parse);
        JsonParse_Button.setOnClickListener(this);


        acc_Edit = (EditText) findViewById(R.id.account);
        Pwd_Edit = (EditText) findViewById(R.id.pwd);


    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        }};

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void get() {

        try {
//            实例化URL对象，URL
            URL url = new URL("http://guolin.tech/api/china/1/1");
//            获取HttpURLConnection实例
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            请求方式
            conn.setRequestMethod("GET");
//            请求超时时长
            conn.setConnectTimeout(6000);
//            获得相应码
//            判断响应码并获得响应数据
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

//                Java数据读写常用的流操作
//                InputStream负责读数据(文件)，OutputStream负责写数据（文件）
//                为了进一步提高文件的效率，又引入
//                字节缓冲输出流 BufferedOutputStream
//                字节缓冲输入流 BufferedInputStream

//                获得输入流
                InputStream in = conn.getInputStream();

//                设置字节数组操作输入流
                byte[] b = new byte[1024];
                int len = 0;

//                将输入流数据写入到缓冲流中，效率高。
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                在循环中读取输入流
//                in.read(b) 返回int数据，代表字节数组中实际读到的长度
                while ((len = in.read(b)) > -1) {
//                    将字节数组的数据写入到缓存流中
//                    参数1：待写入的字节数组
//                    参数2：字节数组的写入起点
//                    参数3：长度（写入的终点）
                    baos.write(b, 0, len);
                }

                String msg = new String(baos.toByteArray());
                Log.d(Tag, msg);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void post(String account, String pwd) {
        try {
//            实例化URL对象，URL
            URL url = new URL("https://www.imooc.com/api/okhttp/postmethod");

            HttpURLConnection conn = null;

            if (url.getProtocol().toLowerCase().equals("https")) {
                trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                conn = https;
            } else {
                conn = (HttpURLConnection) url.openConnection();
            }



//            请求方式
            conn.setRequestMethod("POST");
//            请求超时时长
            conn.setConnectTimeout(6000);

//            设置允许输出
            conn.setDoOutput(true);
//            设置请求数据类型
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");


//            获取输出流(写入POST请求的正文)
            OutputStream out = conn.getOutputStream();
            out.write(("account=" + account + "&pwd=" + pwd).getBytes());

//            获得相应码
//            判断响应码并获得响应数据
            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

//                Java数据读写常用的流操作
//                InputStream负责读数据(文件)，OutputStream负责写数据（文件）
//                为了进一步提高文件的效率，又引入
//                字节缓冲输出流 BufferedOutputStream
//                字节缓冲输入流 BufferedInputStream

//                获得输入流(响应正文)
                InputStream in = conn.getInputStream();

//                设置字节数组操作输入流
                byte[] b = new byte[1024];
                int len = 0;

//                将输入流数据写入到缓冲流中，效率高。
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                在循环中读取输入流
//                in.read(b) 返回int数据，代表字节数组中实际读到的长度
                while ((len = in.read(b)) > -1) {
//                    将字节数组的数据写入到缓存流中
//                    参数1：待写入的字节数组
//                    参数2：字节数组的写入起点
//                    参数3：长度（写入的终点）
                    baos.write(b, 0, len);
                }

                String msg = new String(baos.toByteArray());
                Log.d(Tag, msg);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void post_Method(String account, String pwd) {
        try {
            URL url = new URL("https://www.imooc.com/api/okhttp/postmethod");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStream out = conn.getOutputStream();
//            out.write((account + "&" + pwd).getBytes());
            out.write(("account=" + account + "&pwd=" + pwd).getBytes());

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = conn.getInputStream();
                byte[] b = new byte[1024 * 1024];
                int len = 0;
                ByteArrayOutputStream buff = new ByteArrayOutputStream();

                while ((len = in.read(b)) != -1) {
                    buff.write(b, 0, len);
                }

                Log.d(Tag, buff.toString());
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            Log.d 显示
            case R.id.get:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        get();
                    }
                }).start();
                break;

            case R.id.post:
//                Log.d显示
                final String account = acc_Edit.getText().toString();
                final String pwd = Pwd_Edit.getText().toString();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        post(account, pwd);
                    }
                }).start();
                break;

            case R.id.Json_Parse:
                startActivity(new Intent(MainActivity.this, JsonActivity.class));
                break;

            default:
                break;
        }
    }
}

