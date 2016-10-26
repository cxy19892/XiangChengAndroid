package com.yzm.sleep.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Environment;

public class ImageCompressUtil {
	
	/**
	 * 对图片质量进行压缩
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		 
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
			image.recycle();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return bitmap;
    }
	
	/**
	 * 对图片质量进行压缩
	 * @param image
	 * @return
	 */
	public static byte[] compressImageTobyte(Bitmap image) {
		 
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
        byte[] arr = baos.toByteArray();
        
        try {
        	baos.flush();
        	baos.close();
        	image.recycle();
        	System.gc();
		} catch (Exception e) {
		}
        
        return arr;
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
//        try { // 用完记得释放资源
//        	baos.close();
//			isBm.close();
//			image.recycle();
//			System.gc();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        return bitmap;
    }
	
	/**
	 * 对图片质量进行压缩分享使用将图片压缩到32k以下
	 * @param image
	 * @return
	 */
	private static byte[] compressImage32k(Bitmap image) {
		 
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int options = 100;
        while ( baos.toByteArray().length / 1024 > 30) {   
        	baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        	if(options == 0)
        		break;
        	options -= 10;
        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        byte[] arr = baos.toByteArray();
        try { // 用完记得释放资源
        	baos.flush();
        	baos.close();
			image.recycle();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
        return arr;
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);
//        try { // 用完记得释放资源
//        	baos.close();
//			isBm.close();
//			image.recycle();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//        System.out.println("sharepic-->"+getBitmapsize(bitmap));
//        if(getBitmapsize(bitmap) > 32*1000)
//        	return createBitmapThumbnail(bitmap);
//        else
//        	return bitmap;
    }
	
	public static Bitmap createBitmapThumbnail(Bitmap bitMap) {
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = 58;
        int newHeight = 58;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newBitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height,
                matrix, true);
        try {
        	bitMap.recycle();
        	System.gc();
		} catch (Exception e) {
		}
        return newBitMap;
    }
	
