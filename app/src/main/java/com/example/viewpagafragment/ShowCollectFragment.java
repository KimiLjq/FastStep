package com.example.viewpagafragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.viewpagafragment.JavaBean.CouponMessageBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ShowCollectFragment extends Fragment {

    private Context mcontext;
    private ListView lv_show_collect;
    private MyAdapter<CouponMessageBean> myAdapter;

    private HttpRequest httpRequest = new HttpRequest(mcontext);
    private List<CouponMessageBean> collectMessageList = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x1) {
                String str = msg.getData().getString("getCollect");

                parseJson(str);

                if(!collectMessageList.isEmpty()){
                    setAdapter();
                }
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_show_collect,container,false);
        mcontext = view.getContext();

        init(view);
        sendRequest();

        return view;
    }

    private void init(View view){
        lv_show_collect = view.findViewById(R.id.lv_show_collect);
    }

    private void sendRequest(){
        new Thread() {
            @Override
            public void run() {
                //传递参数获取响应内容
                String result = httpRequest.getCollect();
//                System.out.println("Thread**********"+result);
                //判断数据库查询出来值是否与输入的值相等
                if (result != null && !result.isEmpty()){
                    Message msg = new Message();
                    msg.what = 0x1;

                    Bundle bundle = new Bundle();
                    bundle.putString("getCollect",result);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
                else {
                    Log.e("ShowCollectFragment","获取收藏夹失败");
                }
            }
        }.start();
    }

    private void parseJson(String str){
        try{
            JSONObject jsonObject = new JSONObject(str);
            int code = jsonObject.getInt("errcode");
            if(code == 500){
                Log.e("showCollect","获取数据失败");
                System.out.println(str);
                return;
            }

            JSONArray dataArray = jsonObject.getJSONArray("data");

            for(int i=0;i<dataArray.length();i++){
                JSONObject data = (JSONObject)dataArray.get(i);
                String image = data.getString("image");
                String name = data.getString("name");
                int saleNumber = data.getInt("saleNumber");
                double afterMoney = data.getDouble("afterMoney");
                String endTime = data.getString("endTime");
                int id = data.getInt("id");
                double beforeMoney = data.getDouble("beforeMoney");
                String url = data.getString("url");

                collectMessageList.add(new CouponMessageBean(image,name,saleNumber,afterMoney,endTime,id,beforeMoney,url));
            }
        }catch (Exception e){
            Log.e("showCollect","collectJson解析失败");
            e.printStackTrace();
        }
    }

    private void setAdapter(){
        myAdapter = new MyAdapter<CouponMessageBean>((ArrayList)collectMessageList,R.layout.coupon_result) {
            @Override
            public void bindView(ViewHolder holder, CouponMessageBean obj) {
                holder.setImageResource(R.id.iv_coupon_picture,obj.getImage());
                holder.setText(R.id.tv_coupon_name,obj.getName());
                holder.setText(R.id.tv_coupon_type,Integer.toString(obj.getSaleNumebr()));
                holder.setText(R.id.tv_coupon_describe,Double.toString(obj.getBeforeMoney()));
                holder.setText(R.id.tv_coupon_price,Double.toString(obj.getAfterMoney()));
            }
        };

        lv_show_collect.setAdapter(myAdapter);
        lv_show_collect.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CouponMessageBean searchResultBean = collectMessageList.get(position);

//                httpRequest.addTrack(Integer.toString(searchResultBean.getId()));
                System.out.println("onItemSelected"+searchResultBean.getImage());
                Bundle bundle = new Bundle();
                bundle.putString("image",searchResultBean.getImage());
                bundle.putString("name",searchResultBean.getName());
                bundle.putInt("couponId",searchResultBean.getId());
                bundle.putDouble("afterMoney",searchResultBean.getAfterMoney());
                bundle.putDouble("beforeMoney",searchResultBean.getBeforeMoney());
                bundle.putString("url",searchResultBean.getUrl());

                Intent intent = new Intent(mcontext,SpecificCouponActivity.class);
                intent.putExtra("thisCoupon",bundle);
                startActivity(intent);
            }
        });
    }
}
