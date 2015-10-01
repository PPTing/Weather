package me.ppting.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.*;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private String[] data;
    private String url;
    private String provider;
    public final static String TAG = MainActivity.class.getName();
    private TextView currentTemTextView;
    private TextView todayTemTextView;
    private ImageView logoImageView;
    private ImageView weatherImage;
    private ListView weatherInfoListView;


    private String location;
    private double latitude;
    private double longitute;
    public static final int UPDATEREALTEM = 1;
    public static final int UPDATETODAYTEM = 2;
    public static final int UPDATEDAYPICURL = 3;

    Context context = MyApplication.getContext();
    private List<Weather> weatherList = new ArrayList<Weather>();

    private LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        //初始化
        init();

        Adapter adapter = new Adapter(MainActivity.this,R.layout.weatherinfo,weatherList);
        weatherInfoListView.setAdapter(adapter);
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
        //定位
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        Log.d(TAG,"location is "+location);
        if (location!=null) {
            double x = location.getLatitude();
            double y = location.getLongitude();
            Log.d(TAG, "x is " + x);
            Log.d(TAG, "y is " + y);
            getCity(x,y);//获取到城市以后
        }
        //getCity();
        showWeather();//显示天气
        currentTemTextView = (TextView)findViewById(R.id.currentTemTextView);
        todayTemTextView = (TextView)findViewById(R.id.todayTemTextView);
        logoImageView = (ImageView)findViewById(R.id.logoImageView);
        weatherInfoListView = (ListView)findViewById(R.id.listview);
        weatherImage = (ImageView)findViewById(R.id.weatherIamge);
        //初始化Listview
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        MyAsyncTask myAsyncTask = new MyAsyncTask(context);

        String day2tem = sharedPreferences.getString("secondTem", "");
        String day2PictureUrl = sharedPreferences.getString("day2Picture", "");
        Bitmap day2bitmap = myAsyncTask.doInBackground(day2PictureUrl);
        //Log.d(TAG,"myAsyncTask.execute is "+myAsyncTask.execute(day2PictureUrl));
        myAsyncTask.execute(day2PictureUrl);
        Weather day2 = new Weather(day2bitmap,day2tem);
        weatherList.add(day2);

        String day3tem = sharedPreferences.getString("thirdTem", "");
        String day3PictureUrl = sharedPreferences.getString("day2Picture", "");
        Bitmap day3bitmap = myAsyncTask.doInBackground(day3PictureUrl);
        Weather day3 = new Weather(day3bitmap,day3tem);
        weatherList.add(day3);

        String day4Tem = sharedPreferences.getString("fourthTem", "");
        String day4PictureUrl = sharedPreferences.getString("day4Picture", "");
        Bitmap day4bitmap = myAsyncTask.doInBackground(day4PictureUrl);
        Weather day4 = new Weather(day4bitmap,day4Tem);
        weatherList.add(day4);


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
                        Log.d(TAG,"bitmap of big is "+bitmap);
                        handler.obtainMessage(UPDATEDAYPICURL, bitmap).sendToTarget();
                    }else {
                        Toast.makeText(context,"网络请求失败",Toast.LENGTH_LONG).show();}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void getCity(final double latitude,final double longitute)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Log.d(TAG,"latitude is "+latitude);
                    Log.d(TAG,"longitute is "+longitute);
                    HttpClient httpClient = new DefaultHttpClient();
                    url = "http://api.map.baidu.com/geocoder?output=json&location="+latitude+","+longitute+"&key=gVdU1hNhSplDXKmdLtoRvK0O";
                    //url = "http://api.map.baidu.com/geocoder?output=json&location=32.046636,118.803883&key=gVdU1hNhSplDXKmdLtoRvK0O";
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200)
                    {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "UTF-8");
                        Log.d(TAG,"获取到的数据 "+response);
                        //创建对象调用解析方法
                        ParseJson parseJson = new ParseJson();
                        final String city = parseJson.parseJsonWithGsonForCity(response);
                        Log.d(TAG,"city is "+city);
                        //获取天气信息
                        getWeatherInfo();
                    }
                }
                catch (Exception e)
                {e.printStackTrace();};
            }
        }).start();
    }
    //发送请求获得返回的天气信息json数据
    public void getWeatherInfo()
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
                        //parseJson.parseJsonWithGson(response);
                        parseJson.parseJson(response);
                    }
                }
                catch (Exception e)
                {e.printStackTrace();}

            }
        }).start();
    }
}
