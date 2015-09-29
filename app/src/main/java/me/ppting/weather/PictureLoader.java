package me.ppting.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LinearGradient;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
/**
 * Created by PPTing on 15/9/28.
 */

class MyAsyncTask extends AsyncTask<String, Integer, Bitmap> {
    // 可变长的输入参数，与AsyncTask.exucute()对应

    Context context = MyApplication.getContext();
    public MyAsyncTask(Context context) {}
    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        try {
            //根据URL取得图片并返回
            URL url = new URL(params[0]);//("http://api.map.baidu.com/images/weather/day/duoyun.png");
            Log.d("PictureLoader","url is "+url);
            URLConnection conn = url.openConnection();
            conn.connect();
            InputStream inputStream = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.d("PictureLoader","bitmap is "+bitmap);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
    @Override
    protected void onPostExecute(Bitmap bitmap)
    {
        if (bitmap != null)
        {
            Log.d("PictureLoader","bitmap in onPostExecute is "+bitmap);
        }
        else
        {
            Log.d("PictureLoader","bitmap in onPostExecute is "+bitmap);
        }
    }
    protected void onPreExecute()
    {
        // 任务启动
        Toast.makeText(context,"",Toast.LENGTH_LONG).show();
    }
}
//public class PictureLoader
//{
//    public static Bitmap loadImage(String url)// throws IOException
//    {
//        new Thread(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                HttpClient httpClient = new DefaultHttpClient();
//                HttpResponse response = null;
//                InputStream inputStream = null;
//                try {
//                    response = httpClient.execute(new HttpGet(url));
//                    HttpEntity httpEntity = response.getEntity();
//                    inputStream = httpEntity.getContent();
//                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//                    Log.d("PictureLoader","bitmap is "+bitmap);
//                    //return bitmap;
//                }
//                catch (ClientProtocolException e) {
//                    e.printStackTrace();
//                    //return null;
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    //return null;
//                }
//
//            }
//        }).start();
//        return bitmap;
//    }
//}