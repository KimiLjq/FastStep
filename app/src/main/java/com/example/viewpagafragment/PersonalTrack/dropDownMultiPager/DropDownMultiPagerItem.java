package com.example.viewpagafragment.PersonalTrack.dropDownMultiPager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.viewpagafragment.HttpRequest;
import com.example.viewpagafragment.R;
import com.example.viewpagafragment.SpecificCouponActivity;
//import com.yaphetzhao.dropdownmultipager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
//import org.json.JSONException;
//import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


@SuppressLint("ViewConstructor")
public class DropDownMultiPagerItem extends LinearLayout {
    Bitmap bmp;
    ImageView img;
    private JSONObject jsonObject;
    private Context context;
    final String url = null;

    public DropDownMultiPagerItem(final Context context, int num, JSONObject jsonObject) throws JSONException {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_dropdownfootprint, this);

        this.context = context;
        final JSONObject jsonObject1 = jsonObject;
        final String url=jsonObject.getString("image");
        ((TextView) findViewById(R.id.item_num)).setText(jsonObject.getString("name"));
        img = (ImageView) findViewById(R.id.imageV);

        final String id= String.valueOf(num);

        new Thread(new Runnable() {

            @Override

            public void run() {

                bmp = getURLimage(url);

                //Log.e("faststep", String.valueOf(bmp));

                Message msg = new Message();

                msg.what = 0;

                msg.obj = bmp;

                handle.sendMessage(msg);

            }

        }).start();
        
        img.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,SpecificCouponActivity.class);
                Bundle bundle = new Bundle();
                try{
//                        System.out.println("@@@@@@@@@@@@"+jsonObject.toString());
                    bundle.putString("image",url);
                    System.out.println("bundle=============="+bundle.getString("image"));
                    bundle.putString("name",jsonObject1.getString("name"));
                    bundle.putInt("couponId",jsonObject1.getInt("id"));
                    bundle.putDouble("afterMoney",jsonObject1.getDouble("afterMoney"));
                    bundle.putDouble("beforeMoney",jsonObject1.getDouble("beforeMoney"));
                    bundle.putString("url",jsonObject1.getString("url"));

                    intent.putExtra("thisCoupon",bundle);
                    context.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("DropDownMultiPagerItem","Json解析出错");
                }
            }
        });
    }


    //在消息队列中实现对控件的更改

    private Handler handle = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    System.out.println("111");
                    bmp = (Bitmap) msg.obj;
                    try {
                        Log.e("me", String.valueOf(bmp));
                        img.setImageBitmap(bmp);
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                   break;
                case 0x1:
                    break;
           }

        }

    };


    //加载图片

    public Bitmap getURLimage(String url) {

        Bitmap bmp = null;

        try {

            URL myurl = new URL(url);

            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();

            conn.setConnectTimeout(1000);//设置超时

            conn.setRequestMethod("GET");

            conn.setDoInput(true);

            conn.setUseCaches(false);//不缓存

            InputStream is = conn.getInputStream();//获得图片的数据流
            Log.e("me","is:"+is);

            bmp = BitmapFactory.decodeStream(is);//读取图像数据

            //读取文本数据

            //byte[] buffer = new byte[100];

            //inputStream.read(buffer);

            //text = new String(buffer);

            is.close();

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

            return bmp;



    }
}
