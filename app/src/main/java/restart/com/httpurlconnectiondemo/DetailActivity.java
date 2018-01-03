package restart.com.httpurlconnectiondemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {
    private TextView nameView,authorView,contentView;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Essay e = (Essay) msg.obj;
            nameView.setText(e.getTitle());
            authorView.setText(e.getAuthor());
            contentView.setText(e.getContent());
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        initData();//初始化数据
    }

    private void initView() {
        nameView = findViewById(R.id.name);
        authorView = findViewById(R.id.author);
        contentView = findViewById(R.id.content);
    }

    private void initData() {
        //HttpUrlConnection
        /*
        * 实例化url对象
        * 获取HttpUrlConnection对象
        * 设置请求链接属性
        * 获取响应码，判断是否连接成功
        * 获取输入流并解析
        *
        * */
        //url
        //所有网络访问都不能在主线程中完成！
        new Thread(){
            @Override
            public void run() {
                try {
                    URL url = new URL("http://www.imooc.com/api/teacher?type=3&cid=1");
                    HttpURLConnection coon = (HttpURLConnection) url.openConnection();
                    //设置请求方式
                    coon.setRequestMethod("GET");
                    //设置相应时间
                    coon.setReadTimeout(6000);
                    //发送请求
                    //获取响应码（该方法会自动向服务器请求连接）

                    if(coon.getResponseCode()==200){
                        //获取输入流
                        InputStream in = coon.getInputStream();
                        byte[] b = new byte[1024*521];
                        int len = 0;
                        //建立缓存流  保存所读取的字节数组
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();

                        while ((len = in.read(b))>-1){
                            baos.write(b,0,len);
                        }
                        String msg = baos.toString();
                        Log.i("TAG", "run: "+ msg);
                        //JSON数据的解析
                        JSONObject obj = new JSONObject(msg);
                        int status = obj.getInt("status");
                        String msg2 = obj.getString("msg");
                        Log.i("TAG", "run: "+status+"   "+msg2);
                        JSONObject data = obj.getJSONObject("data");
                        String title = data.getString("title");
                        String author = data.getString("author");
                        String content = data.getString("content");
                        Log.i("TAG", "run: 标题："+title+",作者："+author+", 内容："+content);
                        //将操作交还给主线程
                        Message message = handler.obtainMessage();
                        message.obj = new Essay(title,author,content);
                        handler.sendMessage(message);

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
