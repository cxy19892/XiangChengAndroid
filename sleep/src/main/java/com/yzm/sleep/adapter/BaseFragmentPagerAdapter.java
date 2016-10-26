package com.yzm.sleep.adapter;

import java.util.List;

import com.yzm.sleep.utils.LogUtil;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class BaseFragmentPagerAdapter extends FragmentPagerAdapter{  

	private List<Fragment> fragments;  
    public BaseFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragments) {  
        super(fm);  
        this.fragments = fragments;  
    }  
    
    public void setData(List<Fragment> fragments){
    	this.fragments = fragments;
    	this.notifyDataSetChanged();
    }
      
    @Override  
    public int getCount() {  
        return fragments.size();  
    }  
      
    @Override  
    public Fragment getItem(int arg0) {  
        return fragments.get(arg0);  
    }  
    
    
}  
