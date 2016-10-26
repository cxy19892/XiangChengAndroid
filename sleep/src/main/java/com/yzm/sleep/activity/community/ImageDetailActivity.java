package com.yzm.sleep.activity.community;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;

import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.bean.ArticleBean;
import com.yzm.sleep.bean.ArticleImageBean;
import com.yzm.sleep.bean.CommunityTopicBean;

/**
 * 显示图片详情 <br>
 * 需要参数： <br>
 * 话题信息            			img_info       <br>
 * 选中图片在gridview的位置       position      <br>
 * 如果取不到话题信息可以传CommunityTopicBean.getImages()
 * 图片列表        字段   img_list <br>
 * @author chen
 *
 */
public class ImageDetailActivity extends BaseActivity{

	private ViewPager mViewpager;
	private ImagePagerAdapter mAdapter;
	private int selectid;
	private List<ArticleImageBean> imagelist;
	
	 @SuppressWarnings("unchecked")
	@Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_viewpage_detail); 
			List<Fragment> list = new ArrayList<Fragment>();
			if("1".equals(getIntent().getStringExtra("chatfrom"))){ //聊天看大图
				ImageDetailFragment fragment = new ImageDetailFragment();
				Bundle bundle = new Bundle();
				bundle.putParcelable("uri", getIntent().getParcelableExtra("uri"));
				bundle.putString("remotepath", getIntent().getStringExtra("remotepath"));
				bundle.putString("chatfrom", "1");
				fragment.setArguments(bundle);
				list.add(fragment);
			}else{
				Intent mIntent = getIntent();
		        ArticleBean mArticleBean = null;
				if(mIntent.getSerializableExtra("img_info") instanceof CommunityTopicBean)
					mArticleBean = (ArticleBean) mIntent.getSerializableExtra("img_info");//ArrayListExtra("img_list");
				selectid = mIntent.getIntExtra("position", 0);
				if(mArticleBean == null){
					if(mIntent.getSerializableExtra("img_list") instanceof List)
						imagelist = (List<ArticleImageBean>) mIntent.getSerializableExtra("img_list");
					if(imagelist == null)
						AppManager.getAppManager().finishActivity();
				}else if(mArticleBean.getImages()==null || mArticleBean.getImages().size()==0){
					AppManager.getAppManager().finishActivity();
				}else{
					imagelist = mArticleBean.getImages();
				}
				for (int i = 0; i < imagelist.size() && i < 9 ; i++) {
					ImageDetailFragment fragment = new ImageDetailFragment();
					Bundle bundle = new Bundle();
					bundle.putSerializable("item", imagelist.get(i));
					fragment.setArguments(bundle);
					list.add(fragment);
				}
			}
			
	        mAdapter = new ImagePagerAdapter(getSupportFragmentManager(), list);
	        mViewpager = (ViewPager) findViewById(R.id.my_img_viewpager);
	        mViewpager.setAdapter(mAdapter);
	        mViewpager.setCurrentItem(selectid, false);
	    }
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
	    public class ImagePagerAdapter extends FragmentStatePagerAdapter {
	        private List<Fragment> list;
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
	        public ImagePagerAdapter(FragmentManager fm, List<Fragment> list) {
	            super(fm);
	            this.list = list;
	        }
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
	        @Override
	        public int getCount() {
	            return list.size();
	        }
	                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
	        @Override
	        public Fragment getItem(int position) {
	            return list.get(position);
	        }
	    }

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AppManager.getAppManager().finishActivity();
		}
		return true;
	}
	
}
