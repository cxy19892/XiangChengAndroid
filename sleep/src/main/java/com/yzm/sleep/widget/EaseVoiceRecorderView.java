package com.yzm.sleep.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.PowerManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yzm.sleep.R;
import com.yzm.sleep.model.EaseVoiceRecorder;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.Util;

/**
 * 按住说话录制控件
 *
 */
public class EaseVoiceRecorderView extends RelativeLayout {
    protected Context context;
    protected LayoutInflater inflater;
    protected Drawable[] micImages;
    protected EaseVoiceRecorder voiceRecorder;

    protected PowerManager.WakeLock wakeLock;
    protected ImageView micImage;
    protected TextView recordingHint;

    protected Handler micImageHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            // 切换msg切换图片
            micImage.setImageDrawable(micImages[msg.what]);
        }
    };

    public EaseVoiceRecorderView(Context context) {
        super(context);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EaseVoiceRecorderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ease_widget_voice_recorder, this);

        micImage = (ImageView) findViewById(R.id.mic_image);
        recordingHint = (TextView) findViewById(R.id.recording_hint);

        voiceRecorder = new EaseVoiceRecorder(micImageHandler);

        // 动画资源文件,用于录制语音时
        micImages = new Drawable[] { getResources().getDrawable(R.drawable.ease_record_animate_01),
                getResources().getDrawable(R.drawable.ease_record_animate_02),
                getResources().getDrawable(R.drawable.ease_record_animate_03),
                getResources().getDrawable(R.drawable.ease_record_animate_04),
                getResources().getDrawable(R.drawable.ease_record_animate_05)};

        wakeLock = ((PowerManager) context.getSystemService(Context.POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_DIM_WAKE_LOCK, "demo");
    }

    /**
     * 长按说话按钮touch事件
     * 
     * @param v
     * @param event
     */
    public boolean onPressToSpeakBtnTouch(View v, MotionEvent event, EaseVoiceRecorderCallback recorderCallback) {
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            try {
                if (EaseChatRowVoicePlayClickListener.isPlaying)
                    EaseChatRowVoicePlayClickListener.currentPlayListener.stopPlayVoice();
                v.setPressed(true);
                startRecording();
            } catch (Exception e) {
                v.setPressed(false);
            }
            return true;
        case MotionEvent.ACTION_MOVE:
            if (event.getY() < 0) {
                showReleaseToCancelHint();
            } else {
                showMoveUpToCancelHint();
            }
            return true;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
            v.setPressed(false);
            if (event.getY() < 0) {
                discardRecording();
            } else {
                try {
                    int length = stopRecoding();
                    if (length > 0 && length <= 60) {
                        if (recorderCallback != null) {
                            recorderCallback.onVoiceRecordComplete(getVoiceFilePath(), length);
                        }
                    } else if (length > 60) {
                    	 Util.show(context,"录音不得超过60秒");
                    } else {
                      Util.show(context,"录音太短");
                    }
                } catch (Exception e) {
                	 Util.show(context,"发送失败,检测是否连接");
                }
            }
            return true;
        default:
            discardRecording();
            return false;
        }
    }

    public interface EaseVoiceRecorderCallback {
        /**
         * 录音完毕
         * 
         * @param voiceFilePath
         *            录音完毕后的文件路径
         * @param voiceTimeLength
         *            录音时长
         */
        void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength);
    }

    public void startRecording() {
        if (!FileUtil.isExitsSdcard()) {
        	Util.show(context, "没有SD卡不支持录音");
            return;
        }
        try {
            wakeLock.acquire();
            this.setVisibility(View.VISIBLE);
            recordingHint.setText("手指上滑取消");
            voiceRecorder.startRecording(context);
        } catch (Exception e) {
            if (wakeLock.isHeld())
                wakeLock.release();
            if (voiceRecorder != null)
                voiceRecorder.discardRecording();
            this.setVisibility(View.INVISIBLE);
            	Util.show(context, "录音失败,再试一次");
            return;
        }
    }

    public void showReleaseToCancelHint() {
        recordingHint.setText("松开手指取消发送");
    }

    public void showMoveUpToCancelHint() {
        recordingHint.setText("手指上滑取消发送");
    }

    public void discardRecording() {
        if (wakeLock.isHeld())
            wakeLock.release();
        try {
            // 停止录音
            if (voiceRecorder.isRecording()) {
                voiceRecorder.discardRecording();
                this.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
        }
    }

    public int stopRecoding() {
        this.setVisibility(View.INVISIBLE);
        if (wakeLock.isHeld())
            wakeLock.release();
        return voiceRecorder.stopRecoding();
    }

    public String getVoiceFilePath() {
        return voiceRecorder.getVoiceFilePath();
    }

    public String getVoiceFileName() {
        return voiceRecorder.getVoiceFileName();
    }

    public boolean isRecording() {
        return voiceRecorder.isRecording();
    }

}
