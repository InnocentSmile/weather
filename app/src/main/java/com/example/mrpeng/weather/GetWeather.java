package com.example.mrpeng.weather;


import android.os.Handler;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Mr.Peng on 2017/4/25.
 */


public class GetWeather {
    public String nowTemp;
    public String ganmao;
    public String[] lowTemp = new String[5];
    public String[] highTemp = new String[5];
    public String[] weatherType = new String[5];
    public String[] windDir = new String[5];
    public String[] windPow = new String[5];
    public String[] date = new String[5];
    public String src;
    private JSONObject root;
    private JSONObject info;
    private JSONArray forecast;

    public String errorMes;

    public GetWeather(String cityName,Handler handler) {
        getSrc(cityName, handler);
    }

    /*
    *   功能：
    *       初始化info对象，内容为weatherinfo的属性
    *
    *
    * */
    public void getSrc(final String cityName, final Handler handler) {
        new Thread() {
            public void run() {
                try {
                    src = HtmlService.getHtml("http://wthrcdn.etouch.cn/weather_mini?city=" + cityName);
                    System.out.println("获取temp中");
                    root = new JSONObject(src);
                    src = root.getString("data");
                    info = new JSONObject(src);
                    //获取信息
                    nowTemp = info.getString("wendu");
                    ganmao = info.getString("ganmao");
                    forecast = new JSONArray(info.getString("forecast"));
                    for (int i = 0; i < 5; i++) {
                        lowTemp[i] = (new JSONObject(forecast.getString(i))).getString("low");
                        highTemp[i] = (new JSONObject(forecast.getString(i))).getString("high");
                        weatherType[i] = (new JSONObject(forecast.getString(i))).getString("type");
                        windDir[i] = (new JSONObject(forecast.getString(i))).getString("fengxiang");
                        windPow[i] = (new JSONObject(forecast.getString(i))).getString("fengli");
                        date[i] = (new JSONObject(forecast.getString(i))).getString("date");
                    }

                    System.out.println("temp获取成功");
                    errorMes="刷新成功";
                    Message message = handler.obtainMessage();
                    message.what = 1;
                    handler.sendMessage(message);
                } catch (Exception e) {

                    errorMes="数据获取失败";
                    System.out.println("temp失败");
                }
            }

        }.start();

    }
}
