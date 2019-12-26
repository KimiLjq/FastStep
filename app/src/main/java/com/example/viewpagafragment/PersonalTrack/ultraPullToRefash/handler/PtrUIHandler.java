package com.example.viewpagafragment.PersonalTrack.ultraPullToRefash.handler;


import com.example.viewpagafragment.PersonalTrack.ultraPullToRefash.component.PtrFrameLayout;
import com.example.viewpagafragment.PersonalTrack.ultraPullToRefash.indicator.PtrIndicator;

public interface PtrUIHandler {

    void onUIReset(PtrFrameLayout frame);

    void onUIRefreshPrepare(PtrFrameLayout frame);

    void onUIRefreshBegin(PtrFrameLayout frame);

    void onUIRefreshComplete(PtrFrameLayout frame);

    void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator);
}
