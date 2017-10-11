package com.example.mrpeng.weather;

import android.app.Activity;
import android.app.NotificationManager;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import android.view.WindowManager;
import android.widget.BaseAdapter;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;

import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.Calendar;


public class MainActivity extends Activity {
    GetWeather getweather;
    private TextView tvTemp, tvGanmao, tvType, tvWind,city_name,Text_Lunar;
    private ListView listView;
    private ImageView ivToday, im_bg_Wtype;

    private String msg;

    public String firstmsg;

    public static final int N_ID1 = 0x1;


//
   LocationClient mLocationClient;
   MyLocationListener myLocationListener;

    private Button locateBtn;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(msg.what==2)
            {
                locateBtn.setText(myLocationListener.cityName);

            }
            if(msg.what==1){

                tvType.setText(getweather.weatherType[0]);
                tvTemp.setText(getweather.nowTemp + "℃");
                tvGanmao.setText(getweather.ganmao);

                tvWind.setText(getweather.windDir[0] + "  " + getweather.windPow[0]);
                //vh.iv_icon.setImageResource(icons[i]);
                ivToday.setImageResource(adjust.getPicID(getweather.weatherType[0]));

                im_bg_Wtype.setImageResource(adjust.bg_getpicID(getweather.weatherType[0]));

                listView.setAdapter(new MyAdapter(MainActivity.this, getweather));

                sendNotification();
            }



        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

//        ThreadLocal<MyLocationListener> tl = new ThreadLocal<MyLocationListener>() {
//            MyLocationListener  ml = new MyLocationListener();
//        };

        System.out.println("2.weather实例化完毕");
        init();


        //定位

        //
//        if(tl.get()==null)
//        {
//            myLocationListener = new MyLocationListener();
//            tl.set(myLocationListener);
//        }
//        else
//        {
//            myLocationListener = tl.get();
//        }

        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        initLocation();
        mLocationClient.start();

        //System.out.println("yuigyjgiu"+myLocationListener.cityName);
        //city_name.setText(myLocationListener.cityName);


        //公历
        Calendar c=Calendar.getInstance();
        int month=c.get(Calendar.MONTH);
        int day=c.get(Calendar.DAY_OF_MONTH);
        String str=(month+1)+"月"+day+"日";
        //农历
        Lunar lunar = new Lunar(c);
        String lunarStr ="";
        lunarStr=lunar.animalsYear()+"年(";
        lunarStr +=lunar.cyclical()+"年)";
        lunarStr +=lunar.toString();
        Text_Lunar.setText(lunarStr+" "+str);



        //定位后获取天气
//        String first = getIntent().getStringExtra("first");
//        while(myLocationListener.cityName==null && first==null)
//        {
//            if(CheckNet.getNetState(this)==CheckNet.NET_NONE)
//                break;
//        }

        SharedPreferences sharedPreferences= getSharedPreferences("CityCodePreference", Activity.MODE_PRIVATE);
        firstmsg=sharedPreferences.getString("dingweichengshi","南昌");

        System.out.println("反倒是公司的发个梵蒂冈地方"+firstmsg);

          locateBtn.setText(firstmsg);
          city_name.setText(firstmsg);

        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.commit();
        //System.out.println("djiusfhidsuif"+city_name.getText().toString());
          getweather = new GetWeather(city_name.getText().toString(),handler);


       // System.out.println("电视剧分公司登记高峰和贵金属的凤凰健康"+adjust.hour);



//         if(myLocationListener.cityName!=null)
//         {
//             city_name.setText(myLocationListener.cityName);
//             getweather = new GetWeather(city_name.getText().toString(),handler);
//         }
//
//         if(myLocationListener.cityName==null)
//         {
//             city_name.setText("南昌");
//             getweather = new GetWeather(city_name.getText().toString(),handler);
//         }

