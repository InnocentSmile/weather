package com.example.mrpeng.weather;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class SelectcityActivity extends Activity {

    private GridView gridView;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // 隐藏状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_selectcity);
        gridView = (GridView) findViewById(R.id.gridview_city);
        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.editText_seach);


        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.city_name,
                android.R.layout.simple_spinner_dropdown_item);
        autoCompleteTextView.setAdapter(adapter);


        gridView.setAdapter(new MyAdapter(this));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                System.out.println("adapterView"+adapterView); //gridview对象
//                System.out.println("view"+view); //就是每个选项的布局LinearLayout
//                System.out.println("i"+i);//位置
//                System.out.println("l"+l);//id
                //TextView textView= (TextView) view.findViewById(R.id.textView);
                //Toast.makeText(SelectcityActivity.this,names[i],Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SelectcityActivity.this, MainActivity.class);
                intent.putExtra("msg", names[i].toString());

               // intent.putExtra("first","1");
                startActivity(intent);


            }
        });


    }

    private String[] names = {"上海", "北京", "杭州", "南京", "苏州",
            "深圳", "成都", "重庆", "南昌", "武汉", "西安", "广州",
              "东莞","郑州","深圳","天津","青岛","沈阳",};

    public void backMain(View v) {
        Intent intent = new Intent(SelectcityActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void backMain_seach(View v) {

        System.out.println("123"+autoCompleteTextView.getText());
        if(autoCompleteTextView.getText().toString().equals(""))
        {
            new AlertDialog.Builder(SelectcityActivity.this).setTitle("提示")
                    .setMessage("请选择城市！")
                    .setPositiveButton("OK", null)
                    .show();

        }
        else
        {
            Intent intent = new Intent(SelectcityActivity.this, MainActivity.class);
            intent.putExtra("msg", autoCompleteTextView.getText().toString());
            startActivity(intent);

        }


    }

    class MyAdapter extends BaseAdapter {


        private Context context;

        public MyAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int i) {
            return names[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {


            LayoutInflater inflater = LayoutInflater.from(context);

            View view1 = inflater.inflate(R.layout.hot_city_item, null);

            TextView textView = (TextView) view1.findViewById(R.id.textView_city);

            textView.setText(names[i]);

            return view1;
        }
    }


}
