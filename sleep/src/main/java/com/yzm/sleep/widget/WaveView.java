package com.yzm.sleep.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
 import android.view.View;

public class WaveView extends View {

    private Paint wavePaint;
    private int waveColor = 0xef394790;
    private Path path;

    // 左右偏移 φ
    private int fai = 0;
    // 上下偏移
    private float k = -50;
    // 角速度
    private float w = 0.5f;
    // 振幅
    private int a = 20;

    private int height;
    private int width;
    private float targetHeight;
    private float textHeight;
    private int progress = 0;
    // 0% 时，空白的高度
    private float baseBlank;

    private OnFinishedListener listener;

    private int ms = 4;

    private boolean isRun = true;

    public WaveView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context) {
        super(context);
        init();
    }

    private void init() {
        wavePaint = new Paint();
        wavePaint.setAntiAlias(true);
        wavePaint.setColor(waveColor);

        path = new Path();

        new MyThread().start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setPath();
        canvas.drawPath(path, wavePaint);
    }

    private void setPath(){
        int x = 0;
        int y = 0;
        path.reset();
        for (int i = 0; i < width; i++) {
            x = i;
            y = (int) (a * Math.sin((i * w + fai) * Math.PI / 180) + k);
            if (i == 0) {
                path.moveTo(x, y);
            }
            path.quadTo(x, y, x + 1, y);
        }
        path.lineTo(width, height);
        path.lineTo(0, height);
        path.close();
    }

    /**
     *
     * @param p 0~1
     */
    public void updateProgress(float p) {
    	this.p = p;
        if(p >=0 && p <= 1){
            targetHeight = (float) (baseBlank * (1 - p));
        }
    }

    private float p;
    
    /**
    *
    * @param p 0~1
    */
   public float getProgress() {
       return p;
   }
   
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        initLayoutParams();
    }

    private void initLayoutParams(){
        height = this.getHeight();
        width = this.getWidth();
        baseBlank = (float) (height * 0.9);
        targetHeight = baseBlank;
        k = baseBlank;
        textHeight = baseBlank;
    }

    public WaveView setWaveColor(int color) {
        this.waveColor = color;
        wavePaint.setColor(waveColor);
        return this;
    }

    /**
     *
     * @param amplitude
     *            波浪振幅， 默认为 20
     */
    public WaveView setAmplitude(int a) {
        this.a = a;
        return this;
    }

    /**
     *
     * @param w
     *            默认为0.5
     */
    public WaveView setPalstance(float w) {
        this.w = w;
        return this;
    }

    /**
     *
     * @param ms
     *            默认为4毫秒
     */
    public WaveView setRefreshTime(int ms) {
        this.ms = ms;
        return this;
    }

    public void setOnFinishedListener(OnFinishedListener l) {
        this.listener = l;
    }

    class MyThread extends Thread {

        @Override
        public void run() {
            while (isRun) {
                fai++;
                if (k > targetHeight) {
                    k -= 0.5;
                    progress = (int) ((baseBlank - k) / baseBlank * 100);
                    if (textHeight > (height / 2)) {
                        textHeight -= 0.5;
                    }
                }
                if (progress >= 100 && listener != null) {
                    listener.onFinished();
                    isRun = false;
                }
                if (fai == 360) {
                    fai = 0;
                }
                mHandler.sendEmptyMessage(1);
                try {
                    Thread.sleep(ms);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what == 1){
                invalidate();
            }
        }
    };

    public interface OnFinishedListener {
        public void onFinished();
    }

}