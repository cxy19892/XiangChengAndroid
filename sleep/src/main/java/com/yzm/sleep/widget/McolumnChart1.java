package com.yzm.sleep.widget;

import java.math.BigDecimal;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.yzm.sleep.R;
import com.yzm.sleep.bean.PlanListBean;
import com.yzm.sleep.utils.TimeFormatUtil;

/**
 * Created by Administrator on 2016/3/7.
 */
public class McolumnChart1 extends View {
    //刻度间隔距离
    private int ITEM_DIVIDER = 20;
    //图形宽度
    private int ITEM_WIDTH;
    //字体大小
    private static final int TEXT_SIZE = 12;
    //屏幕参数
    private float mDensity;

    private int mWidth, mHeight;
    private PlanListBean mBean;
    //画笔
    private Paint ItembgPaint;//item背景的画笔
    private Paint betterPaint;//效率为优的画笔
    private Paint goodPaint;//效率为良的画笔
    private Paint poorPaint;//效率为差的画笔
    private Paint todayPaint;//今天的画笔
    private Paint afterDaysPaint;//今天之后的画笔
    private Paint WhitePaint;//白色
    private Paint mTrancePaint;//白色
    private Paint bgPaint;
    private TextPaint mtextPaint;
    private int COLOR_BG = 0xaf3c3c3c;
    private int COLOR_TODAY = 0xff6f9ef6;
    private int COLOR_AFTER = 0xFFC8C8C8;
    private int COLOR_BETTER = 0xffb2c46b;
    private int COLOR_POOR = 0xFFFF8037;
    private int COLOR_WHITE = 0x7fffffff;
    private int COLOR_DARK_BG = 0xff323232;
    private float fontHeight;
    private Resources mResources;
    private boolean islogin = false;


    public McolumnChart1(Context context) {
        super(context);
        mDensity = getContext().getResources().getDisplayMetrics().density;
        mResources = getContext().getResources();
        initPaints();
    }

