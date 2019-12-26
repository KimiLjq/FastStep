package com.example.viewpagafragment.PersonalTrack;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.viewpagafragment.HttpRequest;
import com.example.viewpagafragment.LoginActivity;
import com.example.viewpagafragment.MainActivity;
import com.example.viewpagafragment.PersonalTrack.dropDownMultiPager.DropDownMultiPagerView;
import com.example.viewpagafragment.PersonalTrack.ultraPullToRefash.component.PtrFrameLayout;
import com.example.viewpagafragment.PersonalTrack.ultraPullToRefash.handler.PtrDefaultHandler;
import com.example.viewpagafragment.R;
import com.example.viewpagafragment.RegisterActivity;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class TraceFragment extends Fragment implements View.OnClickListener, MyScrollView.ScrollingListener {

    private Toolbar toolbar;
    private MyScrollView my_scroll_view;
    private View divider0;
    private ImageView iv_back, iv_settings;//返回,设置
    private TextView tv_title, tv_login, setting;//"个人中心",登录
    private RelativeLayout rl_un_login, rl_login;//未登录和登录后的布局
    private TextView tv_trace_register;

    private float alphaHeight = 0;//透明度渐变的高度

    private PtrFrameLayout ptrLayout;
    JSONArray json, jsonArray;
    String idsetstr = null;
    JSONObject idJson;
    Context mcontext;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_track, container, false);
        mcontext = view.getContext();

        ptrLayout = view.findViewById(R.id.ptrlayout);

        final TextView textView = new TextView(mcontext);
        ptrLayout.setHeaderView(textView);
        ptrLayout.disableWhenHorizontalMove(true);
        initViews(view);
        initData();
        setToolbar();
        setListeners();


        TimerTask task = new TimerTask() {
            public void run() {
                new Thread(new getCp()).start();//每次需要执行的代码放到这里面。
            }
        };
        Timer timer = new Timer();
        new Thread(new getCp()).start();
        timer.schedule(task, 5000);

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return true;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout.refreshComplete();

                new DropDownMultiPagerView(mcontext, json).show();

            }

        });

        return view;
    }


    private void initData() {
        alphaHeight = Utils.dip2px(mcontext, 160);
        tv_title.setVisibility(View.INVISIBLE);//"个人中心"暂时不可见

        EventBus.getDefault().unregister(this);//订阅
    }

    private void initViews(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        my_scroll_view = (MyScrollView) view.findViewById(R.id.my_scroll_view);
        divider0 = view.findViewById(R.id.divider0);
        iv_back = (ImageView) view.findViewById(R.id.iv_back);
        iv_settings = (ImageView) view.findViewById(R.id.iv_settings);
        setting = (TextView) view.findViewById(R.id.settings);
        tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_login = (TextView) view.findViewById(R.id.tv_trace_login);
        rl_un_login = (RelativeLayout) view.findViewById(R.id.rl_un_login);
        rl_login = (RelativeLayout) view.findViewById(R.id.rl_login);

        tv_trace_register = view.findViewById(R.id.tv_register);
    }

    private void setListeners() {
        iv_back.setOnClickListener(this);
        iv_settings.setOnClickListener(this);
        setting.setOnClickListener(this);
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,LoginActivity.class);

                startActivity(intent);
            }
        });

        tv_trace_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mcontext,RegisterActivity.class);

                startActivity(intent);
            }
        });
        my_scroll_view.setScrollingListener(this);
    }

    private void setToolbar() {
        //设置导航图标要在setSupportActionBar方法之后
//        Utils.initToolbar(this.getActivity(), toolbar, "", "", 0, null);

        toolbar.setAlpha(0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
//                finish();
                break;
            case R.id.iv_settings:
            case R.id.settings:
            case R.id.tv_login:
                startProfileActivity();
                break;
        }
    }

    //启动修改资料的页面
    private void startProfileActivity() {
        Intent intent = new Intent(mcontext, ChangeProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onScrolling(int l, int t, int oldl, int oldt) {
        float alpha = t * 1.0f / alphaHeight;
        if (alpha < 0.4f) {
            alpha = 0;
            if (divider0.getVisibility() == View.VISIBLE) {
                divider0.setVisibility(View.INVISIBLE);
                tv_title.setVisibility(View.INVISIBLE);
            }
        } else {
            if (divider0.getVisibility() == View.INVISIBLE) {
                divider0.setVisibility(View.VISIBLE);
                tv_title.setVisibility(View.VISIBLE);
            }
        }
        toolbar.setAlpha(alpha);
    }

    //ThreadMode共四个:MAIN UI线程 BACKGROUND 后台线程 POSTING 和发布者处在同一个线程 ASYNC 异步线程
    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(EventModel event) {
        switch (event.getEventCode()) {
            case EventModel.CODE_LOGIN://登录
                rl_login.setVisibility(View.VISIBLE);
                rl_un_login.setVisibility(View.GONE);
                break;
            case EventModel.CODE_LOGOUT://登出
                rl_un_login.setVisibility(View.VISIBLE);
                rl_login.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);//解除订阅
    }


    class getCp implements Runnable {
        @Override
        public void run() {
            HttpRequest httpRequest = new HttpRequest(mcontext);
            String dataset = httpRequest.getSteps();

            try {
                JSONObject jsonObject = new JSONObject(dataset);
                Log.e("faststep", String.valueOf(jsonObject));
                show(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }


    }

    public JSONArray show(final JSONObject jsondata) throws JSONException {


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    json = jsondata.getJSONArray("data");
                    Log.e("main", "This:" + json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
        return json;
    }

}
