package com.yzm.sleep.model;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.yzm.sleep.Constant;
import com.yzm.sleep.R;
import com.yzm.sleep.adapter.BaseFragmentPagerAdapter;
import com.yzm.sleep.fragment.FragmentRingsDownload;
import com.yzm.sleep.fragment.FragmentRingsMine;
import com.yzm.sleep.indicator.UnderlinePageIndicator;

public class DialogFragmentWindow extends DialogFragment implements OnClickListener, OnPageChangeListener {
	private FragmentRingsMine FragmentT1;
	private FragmentRingsDownload FragmentT2;
	private ImageButton btnCancle, btnConfirm;
	private Button btnMyown, btnGetMore;
	private ViewPager vp_contentAcoesMusculares_SequenciaExercicios;
	private OnMusicSelectedListener mListener;
//	private static RingtoneInfo getRingtoneInfo;
//	private View myRingsLines, getMoreLines;
	private RelativeLayout myRingsRel, getMoreRel;
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			this.mListener = (OnMusicSelectedListener) activity;
		} catch (final ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
		}
		
	}



	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_window, container);

        vp_contentAcoesMusculares_SequenciaExercicios = (ViewPager) view.findViewById(R.id.vp_fragment_dialog_for_rings);
        List<Fragment> fragments = getFragments();
        BaseFragmentPagerAdapter ama = new BaseFragmentPagerAdapter(getChildFragmentManager(), fragments);
        vp_contentAcoesMusculares_SequenciaExercicios.setAdapter(ama);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().setCanceledOnTouchOutside(true);
        initViews(view);
        return view;
    }
	
	
	
	private void initViews(View view) {
		btnCancle  = (ImageButton) view.findViewById(R.id.pop_btn_cancle);
		btnConfirm = (ImageButton) view.findViewById(R.id.pop_btn_confirm);
		btnMyown   = (Button) view.findViewById(R.id.btn_my_rings);
		btnGetMore = (Button) view.findViewById(R.id.btn_get_more);
//		myRingsLines = view.findViewById(R.id.my_rings_lines);
//		getMoreLines = view.findViewById(R.id.get_more_lines);
		myRingsRel = (RelativeLayout) view.findViewById(R.id.rel_my_rings);
		getMoreRel = (RelativeLayout) view.findViewById(R.id.rel_get_more);
		
		btnCancle.setOnClickListener(this);
		btnConfirm.setOnClickListener(this);
		btnMyown.setOnClickListener(this);
		btnGetMore.setOnClickListener(this);
		btnMyown.setTextColor(getResources().getColor(R.color.theme_color));
		
		UnderlinePageIndicator indicator = (UnderlinePageIndicator) view
				.findViewById(R.id.dialog_f_indicator);
		indicator.setViewPager(vp_contentAcoesMusculares_SequenciaExercicios);
		indicator.setFades(false);
		indicator.setSelectedColor(getResources().getColor(R.color.theme_color));
		indicator.setOnPageChangeListener(this);
		
//		vp_contentAcoesMusculares_SequenciaExercicios.setOnPageChangeListener(this);
	}



	@Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
           dialog.getWindow().setWindowAnimations(R.style.bottom_animation);
           dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, (int) (Constant.screenHeight * 0.75));
           dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
           dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
    }
	
	@Override
	public void onDestroy() {
		Intent intent = new Intent();
		intent.setAction("com.yzm.media.MUSIC_SERVICE");
		intent.putExtra("url", "");
		intent.putExtra("MSG", PlayerMsg.STOP_MSG);
		//启动服务
		getActivity().stopService(intent);
		Intent intent2 = new Intent();
		intent2.setAction("com.audio.play.dialog.DIALOG_DISMISS");
		getActivity().sendBroadcast(intent2);
		super.onDestroy();
	}

    private List<Fragment> getFragments(){
        List<Fragment> fList = new ArrayList<Fragment>();
        FragmentT1 = new FragmentRingsMine();
        FragmentT2 = new FragmentRingsDownload();
            fList.add(FragmentT1);
            fList.add(FragmentT2);
        return fList;
    }

	@Override
	public void onStop() {
		
		super.onStop();
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.pop_btn_cancle:
			DialogFragmentWindow.this.dismiss();
			break;
		case R.id.pop_btn_confirm:
			RingtoneInfo mRingtoneInfo = null;
			if(FragmentT1!=null){
				mRingtoneInfo = FragmentT1.getSelectRingtone();
			}
			if(mRingtoneInfo != null){
				
				mListener.onMusicSelected(mRingtoneInfo);
			}
			DialogFragmentWindow.this.dismiss();
			break;
		case R.id.btn_my_rings:
//			btnMyown.setTextColor(ColorStateList.valueOf(R.color.color_ff5722));
//			btnGetMore.setTextColor(ColorStateList.valueOf(R.color.color_999999));
			vp_contentAcoesMusculares_SequenciaExercicios.setCurrentItem(0);
			break;
			
		case R.id.btn_get_more:
//			btnMyown.setTextColor(ColorStateList.valueOf(R.color.color_999999));
//			btnGetMore.setTextColor(ColorStateList.valueOf(R.color.color_ff5722));
			vp_contentAcoesMusculares_SequenciaExercicios.setCurrentItem(1);
			break;
		
 		default:
			break;
		}
		
	}
	
	
	public static interface OnMusicSelectedListener {
		public void onMusicSelected(RingtoneInfo mRingtoneInfo);
	}


	@Override
	public void onPageSelected(int arg0) {
		switch (arg0) {
		case 0:
			btnMyown.setTextColor(getResources().getColor(R.color.theme_color));
			btnGetMore.setTextColor(getResources().getColor(R.color.ct_color));
//			myRingsLines.setBackgroundColor(getResources().getColor(R.color.color_8745ff));
//			getMoreLines.setBackgroundColor(getResources().getColor(R.color.transparent));
			myRingsRel.setBackgroundColor(getResources().getColor(R.color.transparent));
			getMoreRel.setBackgroundColor(getResources().getColor(R.color.bg_color));
			break;
		case 1:
			btnMyown.setTextColor(getResources().getColor(R.color.ct_color));
			btnGetMore.setTextColor(getResources().getColor(R.color.theme_color));
//			myRingsLines.setBackgroundColor(getResources().getColor(R.color.transparent));
//			getMoreLines.setBackgroundColor(getResources().getColor(R.color.color_8745ff));
			myRingsRel.setBackgroundColor(getResources().getColor(R.color.bg_color));
			getMoreRel.setBackgroundColor(getResources().getColor(R.color.transparent));
			FragmentT1.leaveThisPage();
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
		
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
		
	}
    
  
}
