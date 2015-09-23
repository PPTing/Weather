package me.ppting.weather;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;


import java.net.HttpURLConnection;

import java.util.List;
import java.util.logging.LogRecord;

public class MainActivity extends ActionBarActivity {
    private String[] data;
    private String url;
    public final static String TAG = MainActivity.class.getName();
    private Button testbutton;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    //初始化
        init();

        //ListView
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
//                MainActivity.this, android.R.layout.simple_list_item_1, data);
//        ListView listView = (ListView) findViewById(R.id.listview);
//        listView.setAdapter(adapter);

    }
    private void sendRequest2Server()
    {
        new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    HttpClient httpClient = new DefaultHttpClient();
                    url = "http://api.map.baidu.com/telematics/v3/weather?location=南京&output=json&ak=gVdU1hNhSplDXKmdLtoRvK0O";
                    HttpGet httpGet = new HttpGet(url);
                    HttpResponse httpResponse = httpClient.execute(httpGet);
                    if (httpResponse.getStatusLine().getStatusCode()==200)
                    {
                        Log.d(TAG,"get 成功");
                        HttpEntity entity = httpResponse.getEntity();
                        String response = EntityUtils.toString(entity, "UTF-8");
                        //传递message给handler 用于改变天气图标
                        Message message = new Message();
                        message.what = 1;
                        message.obj = response.toString();
                        handler.sendMessage(message);
                        //
                        //调用解析
                        Log.d(TAG,""+response);
                        parseJsonWithGson(response);
                        parseJson(response);

                    }

                }
                catch (Exception e)
                {e.printStackTrace();}
            }
        }).start();
    }
    //获取解析后的数据然后改变图标和温度 先写Log 无内容
    private Handler handler = new Handler()
    {
        public void handleMessage(Message message)
        {
            switch (message.what)
            {
                case 1:
                    Log.d(TAG,"获取数据成功");
                    break;
                default:
                    break;
            }
        }
    };

    //用gson解析返回的数据
    private void parseJsonWithGson(String jsonData) {
        Gson gson = new Gson();
        WeatherInfo weatherInfo = gson.fromJson(jsonData,WeatherInfo.class);
        Log.d(TAG,"weatherInfo"+weatherInfo);
        //下面两行 解析为数组
        //List<WeatherInfo> weatherList = gson.fromJson(jsonData, new TypeToken<List<WeatherInfo>>(){}.getType());
        //for (WeatherInfo weatherInfo : weatherList)
        WeatherInfo.Results results = gson.fromJson(jsonData,WeatherInfo.Results.class);
        Log.d(TAG,"error is "+weatherInfo.getError());
        Log.d(TAG,"status is "+weatherInfo.getStatus());
        Log.d(TAG,"date is "+weatherInfo.getDate());
        /////////////////////////////////////////////////////

        WeatherInfo w = new WeatherInfo();
        WeatherInfo.Results results1 = new WeatherInfo.Results();
        Log.d(TAG, "" + results1.getCurrentCity());

    }
    //解析response
    private void parseJson(String jsonData)
    {
        try
        {

            org.json.JSONObject jsonObject = new org.json.JSONObject(jsonData);
            Log.d(TAG,"error is "+jsonObject.get("error"));
            Log.d(TAG,"status is "+jsonObject.get("status"));
            Log.d(TAG,"date is "+jsonObject.get("date"));
            org.json.JSONArray resultsJsonArray = jsonObject.getJSONArray("results");
            for (int i = 0;i<resultsJsonArray.length();i++)
            {
                org.json.JSONObject jsonObjectInResults = resultsJsonArray.getJSONObject(i);
                Log.d(TAG,"pm25 is "+jsonObjectInResults.get("pm25"));
                Log.d(TAG,"currentCity is "+jsonObjectInResults.get("currentCity"));
                //遍历index
                org.json.JSONArray indexArray = jsonObjectInResults.getJSONArray("index");
                for (int j = 0;j<indexArray.length();j++)
                {
                    JSONObject jsonObjectInIndex = indexArray.getJSONObject(j);
                    Log.d(TAG,"title is "+jsonObjectInIndex.get("title"));
                    Log.d(TAG,"zs is "+jsonObjectInIndex.get("zs"));
                    Log.d(TAG,"tips is "+jsonObjectInIndex.get("tipt"));
                    Log.d(TAG,"des is "+jsonObjectInIndex.get("des"));
                }
                //遍历weather_data
                JSONArray weatherDataArray = jsonObjectInResults.getJSONArray("weather_data");
                for (int k = 0; k<weatherDataArray.length();k++)
                {
                    JSONObject jsonObjectInWeatherData = weatherDataArray.getJSONObject(k);
                    Log.d(TAG,"date is "+jsonObjectInWeatherData.get("date"));
                    Log.d(TAG,"dayPictureUrl is "+jsonObjectInWeatherData.get("dayPictureUrl"));
                    Log.d(TAG,"nightPictureUrl is "+jsonObjectInWeatherData.get("nightPictureUrl"));
                    Log.d(TAG,"weather is "+jsonObjectInWeatherData.get("weather"));
                    Log.d(TAG,"wind is "+jsonObjectInWeatherData.get("wind"));
                    Log.d(TAG,"temperature is "+jsonObjectInWeatherData.get("temperature"));
                }
            }


        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    //初始化
    public void init()
    {
        testbutton = (Button)findViewById(R.id.testbutton);
        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRequest2Server();
                Log.d(TAG,"button click");
            }
        });
    }
}
