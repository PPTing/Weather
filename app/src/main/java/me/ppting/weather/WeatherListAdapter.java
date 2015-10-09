package me.ppting.weather;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by PPTing on 15/10/8.
 */
public class WeatherListAdapter extends BaseAdapter {
    private List<WeatherBean> weatherBeanList;
    private LayoutInflater layoutInflater;

    public WeatherListAdapter (Context context,List<WeatherBean> data)
    {
        weatherBeanList = data;
        layoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return weatherBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView==null)
        {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.weatherinfo,null);
            viewHolder.imageView = (ImageView)convertView.findViewById(R.id.weatherIamge);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.weatherInfo);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imageView.setImageResource(R.mipmap.ic_launcher);
        String url = weatherBeanList.get(position).weatherUrl;
        Log.d("WeatherListAdapter","weatherUrl is "+url);
        viewHolder.imageView.setTag(url);
        new PictureLoader().showImageByAsyncTask(viewHolder.imageView,url);
        viewHolder.textView.setText(weatherBeanList.get(position).weatherTem);
        return convertView;
    }
    class ViewHolder
    {
        public ImageView imageView;
        public TextView textView;
    }
}