	//
	@SuppressLint("NewApi") 
	private static long getBitmapsize(Bitmap bitmap ){

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		}
		// Pre HC-MR1
		return bitmap.getRowBytes() * bitmap.getHeight();

	}

	/**
	 * 先按比例压缩，再按质量压缩
	 * @param image
	 * @return
	 */
	public static Bitmap comp(Bitmap image) {  
		if(null == image){
			return null;
		}
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();         
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos); 
	    if( baos.toByteArray().length / 1024>1024) {
	        baos.reset();  
	        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
	    }  
	    ByteArrayInputStream isBm = null;
		try {
			isBm = new ByteArrayInputStream(baos.toByteArray());
		} catch (OutOfMemoryError e1) {
			e1.printStackTrace();
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			image.recycle();
		}  
	    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	    newOpts.inJustDecodeBounds = true;  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	    newOpts.inJustDecodeBounds = false;  
	    int w = newOpts.outWidth;  
	    int h = newOpts.outHeight;  
	    float hh = 480f; 
	    float ww = 480f;
	    int be = 1;
	    if (w > h && w > ww) {
	        be = (int) (newOpts.outWidth / ww);  
	    } else if (w < h && h > hh) {
	        be = (int) (newOpts.outHeight / hh);  
	    }  
	    if (be <= 0)  
	        be = 1;  
	    newOpts.inSampleSize = be;
	    
	    isBm = new ByteArrayInputStream(baos.toByteArray());  
	    bitmap = compressImage(BitmapFactory.decodeStream(isBm, null, newOpts));  
	    try { 
	    	baos.flush();
        	baos.close();
			isBm.close();
			image.recycle();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}catch (OutOfMemoryError e) {
			return null;
		}
	    return bitmap;
	}  
	
	
	/**
	 * 先按比例压缩，再按质量压缩
	 * @param image
	 * @return
	 */
	public static byte[] compToByte(Bitmap image) {  
		if(null == image){
			return null;
		}
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();         
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos); 
	    if( baos.toByteArray().length / 1024>1024) {
	        baos.reset();  
	        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
	    }  
	    ByteArrayInputStream isBm = null;
		try {
			isBm = new ByteArrayInputStream(baos.toByteArray());
		} catch (OutOfMemoryError e1) {
			e1.printStackTrace();
			try {
				baos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			image.recycle();
			return null;
		}  
	    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	    newOpts.inJustDecodeBounds = true;  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	    newOpts.inJustDecodeBounds = false;  
	    int w = newOpts.outWidth;  
	    int h = newOpts.outHeight;  
	    float hh = 480f; 
	    float ww = 480f;
	    int be = 1;
	    if (w > h && w > ww) {
	        be = (int) (newOpts.outWidth / ww);  
	    } else if (w < h && h > hh) {
	        be = (int) (newOpts.outHeight / hh);  
	    }  
	    if (be <= 0)  
	        be = 1;  
	    newOpts.inSampleSize = be;
	    
	    isBm = new ByteArrayInputStream(baos.toByteArray());  
	    try {
	    	if(bitmap!=null)
	    	bitmap.recycle();
	    	baos.flush();
        	baos.close();
			image.recycle();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			return null;
		}
	    return compressImageTobyte(BitmapFactory.decodeStream(isBm, null, newOpts));
	}  
	
	/**
	 * 先按比例压缩，再按质量压缩 分享的时候使用 
	 * @param image
	 * @return
	 */
	public static byte[] comp32k(Bitmap image) {  
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();         
	    image.compress(Bitmap.CompressFormat.JPEG, 100, baos);  
	    if( baos.toByteArray().length / 1024>30) {
	        baos.reset();  
	        image.compress(Bitmap.CompressFormat.JPEG, 50, baos);
	    }  
	    ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());  
	    BitmapFactory.Options newOpts = new BitmapFactory.Options();  
	    newOpts.inJustDecodeBounds = true;  
	    Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	    newOpts.inJustDecodeBounds = false;  
	    int w = newOpts.outWidth;  
	    int h = newOpts.outHeight;  
	    float hh = 58f; 
	    float ww = 58f;
	    int be = 1;
	    if (w > h && w > ww) {
	        be = (int) (newOpts.outWidth / ww);  
	    } else if (w < h && h > hh) {
	        be = (int) (newOpts.outHeight / hh);  
	    }  
	    if (be <= 0)  
	        be = 1;  
	    newOpts.inSampleSize = be;
	    
	    isBm = new ByteArrayInputStream(baos.toByteArray());  
	    try { 
	    	bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);  
	    	baos.flush();
        	baos.close();
			isBm.close();
			image.recycle();
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return compressImage32k(bitmap);
	}  

	
    /**
     * 从sd中读取图片bitmap
     * @param imgPath
     * @return
     */
    public static Bitmap readBitmapFromSD(String imgPath, int SampleSize){
    	try {
			FileInputStream fis = new FileInputStream(imgPath);
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inPreferredConfig = Config.RGB_565;
			opts.inPurgeable = true;
			opts.inInputShareable = true;
			opts.inSampleSize = SampleSize;
			Bitmap bm = BitmapFactory.decodeStream(fis, null, opts);
			fis.close();
			return bm;
		} catch (Exception e) {
			return null;
		}
    }
	
	/**   
	 * 以最省内存的方式读取本地资源的图片   
	 * @param context   
	 * @param resId   
	 * @return   
	 */    
	public static Bitmap readBitMap(Context context, int resId){     
	    BitmapFactory.Options opt = new BitmapFactory.Options();     
	    opt.inPreferredConfig = Bitmap.Config.RGB_565;      
	    opt.inPurgeable = true;     
	    opt.inInputShareable = true;     
	       //获取资源图片     
	    try {
	    	InputStream is = context.getResources().openRawResource(resId); 
	    	Bitmap bm =  BitmapFactory.decodeStream(is,null,opt);
		    is.close();
		    return bm;
		} catch (Exception e) {
		}
	    return null;     
	}    
	
    /**
     * 得到图片的缩放比例
     * @param options
     * @param target_w 宽 
     * @param target_h 高
     * @return
     */
    public static int computeSampleSize(BitmapFactory.Options options, int target_w, int target_h)
    {
    	int h = Math.max(options.outWidth, options.outHeight);
    	int w = Math.min(options.outWidth, options.outHeight);
        int candidateW = w / target_w;
        int candidateH = h / target_h;
        int candidate = Math.max(candidateW, candidateH);
        if (candidate == 0)
            return 1;
        if (candidate > 1)
        {
            if ((w > target_w) && (w / candidate) < target_w)
                candidate -= 1;
        }
        if (candidate > 1)
        {
            if ((h > target_h) && (h / candidate) < target_h)
                candidate -= 1;
        }
        return candidate;
    }

    
    /**
     * 保存图片到SD卡上
     * @param mBitmap
     * @return
     */
