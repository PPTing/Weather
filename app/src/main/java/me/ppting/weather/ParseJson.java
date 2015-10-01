package me.ppting.weather;

import android.app.*;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Message;
import android.preference.PreferenceManager;
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
    Context context = MyApplication.getContext();
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
        //WeatherInfo.Results results = gson.fromJson(jsonData,WeatherInfo.Results.class);
        Log.d(TAG,"error is "+weatherInfo.getError());
        Log.d(TAG,"status is "+weatherInfo.getStatus());
        Log.d(TAG,"date is "+weatherInfo.getDate());
        Log.d(TAG,"currentcity is "+weatherInfo.getResults().getCurrentCity());
        /////////////////////////////////////////////////////

    }
    public String parseJsonWithGsonForCity(String jsonData)
    {
        Log.d(TAG,"用gson解析json获取城市");
        Gson gson = new Gson();
        CityInfo cityInfo = gson.fromJson(jsonData,CityInfo.class);
        Log.d(TAG,"city is "+cityInfo.getResult().getAddressComponent().getCity());
        return cityInfo.getResult().getAddressComponent().getCity();
    }
    //解析response
    public void parseJson(String jsonData)
    {
        try
        {
            Log.d(TAG,"解析json数据");
            JSONObject jsonObject = new JSONObject(jsonData);
            Log.d(TAG,"error is "+jsonObject.get("error"));
            Log.d(TAG,"status is "+jsonObject.get("status"));
            Log.d(TAG,"date is "+jsonObject.get("date"));
            //找到今天的日期
            String strGetDate = jsonObject.get("date").toString();
            String regExGetDate = "(\\d{4})-(\\d{2})-(\\d{2})";
            Pattern patternGetDate = Pattern.compile(regExGetDate);
            Matcher matcherGetDate = patternGetDate.matcher(strGetDate);
            boolean isFindDate = matcherGetDate.find();
            final String date = matcherGetDate.group();
            Log.d(TAG,"是否找到日期 "+isFindDate);
            Log.d(TAG,"今天日期是 "+date);

            JSONArray resultsJsonArray = jsonObject.getJSONArray("results");//遍历results
            for (int i = 0;i<resultsJsonArray.length();i++)
            {
                org.json.JSONObject jsonObjectInResults = resultsJsonArray.getJSONObject(i);
                Log.d(TAG,"pm25 is "+jsonObjectInResults.get("pm25"));

                String currentCity = jsonObjectInResults.get("currentCity").toString();
                Log.d(TAG,"currentCity is "+currentCity);
                //遍历index 该数据并不需要
                /*
                org.json.JSONArray indexArray = jsonObjectInResults.getJSONArray("index");
                for (int j = 0;j<indexArray.length();j++)
                {
                    JSONObject jsonObjectInIndex = indexArray.getJSONObject(j);
                    Log.d(TAG,"title is "+jsonObjectInIndex.get("title"));
                    Log.d(TAG,"zs is "+jsonObjectInIndex.get("zs"));
                    Log.d(TAG,"tips is "+jsonObjectInIndex.get("tipt"));
                    Log.d(TAG,"des is "+jsonObjectInIndex.get("des"));
                }
                */
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
                    Log.d(TAG, "temperature is " + jsonObjectInWeatherData.get("temperature"));

                    if (k==0)//当天全天温度
                    {
                        //获取当天全天温度
                        String strGetTodayTem = jsonObjectInWeatherData.get("temperature").toString();
                        String regExGetTodayTem = "\\d+\\s\\~\\s\\d+";
                        Pattern patternGetTodayTem = Pattern.compile(regExGetTodayTem);
                        Matcher matcherGetTodayTem = patternGetTodayTem.matcher(strGetTodayTem);
                        boolean isFindTodaytem = matcherGetTodayTem.find();
                        final String todayTem = matcherGetTodayTem.group();
                        Log.d(TAG, "是否找到了一天温度 " + isFindTodaytem);
                        Log.d(TAG, "正则表达式找到的一天温度 " + todayTem);
                        //获取白天天气图Url
                        String dayPictureUrl = jsonObjectInWeatherData.get("dayPictureUrl").toString();
                        Log.d(TAG,"dayPictureUrl is "+dayPictureUrl);

                        //获取实时温度
                        String strGetRealTem = jsonObjectInWeatherData.get("date").toString();
                        Log.d(TAG, "strGetRealTem is " + strGetRealTem);
                        String regExGetRealTem = "：\\d+";
                        Pattern patternGetRealTem = Pattern.compile(regExGetRealTem);
                        Matcher matcherGetRealTem = patternGetRealTem.matcher(strGetRealTem);
                        boolean isFindRealTemWithColon = matcherGetRealTem.find();
                        final String realTemWithColon = matcherGetRealTem.group();
                        Log.d(TAG, "realTemWithColon is " + realTemWithColon);
                        String regExWithoutColon = "\\d+";
                        Pattern patternWithoutColon = Pattern.compile(regExWithoutColon);
                        Matcher matcherWithoutColon = patternWithoutColon.matcher(realTemWithColon);
                        boolean isFindRealtem = matcherWithoutColon.find();
                        final String realTem = matcherWithoutColon.group();
                        Log.d(TAG, "是否找到了实时温度 " + isFindRealtem);
                        Log.d(TAG, "正则表达式找到的实时温度 realTem " + realTem);
                        //将解析到的天气信息存储到sharePerfence中 城市、日期、实时温度、一天温度、白天天气图Url
                        saveWeatherInfoDay1(context, currentCity, date, realTem, todayTem, dayPictureUrl);
                    }


                    if (k==1)//第二天天气信息
                    {
                        //白天天气图url
                        String day2PictureUrl = jsonObjectInWeatherData.get("dayPictureUrl").toString();
                        Log.d(TAG, "dayPictureUrl of second day is " + day2PictureUrl);
                        //第二天温度
                        String strGetSecondTem = jsonObjectInWeatherData.get("temperature").toString();
                        String regExGetSecondTem = "\\d+\\s\\~\\s\\d+";
                        Pattern patternGetSecondTem = Pattern.compile(regExGetSecondTem);
                        Matcher matcherGetSecondTem = patternGetSecondTem.matcher(strGetSecondTem);
                        boolean isFindSecondTem = matcherGetSecondTem.find();
                        final String secondTem = matcherGetSecondTem.group();
                        Log.d(TAG, "是否找到了第二天全天温度 " + isFindSecondTem);
                        Log.d(TAG, "正则表达式找到的第二天全天温度 " + secondTem);
                        saveWeatherInfoDay2(context,day2PictureUrl,secondTem);
                    }
                    if (k==2)//第三天天气信息
                    {
                        //白天天气图url
                        String day3PictureUrl = jsonObjectInWeatherData.get("dayPictureUrl").toString();
                        Log.d(TAG, "dayPictureUrl of 3 day is " + day3PictureUrl);
                        //第二天温度
                        String strGet3Tem = jsonObjectInWeatherData.get("temperature").toString();
                        String regExGet3Tem = "\\d+\\s\\~\\s\\d+";
                        Pattern patternGet3Tem = Pattern.compile(regExGet3Tem);
                        Matcher matcherGet3Tem = patternGet3Tem.matcher(strGet3Tem);
                        boolean isFind3Tem = matcherGet3Tem.find();
                        final String thirdTem = matcherGet3Tem.group();
                        Log.d(TAG, "是否找到了第3天全天温度 " + isFind3Tem);
                        Log.d(TAG, "正则表达式找到的第3天全天温度 " + thirdTem);
                        saveWeatherInfoDay3(context,day3PictureUrl,thirdTem);
                    }
                    if (k==3)//第四天天气信息
                    {
                        //白天天气图url
                        String day4PictureUrl = jsonObjectInWeatherData.get("dayPictureUrl").toString();
                        Log.d(TAG, "dayPictureUrl of 4 day is " + day4PictureUrl);
                        //第二天温度
                        String strGet4Tem = jsonObjectInWeatherData.get("temperature").toString();
                        String regExGet4Tem = "\\d+\\s\\~\\s\\d+";
                        Pattern patternGet4Tem = Pattern.compile(regExGet4Tem);
                        Matcher matcherGet4Tem = patternGet4Tem.matcher(strGet4Tem);
                        boolean isFind4Tem = matcherGet4Tem.find();
                        final String fourthTem = matcherGet4Tem.group();
                        Log.d(TAG, "是否找到了第4天全天温度 " + isFind4Tem);
                        Log.d(TAG, "正则表达式找到的第4天全天温度 " + fourthTem);
                        saveWeatherInfoDay4(context,day4PictureUrl,fourthTem);
                    }



                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //存储天气信息
    public void saveWeatherInfoDay1(Context context,String currentCity,String date,String realTem,String todayTem,String dayPictureUrl)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("currentCity", currentCity);
        editor.putString("date",date);
        editor.putString("realTem",realTem);
        editor.putString("todayTem",todayTem);
        editor.putString("dayPictureUrl",dayPictureUrl);
        editor.commit();
    }
    public void saveWeatherInfoDay2(Context context,String day2Picture,String secondTem)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("day2Picture",day2Picture);
        editor.putString("secondTem",secondTem);
        editor.commit();
    }
    public void saveWeatherInfoDay3(Context context,String day3Picture,String thirdTem)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("day3Picture",day3Picture);
        editor.putString("thirdTem",thirdTem);
        editor.commit();
    } public void saveWeatherInfoDay4(Context context,String day4Picture,String fourthTem)
    {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putString("day4Picture",day4Picture);
        editor.putString("fourthTem",fourthTem);
        editor.commit();
    }

}
