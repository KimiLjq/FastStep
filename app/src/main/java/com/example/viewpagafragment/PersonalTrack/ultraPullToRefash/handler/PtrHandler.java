package com.example.viewpagafragment.PersonalTrack.ultraPullToRefash.handler;

import android.view.View;

import com.example.viewpagafragment.PersonalTrack.ultraPullToRefash.component.PtrFrameLayout;

import org.json.JSONException;


public interface PtrHandler {

    boolean checkCanDoRefresh(final PtrFrameLayout frame, final View content, final View header);

    void onRefreshBegin(final PtrFrameLayout frame) throws JSONException;
}