//    public static String saveMyBitmap(Bitmap mBitmap)  {
//    	File file = new File(Environment.getExternalStorageDirectory() + "/data/yzm/compress");
//        if(!file.exists())
//        	file.mkdirs();
//     // 创建一个以当前时间为名称的文件
//        File tempFile = new File(file.getAbsolutePath(), "temp_publish.jpg");
//        
//        FileOutputStream fOut = null;
//        try {
//                fOut = new FileOutputStream(tempFile);
//                boolean isSave = mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
//                fOut.flush();
//                fOut.close();
//                mBitmap.recycle();
//                if(isSave)
//                	return tempFile.getAbsolutePath();
//                else
//                	return null;
//        }catch (FileNotFoundException e) {
//                e.printStackTrace();
//        }catch (IOException e) {
//                e.printStackTrace();
//        }
//        return null;
//    }
    
    public static String saveMyBitmap(Bitmap mBitmap, int position)  {
    	
    	File file = new File(Environment.getExternalStorageDirectory() + "/data/yzm/compress");
        if(!file.exists())
        	file.mkdirs();
     // 创建一个以当前时间为名称的文件
        File tempFile = new File(file.getAbsolutePath(), "temp_publish"+ ("__" + SleepUtils.getSystemCurrentTime())
        		+ (Math.random() * 1000 + 1)+".jpg");
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(tempFile);
                boolean isSave = mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.flush();
                fOut.close();
                mBitmap.recycle();
                System.gc();
                if(isSave){
                	 return tempFile.getAbsolutePath();
                }
                else{
                	return null;
                }
        }catch (FileNotFoundException e) {
                e.printStackTrace();
        }catch (IOException e) {
                e.printStackTrace();
        }catch (OutOfMemoryError e) {
        	return null;
		}
        return null;
    }
    
    
    public static String saveMyBitmap(byte[] mBitmap, int position)  {
    	if(null == mBitmap){
    		return null;
    	}
    	File file = new File(Environment.getExternalStorageDirectory() + "/data/yzm/compress");
        if(!file.exists())
        	file.mkdirs();
     // 创建一个以当前时间为名称的文件
        
        String pathString = file.getAbsolutePath()+"/temp_publish"+ ("_" + SleepUtils.getSystemCurrentTime())
        		+ (Math.random() * 1000 + 1)+".jpg";
        File tempFile = new File(pathString);
        
        FileOutputStream fOut = null;
        try {
                fOut = new FileOutputStream(tempFile);
                fOut.write(mBitmap);
                fOut.flush();
                fOut.close();
        }catch (FileNotFoundException e) {
                e.printStackTrace();
        }catch (IOException e) {
                e.printStackTrace();
        }
        return pathString;
    }
	

    public static int computeSampleSizenew(BitmapFactory.Options options,
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
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));
     
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
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
    
    /**
     * 压缩资源图片
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,   
            int reqWidth, int reqHeight) {   
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小   
        final BitmapFactory.Options options = new BitmapFactory.Options();   
        options.inJustDecodeBounds = true;   
        BitmapFactory.decodeResource(res, resId, options);   
        // 调用上面定义的方法计算inSampleSize值   
        options.inSampleSize = computeSampleSize(options, reqWidth, reqHeight);   
        // 使用获取到的inSampleSize值再次解析图片   
        options.inJustDecodeBounds = false;   
        return BitmapFactory.decodeResource(res, resId, options);   
    }
    
  //下载图片
//    public static Bitmap loadImageFromUrl(String url) {
//    	URL m;
//    	InputStream i = null;
//    	Bitmap bitmap = null;
//    	try {
//    		m = new URL(url);
//    		i = (InputStream) m.getContent();
//
//    		bitmap = comp32k(BitmapFactory.decodeStream(i)); 
//    		
//    	} catch (IOException e) {
//    		// TODO Auto-generated catch block
//    		e.printStackTrace();
//    	} 
//    	return bitmap;
//    }
    
	/**
	 * 质量压缩
	 * @param image
	 * @return
	 */