        //接收选择城市界面的城市
        msg = getIntent().getStringExtra("msg");
        if (msg != null) {
            city_name.setText(msg);
            updateInfo();
        }

    }

    //绑定控件
    public  void init()
    {
        tvTemp = (TextView) findViewById(R.id.textView);
        tvGanmao = (TextView) findViewById(R.id.textView4);
        tvType = (TextView) findViewById(R.id.textView2);
        tvWind = (TextView) findViewById(R.id.textView3);
        listView = (ListView) findViewById(R.id.listview);
        ivToday = (ImageView) findViewById(R.id.imageView_today);
        im_bg_Wtype = (ImageView) findViewById(R.id.im_bg_Wtype);
        city_name= (TextView) findViewById(R.id.Mcity_name);
        locateBtn = (Button) findViewById(R.id.location_city_name);
        Text_Lunar= (TextView) findViewById(R.id.Text_Lunar);
    }


    public void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setIsNeedAddress(true);
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }


    //跳转添加页面
    public void addcityClick(View view)
    {
        Intent intent = new Intent(MainActivity.this, SelectcityActivity.class);
        startActivity(intent);
    }

     //当地位置天气信息刷新
    public void location_refresh(View view) {

        mLocationClient = new LocationClient(this);
        myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        initLocation();
        mLocationClient.start();


        if(locateBtn.getText().toString()==null)
        {

        }
        else{
            Toast.makeText(this,"定位获取天气中",Toast.LENGTH_SHORT).show();
            String a=locateBtn.getText().toString();

            city_name.setText(a);

            getweather.getSrc(a,handler);

            Toast.makeText(this,getweather.errorMes,Toast.LENGTH_SHORT).show();
        }



    }
    //刷新
    public void refresh(View view)
    {
        Toast.makeText(this,"正在刷新....",Toast.LENGTH_SHORT).show();
        String a=city_name.getText().toString();

//        msg = a.substring(1);

        getweather.getSrc(a,handler);

        Toast.makeText(this,getweather.errorMes,Toast.LENGTH_SHORT).show();


    }

    //开启软件后设置常驻通知
    public void sendNotification() {
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        //设置相关的属性
        builder.setSmallIcon(android.R.drawable.alert_dark_frame);//设置图标

        builder.setOngoing(true);
        builder.setContentTitle("今日");


        //创建一个远程的视图（通知栏里）
        RemoteViews views = new RemoteViews(getPackageName(), R.layout.custom_layout);
        builder.setContent(views);
        builder.setTicker("今日天气预报");

        builder.setOngoing(true);//设置为常驻通知
        views.setImageViewResource(R.id.imageView_C_icon, adjust.getPicID(getweather.weatherType[0]));

        views.setTextViewText(R.id.text_C_temp, getweather.nowTemp + "℃");

        views.setTextViewText(R.id.text_Wtype, getweather.weatherType[0]);

        views.setTextViewText(R.id.text_City, city_name.getText().toString());

        //设置按钮的单击事件
        // views.setOnClickPendingIntent();

        //获取系统的通知管理器，发送
        final NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(N_ID1, builder.build());
    }




    //设置今天的天气
    public void updateInfo() {
        getweather.getSrc(msg,handler);


//        while (!getweather.prepare)
//            ;
    }


    //listview数据填充
    static class MyAdapter extends BaseAdapter {

        private int[] icons = {android.R.drawable.ic_input_add,
                android.R.drawable.ic_input_delete,
                android.R.drawable.ic_dialog_email,
                android.R.drawable.ic_dialog_info,
                android.R.drawable.ic_dialog_map,

        };
        private Context context;
        private GetWeather getWeather;

        public MyAdapter(Context context, GetWeather getWeather) {
            this.context = context;
            this.getWeather = getWeather;

            //this.main.updateInfo();
        }


        @Override
        public int getCount() {
            return icons.length;
        }

        @Override
        public Object getItem(int i) {
            return icons[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            ViewHolder vh;

            if (view == null)//优化
            {
                LayoutInflater inflater = LayoutInflater.from(context);
                //实例化布局文件
                view = inflater.inflate(R.layout.listview1, null);
                vh = new ViewHolder();
                vh.iv_icon = (ImageView) view.findViewById(R.id.imageView_icon);
                vh.tv_date = (TextView) view.findViewById(R.id.textView_date);
                vh.tv_weatherType = (TextView) view.findViewById(R.id.textView_weatherType);
                vh.tv_textView_temp = (TextView) view.findViewById(R.id.textView_temp);
                view.setTag(vh);
            } else {
                vh = (ViewHolder) view.getTag();
            }


            vh.iv_icon.setImageResource(adjust.getPicID(getWeather.weatherType[i]));


            switch (i) {
                case 0:
                    vh.tv_date.setText("今天");
                    break;
                case 1:
                    vh.tv_date.setText("明天");
                    break;
                case 2:
                    vh.tv_date.setText("后天");
                    break;
                default:
                    vh.tv_date.setText(getWeather.date[i]);
                    break;
            }
            //vh.tv_date.setText(getWeather.date[i]);
            vh.tv_weatherType.setText(getWeather.weatherType[i]);
            vh.tv_textView_temp.setText(getWeather.highTemp[i] + "/" + getWeather.lowTemp[i]);
            //System.out.println("3.listView赋值完毕");
            return view;

        }

        //用于保存第一次查找的组件，避免下次重复查找(优化)
        static class ViewHolder {
            ImageView iv_icon;
            TextView tv_date;
            TextView tv_weatherType;
            TextView tv_textView_temp;
        }
    }

    //图片选择器
   static class adjust {

        //获取系统当前时间
         static Calendar c=Calendar.getInstance();
         static int  hour=c.get(Calendar.HOUR_OF_DAY);
        //int minute=c.get(Calendar.MINUTE);
        static int bg_getpicID(String weatherType) {
            switch (weatherType) {
                case "雾":
                    if (hour>6&&hour<18)
                        return R.drawable.bg_fog_day;
                    else
                        return R.drawable.bg18_fog_night;

                case "晴":
                    if (hour>6&&hour<18)
                        return R.drawable.blur_bg0_fine_day;
                    else
                        return R.drawable.bg0_fine_night;
                case "阵雨":
                    if (hour>6&&hour<18)
                        return R.drawable.bg_moderate_rain_day;
                    else
                        return R.drawable.bg_heavy_rain_night;
                case "小雨":
                    if (hour>6&&hour<18)
                        return R.drawable.bg_moderate_rain_day;
                    else
                        return R.drawable.bg_heavy_rain_night;
                case "中雨":
                    if (hour>6&&hour<18)
                        return R.drawable.bg_moderate_rain_day;
                    else
                        return R.drawable.bg_heavy_rain_night;
                case "小到中雨":
                    if (hour>6&&hour<18)
                        return R.drawable.bg_moderate_rain_day;
                    else
                        return R.drawable.bg_heavy_rain_night;
                case "中到大雨":
                    if (hour>6&&hour<18)
                        return R.drawable.bg_moderate_rain_day;
                    else
                        return R.drawable.bg_heavy_rain_night;
                case "大雨":
                    if (hour>6&&hour<18)
                        return R.drawable.bg_moderate_rain_day;
                    else
                        return R.drawable.bg_heavy_rain_night;
                case "多云":
                    if (hour>6&&hour<18)
                        return R.drawable.blur_bg0_fine_day;
                    else
                        return R.drawable.bg0_fine_night;
                case "阴":
                    if (hour>6&&hour<18)
                        return R.drawable.blur_bg0_fine_day;
                    else
                        return R.drawable.bg0_fine_night;
                default:
                    return R.drawable.blur_bg_na;
            }
        }

      static  int getPicID(String weatherType) {
            switch (weatherType) {
                case "晴":
                    return R.drawable.ww0;
                case "阴":
                    return R.drawable.ww1;
                case "多云":
                    return R.drawable.ww1;
                case "阵雨":
                    return R.drawable.ww3;
                case "雷阵雨":
                    return R.drawable.ww4;
                case "小雨":
                    return R.drawable.ww7;
                case "小到中雨":
                    return R.drawable.ww19;
                case "中到大雨":
                    return R.drawable.ww19;
                case "中雨":
                    return R.drawable.ww8;
                case "大雨":
                    return R.drawable.ww9;
                case "暴雨":
                    return R.drawable.ww10;


                default:
                    return R.drawable.wna;
            }
        }
    }



    //获取定位城市监听器
    public class MyLocationListener implements BDLocationListener {
        String cityName;
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            cityName = bdLocation.getCity();
            //Log.d("Locate", cityName);

            Message message = handler.obtainMessage();
            message.what =2;
            handler.sendMessage(message);

        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {

        }


    }


}