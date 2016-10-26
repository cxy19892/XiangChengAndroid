package com.yzm.sleep.fragment;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.yzm.sleep.R;
import com.yzm.sleep.bean.CheckBean;
import com.yzm.sleep.bean.FankuiDataBean;
import com.yzm.sleep.utils.InterfaceMallUtillClass.InterfaceSupplyFeelRecordCallBack;
import com.yzm.sleep.utils.LogUtil;
import com.yzm.sleep.utils.PreManager;
import com.yzm.sleep.utils.ProgressUtils;
import com.yzm.sleep.utils.Util;
import com.yzm.sleep.utils.XiangchengMallProcClass;
import com.yzm.sleep.widget.LazyFragment;
import com.yzm.sleep.widget.TagStreamLayout;

public class FragmentPageRecordFeel extends LazyFragment implements OnClickListener{

	private String tag;
	private boolean isInitView = false;
	private MarginLayoutParams lp;
	private Activity activity;
	private TextView rlTitle;
	private TagStreamLayout tagItem;
	private FankuiDataBean datas;
	private View rlType1, rlType2 ,rlType3;
	private String[] answers= {"A","B","C","D","E","F","G"};
	private InterSubmit mInterSubmit;
	private TextView btn1, btn2, btn3, btn4, btn5;
	private EditText edtWeight;

	public void setMyTag(String tag){
		this.tag = tag;
	}

	public String getMyTag(){
		return tag;
	}

	public FankuiDataBean getData(){
		return datas;
	}

	/**
	 * 提交回调接口
	 * @author Administrator
	 *
	 */
	public interface InterSubmit{
		void submitFinish(String key, FankuiDataBean datas,String ams);
	}

	private ProgressUtils pro;
	/**
	 * 显示进度
	 */
	private void showPro() {
		if (pro == null) {
			pro = new ProgressUtils(activity);
		}
		pro.show();
	}

	/**
	 * 取消进度
	 */
	private void cancelPro() {
		if (pro != null) {
			pro.dismiss();
			pro = null;
		}
	}

