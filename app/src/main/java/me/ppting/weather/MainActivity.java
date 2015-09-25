package me.ppting.weather;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private String[] data;
    private String url;
    public final static String TAG = MainActivity.class.getName();
    private TextView currentTemTextView;
    private TextView todayTemTextView;
    private ImageView logoImageView;


    private String location;
    public static final int UPDATEREALTEM = 1;
    public static final int UPDATETODAYTEM = 2;
    public static final int UPDATEDAYPICURL = 3;

    Context context = MyApplication.getContext();


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        init();

        //ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                MainActivity.this, android.R.layout.simple_list_item_1, data);
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(adapter);

    }

    //获取解析后的数据然后改变图标和温度
    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    private Handler handler = new Handler()
    {
        public void handleMessage(Message message)
        {

            switch (message.what)
            {
                case UPDATEREALTEM:
                    currentTemTextView.setText(sharedPreferences.getString("realTem", ""));//更新实时温度
                    Log.d(TAG,"更新实时温度");
                    break;
                case UPDATETODAYTEM:
                    Log.d(TAG,"更新全天温度");
                    todayTemTextView.setText(sharedPreferences.getString("todayTem", ""));//更新全天温度
                    break;
                case UPDATEDAYPICURL:
                    Log.d(TAG,"更新天气图");
                    //这样获取到的是点击按钮前存放在xml文件里头的url
                    Log.d(TAG,"updatedaypicurl is "+sharedPreferences.getString("dayPictureUrl",""));
                    logoImageView.setImageBitmap((Bitmap)message.obj);
                    break;
                default:
                    break;
            }
        }
    };
    //初始化
    public void init()
    {
        sendRequest2Server();
        showWeather();
        currentTemTextView = (TextView)findViewById(R.id.currentTemTextView);
        todayTemTextView = (TextView)findViewById(R.id.todayTemTextView);
        logoImageView = (ImageView)findViewById(R.id.logoImageView);
    }
    //发送请求获得返回的天气信息json数据
    private void sendRequest2Server()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    location = "南京";
                    url = "http://api.map.baidu.com/telematics/v3/weather?location="+location+"&output=json&ak=gVdU1hNhSplDXKmdLtoRvK0O";
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode()==200)
                    {
                        Log.d(TAG,"服务器返回的代码为 "+httpResponse.getStatusLine().getStatusCode());
                        Log.d(TAG,"获取json成功");
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "UTF-8");

                        //调用解析
                        Log.d(TAG,"获取到的数据 "+response);
                        //创建对象调用解析方法
                        ParseJson parseJson = new ParseJson();
                        parseJson.parseJsonWithGson(response);
                        parseJson.parseJson(response);
                    }
                }
                catch (Exception e)
                {e.printStackTrace();}

            }
        }).start();
    }
    //显示天气
    public void showWeather()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                Log.d(TAG, "Runnable is running");
                Message msgUpdateRealTem = new Message();//更新实时温度
                msgUpdateRealTem.what = UPDATEREALTEM;
                handler.sendMessage(msgUpdateRealTem);
                Message msgUpdateTodayTem = new Message();//更新全天温度
                msgUpdateTodayTem.what = UPDATETODAYTEM;
                handler.sendMessage(msgUpdateTodayTem);

                try {//更新天气图标 获取存储在xml文件中的url并访问获取图标图片
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    String dayUrl = sharedPreferences.getString("dayPictureUrl","");
                    URL url = new URL(dayUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    if (connection.getResponseCode()==200) {
                        InputStream inputStream = connection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        handler.obtainMessage(UPDATEDAYPICURL, bitmap).sendToTarget();
                    }else {
                        Toast.makeText(context,"网络请求失败",Toast.LENGTH_LONG).show();}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
