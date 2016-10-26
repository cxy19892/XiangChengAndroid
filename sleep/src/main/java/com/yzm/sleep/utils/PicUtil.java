package com.yzm.sleep.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.yzm.sleep.crop.CropImageUI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class PicUtil {

	private static final String TAG = "PicUtil";

	/**
	 * 压缩图片
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
		int options = 100;
		// 循环判断如果压缩后图片是否大于200kb,大于继续压缩
		while (baos.toByteArray().length / 1024 > 200) {
			baos.reset();// 重置baos即清空baos
			options -= 10;// 每次都减少10
			// 这里压缩options%，把压缩后的数据存放到baos中
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);

		}
		// 把压缩后的数据baos存放到ByteArrayInputStream中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		// 把ByteArrayInputStream数据生成图片
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
		return bitmap;
	}

	/**
	 * 将Bitmap图像转换为byte数组
	 * 
	 * @param bm
	 * @return
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 根据一个网络连接(URL)获取bitmapDrawable图像
	 * 
	 * @param imageUri
	 * @return
	 */
	public static BitmapDrawable getBitmapDrawableByUri(URL imageUri) {

		BitmapDrawable icon = null;
		try {
			HttpURLConnection hp = (HttpURLConnection) imageUri
					.openConnection();
			icon = new BitmapDrawable(hp.getInputStream());// 将输入流转换成bitmap
			hp.disconnect();// 关闭连接
		} catch (Exception e) {
		}
		return icon;
	}
	public static Drawable bitmap2Drawable(Bitmap bitmap) {
		
		Drawable drawable = new BitmapDrawable(bitmap);   
		return drawable;
	}

	/**
	 * 根据一个网络连接(String)获取bitmapDrawable图像
	 * 
	 * @param imageUri
	 * @return
	 */
	public static BitmapDrawable getcontentPic(String imageUri) {
		URL imgUrl = null;
		try {
			imgUrl = new URL(imageUri);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		BitmapDrawable icon = null;
		try {
			HttpURLConnection hp = (HttpURLConnection) imgUrl.openConnection();
			icon = new BitmapDrawable(hp.getInputStream());// 将输入流转换成bitmap
			hp.disconnect();// 关闭连接
		} catch (Exception e) {
		}
		return icon;
	}

	/**
	 * 根据一个网络连接(URL)获取bitmap图像
	 * 
	 * @param imageUri
	 * @return
	 */
	public static Bitmap getusericon(URL imageUri) {
		// 显示网络上的图片
		URL myFileUrl = imageUri;
		Bitmap bitmap = null;
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	/**
	 * 根据一个网络连接(String)获取bitmap图像
	 * 
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmap(String imageUri) {
		// 显示网络上的图片
		Bitmap bitmap = null;
		try {
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();

			Log.i(TAG, "image download finished." + imageUri);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bitmap;
	}

	/**
	 * 下载图片 同时写道本地缓存文件中
	 * 
	 * @param context
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmapAndwrite(String imageUri) {
		Bitmap bitmap = null;
		try {
			// 显示网络上的图片
			URL myFileUrl = new URL(imageUri);
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();

			InputStream is = conn.getInputStream();
			File cacheFile = FileUtil.getCacheFile(imageUri);
			BufferedOutputStream bos = null;
			bos = new BufferedOutputStream(new FileOutputStream(cacheFile));
			Log.i(TAG, "write file to " + cacheFile.getCanonicalPath());

			byte[] buf = new byte[1024];
			int len = 0;
			// 将网络上的图片存储到本地
			while ((len = is.read(buf)) > 0) {
				bos.write(buf, 0, len);
			}

			is.close();
			bos.close();

			// 从本地加载图片
			bitmap = BitmapFactory.decodeFile(cacheFile.getCanonicalPath());
			// String name = MD5Util.MD5(imageUri);

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}

	
	/**
	 * 下载图片 本地缓存文件中有图片  图片就不下载
	 * 
	 * @param context
	 * @param imageUri
	 * @return
	 * @throws MalformedURLException
	 */
	public static Bitmap getbitmapByDefalut(String imageUri) {
		Bitmap bitmap = null;
		try {
		
			File cacheFile = FileUtil.getCacheFile(imageUri);
			if(cacheFile.exists())
			{				
				
				bitmap = BitmapFactory.decodeFile(cacheFile.getCanonicalPath());
								
			}else {
				
				// 显示网络上的图片
				URL myFileUrl = new URL(imageUri);
				HttpURLConnection conn = (HttpURLConnection) myFileUrl
						.openConnection();
				conn.setDoInput(true);
				conn.connect();

				InputStream is = conn.getInputStream();

				BufferedOutputStream bos = null;
				bos = new BufferedOutputStream(new FileOutputStream(cacheFile));
				Log.i(TAG, "write file to " + cacheFile.getCanonicalPath());

				byte[] buf = new byte[1024];
				int len = 0;
				// 将网络上的图片存储到本地
				while ((len = is.read(buf)) > 0) {
					bos.write(buf, 0, len);
				}

				is.close();
				bos.close();

				// 从本地加载图片
				bitmap = BitmapFactory.decodeFile(cacheFile.getCanonicalPath());
				// String name = MD5Util.MD5(imageUri);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	
	
	public static boolean downpic(String picName, Bitmap bitmap) {
		boolean nowbol = false;
		try {
			File saveFile = new File("/mnt/sdcard/download/weibopic/" + picName
					+ ".png");
			if (!saveFile.exists()) {
				saveFile.createNewFile();
			}
			FileOutputStream saveFileOutputStream;
			saveFileOutputStream = new FileOutputStream(saveFile);
			nowbol = bitmap.compress(Bitmap.CompressFormat.PNG, 100,
					saveFileOutputStream);
			saveFileOutputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowbol;
	}

	public static void writeTofiles(Context context, Bitmap bitmap,
			String filename) {
		BufferedOutputStream outputStream = null;
		try {
			outputStream = new BufferedOutputStream(context.openFileOutput(
					filename, Context.MODE_PRIVATE));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将文件写入缓存系统中
	 * 
	 * @param filename
	 * @param is
	 * @return
	 */
	public static String writefile(Context context, String filename,
			InputStream is) {
		BufferedInputStream inputStream = null;
		BufferedOutputStream outputStream = null;
		try {
			inputStream = new BufferedInputStream(is);
			outputStream = new BufferedOutputStream(context.openFileOutput(
					filename, Context.MODE_PRIVATE));
			byte[] buffer = new byte[1024];
			int length;
			while ((length = inputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, length);
			}
		} catch (Exception e) {
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return context.getFilesDir() + "/" + filename + ".jpg";
	}

	// 放大缩小图片
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	// 将Drawable转化为Bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	// 获得圆角图片的方法
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		if (bitmap == null) {
			return null;
		}

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));//取两层绘制交集。显示上层。
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	//
	public static Bitmap getBottom(Bitmap bitmap, int roundPx) {
		if (bitmap == null) {
			return null;
		}
		
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		
		final  Rect rect2 = new Rect(0, bitmap.getHeight() - roundPx , bitmap.getWidth(), bitmap.getHeight());
		canvas.drawRect(rect2, paint);
		
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));//取两层绘制交集。显示上层。
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}
	
	public static Bitmap getHalfCircleCornerBitmap(Bitmap bitmap ,int roundPx){
		if (bitmap == null) {
			return null;
		}
		
		Bitmap bitmap1 = getRoundedCornerBitmap(bitmap, roundPx);
		Bitmap bitmap2 = getBottom(bitmap, roundPx);
		
		Bitmap bitmap3 = Bitmap.createBitmap(bitmap1.getWidth(), bitmap1.getHeight(), bitmap1.getConfig());  
		Canvas canvas = new Canvas(bitmap3);  
		canvas.drawBitmap(bitmap1, new Matrix(), null);  
		canvas.drawBitmap(bitmap2, 0, bitmap.getHeight() - roundPx, null);  //写入点的x、y坐标  
		return bitmap3;
	}
	
	

	// 获得带倒影的图片方法
	public static Bitmap createReflectionImageWithOrigin(Bitmap bitmap) {
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2,
				width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(bitmap, 0, 0, null);
		Paint deafalutPaint = new Paint();
		canvas.drawRect(0, height, width, height + reflectionGap, deafalutPaint);

		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0, bitmap.getHeight(), 0,
				bitmapWithReflection.getHeight() + reflectionGap, 0x70ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// Draw a rectangle using the paint with our linear gradient
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);

		return bitmapWithReflection;
	}
	
	/**
	 * 得到小图
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(filePath);
		} catch (Exception e) {
		}
		if(fis == null)
			return null;
		Bitmap image = BitmapFactory.decodeStream(fis);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while ( baos.toByteArray().length / 1024 > 200) {   
        	baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        	if(options == 0)
        		break;
        	options -= 10;
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
        try { // 用完记得释放资源
        	baos.flush();
        	baos.close();
			isBm.close();
			fis.close();
			image.recycle();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bitmap;
	}
//	public static Bitmap getSmallBitmapNew(Context context,String filePath) {
//		final Options options = new BitmapFactory.Options();
//		options.inSampleSize = 2; //这个的值压缩的倍数（2的整数倍），数值越小，压缩率越小，图片越清晰
//		options.inJustDecodeBounds = true;
//		BitmapFactory.decodeFile(filePath, options);
//		options.inJustDecodeBounds = false;
//		
//		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
//		bitmap.compress(Bitmap.CompressFormat.JPEG, 20, new ByteArrayOutputStream());
//		
////		Bitmap compressImage = compressImage(bitmap);
////		if(bitmap != null && !bitmap.isRecycled()){
////			bitmap.recycle();
////			bitmap = null;
////		}
////		System.gc();
//		return bitmap;
//	}
	/**
	 * 得到bitmap
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmap(String filePath) {
		final Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		options.inJustDecodeBounds = false;
		
		Bitmap bm = BitmapFactory.decodeFile(filePath, options);
		if (bm == null) {
			return null;
		}
		bm.compress(Bitmap.CompressFormat.JPEG, 100, new ByteArrayOutputStream());
		
		return bm;
	}
	
	
	public static InputStream getBitmapInputStream(Bitmap bm){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		InputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		return isBm;
	}
	
	public static Bitmap regBitmap(Bitmap bitmap){
		BitmapFactory.Options options = new  BitmapFactory.Options();
		options.inSampleSize = 2 ; //图片宽高都为原来的二分之一，即图片为原来的四分之一 
		return bitmap = BitmapFactory.decodeStream(getBitmapInputStream(bitmap),null,options);
	}
	
	public static byte[] decodeBitmap(String path) {  
        BitmapFactory.Options opts = new BitmapFactory.Options();  
        opts.inJustDecodeBounds = true;// 设置成了true,不占用内存，只获取bitmap宽高  
        BitmapFactory.decodeFile(path, opts);  
        opts.inSampleSize = computeSampleSize(opts, -1, 1024 * 800);  
        opts.inJustDecodeBounds = false;// 这里一定要将其设置回false，因为之前我们将其设置成了true  
        opts.inPurgeable = true;  
        opts.inInputShareable = true;  
        opts.inDither = false;  
        opts.inPurgeable = true;  
        opts.inTempStorage = new byte[16 * 1024];  
        FileInputStream is = null;  
        Bitmap bmp = null;  
        ByteArrayOutputStream baos = null;  
        try {  
            is = new FileInputStream(path);  
            bmp = BitmapFactory.decodeFileDescriptor(is.getFD(), null, opts);  
            double scale = getScaling(opts.outWidth * opts.outHeight,  
                    1024 * 600);  
            Bitmap bmp2 = Bitmap.createScaledBitmap(bmp,  
                    (int) (opts.outWidth * scale),  
                    (int) (opts.outHeight * scale), true);  
            bmp.recycle();  
            baos = new ByteArrayOutputStream();  
            bmp2.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
            bmp2.recycle();  
            return baos.toByteArray();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                is.close();  
                baos.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            System.gc();  
        }  
        return baos.toByteArray();  
    }  
	
	private static double getScaling(int src, int des) {  
        /** 
         * 48 目标尺寸÷原尺寸 sqrt开方，得出宽高百分比 49 
         */  
        double scale = Math.sqrt((double) des / (double) src);  
        return scale;  
    }  
	
	public static int computeSampleSize(BitmapFactory.Options options,  
            int minSideLength, int maxNumOfPixels) {  
        int initialSize = computeInitialSampleSize(options, minSideLength,  
                maxNumOfPixels);  
  
        int roundedSize;  
        if (initialSize <= 8) {  
            roundedSize = 1;  
            while (roundedSize < initialSize) {  
                roundedSize <<= 1;  
            }  
        } else {  
            roundedSize = (initialSize + 7) / 8 * 8;  
        }  
  
        return roundedSize;  
    }  
	 private static int computeInitialSampleSize(BitmapFactory.Options options,  
	            int minSideLength, int maxNumOfPixels) {  
	        double w = options.outWidth;  
	        double h = options.outHeight;  
	  
	        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math  
	                .sqrt(w * h / maxNumOfPixels));  
	        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(  
	                Math.floor(w / minSideLength), Math.floor(h / minSideLength));  
	  
	        if (upperBound < lowerBound) {  
	            return lowerBound;  
	        }  
	  
	        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {  
	            return 1;  
	        } else if (minSideLength == -1) {  
	            return lowerBound;  
	        } else {  
	            return upperBound;  
	        }  
	    } 

}
