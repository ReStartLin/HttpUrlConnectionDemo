package restart.com.httpurlconnectiondemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity {
    private TextView tvheader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvheader = findViewById(R.id.header);
        tvheader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, DetailActivity.class));
            }
        });
        initData();
    }

    private void initData() {
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.imooc.com/api/teacher?type=2");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(6000);
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        byte[] b = new byte[1024 * 512];
                        int len = 0;
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        while ((len = is.read(b)) > -1) {
                            baos.write(b, 0, len);
                        }
                        String result = baos.toString();
                        Log.i("TAG", "run: " + result);
                        //Gson工具（快速解析json）
                        //解析普通的json对象
                        String data = new JSONObject(result).getString("data");
                        Gson gson = new Gson();
                        //参数一：满足json数据形式的字符串
                        //参数二：Type对象泛型将会决定你的json字符串最后被转化成的类型
                        ArrayList<Outline> outlines=gson.fromJson(data,new TypeToken<ArrayList<Outline>>(){}.getType());
                        for (Outline o:outlines) {
                            Log.i("TAG", "run: id:"+o.getId()+"，标题：" +
                                    o.getDescription());

                        }
                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}
