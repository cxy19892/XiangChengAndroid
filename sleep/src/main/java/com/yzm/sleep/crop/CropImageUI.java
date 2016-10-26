package com.yzm.sleep.crop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.Base;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.R;
import com.yzm.sleep.activity.BaseActivity;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.ImageCompressUtil;
import com.yzm.sleep.utils.PicUtil;
import com.yzm.sleep.utils.ToastManager;

public class CropImageUI extends BaseActivity {

	private Drawable bitmap2Drawable;
	private int readPictureDegree;
	 // 读取uri所在的图片
	Bitmap bitmap = null;
	Bitmap rotateBitmap = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);	
		readPictureDegree = getIntent().getIntExtra("readPictureDegree", 0);
		String uriString = getIntent().getStringExtra("uri");
//		Uri data =Uri.parse(uriString);
		
		boolean isNewImg = getIntent().getBooleanExtra("isNew", false);
		
//		BitmapFactory.Options options = new  BitmapFactory.Options();   
//		options.inSampleSize = 2 ; //图片宽高都为原来的二分之一，即图片为原来的四分之一    

		/**
		 * 20150826 修改：原方法统一缩放4分之一对于太大的图片不适用
		 * =====================================================================================
		 */
//        ParcelFileDescriptor pfd = null;
//        try
//        {
//            pfd = this.getContentResolver().openFileDescriptor(data, "r");
//        } catch (IOException ex)
//        {
//        	Toast.makeText(CropImageUI.this, "读取图片失败", Toast.LENGTH_SHORT).show();
//        	this.finish();
//        }
//        if(null == pfd){
//        	Toast.makeText(CropImageUI.this, "读取图片失败", Toast.LENGTH_SHORT).show();
//        	this.finish();
//        }else{
//        
//        java.io.FileDescriptor fd = pfd.getFileDescriptor();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        //先指定原始大小   
//        options.inSampleSize = 1;
//        //只进行大小判断   
//        options.inJustDecodeBounds = true;
//        //调用此方法得到options得到图片的大小   
//        BitmapFactory.decodeFileDescriptor(fd, null, options);
//        //BitmapFactory.decodeStream(is, null, options);
//        //我们的目标是在480pixel的画面上显示。   
//        //所以需要调用computeSampleSize得到图片缩放的比例   
//        options.inSampleSize = ImageCompressUtil.computeSampleSize(options, 480, 800);
//        //OK,我们得到了缩放的比例，现在开始正式读入BitMap数据   
//        options.inJustDecodeBounds = false;
//        options.inDither = false;
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        //======================================================================================
////		String fileName = FileUtil.getRealFilePath(CropImageUI.this, data);
//        
//        try {
//			bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(data),null, options);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}
        
//		bitmap = dealPicture(uriString);
		if (isNewImg) {
			bitmap = getBitpMapNew(uriString);
			
		}else {
			bitmap = getBitpMapNew(uriString);//PicUtil.getSmallBitmapNew(CropImageUI.this,uriString);
		}
		
        if (bitmap != null) {
        	rotateBitmap = rotateBitmap(readPictureDegree, bitmap);
		}
		
		
		if (rotateBitmap != null) {
			bitmap2Drawable = PicUtil.bitmap2Drawable(rotateBitmap);
		}else{
			Toast.makeText(CropImageUI.this, "选择图片有误", Toast.LENGTH_SHORT).show();
			AppManager.getAppManager().finishActivity(CropImageUI.this);
		}
		
		if(null == bitmap2Drawable){
			Toast.makeText(CropImageUI.this, "选择图片有误", Toast.LENGTH_SHORT).show();
			AppManager.getAppManager().finishActivity(CropImageUI.this);
		}
		
		int index=getIntent().getIntExtra("index", 1);
		index = 1;
		if(index==1)
		{
			cropImage1(isNewImg);
		}
		else if(index==2)
		{
//			cropImage2();
		}
		else if(index==3)
		{
//			cropImage3();
		}
		else if(index==4)
		{
//			cropImage4();
		}
