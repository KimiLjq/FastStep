package com.example.viewpagafragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.viewpagafragment.JavaBean.CouponMessageBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SearchCouponResultActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private Toolbar toolbar = null;
    private ImageView iv_search = null;
    private ListView listView = null;
    private MyAdapter<CouponMessageBean> myAdapter = null;

    private List<CouponMessageBean> searchResult = new ArrayList<>();
    private HttpRequest httpRequest = new HttpRequest(SearchCouponResultActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_coupon_result);

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("couponBundle");
        String str = bundle.getString("coupon");
        System.out.println("SearchCouponResultActivity:"+str);
        parseJSON(str);

        initView();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        //设置返回键是否可以点击
        actionBar.setDisplayHomeAsUpEnabled(true);
        //隐藏默认标题
        actionBar.setDisplayShowTitleEnabled(false);

        setListener();

    }

    private void initView(){
        toolbar = findViewById(R.id.tb_coupon_result);
        iv_search = findViewById(R.id.iv_coupon_result_search);
        listView = findViewById(R.id.lv_coupon_result);

        myAdapter = new MyAdapter<CouponMessageBean>((ArrayList)searchResult,R.layout.coupon_result) {
            @Override
            public void bindView(ViewHolder holder, CouponMessageBean obj) {
                holder.setImageResource(R.id.iv_coupon_picture,obj.getImage());
                holder.setText(R.id.tv_coupon_name,obj.getName());
                holder.setText(R.id.tv_coupon_type,Integer.toString(obj.getSaleNumebr()));
                holder.setText(R.id.tv_coupon_describe,Double.toString(obj.getBeforeMoney()));
                holder.setText(R.id.tv_coupon_price,Double.toString(obj.getAfterMoney()));
            }
        };
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CouponMessageBean searchResultBean = searchResult.get(i);

//                httpRequest.addTrack(Integer.toString(searchResultBean.getId()));
                System.out.println("onItemSelected"+searchResultBean.getImage());
                Bundle bundle = new Bundle();
                bundle.putString("image",searchResultBean.getImage());
                bundle.putString("name",searchResultBean.getName());
                bundle.putInt("couponId",searchResultBean.getId());
                bundle.putDouble("afterMoney",searchResultBean.getAfterMoney());
                bundle.putDouble("beforeMoney",searchResultBean.getBeforeMoney());
                bundle.putString("url",searchResultBean.getUrl());

                Intent intent = new Intent(SearchCouponResultActivity.this,SpecificCouponActivity.class);
                intent.putExtra("thisCoupon",bundle);
                startActivity(intent);
            }
        });

    }

    private void setListener(){
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                finish();
                return true;
            }
        });

    }

    private void parseJSON(String str){
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

                searchResult.add(new CouponMessageBean(image,name,saleNumber,afterMoney,endTime,id,beforeMoney,url));
            }


        }catch (Exception e){
            Log.e("CouponResult","Json解析出错");
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CouponMessageBean searchResultBean = searchResult.get(position);

        httpRequest.addTrack(Integer.toString(searchResultBean.getId()));
        System.out.println("onItemSelected"+searchResultBean.getImage());
        Bundle bundle = new Bundle();
        bundle.putString("image",searchResultBean.getImage());
        bundle.putString("name",searchResultBean.getName());
        bundle.putDouble("afterMoney",searchResultBean.getAfterMoney());
        bundle.putDouble("beforeMoney",searchResultBean.getBeforeMoney());
        bundle.putString("url",searchResultBean.getUrl());

        Intent intent = new Intent(SearchCouponResultActivity.this,SpecificCouponActivity.class);
        intent.putExtra("thisCoupon",bundle);
        startActivity(intent);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