    public McolumnChart1(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    private void initPaints(){
        ItembgPaint = new Paint();
        ItembgPaint.setColor(COLOR_BG);
        ItembgPaint.setStyle(Paint.Style.FILL);

        todayPaint = new Paint();
        todayPaint.setColor(COLOR_TODAY);
        todayPaint.setAntiAlias(true);
        todayPaint.setStyle(Paint.Style.FILL);
        todayPaint.setDither(true);

        afterDaysPaint = new Paint(todayPaint);
        afterDaysPaint.setColor(COLOR_AFTER);

        betterPaint = new Paint(todayPaint);
        betterPaint.setColor(COLOR_BETTER);

        WhitePaint = new Paint(ItembgPaint);
        WhitePaint.setColor(COLOR_WHITE);
        WhitePaint.setAntiAlias(true);
        WhitePaint.setDither(true);

        mTrancePaint = new Paint(ItembgPaint);
        mTrancePaint.setColor(Color.TRANSPARENT);
        
        bgPaint = new Paint(ItembgPaint);
        bgPaint.setColor(COLOR_DARK_BG);

        poorPaint = new Paint(todayPaint);
        poorPaint.setColor(COLOR_POOR);

        mtextPaint = new TextPaint();
        mtextPaint.setAntiAlias(true);
        // 防抖动
        mtextPaint.setDither(true);
        mtextPaint.setTextSize(TEXT_SIZE * mDensity);
        mtextPaint.setTextAlign(Paint.Align.CENTER);
        mtextPaint.setColor(Color.GREEN);

        Paint.FontMetrics mfontMetrics = mtextPaint.getFontMetrics();
        //计算文字高度
        fontHeight = mfontMetrics.bottom - mfontMetrics.top;
    }

    public void setValue(PlanListBean bean, boolean islogin){
        mBean = bean;
        this.islogin = islogin;
        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mWidth = getWidth();
        mHeight = getHeight();
        ITEM_DIVIDER = mWidth/5;
        ITEM_WIDTH = mWidth-ITEM_DIVIDER;
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
    	super.onDraw(canvas);
    	if(mBean == null){
    		return;
    	}
    	
    	canvas.drawRect(0, 0, mWidth, mHeight-20, bgPaint);
    	//画每一项的背景
    	canvas.drawRect(ITEM_DIVIDER / 2, 0, ITEM_WIDTH + ITEM_DIVIDER / 2, mHeight-20, ItembgPaint);
    	canvas.drawRect(0, mHeight-20, ITEM_WIDTH, mHeight, mTrancePaint);
    	//画前景色
    	mtextPaint.setColor(Color.WHITE);
    	if(mBean.getDateline().equals(TimeFormatUtil.getTodayTimeyyyyMMdd()) && islogin){//如果是今天
    		drawTodayItems(canvas, todayPaint, 0, mHeight - fontHeight * 2 - ITEM_WIDTH / 2 - 30, mBean.getXiaolv());
    		canvas.drawText("今日", ITEM_WIDTH / 2 + ITEM_DIVIDER/2, fontHeight, mtextPaint);
    		canvas.drawText("计划", ITEM_WIDTH / 2 + ITEM_DIVIDER / 2, 2 * fontHeight, mtextPaint);
    		mtextPaint.setColor(Color.WHITE);
    		canvas.drawText(TimeFormatUtil.ExchangeTimeformat(mBean.getDateline(), "yyyyMMdd", "MM/dd"),
                    ITEM_WIDTH / 2 + ITEM_DIVIDER / 2, mHeight - 10 - fontHeight / 2, mtextPaint);
    	}else{//如果不是今天   今天之前
    		if(TimeFormatUtil.isDateBiger(TimeFormatUtil.getTodayTimeyyyyMMdd(), mBean.getDateline(), "yyyyMMdd")){
    			if(mBean.getFlag() != 0){
    				if(mBean.getXiaolv() >= 0.8f){//优
    					drawItems(canvas, betterPaint, 0, mHeight - fontHeight * 2  - ITEM_WIDTH/2 - 30, mBean.getXiaolv());
    				}else{//差
    					drawItems(canvas, poorPaint, 0, mHeight - fontHeight * 2  - ITEM_WIDTH/2 - 30, mBean.getXiaolv());
    				}
    				String xvString = new BigDecimal(mBean.getXiaolv()*100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + "%";
            		float strlenth = Layout.getDesiredWidth(xvString, mtextPaint);
            		strlenth = strlenth > ITEM_WIDTH ? ITEM_WIDTH : strlenth;
            		WhitePaint.setStrokeWidth(1);
            		WhitePaint.setStyle(Paint.Style.STROKE);
            		RectF r2=new RectF();                           //RectF对象  
                    r2.left=ITEM_WIDTH / 2 + ITEM_DIVIDER/2 - strlenth/2 - 2* mDensity ;                                 //左边  
                    r2.top=10;                                 //上边  
                    r2.right=ITEM_WIDTH / 2 + ITEM_DIVIDER/2 + strlenth/2 + 2* mDensity;                                   //右边  
                    r2.bottom=10+fontHeight;
            		canvas.drawRoundRect(r2, fontHeight / 2, fontHeight / 2 , WhitePaint);
            		WhitePaint.setStyle(Paint.Style.FILL);
        			canvas.drawText(new BigDecimal(mBean.getXiaolv()*100).setScale(0, BigDecimal.ROUND_HALF_UP).intValue() + "%",
        					ITEM_WIDTH / 2 + ITEM_DIVIDER/2, fontHeight , mtextPaint);
    			}else{
    				drawItems(canvas, afterDaysPaint, 0, mHeight - fontHeight * 2  - ITEM_WIDTH/2 - 20, mBean.getXiaolv());
                    BitmapDrawable bmapbg = (BitmapDrawable) mResources.getDrawable(R.drawable.ic_sleep_zz_bg);
                    Rect dstbg = new Rect();
                    dstbg.left = ITEM_WIDTH/4 + ITEM_DIVIDER/2;
                    dstbg.top =  (int)(mHeight - fontHeight - ITEM_WIDTH/2 - 20);
                    dstbg.right = ITEM_WIDTH * 3 / 4 + ITEM_DIVIDER/2;
                    dstbg.bottom = (int)(mHeight - fontHeight - 20);
                    canvas.drawBitmap(bmapbg.getBitmap(), null, dstbg, afterDaysPaint);
    			}
    		}else{//今天之后
                    drawItems(canvas, afterDaysPaint, 0, mHeight - fontHeight * 2 - ITEM_WIDTH / 2 - 20, mBean.getXiaolv());
                    BitmapDrawable bmapbg = (BitmapDrawable) mResources.getDrawable(R.drawable.ic_lock_bg);
                    Rect dstbg = new Rect();
                    dstbg.left = ITEM_WIDTH / 4 + ITEM_DIVIDER / 2;
                    dstbg.top = (int) (mHeight - fontHeight - ITEM_WIDTH / 2 - 20);
                    dstbg.right = ITEM_WIDTH * 3 / 4 + ITEM_DIVIDER / 2;
                    dstbg.bottom = (int) (mHeight - fontHeight - 20);
                    canvas.drawBitmap(bmapbg.getBitmap(), null, dstbg, afterDaysPaint);
    		}
    		mtextPaint.setColor(Color.BLACK);
    		canvas.drawText(TimeFormatUtil.ExchangeTimeformat(mBean.getDateline(), "yyyyMMdd", "MM/dd"),
                    ITEM_WIDTH / 2 + ITEM_DIVIDER / 2, mHeight - 10 - fontHeight / 2, mtextPaint);

    		//画底部白线
    		WhitePaint.setColor(COLOR_WHITE);
    		WhitePaint.setStrokeWidth((int) (fontHeight));
    		canvas.drawLine(ITEM_DIVIDER/2 , (float) (mHeight-20 - (fontHeight/2)), ITEM_WIDTH + ITEM_DIVIDER/2, (float) (mHeight-20 - (fontHeight/2)), WhitePaint);
    	}
    	WhitePaint.setColor(Color.WHITE);
    	WhitePaint.setStrokeWidth((int) fontHeight);
    	canvas.drawLine(0, (float) (mHeight-20 - (fontHeight/2)), ITEM_DIVIDER/2, (float) (mHeight-20 - (fontHeight/2)), WhitePaint);
    	canvas.drawLine(ITEM_WIDTH + ITEM_DIVIDER/2, (float) (mHeight-20 - (fontHeight/2)), mWidth, (float) (mHeight-20 - (fontHeight/2)), WhitePaint);
    }


    private void drawItems(Canvas canvas, Paint mPaint, float x_length, float maxLength, float per){
        canvas.save();
        float lineHeight = mHeight - (float)fontHeight - maxLength * per;
        mPaint.setStrokeWidth(ITEM_WIDTH);
        //底部默认的文字高度
        canvas.drawLine(x_length + ITEM_DIVIDER/2 + ITEM_WIDTH/2, mHeight-20, x_length+ ITEM_DIVIDER/2 + ITEM_WIDTH/2, mHeight - 20 - (float)fontHeight, mPaint);
        //柱形图高度
        canvas.drawLine(x_length + ITEM_DIVIDER/2 + ITEM_WIDTH/2, mHeight-20 - (float)fontHeight, x_length + ITEM_DIVIDER/2 + ITEM_WIDTH/2, lineHeight - 20, mPaint);
        RectF oval = new RectF();
        oval.left = x_length + ITEM_DIVIDER/2;
        oval.right = x_length + ITEM_DIVIDER/2 + ITEM_WIDTH;
        oval.bottom = lineHeight - 20 + ITEM_WIDTH/2 + 2;//TEXT_SIZE * mDensity + 2 ;
        oval.top = lineHeight - 20 - ITEM_WIDTH/2;//TEXT_SIZE * mDensity;
        mPaint.setStrokeWidth(ITEM_WIDTH / 2);
        canvas.drawArc(oval, 180, 180, true, mPaint);
        canvas.restore();
    }

    
    
    private void drawTodayItems(Canvas canvas, Paint mPaint, float x_length, float maxLength, float per){
        canvas.save();
        float lineHeight = mHeight - (float)(fontHeight) - maxLength * per;
        mPaint.setStrokeWidth(ITEM_WIDTH);
        canvas.drawLine(x_length + ITEM_DIVIDER/2 + ITEM_WIDTH/2, mHeight-20, x_length + ITEM_DIVIDER/2 + ITEM_WIDTH/2, mHeight- 20 - (float)fontHeight, mPaint);
        canvas.drawLine(x_length + ITEM_DIVIDER/2 + ITEM_WIDTH/2, mHeight-20 - (float)(fontHeight), x_length + ITEM_DIVIDER/2 + ITEM_WIDTH/2, lineHeight, mPaint);
        RectF oval = new RectF();
        oval.left = x_length + ITEM_DIVIDER/2;
        oval.right = x_length + ITEM_DIVIDER/2 + ITEM_WIDTH;
        oval.bottom = lineHeight - 20 + ITEM_WIDTH/2 +2;//TEXT_SIZE * mDensity + 2 ;
        oval.top = lineHeight - 20 - ITEM_WIDTH/2;//TEXT_SIZE * mDensity;
        mPaint.setStrokeWidth(ITEM_WIDTH / 2);
        canvas.drawArc(oval, 180, 180, true, mPaint);
        
      //画橙色三角形指标标记
        int point1X = (int)(x_length + ITEM_DIVIDER/2 + ITEM_WIDTH / 2);
        int point2X = (int) x_length + ITEM_DIVIDER/2 + ITEM_WIDTH / 2 - 20;
        int point3X = (int) x_length + ITEM_DIVIDER/2 + ITEM_WIDTH / 2 + 20;
        int point1Y = (int) mHeight;
        int point2Y = (int) mHeight-20;
        int point3Y = (int) mHeight-20;
        Paint paint = new Paint();
        paint.setColor(COLOR_TODAY);
        paint.setStyle(Paint.Style.FILL);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        paint.setAntiAlias(true);
        Path path = new Path();
        path.moveTo(point1X, point1Y);
        path.lineTo(point2X, point2Y);
        path.lineTo(point3X, point3Y);
        path.lineTo(point1X, point1Y);
        path.close();
        canvas.drawPath(path, paint);
        canvas.restore();
        
        
    }
    
  

}
