package com.example.viewpagafragment.PersonalTrack.dropDownMultiPager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.viewpagafragment.PersonalTrack.dropDownMultiPager.transformer.ZoomPageTransformer;
import com.example.viewpagafragment.R;
//import com.yaphetzhao.dropdownmultipager.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YaphetZhao
 * on 2016/12/12.
 */
@SuppressLint({"SetTextI18n", "InflateParams"})
public class DropDownMultiPagerView extends Dialog {

    private Context context;
    private JSONArray jsonArray;
    private MultiViewPager pager;
    private DropDownMultiPagerItem item = null;

    public DropDownMultiPagerView(Context context,JSONArray jsonArray) {
        super(context, R.style.DropDown);
        this.context = context;
        this.jsonArray=jsonArray;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_dropdownfootprint, null);
        setContentView(view);
        setCanceledOnTouchOutside(true);
        setCancelable(true);

        Window dialogWindow = getWindow();
        assert dialogWindow != null;
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        lp.width = dm.widthPixels;
        lp.height = dip2px(context, 290);
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.TOP);
        dialogWindow.setWindowAnimations(R.style.DropDown);

        pager = (MultiViewPager) view.findViewById(R.id.pager);
        pager.setPageTransformer(true, new ZoomPageTransformer());

        List<View> list = new ArrayList<>();
        String id=null;
        String idsetstr=null;
        JSONObject idJson;

//        DropDownMultiPagerItem item = null;
        for (int i = 0; i < 10; i++) {

            try {
            if(jsonArray.isNull(i)) {
                item = new DropDownMultiPagerItem(context, i, jsonArray.getJSONObject(1));
            }else {
                item=new DropDownMultiPagerItem(context,jsonArray.getJSONObject(i).getInt("id"),jsonArray.getJSONObject(i));
            }
            list.add(item);
            } catch (JSONException e) {
            e.printStackTrace();
            }catch (NullPointerException e){
                e.printStackTrace();
            }
        }
        DropDownMultiPagerAdapter adapter = new DropDownMultiPagerAdapter(list);

        pager.setAdapter(adapter);

        adapter.notifyDataSetChanged();

    }

    private int dip2px(Context context, float dpValue) {
        try {
            return (int) (dpValue * context.getResources().getDisplayMetrics().density + 0.5f);
        } catch (Exception e) {
            e.printStackTrace();
            return (int) (dpValue * 1 + 0.5f);
        }
    }


}
