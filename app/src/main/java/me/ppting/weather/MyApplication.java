package me.ppting.weather;

import android.app.Application;
import android.content.Context;

/**
 * Created by PPTing on 15/9/24.
 */
public class MyApplication extends Application
{

        private static Context context;
        public void onCreate()
        {
            super.onCreate();
            context = getApplicationContext();
        }
        public static Context getContext()
        {
            return context;
        }
}
