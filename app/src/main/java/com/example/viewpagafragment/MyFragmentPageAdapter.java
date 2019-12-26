package com.example.viewpagafragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.example.viewpagafragment.PersonalTrack.TraceFragment;

/**
 * 使用Fragment来填充ViewPager
 * 这个类是为ViewPager提供的适配器
 */
public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    private final int PAGER_COUNT = 3;
    private IndexFragment indexFragment1 = null;
    private ShowCollectFragment showCollectFragment = null;
    private TraceFragment traceFragment = null;

    public MyFragmentPageAdapter(FragmentManager fm){
        super(fm);
        indexFragment1 = new IndexFragment();
        indexFragment1.setNum(1);
        showCollectFragment = new ShowCollectFragment();
        traceFragment = new TraceFragment();
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    /**
     * 将选中的页面回传到viewPager
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case MainActivity.PAGE_ONE:
                fragment = indexFragment1;
                break;
            case MainActivity.PAGE_TWO:
                fragment = showCollectFragment;
                break;
            case MainActivity.PAGE_THREE:
                fragment = traceFragment;
                break;
        }
        return fragment;
    }
}
