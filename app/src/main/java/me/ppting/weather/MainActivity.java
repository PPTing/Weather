package me.ppting.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.*;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener{
    private String url;
    private String provider;
    public final static String TAG = MainActivity.class.getName();
    private TextView currentTemTextView;
    private TextView todayTemTextView;
    private ImageView logoImageView;
    private ImageView weatherImage;
    private ListView weatherInfoListView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView chooseCity;
    private DrawerLayout drawerLayout;

    private String location;
    public static final int UPDATEDAYPICURL = 1;

    Context context = MyApplication.getContext();
    private LocationManager locationManager;
    private String[] datatest = {"naning","beijing","shantou"};
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化
        currentTemTextView = (TextView)findViewById(R.id.currentTemTextView);
        todayTemTextView = (TextView)findViewById(R.id.todayTemTextView);
        logoImageView = (ImageView)findViewById(R.id.logoImageView);
        weatherInfoListView = (ListView)findViewById(R.id.listview);
        weatherImage = (ImageView)findViewById(R.id.weatherIamge);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        chooseCity = (ListView)findViewById(R.id.choosecity);
        //获取地理位置
        getLocation();
        //下拉刷新
        swipeRefreshLayout.setColorSchemeResources(R.color.orange,
                R.color.green,
                R.color.blue);
        swipeRefreshLayout.setOnRefreshListener(this);
        //侧边栏
        ArrayAdapter chooseCityAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,datatest);
        Log.d(TAG,"侧边栏adapter");
        chooseCity.setAdapter(chooseCityAdapter);
    }
    /*
    * 下拉刷新
    */
    @Override
    public void onRefresh()
    {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "下拉刷新");
                getLocation();
                swipeRefreshLayout.setRefreshing(false);
            }
        },3000);
    }
    //获取解析后的数据然后改变图标和温度
    private Handler handler = new Handler()
    {
        public void handleMessage(Message message)
        {

            switch (message.what)
            {
                case UPDATEDAYPICURL:
                    Log.d(TAG, "更新天气图");
                    logoImageView.setImageBitmap((Bitmap) message.obj);
                    break;
                default:
                    break;
            }
        }
    };
    //初始化
    public void getLocation()
    {
        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        provider = LocationManager.GPS_PROVIDER;
        Location location = locationManager.getLastKnownLocation(provider);
        Log.d(TAG, "location is " + location);
        if (location!=null) {
            double x = location.getLatitude();
            double y = location.getLongitude();
            Log.d(TAG, "x is " + x);
            Log.d(TAG, "y is " + y);
            getCity(x,y);//获取城市后getWeatherInfo()
        }
    }


    //通过经纬度获取城市
    public void getCity(final double latitude,final double longitute)
    {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    Log.d(TAG,"latitude is "+latitude);
                    Log.d(TAG, "longitute is " + longitute);
                    HttpClient httpClient = new DefaultHttpClient();
                    url = "http://api.map.baidu.com/geocoder?output=json&location="+latitude+","+longitute+"&key=gVdU1hNhSplDXKmdLtoRvK0O";
                    //url = "http://api.map.baidu.com/geocoder?output=json&location=32.046636,118.803883&key=gVdU1hNhSplDXKmdLtoRvK0O";
                    HttpPost httpPost = new HttpPost(url);
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode() == 200)
                    {
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "UTF-8");
                        Log.d(TAG, "获取到的城市地址数据 " + response);
                        //创建对象调用解析方法
                        ParseJson parseJson = new ParseJson();
                        final String city = parseJson.parseJsonWithGsonForCity(response);
                        Log.d(TAG, "city is " + city);
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
                        //将response传给 MyAsyncTask 并进行异步加载 //或者是将url传给 MyAsyncTask 然后解析出response
                        MyAsyncTask myAsyncTask = new MyAsyncTask();
                        myAsyncTask.execute(response);
                        //showWeather(response);
                        ShowTodayAsyncTask showTodayAsyncTask = new ShowTodayAsyncTask();
                        showTodayAsyncTask.execute(response);

                        //调用解析
                        Log.d(TAG,"获取到的天气数据 "+response);
                    }
                }
                catch (Exception e)
                {e.printStackTrace();}
            }
        }).start();
    }

    //加载天气列表 ListView
    class MyAsyncTask extends AsyncTask<String,Void,List<WeatherBean>>
    {
        @Override
        protected List<WeatherBean> doInBackground(String... params)
        {
            Log.d(TAG,"传入的url是 "+params[0]);
            //传入的是服务器返回的json数据，接着用parseJsonWithGsonTest 方法去解析得到天气图的url和tem，返回 url和tem到onPostExecute中
            return new ParseJson().parseJsonWithGsonTest(params[0]);
        }
        @Override
        protected void onPostExecute(List<WeatherBean> weatherBeans)
        {
            super.onPostExecute(weatherBeans);
            WeatherListAdapter adapter = new WeatherListAdapter(MainActivity.this,weatherBeans);
            weatherInfoListView.setAdapter(adapter);
        }
    }
    //异步加载当天天气图，温度，实时温度
    class ShowTodayAsyncTask extends AsyncTask<String,Void,List<TodayWeatherInfo>>
    {
        @Override
        protected List<TodayWeatherInfo> doInBackground(String... params) {
            Log.d(TAG,"传入的url 是 "+params[0]);
            return new ParseJson().parseJsonWithGsonForTodayInfo(params[0]);
        }

        @Override
        protected void onPostExecute(final List<TodayWeatherInfo> todayWeatherInfos) {
            super.onPostExecute(todayWeatherInfos);
            Log.d(TAG, "todayweatherinfos" + todayWeatherInfos);
            Log.d(TAG, "currenttem is " + todayWeatherInfos.get(0).currentTem);
            currentTemTextView.setText(todayWeatherInfos.get(0).currentTem);
            todayTemTextView.setText(todayWeatherInfos.get(0).todayTem);
            InputStream inputStream =null;
            Bitmap bitmap = null;
            //多线程加载图片
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(todayWeatherInfos.get(0).todayUrl);
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        Log.d(TAG,"bitmap of big is "+bitmap);
                        handler.obtainMessage(UPDATEDAYPICURL, bitmap).sendToTarget();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}