	public void setMInterSubmit(InterSubmit mInterSubmit){
		this.mInterSubmit = mInterSubmit;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		datas = (FankuiDataBean) getArguments().getSerializable("data");
		lp= new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		lp.rightMargin = Util.Dp2Px(activity, 10);
		lp.bottomMargin = Util.Dp2Px(activity, 10);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_recordfeel, container, false);
	}

	@SuppressLint("NewApi")
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		rlType1 = view.findViewById(R.id.rl_type1);
		rlType2 = view.findViewById(R.id.rl_type2);
		rlType3 = view.findViewById(R.id.rl_jf);
		btn1 = (TextView) view.findViewById(R.id.btn_1);
		btn1.setOnClickListener(this);
		btn2 = (TextView) view.findViewById(R.id.btn_2);
		btn2.setOnClickListener(this);
		btn3 = (TextView) view.findViewById(R.id.btn_3);
		btn3.setOnClickListener(this);
		btn4 = (TextView) view.findViewById(R.id.btn_4);
		btn4.setOnClickListener(this);
		btn5 = (TextView) view.findViewById(R.id.btn_5);
		btn5.setOnClickListener(this);
		tagItem = (TagStreamLayout) view.findViewById(R.id.tag_item);
		rlTitle = (TextView) view.findViewById(R.id.rl_title);
		edtWeight = (EditText) view.findViewById(R.id.edt_weight);

		isInitView = true;
		lazyLoad();

	}


	@Override
	protected void lazyLoad() {
		if (!isInitView)
			return;
		if("feeling".equals(tag)){
			rlType2.setVisibility(View.GONE);
			rlType3.setVisibility(View.GONE);
			rlType1.setVisibility(View.VISIBLE);

			switch (getChoiceIndex(datas.getCheck())) {
				case 0:
					btn1.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn1.setText("很差");
					break;
				case 1:
					btn2.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn2.setText("差");
					break;
				case 2:
					btn3.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn3.setText("一般");
					break;
				case 3:
					btn4.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn4.setText("好");
					break;
				case 4:
					btn5.setBackgroundResource(R.drawable.custom_bg2_ff);
					btn5.setText("很好");
					break;
			}
		}else{
			if("weight".equals(tag)){
				rlType2.setVisibility(View.GONE);
				rlType3.setVisibility(View.VISIBLE);
				rlType1.setVisibility(View.GONE);
			}else{
				rlType2.setVisibility(View.VISIBLE);
				rlType3.setVisibility(View.GONE);
				rlType1.setVisibility(View.GONE);
				rlTitle.setText(datas.getTitle());
				tagItem.removeAllViews();
				for(int i =0; i< datas.getCheck().size(); i++){
					View itemView =LayoutInflater.from(activity).inflate(R.layout.item_habit_layout, null);
					itemView.setTag(i);
					TextView texteView = (TextView)itemView.findViewById(R.id.radiobtn);
					texteView.setText(datas.getCheck().get(i).getChoice_title());
					texteView.setTextColor(getResources().getColor(R.color.ct_color));
					if("1".equals(datas.getCheck().get(i).getChoice_flag())){
						texteView.setTextColor(getResources().getColor(R.color.cbg_color));
						texteView.setBackgroundResource(R.drawable.custom_round_select);
					}else{
						texteView.setTextColor(getResources().getColor(R.color.ct_color));
						texteView.setBackgroundResource(R.drawable.custom_round_normal);
					}
					itemView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							int index = Integer.parseInt(v.getTag().toString());
							if("customers".equals(tag)){
								if("1".equals(datas.getCheck().get(index).getChoice_flag())){
									datas.getCheck().get(index).setChoice_flag("0");
									TextView texteView = (TextView) tagItem.getChildAt(index).findViewById(R.id.radiobtn);
									texteView.setTextColor(getResources().getColor(R.color.ct_color));
									texteView.setBackgroundResource(R.drawable.custom_round_normal);
								}else{
									datas.getCheck().get(index).setChoice_flag("1");
									TextView texteView = (TextView) tagItem.getChildAt(index).findViewById(R.id.radiobtn);
									texteView.setTextColor(getResources().getColor(R.color.cbg_color));
									texteView.setBackgroundResource(R.drawable.custom_round_select);
								}
							}else{
								for (int j = 0; j < datas.getCheck().size(); j++) {
									datas.getCheck().get(j).setChoice_flag("0");
								}
								datas.getCheck().get(index).setChoice_flag("1");

								for (int k = 0; k < datas.getCheck().size(); k++) {
									TextView texteView = (TextView) tagItem.getChildAt(k).findViewById(R.id.radiobtn);
									texteView.setTextColor(getResources().getColor(R.color.ct_color));
									texteView.setBackgroundResource(R.drawable.custom_round_normal);
								}

								TextView texteView = (TextView) tagItem.getChildAt(index).findViewById(R.id.radiobtn);
								texteView.setTextColor(getResources().getColor(R.color.cbg_color));
								texteView.setBackgroundResource(R.drawable.custom_round_select);
							}
						}
					});
					tagItem.addView(itemView, lp);
				}
			}
		}
	}

	private String valur;
	public void setValur(String valur){
		this.valur =valur;
	}

	public void submit1(){
		if("weight".equals(tag)){
			if(TextUtils.isEmpty(edtWeight.getText().toString())){
				Util.show(activity, "减了多少");
				return;
			}
			try {
				double tem1 = Double.parseDouble(edtWeight.getText().toString());
				if (tem1 > 200) {
					Util.show(activity, "超过了我们地记录了");
					return;
				}
				if(tem1 <= 0){
					Util.show(activity, "体重必须大于0");
				}
				submit(edtWeight.getText().toString());
			} catch (Exception e) {
				Util.show(activity, "减了多少");
				return;
			}

		}else{
			StringBuffer strs = new StringBuffer();
			for (int i = 0; i < datas.getCheck().size(); i++) {
				if("1".equals(datas.getCheck().get(i).getChoice_flag()))
					strs.append(answers[i]).append(",");
			}
			String an = strs.toString();
			if("customers".equals(tag))
				submit(an.length() < 1 ? "无" : an.substring(0, an.length() - 1));
			else{
				if(an.length() > 0)
					submit(an.substring(0, an.length() - 1));
				else{
					Util.show(activity, "请选择");
				}
			}
		}
	}

	public void submit(final String answers){
		showPro();
		RequestParams params = new RequestParams();
		params.put("my_int_id", PreManager.instance().getUserId(activity));
		params.put("type", "1");
		params.put(tag, answers);
		if("smoke".equals(tag) && "A".equals(answers)){
			params.put("issmoke", "");
		}
		if("issmoke".equals(tag)){
			params.put("smoke", valur);
		}

		LogUtil.i("tx", params.toString());

		new XiangchengMallProcClass(activity).supplyFeelDataRecord(params, new InterfaceSupplyFeelRecordCallBack() {

			@Override
			public void onSuccess(String icode, String strMsg) {
				cancelPro();
				if(mInterSubmit != null)
					mInterSubmit.submitFinish(tag, datas, answers);
			}

			@Override
			public void onError(String icode, String strErrMsg) {
				Util.show(activity, strErrMsg);
				cancelPro();
				if(mInterSubmit != null)
					mInterSubmit.submitFinish(tag, datas, answers);
			}
		});
	}

	@Override
	public void onClick(View v) {
		btn1.setBackgroundResource(R.drawable.feel_1);
		btn1.setText("");
		btn2.setBackgroundResource(R.drawable.feel_2);
		btn2.setText("");
		btn3.setBackgroundResource(R.drawable.feel_3);
		btn3.setText("");
		btn4.setBackgroundResource(R.drawable.feel_4);
		btn4.setText("");
		btn5.setBackgroundResource(R.drawable.feel_5);
		btn5.setText("");
		for (int j = 0; j < datas.getCheck().size(); j++) {
			datas.getCheck().get(j).setChoice_flag("0");
		}

		switch (v.getId()) {
			case R.id.btn_1:
				datas.getCheck().get(0).setChoice_flag("1");
				btn1.setBackgroundResource(R.drawable.custom_bg2_ff);
				btn1.setText("很差");
				break;
			case R.id.btn_2:
				datas.getCheck().get(1).setChoice_flag("1");
				btn2.setBackgroundResource(R.drawable.custom_bg2_ff);
				btn2.setText("差");
				break;
			case R.id.btn_3:
				datas.getCheck().get(2).setChoice_flag("1");
				btn3.setBackgroundResource(R.drawable.custom_bg2_ff);
				btn3.setText("一般");
				break;
			case R.id.btn_4:
				datas.getCheck().get(3).setChoice_flag("1");
				btn4.setBackgroundResource(R.drawable.custom_bg2_ff);
				btn4.setText("好");
				break;
			case R.id.btn_5:
				datas.getCheck().get(4).setChoice_flag("1");
				btn5.setBackgroundResource(R.drawable.custom_bg2_ff);
				btn5.setText("很好");
				break;
		}
	}

	private int getChoiceIndex(List<CheckBean> datas){
		for (int i = 0; i < datas.size(); i++) {
			if("1".equals(datas.get(i).getChoice_flag()))
				return i;
		}
		return -1;
	}
}
