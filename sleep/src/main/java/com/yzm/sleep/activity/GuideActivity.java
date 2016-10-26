package com.yzm.sleep.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.BaseFragmentPagerAdapter;
import com.yzm.sleep.fragment.FragmentGuidePage;
import com.yzm.sleep.utils.PreManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 */
public class GuideActivity extends BaseActivity implements FragmentGuidePage.OnClickEnter{

    private List<Fragment> fragments;
    private ViewPager mViewPager;
    private BaseFragmentPagerAdapter adapter;
    private String[] imgs = {"drawable://" + R.drawable.guide1, "drawable://" + R.drawable.guide2, "drawable://" + R.drawable.guide3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        fragments = new ArrayList<Fragment>();
        for (int i =0 ; i <  3; i++){
            FragmentGuidePage page = new FragmentGuidePage();
            Bundle bundle = new Bundle();
            bundle.putString("imgurl", imgs[i]);
            if(i == 2){
                page.setListener(this);
            }
            page.setArguments(bundle);
            fragments.add(page);
        }
        adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
    }

    @Override
    public void onClick() {
        startActivity(new Intent(this, HomeActivity.class));
        AppManager.getAppManager().finishActivity();
        PreManager.instance().saveShowGuide(this);
    }
}
