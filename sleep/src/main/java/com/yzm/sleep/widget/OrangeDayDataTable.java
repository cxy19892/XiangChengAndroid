package com.yzm.sleep.widget;

import java.util.List;

import com.yzm.sleep.R;
import com.yzm.sleep.render.ItemBean;
import com.yzm.sleep.render.GetSleepResultValueClass.SleepData;
import com.yzm.sleep.utils.TimeFormatUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;

public class OrangeDayDataTable extends View{
	
	public OrangeDayDataTable(Context context) {
		super(context);
		init();
	}

	public OrangeDayDataTable(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public OrangeDayDataTable(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	/** 数据*/
	private List<SleepData> list;
	/** 数据最大值*/
	private int maxY;
	/** 开始时间， 结束时间*/
	private String startTime, endTime;
	private Paint paint;
	/** X轴坐标*/
	private int LineX;
	/** Y轴移动坐标*/
	private int moveY;
	/** 手指按下的Y轴坐标*/
	private int downY;
	
	private final int SHALLOW_BLUE_COLOR = 0xff48afea;
	private final int DEEP_BLUE_COLOR = 0xff4571ff;
	private final int PURPLE_COLOR = 0xff6f9ef6;
	private final int TEXT_COLOR = 0xff7d7d90;
	private float ts;
	
	public void setData(List<SleepData> list, String startTime, String endTime, int maxY){
		this.list = list;
		this.maxY = maxY;
		this.startTime = startTime;
		this.endTime = endTime;
		this.invalidate();
	}
	
	
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
		ts = (right - left )/30;
	}

	private void init(){
		paint = new Paint();
		setLayerType(View.LAYER_TYPE_HARDWARE, null);
	}
		
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
			if(list != null){
				for (int i = 0; i < list.size(); i++) {
					float per = 0.0f;
					switch (list.get(i).SleepType) {
					//（ 1深度 2浅度 3清醒）
					case 1:
						paint.setColor(PURPLE_COLOR);
						per = 0.5f;
						break;
					case 2:
						paint.setColor(DEEP_BLUE_COLOR);
						per = 0.3f;
						break;
					case 3:
						paint.setColor(SHALLOW_BLUE_COLOR);
						per = 0.0f;
						break;
					default:
						break;
					}
					float lineLength = getHeight() - 1.5f * ts;
					float lineHeight = lineLength * per;
					paint.setAntiAlias(true);
					paint.setStyle(Paint.Style.FILL);
					paint.setStrokeWidth(1);
					canvas.drawLine(
							i, 
							lineLength, 
							i, 
							lineHeight, 
							paint);
					paint.reset();	
				}
				paint.setColor(TEXT_COLOR);
				paint.setAntiAlias(true);
				paint.setTextSize(ts);
				canvas.drawBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.moon_icon)), 0.5f * ts, getHeight() - ts, paint);
				canvas.drawBitmap(drawableToBitmap(getResources().getDrawable(R.drawable.sun_icon)), getWidth() - 1.5f * ts, getHeight() - ts, paint);
				
				paint.setColor(TEXT_COLOR);
				canvas.drawText(startTime, 2.0f * ts, getHeight() - ts * 1/10, paint);
				canvas.drawText(endTime, getWidth() - 4.5f * ts, getHeight() - ts * 1/10, paint);
				
//				canvas.drawText(startTime, 1.5f * ts, getHeight(), paint);
//				canvas.drawText(endTime, getWidth() - 3 * ts, getHeight(), paint);
			}else{
				paint.setColor(SHALLOW_BLUE_COLOR);
				paint.setAntiAlias(true);
				paint.setTextSize(ts);
				float lineLength = getHeight() - 1.5f * ts;
				
				canvas.drawColor(0xffffffff);
				canvas.drawRect(0, 0, getWidth(), lineLength, paint);
				
				paint.setColor(TEXT_COLOR);
				canvas.drawText("--:--", 1.5f * ts, getHeight(), paint);
				canvas.drawText("--:--", getWidth() - 3 * ts, getHeight(), paint);
				
			}
			
	}
	
	
	/**
	 *  根据屏幕大小从资源获取相应的bitmap
	 * @param drawable
	 * @return
	 */
	private Bitmap drawableToBitmap(Drawable drawable) {  
        // 取 drawable 的颜色格式  
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888  
                : Bitmap.Config.RGB_565;  
        // 建立对应 bitmap  
        Bitmap bitmap = Bitmap.createBitmap((int)ts, (int)ts, config);  
        // 建立对应 bitmap 的画布  
        Canvas canvas = new Canvas(bitmap);  
        drawable.setBounds(0, 0, (int)ts, (int)ts);  
        // 把 drawable 内容画到画布中  
        drawable.draw(canvas);  
        return bitmap;  
    }  

}
