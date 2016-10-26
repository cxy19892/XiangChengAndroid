package com.yzm.sleep.widget;

import java.lang.reflect.Field;

import android.support.v4.app.Fragment;  

/** 
 * Author: huang
 */  
public abstract class LazyFragment extends Fragment {  
    protected boolean isVisible;  
    /** 
     * 在这里实现Fragment数据的缓加载. 
     * @param isVisibleToUser 
     */  
    @Override  
    public void setUserVisibleHint(boolean isVisibleToUser) {  
        super.setUserVisibleHint(isVisibleToUser);  
        if(getUserVisibleHint()) {  
            isVisible = true;  
            onVisible();  
        } else {  
            isVisible = false;  
            onInvisible();  
        }  
    }  
    
    @Override
	public void onDetach() {
		super.onDetach();
		try {
	        Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
	        childFragmentManager.setAccessible(true);
	        childFragmentManager.set(this, null);
	    } catch (NoSuchFieldException e) {
	        throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	        throw new RuntimeException(e);
	    }
	}
  
    protected void onVisible(){  
        lazyLoad();  
    }  
  
    protected abstract void lazyLoad();  
  
    protected void onInvisible(){}  
}  
