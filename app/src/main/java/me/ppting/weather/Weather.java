package me.ppting.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Created by PPTing on 15/9/25.
 */
public class Weather
{
    Context context = MyApplication.getContext();
    public final static String TAG = Weather.class.getName();
    private String date;
    public Bitmap getImage() {
        return image;
    }

    //    private int image;
//    public int getImage()
//    {
//        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
//        //image =
//        return image;
//    }
//
//    public void setImage(int image) {
//        this.image = image;
//    }
    private Bitmap image;


    private String tem;
//    public Weather(int image,String tem)
//    {
//        this.image = image;
//        this.tem = tem;
//    }
    public Weather(Bitmap image,String tem)
    {
        this.image = image;
        this.tem = tem;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTem()
    {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }
}
