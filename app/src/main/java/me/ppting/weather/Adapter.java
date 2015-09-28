package me.ppting.weather;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;


/**
 * Created by PPTing on 15/9/25.
 */
public class Adapter extends ArrayAdapter<Weather>
{
    private int resourceId;
    Context context = MyApplication.getContext();
    public Adapter(Context context, int textViewResourceId,List<Weather> objects)
    {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView ,ViewGroup parent)
    {
        Weather weather = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.weatherIamge);
        TextView textView = (TextView) view.findViewById(R.id.weatherInfo);
        textView.setText(weather.getTem());//显示温度
        //显示天气小图标
        try {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            String url = sharedPreferences.getString("dayPictureUrl", "");
            //Bitmap bitmap = PictureLoader.loadImage(url);
            MyAsyncTask myAsyncTask = new MyAsyncTask(getContext());
            Bitmap bitmap = myAsyncTask.doInBackground(url);
            Log.d("Adapter","url is "+url);
            Log.d("Adapter","bitmap is "+bitmap);
            imageView.setImageBitmap(bitmap);
        }
        catch (Exception e)
        {e.printStackTrace();}
        //imageView.setImageResource(weather.getImage());
        return view;
    }

}
