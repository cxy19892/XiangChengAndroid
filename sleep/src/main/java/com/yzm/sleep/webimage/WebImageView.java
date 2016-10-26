package com.yzm.sleep.webimage;

import java.io.File;
import java.io.FileOutputStream;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.yzm.sleep.R;

/**
 * 自定义图片类
 * 
 * @author zhaomeng
 * 
 */
public class WebImageView extends ImageView {

	private final static String LOG_TAG = WebImageView.class.getName();

	private Handler handler = new Handler();

	public WebImageView(Context context) {
		super(context);
	}

	public WebImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public WebImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 加载本地图片
	 * 
	 * @param cacheFile
	 *            本地图片
	 */
	public void setImageUrl(final File cacheFile) {
		if (cacheFile.exists()) {
			Drawable drawable = Drawable.createFromPath(cacheFile
					.getAbsolutePath());
			if (drawable != null) {
				WebImageView.this.setImageDrawable(drawable);
			} else {
//				WebImageView.this.setImageResource(R.drawable.user_icon_default);
			}
			drawable = null;
		} else {
//			WebImageView.this.setImageResource(R.drawable.user_icon_default);
		}
	}

	/**
	 * 加载网络图片
	 * 
	 * @param url
	 *            图片地址
	 * @param cacheFile
	 *            图片保存文件
	 */
	public void setImageUrl(final String url, final File cacheFile) {
		if (cacheFile.exists()) {
			setImageUrl(cacheFile);
		} else {
			loadImage(url, cacheFile);
		}
	}

	/**
	 * 加载网络图片
	 * 
	 * @param url
	 * @param cacheFile
	 */
	private void loadImage(final String url, final File cacheFile) {

		ThreadPoolFactory.getInstance().execute(new Runnable() {
			public void run() {
				try {
					IOUtils.copyUrl(url, new FileOutputStream(cacheFile));
					handler.post(new Runnable() {
						public void run() {
							Drawable drawable = null;
							try {

								drawable = Drawable.createFromPath(cacheFile
										.getAbsolutePath());
							} catch (OutOfMemoryError e) {
								e.printStackTrace();
							}
							if (drawable != null) {
								WebImageView.this.setImageDrawable(drawable);
							}
							drawable = null;
						}
					});
				} catch (Exception e) {
					Log.e(LOG_TAG, "loading image error", e);
				}

			}
		});
	}
}
