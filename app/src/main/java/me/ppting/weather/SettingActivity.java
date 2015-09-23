package me.ppting.weather;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.widget.RelativeLayout;

/**
 * Created by PPTing on 15/9/21.
 */
public class SettingActivity extends Activity
{
    private RelativeLayout settinglayout;
    public void onCreate(Bundle bundle)
    {
        super.onCreate(bundle);
        setContentView(R.layout.setting);
        settinglayout = (RelativeLayout)findViewById(R.id.settinglayout);

    }
}
