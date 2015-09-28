package me.ppting.weather;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Handler;

/**
 * Created by PPTing on 15/9/28.
 */
public class PictureLoader
{
    public static Bitmap loadImage(final String url)// throws IOException
    {
       Bitmap bitmap = null;
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                HttpClient httpClient = new DefaultHttpClient();
                HttpResponse response = null;
                InputStream inputStream = null;
                try {
                    response = httpClient.execute(new HttpGet(url));
                    HttpEntity httpEntity = response.getEntity();
                    inputStream = httpEntity.getContent();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Log.d("PictureLoader","bitmap is "+bitmap);
                    //return bitmap;
                }
                catch (ClientProtocolException e) {
                    e.printStackTrace();
                    //return null;
                } catch (IOException e) {
                    e.printStackTrace();
                    //return null;
                }

            }
        }).start();
        return bitmap;
    }
}
