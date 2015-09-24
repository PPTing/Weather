package me.ppting.weather;

import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by PPTing on 15/9/24.
 */
public class ParseJson
{
    public final static String TAG = ParseJson.class.getName();
    //用gson解析返回的数据
    public void parseJsonWithGson(String jsonData) {
        Log.d(TAG, "用gson进行解析");
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
    public void parseJson(String jsonData)
    {
        try
        {
            Log.d(TAG,"解析json数据");
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
                //遍历index 该数据并不需要

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

                    //用正则表达式取出 实时温度
                    String str = jsonObjectInWeatherData.get("date").toString();
                    String regEx = "：\\d+";
                    Pattern pattern = Pattern.compile(regEx);
                    Matcher matcher = pattern.matcher(str);
                    boolean isFindRealtem = matcher.find();
                    final String realTem = matcher.group();
                    Log.d(TAG,"是否找到了实时温度 "+isFindRealtem);
                    Log.d(TAG,"正则表达式找到的实时温度 "+realTem);
                    new Thread(new Runnable() {
                        @Override
                        public void run()
                        {
                            //Message message;
                            //message.what = UPDATETEXT;
                            //handler.sendMessage(message);

                        }
                    }).start();
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
