package com.example.mrpeng.weather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

public class Main2Activity extends Activity {

    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener;


    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            //欢迎界面完毕后启动主界面
            Intent it = new Intent();
            it.setClass(Main2Activity.this, MainActivity.class);
            Main2Activity.this.startActivity(it);
            Main2Activity.this.finish();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main2);

        //检查网络
        if(CheckNet.getNetState(this)==CheckNet.NET_NONE)
        {
            Toast.makeText(this,"请检查网络连接",Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this,"网络OK",Toast.LENGTH_SHORT).show();
        }

        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        initLocation();
        mLocationClient.start();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
//        Intent intent=new Intent(this,MainActivity.class);
//
//        startActivity(intent);
    }


    public void initLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }
    public class MyLocationListener implements BDLocationListener {

        String cityName;
        public void onReceiveLocation(BDLocation bdLocation) {
            cityName = bdLocation.getCity();
            Log.d("Locate",cityName);
            System.out.println(cityName);

            SharedPreferences sharedPreferences=getSharedPreferences("CityCodePreference",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("dingweichengshi",cityName);
            editor.commit();



        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }
    }
}
