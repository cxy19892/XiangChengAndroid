package com.yzm.sleep.model;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yzm.sleep.R;
import com.yzm.sleep.timepicker.NumericWheelAdapter;
import com.yzm.sleep.timepicker.OnWheelChangedListener;
import com.yzm.sleep.timepicker.StringWheelAdapter;
import com.yzm.sleep.timepicker.WheelView;
import com.yzm.sleep.utils.CalenderUtil;
import com.yzm.sleep.utils.TimeFormatUtil;
import com.yzm.sleep.utils.Util;

public class RollPickerDialog extends AlertDialog implements android.view.View.OnClickListener, OnWheelChangedListener{
	private Context mContext;
	private RollCallBack mRollCallBack;
	private TextView tvTitle;
	private ImageButton cancleBtn, confirmBtn;
	private WheelView mWheelView1, mWheelView2, mWheelView3;
	private String title;
	private int Index1, Index2, Index3;
	private int[] data1, data2, data3;
	private ArrayList<String> strdata1, strdata2, strdata3;
	private int type = 1;
	private boolean isCyclic = true;

	protected RollPickerDialog(Context context, int theme) {
//		super(context, R.style.add_dialog);
		super(context,R.style.pop_dialog);
	}
	/**
	 * 滚动选择器<br>
	 * 使用方法：<br>
	 * 1 传入int型数组 使用SetData <br>
	 * 	a返回数据使用回调函数 setResaultRoll 取得<br>
	 * 	b注意：int[] data1 后面的 Index 的值：<br>
	 *   如果 int[] data1{0，xx} Index 选中值 == 传入值<br>
	 *   如果如果 int[] data1{aa，xx} Index 选中值 == 默认值-aa<br> 
	 * 2传入string列表 使用 SetStrDate<br>
	 *  a返回数据使用回调函数setResaultRollString 取得<br>
	 *  b传入的String列表参数不会补0 补零操作需要再传入之前自己完成<br>
	 *  c注意 Index = 选中项在列表中的位置 <br>
	 *  
	 * @param context
	 * @param callBack
	 */
	public RollPickerDialog(Context context, RollCallBack callBack){
//		super(context, R.style.add_dialog);
		super(context,R.style.pop_dialog);
		this.mContext = context;
		this.mRollCallBack = callBack;
	}
	/**
	 * 
	 * @param type [0 年月日 1 其他  传入int类型集合] [2 传入String 类型集合]
	 * @param title 标题
	 * @param data1 like this new int[]{1,100} 显示的数据的起点和终点
	 * @param Index1 选中项   即起点到终点之间的第几项
	 * @param data2  like data1
	 * @param Index2 like index1
	 * @param data3  like data1
	 * @param Index3 like index1
	 */
	public void SetData(int type, String title, int[] data1 , int Index1, int[] data2, int Index2, int[] data3, int Index3){
		this.type  = type;
		this.title = title;
		this.Index1 = Index1;
		this.Index2 = Index2;
		this.Index3 = Index3;
		this.data1  = data1;
		this.data2  = data2;
		this.data3  = data3;
	}
	/**
	 * 
	 * @param type [0 年月日 1 其他  传入int类型集合]
	 * @param title 标题
	 * @param data1 like this new int[]{1,100} 显示的数据的起点和终点
	 * @param Index1 选中项   即起点到终点之间的第几项
	 * @param data2
	 * @param Index2
	 * @param data3
	 * @param Index3
	 * @param iscycle 是否循环
	 */
	public void SetData(int type, String title, int[] data1 , int Index1, int[] data2, int Index2, int[] data3, int Index3, boolean iscycle){
		SetData(type, title, data1, Index1, data2, Index2, data3, Index3);
		this.isCyclic = iscycle;
	}
	/**
	 * [0 年月日 1 其他  传入int类型集合 SetData] [2 传入String 类型集合  SetStrDate]
	 * @param type
	 * @param title 标题
	 * @param data1
	 * @param Index1
	 * @param data2
	 * @param Index2
	 * @param data3
	 * @param Index3
	 */
	public void SetStrDate(int type, String title, ArrayList<String> data1 , int Index1, ArrayList<String> data2 , int Index2, ArrayList<String> data3 , int Index3){
		this.type  = type;
		this.title = title;
		this.strdata1 = data1;
		this.strdata2 = data2;
		this.strdata3 = data3;
		this.Index1 = Index1;
		this.Index2 = Index2;
		this.Index3 = Index3;
	}
	/**
	 * 
	 * @param type
	 * @param title
	 * @param data1
	 * @param Index1
	 * @param data2
	 * @param Index2
	 * @param data3
	 * @param Index3
	 * @param iscycle 是否循环
	 */
	public void SetStrDate(int type, String title, ArrayList<String> data1 , int Index1, ArrayList<String> data2 , int Index2, ArrayList<String> data3 , int Index3, boolean iscycle){
		SetStrDate(type, title, data1, Index1, data2, Index2, data3, Index3);
		this.isCyclic = iscycle;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_roll_picker);
		this.getWindow().setGravity(Gravity.BOTTOM | Gravity.FILL_HORIZONTAL);
			this.getWindow().setWindowAnimations(R.style.bottom_animation);
			this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		setCanceledOnTouchOutside(true);
		initViews();
	}

	private void initViews() {
		tvTitle = (TextView) findViewById(R.id.tv_roll_title);
		cancleBtn = (ImageButton) findViewById(R.id.roll_btn_cancle);
		confirmBtn = (ImageButton) findViewById(R.id.roll_btn_confirm);
		mWheelView1 = (WheelView) findViewById(R.id.roll_time_wheelview_1);
		mWheelView2 = (WheelView) findViewById(R.id.roll_time_wheelview_2);
		mWheelView3 = (WheelView) findViewById(R.id.roll_time_wheelview_3);
		mWheelView1.TEXT_SIZE = Util.Dp2Px(mContext, 18);
		mWheelView2.TEXT_SIZE = Util.Dp2Px(mContext, 18);
		mWheelView3.TEXT_SIZE = Util.Dp2Px(mContext, 18);
		tvTitle.setText(title);
		cancleBtn.setOnClickListener(this);
		confirmBtn.setOnClickListener(this);
		if(type == 2){
			if(strdata1 != null){
				mWheelView1.setVisibility(View.VISIBLE);
				mWheelView1.setAdapter(new StringWheelAdapter(strdata1));
				mWheelView1.setCyclic(isCyclic);
				mWheelView1.setCurrentItem(Index1);
				mWheelView1.post(new Runnable() {
					
					@Override
					public void run() {
						mWheelView1.postInvalidate();
					}
				});
			}
			if(strdata2 != null){
				mWheelView2.setVisibility(View.VISIBLE);
				mWheelView2.setAdapter(new StringWheelAdapter(strdata2));
				mWheelView2.setCyclic(isCyclic);
				mWheelView2.setCurrentItem(Index2);
			}
			if(strdata3 != null){
				mWheelView3.setVisibility(View.VISIBLE);
				mWheelView3.setAdapter(new StringWheelAdapter(strdata3));
				mWheelView3.setCyclic(isCyclic);
				mWheelView3.setCurrentItem(Index3);
			}
		}else{
			if(data1 != null){
				mWheelView1.setVisibility(View.VISIBLE);
				mWheelView1.setAdapter(new NumericWheelAdapter(data1[0], data1[1]));
				mWheelView1.setCyclic(isCyclic);
				mWheelView1.setCurrentItem(Index1);
				
			}
			if(data2 != null){
				mWheelView2.setVisibility(View.VISIBLE);
				mWheelView2.setAdapter(new NumericWheelAdapter(data2[0], data2[1]));
				mWheelView2.setCyclic(isCyclic);
				mWheelView2.setCurrentItem(Index2);
			}
			if(data3 != null){
				mWheelView3.setVisibility(View.VISIBLE);
				mWheelView3.setAdapter(new NumericWheelAdapter(data3[0], data3[1]));
				mWheelView3.setCyclic(isCyclic);
				mWheelView3.setCurrentItem(Index3);
			}
			if(type==0){
				mWheelView1.addChangingListener(this);
				mWheelView2.addChangingListener(this);
				mWheelView3.addChangingListener(this);
			}
		}
	}

	
	
	public interface RollCallBack{
		public void setResaultRoll(int select1, int select2, int select3);
		public void setResaultRollString(String select1, String select2, String select3);
	}



	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.roll_btn_cancle){
			dismiss();
		}
		
		if(v.getId()==R.id.roll_btn_confirm){
			if(mRollCallBack != null){
				if(type == 2){
					mRollCallBack.setResaultRollString((strdata1 == null)? "" : strdata1.get(mWheelView1.getCurrentItem()), 
							(strdata2 == null)? "" : strdata2.get(mWheelView2.getCurrentItem()), 
									(strdata3 == null)? "" : strdata3.get(mWheelView3.getCurrentItem()));
				}else{
					if(type==0){
						StringBuffer sb  = new StringBuffer();
						sb.append((data1 == null)? 0 : data1[0]+mWheelView1.getCurrentItem());
						sb.append((data2 == null)? 0 : (data2[0]+mWheelView2.getCurrentItem())<10?("0"+(data2[0]+mWheelView2.getCurrentItem())):data2[0]+mWheelView2.getCurrentItem());
						sb.append((data3 == null)? 0 : (data3[0]+mWheelView3.getCurrentItem())<10?("0"+(data3[0]+mWheelView3.getCurrentItem())):data3[0]+mWheelView3.getCurrentItem());
						try {
							if(!TimeFormatUtil.isGetTimebeforeNow(sb.toString())){
								Toast.makeText(mContext, "请选择正确的日期", Toast.LENGTH_SHORT).show();
								return;
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					mRollCallBack.setResaultRoll((data1 == null)? 0 : data1[0]+mWheelView1.getCurrentItem(), (data2 == null) ? 0 :data2[0]+mWheelView2.getCurrentItem(), (data3 == null) ? 0 : data3[0]+mWheelView3.getCurrentItem());
				}
			}
			dismiss();
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {

		if (wheel == mWheelView1 && mWheelView1 != null) {
			int days = CalenderUtil.getDaysByYearMonth(data1[0]+mWheelView1.getCurrentItem(),mWheelView2.getCurrentItem()+1);
			mWheelView3.setAdapter(new NumericWheelAdapter(1, days));
			if(mWheelView3.getCurrentItem()>(days-1)){
				mWheelView3.setCurrentItem(days-1);
			}
		}

		if (wheel == mWheelView2 && mWheelView2 != null) {
			int days = CalenderUtil.getDaysByYearMonth(data1[0]+mWheelView1.getCurrentItem(),mWheelView2.getCurrentItem()+1);
			mWheelView3.setAdapter(new NumericWheelAdapter(1, days));
			if(mWheelView3.getCurrentItem()>(days-1)){
				mWheelView3.setCurrentItem(days-1);
			}
		}
		
		if (wheel == mWheelView3 && mWheelView3 != null) {
		}
	
		
	}

}
