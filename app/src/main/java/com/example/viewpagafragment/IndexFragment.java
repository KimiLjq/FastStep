package com.example.viewpagafragment;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.viewpagafragment.JavaBean.CouponMessageBean;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IndexFragment extends Fragment {

    private int num;
    private Banner index_banner;
    private ArrayList<String> list_path;
    private ArrayList<String> list_title;
    private GridView index_gridView;
    private MyAdapter<CouponMessageBean> myAdapter;
    private List<CouponMessageBean> couponRecommendList = new ArrayList<>();
    private Context mcontext;
    private HttpRequest httpRequest;

    public IndexFragment(){

    }

    public void setNum(int num){
        this.num = num;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1) {
                String str = msg.getData().getString("recommend");
                parseJson(str);
                if(couponRecommendList.size()!=0){
                    setAdapter();
                }

            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.activity_my_fragment,container,false);
        mcontext = view.getContext();

        init(view);
        sendRequest();

        return view;
    }

    private void init(View view){
        index_banner = view.findViewById(R.id.index_banner);
        index_gridView = view.findViewById(R.id.index_gridView);

        //放图片地址的集合
        list_path = new ArrayList<>();
        //放标题的集合
        list_title = new ArrayList<>();
        httpRequest = new HttpRequest(mcontext);


    }

    private void sendRequest(){
        new Thread() {
            @Override
            public void run() {
                //传递参数获取响应内容
                String result = httpRequest.getRecommend();
//                System.out.println("Thread**********"+result);
                //判断数据库查询出来值是否与输入的值相等
                if (result != null && !result.isEmpty()){
//                                System.out.println("+*****"+searchTip);
                    Message msg = new Message();
                    msg.what = 0x1;

                    Bundle bundle = new Bundle();
                    bundle.putString("recommend",result);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
                else {
                    Log.e("IndexFragment","获取推荐优惠卷失败");
                }

//                            System.out.println(searchTip);
            }
        }.start();
    }

    private void loaderNetImage(ImageView imageView,String path){
        Glide.with(this).load( path).into(imageView);
    }

    private void setAdapter(){

        for(int i=0;i<4;i++){
            list_path.add(couponRecommendList.get(i).getImage());
            list_title.add("骨折优惠");
        }

        //设置内置样式，共有六种可以点入方法内逐一体验使用。
        index_banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器，图片加载器在下方
        index_banner.setImageLoader(new MyLoader());
        //设置图片网址或地址的集合
        index_banner.setImages(list_path);
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        index_banner.setBannerAnimation(Transformer.Default);
        //设置轮播图的标题集合
        index_banner.setBannerTitles(list_title);
        //设置轮播间隔时间
        index_banner.setDelayTime(2000);
        //设置是否为自动轮播，默认是“是”。
        index_banner.isAutoPlay(true);
        //设置指示器的位置，小点点，左中右。
        index_banner.setIndicatorGravity(BannerConfig.CENTER)
                //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
                .setOnBannerListener(new OnBannerListener() {
                    @Override
                    public void OnBannerClick(int position) {
                        CouponMessageBean couponMessageBean = couponRecommendList.get(position);

//                        addTrack(Integer.toString(couponMessageBean.getId()));
//                        System.out.println("onItemSelected"+couponMessageBean.getImage());
                        Bundle bundle = new Bundle();
                        bundle.putString("image",couponMessageBean.getImage());
                        bundle.putString("name",couponMessageBean.getName());
                        bundle.putInt("couponId",couponMessageBean.getId());
                        bundle.putDouble("afterMoney",couponMessageBean.getAfterMoney());
                        bundle.putDouble("beforeMoney",couponMessageBean.getBeforeMoney());
                        bundle.putString("url",couponMessageBean.getUrl());

                        Intent intent = new Intent(mcontext,SpecificCouponActivity.class);
                        intent.putExtra("thisCoupon",bundle);
                        startActivity(intent);
                    }
                })
                //必须最后调用的方法，启动轮播图。
                .start();


        final List<CouponMessageBean> list = new ArrayList<>();
        for(int i=4;i<couponRecommendList.size();i++){
            list.add(couponRecommendList.get(i));
        }
        myAdapter = new MyAdapter<CouponMessageBean>((ArrayList)list,R.layout.index_recommend_layout) {
            @Override
            public void bindView(ViewHolder holder, CouponMessageBean obj) {
                holder.setImageResource(R.id.iv_grid_recommend,obj.getImage());
                holder.setText(R.id.tv_recommend_name,obj.getName());
                holder.setText(R.id.tv_recommend_afterMoney,Double.toString(obj.getBeforeMoney()));
                holder.setText(R.id.tv_recommend_beforeMoney,Double.toString(obj.getAfterMoney()));
            }
        };

        index_gridView.setAdapter(myAdapter);
        index_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                CouponMessageBean couponMessageBean = list.get(i);

                addTrack(Integer.toString(couponMessageBean.getId()));
//                System.out.println("onItemSelected"+couponMessageBean.getImage());
                Bundle bundle = new Bundle();
                bundle.putString("image",couponMessageBean.getImage());
                bundle.putString("name",couponMessageBean.getName());
                bundle.putInt("couponId",couponMessageBean.getId());
                bundle.putDouble("afterMoney",couponMessageBean.getAfterMoney());
                bundle.putDouble("beforeMoney",couponMessageBean.getBeforeMoney());
                bundle.putString("url",couponMessageBean.getUrl());

                Intent intent = new Intent(mcontext,SpecificCouponActivity.class);
                intent.putExtra("thisCoupon",bundle);
                startActivity(intent);
            }
        });

    }



    private class MyLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load((String) path).into(imageView);
        }
    }

    private void parseJson(String str){
        try{
            JSONObject jsonObject = new JSONObject(str);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            for(int i=0;i<dataArray.length();i++){
                JSONObject jsonObject1 = (JSONObject)dataArray.get(i);

                String image = jsonObject1.getString("image");
                String name = jsonObject1.getString("name");
                int saleNumber = jsonObject1.getInt("saleNumber");
                double afterMoney = jsonObject1.getDouble("afterMoney");
                String endTime = jsonObject1.getString("endTime");
                int id = jsonObject1.getInt("id");
                double beforeMoney = jsonObject1.getDouble("beforeMoney");
                String url = jsonObject1.getString("url");

                couponRecommendList.add(new CouponMessageBean(image,name,saleNumber,afterMoney,endTime,id,beforeMoney,url));
            }


        }catch (Exception e){
            Log.e("CouponResult","Json解析出错");
            e.printStackTrace();
        }
    }

    private void addTrack(final String couponId){
        new Thread() {
            @Override
            public void run() {
                //传递参数获取响应内容
                String result = httpRequest.addTrack(couponId);
                //判断数据库查询出来值是否与输入的值相等
                if (result != null && !result.isEmpty()){
                    System.out.println("addTrack"+result);
                }
                else {
                    Log.e("IndexFragment","新增足迹失败");
                }
            }
        }.start();
    }
}