//        }
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(bitmap != null && !bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
			System.gc();
		}
	}
	/** 
	 * 将图片纠正到正确方向 
	 *  
	 * @param degree ： 图片被系统旋转的角度 
	 * @param bitmap ： 需纠正方向的图片 
	 * @return 纠向后的图片 
	 */  
	public Bitmap rotateBitmap(int degree, Bitmap bitmap) {  
	    Matrix matrix = new Matrix();  
	    matrix.postRotate(degree);  
	  
	    Bitmap bm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),  
	            bitmap.getHeight(), matrix, true);  
	    return bm;  
	}  
	
	/**
	 * 旋转图片，使图片保持正确的方向。
	 * @param bitmap 原始图片
	 * @param degrees 原始图片的角度
	 * @return Bitmap 旋转后的图片
	 */
//	public Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
//		if (degrees == 0 || null == bitmap) {
//			return bitmap;
//		}
//		Matrix matrix = new Matrix();
//		matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
//		Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//		if (null != bitmap) {
//			bitmap.recycle();
//		}
//		return bmp;
//	}
	
	private void cropImage1(boolean isNew)
	{
		setContentView(R.layout.fragment_cropimage);
		final CropImageView mCropImage=(CropImageView)findViewById(R.id.cropImg);
		
		if(isNew)
			mCropImage.setDrawable(bitmap2Drawable,400,300);
		else
			mCropImage.setDrawable(bitmap2Drawable,300,300);
//		mCropImage.setDrawable(getResources().getDrawable(R.drawable.precrop),300,300);
		
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable(){

					@Override
					public void run() {
//						FileUtil.writeImage(mCropImage.getCropImage(), FileUtil.SDCARD_PAHT+"/crop.png", 100);
//						
//						Intent mIntent=new Intent();
//						mIntent.putExtra("cropImagePath", FileUtil.SDCARD_PAHT+"/crop.png");
//						setResult(RESULT_OK, mIntent);
//						finish();
						Bitmap cropImage = mCropImage.getCropImage();
						if(null == cropImage){
							ToastManager.getInstance(CropImageUI.this).show("图片选择出错");
							AppManager.getAppManager().finishActivity(CropImageUI.this);
						}else{
	//						String cropPath = FileUtil.SDCARD_PAHT+"/crop.png";
							 String cropPath = FileUtil.getSDPath() +"/crop.jpg";
							FileUtil.writeImage(cropImage, cropPath, 100);
							Intent mIntent=new Intent();
							mIntent.putExtra("cropImagePath", cropPath);
							setResult(RESULT_OK, mIntent);
							
							//add 2015-06-05(2)
							if(bitmap!=null){
								bitmap.recycle();
							}
							if(rotateBitmap!=null){
								rotateBitmap.recycle();
							}
							if(cropImage != null  && !cropImage.isRecycled()){
								cropImage.recycle();
							}
//							if(compassBitmap!=null && !compassBitmap.isRecycled()){
//								compassBitmap.recycle();
//							}
							AppManager.getAppManager().finishActivity(CropImageUI.this);
						}
					}
				}).start();
			}
		});
	}
	
//	private void cropImage2()
//	{
//		setContentView(R.layout.fragment_cropimage2);
//		final CropImageView2 mCropImage=(CropImageView2)findViewById(R.id.cropImg);
//		mCropImage.setDrawable(getResources().getDrawable(R.drawable.precrop),300,300);
//		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				new Thread(new Runnable(){
//
//					@Override
//					public void run() {
//						FileUtil.writeImage(mCropImage.getCropImage(), FileUtil.SDCARD_PAHT+"/crop.png", 100);
//						Intent mIntent=new Intent();
//						mIntent.putExtra("cropImagePath", FileUtil.SDCARD_PAHT+"/crop.png");
//						setResult(RESULT_OK, mIntent);
//						finish();
//					}
//				}).start();
//			}
//		});
//	}
	
