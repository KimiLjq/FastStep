package com.example.viewpagafragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private android.support.v7.widget.Toolbar toolbar = null;
    private EditText search_coupon = null;
    private ImageView delete_search = null;
    private Button submit_search  = null;
    private TextView delete_record = null;
    private ListView search_tips = null;
    private ListView lv_search_history = null;
    private ArrayAdapter<String> tipAdapter = null;
    private ArrayAdapter<String> historyAdapter = null;

    private String[] mString = {"book","ball","badminton","compute","phone","shoes"};
    public SharedPreferences sp = null;
    public int historyCount = 0;
    private String[] searchHistory = null;
    private String searchResult = null;
    private HttpRequest httpRequest = new HttpRequest(SearchActivity.this);
    private String searchTip = null;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0x123) {
                String str = msg.getData().getString("searchTip");
                mString = parseTipJson(str);

                tipAdapter.notifyDataSetChanged();

                delete_search.setVisibility(View.VISIBLE);
                search_tips.setVisibility(View.VISIBLE);
                search_tips.setFilterText(search_coupon.getText().toString());
                tipAdapter.getFilter().filter(search_coupon.getText().toString());
            }else if(msg.what == 0x111){
                searchResult = msg.getData().getString("searchResult");
//                System.out.println("handle"+searchResult);
                Bundle bundle = new Bundle();
                bundle.putString("coupon",searchResult);

                Intent intent = new Intent(SearchActivity.this,SearchCouponResultActivity.class);
                intent.putExtra("couponBundle",bundle);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initView();

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

        tipAdapter = new ArrayAdapter<>(this,android.R.layout.simple_expandable_list_item_1,mString);
        search_tips.setAdapter(tipAdapter);
        //search_tips.setTextFilterEnabled(false);


        historyAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,searchHistory);
        lv_search_history.setAdapter(historyAdapter);

        setListener();

        search_tips.setOnItemSelectedListener(this);
        lv_search_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String str = adapterView.getItemAtPosition(i).toString();
                submitSearch(str);
            }
        });

    }

    private void initView(){
        toolbar = findViewById(R.id.search_coupon_toorbar);
        search_coupon = findViewById(R.id.search_coupon);
        delete_search = findViewById(R.id.search_coupon_delete);
        submit_search = findViewById(R.id.search_coupon_submit);
        delete_record = findViewById(R.id.tv_delete_record);
        search_tips = findViewById(R.id.search_coupon_tips);
        lv_search_history = findViewById(R.id.lv_search_history);

        sp = this.getSharedPreferences("searchHistory",Context.MODE_PRIVATE);
        historyCount = sp.getInt("historyCount",0);
        searchHistory = new String[historyCount];
        if(historyCount>0){
            for(int i=1;i<=historyCount;i++){
                searchHistory[i-1] = sp.getString("searchHistory"+i,"error");
            }
        }
    }

    private void setListener(){
        search_coupon.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(search_coupon.getText().toString())){
                    delete_search.setVisibility(View.GONE);
                    search_tips.setVisibility(View.GONE);
                    search_tips.clearTextFilter();
                    tipAdapter.getFilter().filter("");
                }else{
                    final String str = s.toString();
                    new Thread() {
                        @Override
                        public void run() {
                            //传递参数获取响应内容
                            String result = httpRequest.searchTip(str);
                            //判断数据库查询出来值是否与输入的值相等
                            if (result != null && !result.isEmpty()){
                                searchTip = result;

//                                System.out.println("+*****"+searchTip);
                                Message msg = new Message();
                                msg.what = 0x123;

                                Bundle bundle = new Bundle();
                                bundle.putString("searchTip",searchTip);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            }
                            else {
                                Log.e("SearchTipResult","查询提示失败");
                            }

//                            System.out.println(searchTip);
                        }
                    }.start();

                }
            }
        });

        delete_search.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                search_coupon.setText(null);
                return true;
            }
        });

        submit_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = search_coupon.getText().toString();

                historyCount++;
                sp.edit().putInt("historyCount",historyCount).commit();
                sp.edit().putString("searchHistory"+historyCount , str).commit();

                submitSearch(str);

                /*Bundle bundle = new Bundle();
                System.out.println("**********"+searchResult);
                bundle.putString("coupon",searchResult);

                Intent intent = new Intent(TraceFragment.this,SearchCouponResultActivity.class);
                intent.putExtra("couponBundle",bundle);
                startActivity(intent);*/
            }
        });

        delete_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyCount = 0;
                sp.edit().putInt("historyCount",historyCount).commit();
                searchHistory = null;
                historyAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String str = parent.getItemAtPosition(position).toString();
        submitSearch(str);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void submitSearch(String text){
        final String str = text;

        new Thread() {
            @Override
            public void run() {
                //传递参数获取响应内容
                String result = httpRequest.searchRequest(str);
                //判断数据库查询出来值是否与输入的值相等
                if (result != null && !result.isEmpty()){
                    searchResult = result;
                    Message msg = new Message();
                    msg.what = 0x111;

                    Bundle bundle = new Bundle();
                    bundle.putString("searchResult",searchResult);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
                else {
                    Log.e("SearchResult","查询失败");
                }

            }
        }.start();
    }

    private String[] parseTipJson(String str){
        try{
            JSONObject jsonObject = new JSONObject(str);

            JSONArray dataArray = jsonObject.getJSONArray("data");
            ArrayList<String> dataList = new ArrayList<>();
            for(int i=0;i<dataArray.length();i++){
                JSONObject jsonObject1 = (JSONObject)dataArray.get(i);
                dataList.add(jsonObject1.getString("name"));
            }

            String[] strings = new String[dataList.size()];
            for(int i=0;i<dataList.size();i++){
                strings[i] = dataList.get(i);
            }
            return strings;
        }catch (Exception e){
            Log.e("parseTipJson","Json解析出错");
            e.printStackTrace();
            return null;
        }

    }
}
