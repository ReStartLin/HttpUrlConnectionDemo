package restart.com.httpurlconnectiondemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initData();//初始化数据
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

                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