//	private void cropImage3()
//	{
//		setContentView(R.layout.fragment_cropimage3);
//		final CropImageView3 mCropImage=(CropImageView3)findViewById(R.id.cropImg);
//		mCropImage.setDrawable(getResources().getDrawable(R.drawable.precrop),300,300);
//		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				new Thread(new Runnable(){
//
//					@Override
//					public void run() {
//						FileUtil.writeImage(mCropImage.getCropImage(), FileUtil.SDCARD_PAHT+"/crop.png", 100);
//						Intent mIntent=new Intent();
//						mIntent.putExtra("cropImagePath", FileUtil.SDCARD_PAHT+"/crop.png");
//						setResult(RESULT_OK, mIntent);
//						finish();
//					}
//				}).start();
//			}
//		});
//	}
	
	@SuppressWarnings("deprecation")
	private void cropImage4()
	{
		setContentView(R.layout.fragment_cropimage4);
		final CropImageView4 mCropImage=(CropImageView4)findViewById(R.id.cropImg);
		mCropImage.setDrawable(bitmap2Drawable,300,300);
//		mCropImage.setBackgroundDrawable(bitmap2Drawable);
		findViewById(R.id.save).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new Thread(new Runnable(){

					@Override
					public void run() {
						Bitmap cropImage = mCropImage.getCropImage();
//						String cropPath = FileUtil.SDCARD_PAHT+"/crop.png";
						String cropPath = FileUtil.getSDPath() +"/crop.jpg";
						
						FileUtil.writeImage(cropImage, cropPath, 100);
						Intent mIntent=new Intent();
						mIntent.putExtra("cropImagePath", cropPath);
						setResult(RESULT_OK, mIntent);
						AppManager.getAppManager().finishActivity(CropImageUI.this);
					}
				}).start();
			}
		});
	}
	
	
	/**
	 * 压缩处理
	 * @param uriString
	 * @return
	 */
	private Bitmap dealPicture(String uriString){
		Bitmap bitmap = null;
		Bitmap rotateBitmap = null;
		bitmap = getBitpMapNew(uriString);

		 if (bitmap != null) {
	        	rotateBitmap = rotateBitmap(readPictureDegree, bitmap);
			}
		if (rotateBitmap != null) {
			if(null == PicUtil.bitmap2Drawable(rotateBitmap)){
				Toast.makeText(CropImageUI.this, "选择图片有误", Toast.LENGTH_SHORT).show();
				return null;
			}
			return ImageCompressUtil.comp(rotateBitmap);
		}
		return null;
	}

	public Bitmap getBitpMapNew(String uriString)
    {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(uriString);
		
		 BitmapFactory.Options newOpts = new BitmapFactory.Options();  
         newOpts.inJustDecodeBounds = true;//只读边,不读内容  
         Bitmap bitmap = BitmapFactory.decodeFile(uriString, newOpts);
		
         newOpts.inJustDecodeBounds = false;  
         int w = newOpts.outWidth;  
         int h = newOpts.outHeight;  
         float hh = 800;//  
         float ww = 480;//  
         int be = 1;  
         if (w > h && w > ww) {  
             be = (int) (newOpts.outWidth / ww);  
         } else if (w < h && h > hh) {  
             be = (int) (newOpts.outHeight / hh);  
         }  
         if (be <= 0)  
             be = 1;  
         newOpts.inSampleSize = be;//设置采样率  
           
         newOpts.inPreferredConfig = Config.ARGB_8888;//该模式是默认的,可不设  
         newOpts.inPurgeable = true;// 同时设置才会有效  
         newOpts.inInputShareable = true;//。当系统内存不够时候图片自动被回收  
         Bitmap mgetBitmap = BitmapFactory.decodeStream(fis, null, newOpts);
         bitmap = ImageCompressUtil.compressImage(mgetBitmap);//decodeFile(uriString, newOpts);  
//       return compressBmpFromBmp(bitmap);//原来的方法调用了这个方法企图进行二次压缩  
                                     //其实是无效的,大家尽管尝试  
         if(mgetBitmap!=null){
        	 mgetBitmap.recycle();
        	 mgetBitmap = null;
         }
         return bitmap;  
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}finally{
			try {
				fis.close();
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		return null;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			AppManager.getAppManager().finishActivity(CropImageUI.this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
}
