package com.yzm.sleep.fragment;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.BaseFragmentPagerAdapter;
import com.yzm.sleep.adapter.OpenCityAdapter;
import com.yzm.sleep.bean.CitysBean;
import com.yzm.sleep.indicator.UnderlinePageIndicator;
import com.yzm.sleep.model.CustomPopupWindow;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceOpenCitysCallback;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;

public class FragmentPage2Doctor extends Fragment implements OnClickListener,
		OnPageChangeListener {

	private TextView openCity, tvTab1, tvTab2;
	private ImageButton btnMsg;
	private BaseFragmentPagerAdapter mAdapter;
	private ViewPager viewPager;
	private FragmentPage2Tab1Medical fragmentPage2Tab1Medical;
	private FragmentPage2Tab2Experts fragmentPage2Tab2Experts;
//	private FragmentPage2Tab3Store fragmentPage2Tab3Store;
	private List<Fragment> fragments;
	private Bundle bundle;
	private View titleView;
	private CustomPopupWindow dialog;
	private ListView mListView;
	private OpenCityAdapter openCityAdapter;
	private LocationClient mLocClient;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		bundle = getArguments();
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_community, container, false);
	}
	
	

	@Override
	public void onPause() {
		super.onPause();
		try {
			mLocClient.unRegisterLocationListener(myListener);
		} catch (Exception e) {
		}
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		titleView = view.findViewById(R.id.community_title);
		btnMsg = (ImageButton) view.findViewById(R.id.community_title_msg);
		tvTab1 = (TextView) view.findViewById(R.id.community_title_tab1);
		tvTab2 = (TextView) view.findViewById(R.id.community_title_tab2);
		view.findViewById(R.id.community_title_tab3).setVisibility(View.GONE);;

		btnMsg.setOnClickListener(this);
		view.findViewById(R.id.community_title_search).setVisibility(View.GONE);
		openCity = (TextView) view.findViewById(R.id.open_city);
		openCity.setVisibility(View.VISIBLE);
		openCity.setOnClickListener(this);

		tvTab1.setText("医馆");
		tvTab2.setText("专家");

		tvTab1.setOnClickListener(this);
		tvTab2.setOnClickListener(this);

		viewPager = (ViewPager) view.findViewById(R.id.community_viewpager);
		fragments = new ArrayList<Fragment>();

		fragmentPage2Tab1Medical = new FragmentPage2Tab1Medical();
		fragmentPage2Tab1Medical.setArguments(bundle);

		fragmentPage2Tab2Experts = new FragmentPage2Tab2Experts();
		fragmentPage2Tab2Experts.setArguments(bundle);

//		fragmentPage2Tab3Store = new FragmentPage2Tab3Store();
//		fragmentPage2Tab3Store.setArguments(bundle);

		fragments.add(fragmentPage2Tab1Medical);
		fragments.add(fragmentPage2Tab2Experts);
//		fragments.add(fragmentPage2Tab3Store);

		mAdapter = new BaseFragmentPagerAdapter(getChildFragmentManager(),
				fragments);
		viewPager.setAdapter(mAdapter);
		viewPager.setOffscreenPageLimit(2);

		UnderlinePageIndicator indicator = (UnderlinePageIndicator) view
				.findViewById(R.id.indicator);
		indicator.setViewPager(viewPager);
		indicator.setFades(false);
		indicator.setSelectedColor(getResources()
				.getColor(R.color.theme_color));
		indicator.setOnPageChangeListener(this);
		
		String city = PreManager.instance().getUserLocationCity(getActivity());
		String longitude = PreManager.instance().getUserLongitude(getActivity());
		String latitude = PreManager.instance().getUserLatitude(getActivity());
		openCity.setText(city.replace("市", ""));
		if(TextUtils.isEmpty(city) || TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)){
			mLocClient = new LocationClient(getActivity());
			mLocClient.registerLocationListener(myListener);
			LocationClientOption option = new LocationClientOption();
			option.setOpenGps(true);// 打开gps
			option.setCoorType("bd09ll"); // 设置坐标类型
			option.setAddrType("all");
			option.setScanSpan(1000);
			mLocClient.setLocOption(option);
			mLocClient.start();
		}
	}

	/**
	 * 定位监听
	 */
	private BDLocationListener myListener = new BDLocationListener() {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if(mLocClient != null)
				mLocClient.stop();
			
			if(location !=null){
				String city= location.getCity();
				if(!TextUtils.isEmpty(city))
					city = "成都";
				
				PreManager.instance().saveUserLocationCity(getActivity(), city);
				PreManager.instance().saveUserLatitude(getActivity(), location.getLatitude()+"");
				PreManager.instance().saveUserLongitude(getActivity(), location.getLongitude()+"");
			}else
				openCity.setText("成都");
			
		}
	};
	
	/**
	 * 展示所有开放城市
	 */
	private void showOpenCity() {
		if(dialog == null){
			LayoutInflater mInflater = LayoutInflater.from(getActivity());
			View view = mInflater.inflate(R.layout.dialog_opencity, null);
			int height = Util.Dp2Px(getActivity(), 50) * 4 + Util.Dp2Px(getActivity(), 45);
			dialog = new CustomPopupWindow(getActivity(), view, Constant.screenWidht, height);
			mListView = (ListView) view.findViewById(R.id.open_citys);
			openCityAdapter = new OpenCityAdapter(getActivity());
			mListView.setAdapter(openCityAdapter);
			mListView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
					CitysBean bean = openCityAdapter.getData().get(position);
					openCity.setText(bean.getCity());
					fragmentPage2Tab1Medical.refresh(bean.getCity());
					fragmentPage2Tab2Experts.refresh(bean.getCity());
					dialog.dismiss();
				}
			});
		}
		
		WindowManager.LayoutParams params = getActivity().getWindow().getAttributes();  
        params.alpha=0.5f;
        getActivity().getWindow().setAttributes(params);
        backgroundAlpha(0.5f);
		dialog.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
		dialog.showAsDropDown(titleView);
		
		if(Util.checkNetWork(getActivity())){
			getOpenCitys();
		}else{
			openCity.setText("成都");
			List<CitysBean> list = new ArrayList<CitysBean>();
			CitysBean bean = new CitysBean();
			bean.setCity("成都");
			bean.setId("1");
			bean.setFlag("1");
			list.add(bean);
			openCityAdapter.setData(list, openCity.getText().toString());
		}
	}
	
	private int index(String city){
		List<CitysBean> list = openCityAdapter.getData();
		if(list ==null)
			return -1;
		int size = list.size();
		for(int i=0; i < size; i++){
			if(city.equals(list.get(i).getCity()))
				return i;
		}
		return -1;
	}
	
	private void getOpenCitys(){
		new XiangchengMallProcClass(getActivity()).getOpenCitys(new InterfaceOpenCitysCallback() {
			
			@Override
			public void onSuccess(int icode, List<CitysBean> citylist) {
				openCityAdapter.setData(citylist, openCity.getText().toString());
				int index = index(openCity.getText().toString());
				if(index >= 0)
					mListView.setSelection(index);
			}
			
			@Override
			public void onError(int icode, String strErrMsg) {
				
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.community_title_tab1:
			viewPager.setCurrentItem(0, true);
			break;

		case R.id.community_title_tab2:
			viewPager.setCurrentItem(1, true);
			break;

		case R.id.community_title_tab3:
//			viewPager.setCurrentItem(2, true);
			break;
		case R.id.open_city:
			showOpenCity();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		try {
			Field childFragmentManager = Fragment.class
					.getDeclaredField("mChildFragmentManager");
			childFragmentManager.setAccessible(true);
			childFragmentManager.set(this, null);
		} catch (NoSuchFieldException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}

	}
	
	private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);
    }


	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		if (position == 0) {
			tvTab1.setTextColor(getResources().getColor(R.color.theme_color));
			tvTab2.setTextColor(getResources().getColor(R.color.ct_color));
//			tvTab3.setTextColor(getResources().getColor(R.color.t_color));
			openCity.setVisibility(View.VISIBLE);
		} else if (position == 1) {
			tvTab1.setTextColor(getResources().getColor(R.color.ct_color));
			tvTab2.setTextColor(getResources().getColor(R.color.theme_color));
//			tvTab3.setTextColor(getResources().getColor(R.color.t_color));
			openCity.setVisibility(View.VISIBLE);
		} 
//		else if (position == 2) {
//			tvTab1.setTextColor(getResources().getColor(R.color.t_color));
//			tvTab2.setTextColor(getResources().getColor(R.color.t_color));
//			tvTab3.setTextColor(getResources().getColor(R.color.theme_color));
//			openCity.setVisibility(View.GONE);
//		}

	}

}