//	private static Bitmap compressByQuality(Bitmap image) {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//		int options = 90;
//		while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
//			baos.reset();// 重置baos即清空baos
//			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//			options -= 10;// 每次都减少10
//			if(options == 0)
//				break;
//		}
//		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
//		image.recycle();
//		return bitmap;
//	}

	/**
	 *  传入图片路径按比例压缩
	 * @param srcPath
	 * @return  注意判断返回值是否为空，为空则表示压缩失败
	 */
//	public static Bitmap compressBitmap1(String srcPath) {
//		Bitmap result = null;
//		try {
//			BitmapFactory.Options newOpts = new BitmapFactory.Options();
//			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
//			newOpts.inJustDecodeBounds = true;
//			Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
//
//			newOpts.inJustDecodeBounds = false;
//			int w = newOpts.outWidth;
//			int h = newOpts.outHeight;
//			int be = 1;// be=1表示不缩放
//			be = (int) ((w / newOpts.outWidth + h/ newOpts.outHeight) / 2);
//			if (be <= 0 || be > 1)
//				be = 1;
//			newOpts.inSampleSize = be;// 设置缩放比例
//			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//			result = compressByQuality(bitmap);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//		return result;// 压缩好比例大小后再进行质量压缩
//	}

	/**
	 *  传入bitmap按比例压缩
	 * @param bm
	 * @return 注意判断返回值是否为空，为空则表示压缩失败
	 */
//	public static Bitmap compressBitmap2(Bitmap bm) {
//		Bitmap result = null;
//		try {
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();
//			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//			if (baos.toByteArray().length / 1024 > 1024) {// 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//				baos.reset();// 重置baos即清空baos
//				bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 这里压缩50%，把压缩后的数据存放到baos中
//			}
//			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//			BitmapFactory.Options newOpts = new BitmapFactory.Options();
//			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
//			newOpts.inJustDecodeBounds = true;
//			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//			newOpts.inJustDecodeBounds = false;
//			int w = newOpts.outWidth;
//			int h = newOpts.outHeight;
//			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//			int be = 1;// be=1表示不缩放
//			be = (int) ((w / newOpts.outWidth + h/ newOpts.outHeight) / 2);
//			if (be <= 0 || be > 1)
//				be = 1;
//			newOpts.inSampleSize = be;// 设置缩放比例
//			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//			isBm = new ByteArrayInputStream(baos.toByteArray());
//			bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//			result = compressByQuality(bitmap);
//		} catch (Exception e) {
//		}
//		return result;// 压缩好比例大小后再进行质量压缩
//	}
}
