package com.yzm.sleep.activity;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;
import com.yzm.sleep.samplewww.HostJsScope.PictureSelectInfo;
import com.yzm.sleep.samplewww.HostJsScope.PictureWallInfo;

public class PictureWallPagerActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_picture_pager);
//		overridePendingTransition(R.anim.zoom_enter, 0);
		PictureSelectInfo pictureSelectInfo = (PictureSelectInfo)(getIntent().getSerializableExtra("picture_wall_info"));
		ArrayList<PictureWallInfo> pictureWallInfos = pictureSelectInfo.photoArray;
		
		ViewPager viewpager = (ViewPager) findViewById(R.id.viewpager);
		
		ArrayList<View> views = new ArrayList<View>();
		PictureWallInfo pictureWallInfo = null;
		for (int i = 0; i < pictureWallInfos.size(); i++) {
			pictureWallInfo = pictureWallInfos.get(i);
			ImageView iv = new ImageView(this);
			int width = getWindowManager().getDefaultDisplay().getWidth();
			LayoutParams params = new LayoutParams(width,
					width);
			iv.setLayoutParams(params);
			iv.setScaleType(ScaleType.FIT_CENTER);
			iv.setImageResource(0);
			ImageLoader.getInstance().displayImage(pictureWallInfo.path,pictureWallInfo.key,iv, MyApplication.defaultOption);
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
//					overridePendingTransition(R.anim.zoom_enter, R.anim.zoom_exit);
					setResult(102);
					AppManager.getAppManager().finishActivity(PictureWallPagerActivity.this);
				}
				
			});
			views.add(iv);

		}
		MyPagerAdapter adapter = new MyPagerAdapter(views);
		viewpager.setAdapter(adapter);
//		System.out.println("pictureSelectInfo.index = " + pictureSelectInfo.index);
		viewpager.setCurrentItem(pictureSelectInfo.index - 1);
	}
	
	
	
	
	public class MyPagerAdapter extends PagerAdapter {
		ArrayList<View> views;

		public MyPagerAdapter(ArrayList<View> views) {
			this.views = views;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = views.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			setResult(102);
        }
        return super.onKeyDown(keyCode,event);
	}
	
}
