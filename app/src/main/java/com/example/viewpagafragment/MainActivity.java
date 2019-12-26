package com.example.viewpagafragment;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener,ViewPager.OnPageChangeListener {

    private TextView txt_topbar;
    private RadioGroup rg_tab_bar;
    private RadioButton rb_channel;
    private RadioButton rb_message;
    private RadioButton rb_circle;
    private ViewPager vpager;
    private ImageView iv_index_search;

    private MyFragmentPageAdapter mAdapter;

    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
//    public static final int PAGE_FOUR = 3;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            setContentView(R.layout.activity_main);

            mAdapter = new MyFragmentPageAdapter(getSupportFragmentManager());
            bindViews();
            rb_channel.setChecked(true);      //由于开始时页面还没变化，设置item为0的页面状态为true
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Thread(){
            @Override
            public void run() {
                HttpRequest httpRequest = new HttpRequest(MainActivity.this);
                String result = httpRequest.isLogin();
                int code = 500;
                try{
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("errcode");
                    Log.e("判断是否登录",""+code);
                }catch (Exception e){
                    Log.e("判断是否登录","json解析出错");
                    e.printStackTrace();
                }
                if(code == 500){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }else {
                    Message msg = new Message();
                    msg.what = 0x123;

                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    private void bindViews(){
        txt_topbar = (TextView) findViewById(R.id.txt_topbar);
        rg_tab_bar = (RadioGroup) findViewById(R.id.rg_tab_bar);
        rb_channel = (RadioButton) findViewById(R.id.rb_channel);
        rb_message = (RadioButton) findViewById(R.id.rb_message);
        rb_circle = (RadioButton) findViewById(R.id.rb_cicle);
        iv_index_search = findViewById(R.id.iv_index_search);

        rg_tab_bar.setOnCheckedChangeListener(this);

        vpager = (ViewPager)findViewById(R.id.vpage);
        vpager.setAdapter(mAdapter);
        vpager.setCurrentItem(0);            //初始化时将Item为0的页面显示
        vpager.addOnPageChangeListener(this);

        iv_index_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

    }

    //监听点击下标栏时页面变化
    @Override
    public void onCheckedChanged(RadioGroup group,int checkId){
        switch (checkId){
            case R.id.rb_channel:
                vpager.setCurrentItem(PAGE_ONE);
                break;
            case R.id.rb_message:
                vpager.setCurrentItem(PAGE_TWO);
                break;
            case R.id.rb_cicle:
                vpager.setCurrentItem(PAGE_THREE);
                break;
//            case R.id.rb_setting:
//                vpager.setCurrentItem(PAGE_FOUR);
//                break;
        }
    }

    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    rb_channel.setChecked(true);
                    break;
                case PAGE_TWO:
                    rb_message.setChecked(true);
                    break;
                case PAGE_THREE:
                    rb_circle.setChecked(true);
                    break;
//                case PAGE_FOUR:
//                    rb_setting.setChecked(true);
//                    break;
            }
        }
    }
}
