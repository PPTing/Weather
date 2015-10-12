//package me.ppting.weather;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v4.widget.DrawerLayout;
//import android.util.Log;
//import android.view.View;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//
///**
// * Created by PPTing on 15/9/21.
// */
//public class SettingActivity extends Activity
//{
//    private RelativeLayout settinglayout;
//    private Button setting;
//    private ListView chooseCity;
//    private String[] datatest = {"nanjing","beijing","shantou"};
//    public void onCreate(Bundle bundle)
//    {
//        super.onCreate(bundle);
//        setContentView(R.layout.setting);
//        settinglayout = (RelativeLayout)findViewById(R.id.settinglayout);
//        setting = (Button)findViewById(R.id.setting);
//        setting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.d("SettingActivity","setting button is click");
//                Intent intent = new Intent(SettingActivity.this,CityActivity.class);
//                startActivity(intent);
//
//            }
//        });
//        ArrayAdapter<String> chooseCityAdapter = new ArrayAdapter<String>(SettingActivity.this,
//                android.R.layout.simple_list_item_1,datatest);
//        chooseCity = (ListView)findViewById(R.id.choosecity);
//        chooseCity.setAdapter(chooseCityAdapter);
//
//    }
//}
