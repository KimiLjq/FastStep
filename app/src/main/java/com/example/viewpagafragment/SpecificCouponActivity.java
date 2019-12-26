package com.example.viewpagafragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.youth.banner.Banner;

import org.json.JSONObject;

public class SpecificCouponActivity extends AppCompatActivity {
    private MyImageView iv_specific_coupon;
    private TextView tv_specific_coupon_price;
    private TextView tv_specific_coupon_priced;
    private TextView tv_specific_coupon_describe;
    private ImageView iv_coupon_collect;
    private Toolbar toolbar;
    private int couponId;

    private int isCollect = 0;
    private String clickUrl;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1) {
                String result = msg.getData().getString("collectResult");
                int code = 500;

                try{
                    JSONObject jsonObject = new JSONObject(result);
                    code = jsonObject.getInt("errcode");
                }catch (Exception e){
                    Log.e("collect","Json解析错误");
                    e.printStackTrace();
                }

                if(code == 200){
                    isCollect = 1;
                    System.out.println("添加收藏成功");
                }else{
                    System.out.println("添加收藏失败"+code);
                }

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_coupon);

        init();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //设置返回键是否可以点击
        actionBar.setDisplayHomeAsUpEnabled(true);
        //隐藏默认标题
        actionBar.setDisplayShowTitleEnabled(false);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        final Bundle bundle = intent.getBundleExtra("thisCoupon");

        iv_specific_coupon.setImageURL(bundle.getString("image"));
        tv_specific_coupon_price.setText(Double.toString(bundle.getDouble("afterMoney")));
        tv_specific_coupon_priced.setText(Double.toString(bundle.getDouble("beforeMoney")));
        tv_specific_coupon_describe.setText(bundle.getString("name"));

        couponId = bundle.getInt("couponId");
        clickUrl = bundle.getString("url");

        iv_specific_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkPackage("com.taobao.taobao")){
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.View");
                    intent.setClassName("com.taobao.taobao", "com.taobao.browser.BrowserActivity");
                    Uri uri = Uri.parse(clickUrl);//clickUrl,领券地址
                    intent.setData(uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(SpecificCouponActivity.this,"您的手机还没有某宝，请先下载哦，亲！！！",Toast.LENGTH_LONG);
                }
            }
        });

        iv_coupon_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(isCollect == 1){
                        return;
                    }
                    new Thread() {
                        @Override
                        public void run() {
                            //传递参数获取响应内容
                            HttpRequest httpRequest = new HttpRequest(SpecificCouponActivity.this);
                            String result = httpRequest.addCollect(Integer.toString(couponId));
                            //判断数据库查询出来值是否与输入的值相等
                            if (result != null && !result.isEmpty()){
                                System.out.println("addTrack"+result);

                                Message msg = new Message();
                                msg.what = 0x1;
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("collectResult",result);
                                msg.setData(bundle1);
                                handler.sendMessage(msg);
                            }
                            else {
                                Log.e("thread","添加收藏失败");
                            }
                        }
                    }.start();

                }catch (Exception e){
                    Log.e("SpecificCouponActivity","添加收藏失败");
                    e.printStackTrace();
                }
            }
        });
    }

    private void init(){
        iv_specific_coupon = findViewById(R.id.iv_specific_coupon);
        tv_specific_coupon_price = findViewById(R.id.tv_specific_coupon_price);
        tv_specific_coupon_priced = findViewById(R.id.tv_specific_coupon_priced);
        tv_specific_coupon_describe = findViewById(R.id.tv_specific_coupon_describe);
        iv_coupon_collect = findViewById(R.id.iv_coupon_collect);
        toolbar = findViewById(R.id.tb_specific_coupon);
    }

    private boolean checkPackage(String packageName)
    {
        if (packageName == null || "".equals(packageName))
            return false;
        try
        {
            this.getPackageManager().getApplicationInfo(packageName, PackageManager
                    .GET_UNINSTALLED_PACKAGES);
            return true;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            return false;
        }
    }
}
