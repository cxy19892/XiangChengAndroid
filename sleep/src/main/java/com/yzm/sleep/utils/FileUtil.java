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
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.OutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Field;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore.Images.ImageColumns;
import android.widget.ImageView;

import com.easemob.util.PathUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.yzm.sleep.MyApplication;
import com.yzm.sleep.R;

public class FileUtil {

	// <!-- 在SDCard中创建与删除文件权限 -->
	//
	// <uses-permission
	// android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"/>
	//
	// <!-- 往SDCard写入数据权限 -->
	//
	// <uses-permission
	// android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	private static final String TAG = "FileUtil";

	public static File getCacheFile(String imageUri) {
		File cacheFile = null;
		try {
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File sdCardDir = Environment.getExternalStorageDirectory();
				String fileName = getFileName(imageUri);
				File dir = new File(sdCardDir.getCanonicalPath() + "/CACHE_DIR");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				cacheFile = new File(dir, fileName);
				LogUtil.i(TAG, "exists:" + cacheFile.exists() + ",dir:" + dir
						+ ",file:" + fileName);
			}
		} catch (IOException e) {
			e.printStackTrace();
			LogUtil.e(TAG, "getCacheFileError:" + e.getMessage());
		}
		return cacheFile;
	}
	
	/**
	 * 通过远程URL，确定下本地下载后的localurl
	 * @param remoteUrl
	 * @return
	 */
	public static String getLocalFilePath(String remoteUrl){
		String localPath;
		if (remoteUrl.contains("/")){
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/"+ remoteUrl.substring(remoteUrl.lastIndexOf("/") + 1);
		}else{
			localPath = PathUtil.getInstance().getImagePath().getAbsolutePath() + "/" + remoteUrl;
		}
		return localPath;
	}
	
	/** 
     * 获取原图片存储路径 
     * @return 
     */  
    public static String getPhotopath() {  
        // 文件夹路径  
        String pathUrl = Environment.getExternalStorageDirectory()
				+ "/data/yzm/compress";  
        String imageName = "imageTest"+ System.currentTimeMillis() +".jpg";  
        File file = new File(pathUrl);
        if(!file.exists())
        	file.mkdirs();// 创建文件夹  
        return new File(new File(pathUrl), imageName).getAbsolutePath();  
    } 
	
	/**
	 * 将资源释放到SD卡
	 * @param name 文件名字
	 * @param dirpath // sd卡路径
	 * @return
	 */
	public static boolean copyResToSdcard(Context context,String name, String dirpath){
		boolean isRestorage2SdcardOk = false;
		BufferedOutputStream bufEcrivain = null; //
		BufferedInputStream videoReader=null;
		FileInputStream videoFile=null;
		
		Field[] raw = R.raw.class.getFields();  
	    File destDir = new File(dirpath);
	    if (!destDir.exists()) {
	    	destDir.mkdirs();
	    }
	    for (Field r : raw) {  
	         try {  
	             int id=context.getResources().getIdentifier(r.getName(), "raw", context.getPackageName());  
	               if(r.getName().equals(name)){
	            	   
	                 String path=dirpath+"/"+r.getName()+".aac";  
	                 bufEcrivain = new BufferedOutputStream((new FileOutputStream(new File(path))));  
	                 videoReader = new BufferedInputStream(context.getResources().openRawResource(id));
	                 
	                 byte[] buff = new byte[20*1024];  
	                 int len;  
	                 while((len = videoReader.read(buff)) > 0 ){  
	                     bufEcrivain.write(buff,0,len);  
	                 }  
	                 bufEcrivain.flush();
	                 
//	                 bufEcrivain.close();  
//	                 videoReader.close();
	                 
	                 videoFile = new FileInputStream(new File(path));  
	                 if(videoFile.available() > 0){
	                	 isRestorage2SdcardOk = true;
	                 }
	                 
//	                 videoReader2Sd.close();
	               }  
	         } catch (Exception e) {  
	             e.printStackTrace(); 
	             isRestorage2SdcardOk = false;
	         }finally{
	        	 if(bufEcrivain!=null){
	        		 try {
						bufEcrivain.close();
					} catch (IOException e) {
						e.printStackTrace();
					}   
	        	 }
	        	 if(videoReader!=null){
	        		 try {
						videoReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
	        	 }
	        	 if(videoFile!=null){
	        		 try{
	        			 videoFile.close(); 
	        		 }catch (IOException e) {
							e.printStackTrace();
					}
	        	 }
	         }
	     }
		return isRestorage2SdcardOk;
		
	}
	
	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
	
    /**   
     * 获取SD path   
     * @return   
     */  
    public static String getSDPath(){   
        File sdDir = null;   
        boolean sdCardExist = Environment.getExternalStorageState()   
                .equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在   
        if (sdCardExist)   
        {   
            sdDir = Environment.getExternalStorageDirectory();// 获取跟目录   
            return sdDir.toString();   
        }  
        return null;  
    } 
    
    /**
     *  字节流以文件形式保存到SD卡
     * @param fileName  
     * 					带上文件后缀名
     * @param arr
     * @return 
     * 					如果返回的字符串为“noSDcard”则表示手机没有sd卡。返回null则表示存储失败.成功则返回数据存储路径
     */
    public static String saveFileToSDcard(String fileName, byte[] arr){
    	String sdPathString = getSDPath();
    	if(sdPathString == null)
    		return "noSDcard";
    	try {
    		ByteArrayInputStream bis = new ByteArrayInputStream(arr);
    		File file = new File(sdPathString + "/data/yzm/pillow/");
    		if(!file.exists())
    			file.mkdirs();
    		File f = new File(file, fileName);
			FileOutputStream bos = new FileOutputStream(f);
			int len = 0;
			byte[] bytearr = new byte[1024];
			while ((len = bis.read(bytearr)) > 0) {
				bos.write(bytearr, 0, len);
			}
			bis.close();
			bos.flush();
			bos.close();
			return f.getAbsolutePath();
		} catch (Exception e) {
		}
		return null;
    }
    
    
    
    /**
     * 判断SD卡上文件是否存在
     * @param fileName
     * @return
*/
    public static boolean isFileExist(String fileName){
        File file=new File( getSDPath()+fileName);
        return file.exists();
    }

	/**
	 * 获取文件名
	 * 
	 * @param path
	 * @return
	 */
	public static String getFileName(String path) {
		int index = path.lastIndexOf("/");
		return path.substring(index + 1);
	}
	/**
	 * 获取文件夹路径
	 * @param path
	 * 				文件完整路径
	 * @return
	 */
	private static String getFilePath(String path) {
		return path.substring(0,path.lastIndexOf("/") + 1);
	}

	/**
	 * 保存文件内容
	 * 
	 * @param c
	 * @param fileName
	 *            文件名称
	 * @param content
	 *            内容
	 * @param mode
	 *            操作模式
	 */
	public static void writeFiles(Context c, String fileName, String content,
			int mode) throws Exception {
		// 打开文件获取输出流，文件不存在则自动创建
		FileOutputStream fos = c.openFileOutput(fileName, mode);
		fos.write(content.getBytes());
		fos.close();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param c
	 * @param fileName
	 *            文件名称
	 * @return 返回文件内容
	 */
	public static String readFiles(Context c, String fileName) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		FileInputStream fis = c.openFileInput(fileName);
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = fis.read(buffer)) != -1) {
			baos.write(buffer, 0, len);
		}
		String content = baos.toString();
		fis.close();
		baos.close();
		return content;
	}

	/**
	 * 保存对象到本地
	 * 
	 * @param basicMsg
	 * @param fileName
	 *            文件名
	 * @param mode
	 *            操作模式
	 * 
	 */
	public static void saveObjectToLocal(Context context, Object basicMsg,
			String fileName, int mode) {
		try {
			// 通过openFileOutput方法得到一个输出流，方法参数为创建的文件名（不能有斜杠），操作模式
			FileOutputStream fos = context.openFileOutput(fileName, mode);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(basicMsg);// 写入
			fos.close(); // 关闭输出流
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从本地读取对象
	 * 
	 * @param basicMsg
	 * @param fileName
	 *            文件名
	 */
	public static Object readObjectFromLocal(Context context, String fileName) {
		Object basicmsg = null;
		try {
			FileInputStream fis = context.openFileInput(fileName); // 获得输入流
			ObjectInputStream ois = new ObjectInputStream(fis);
			basicmsg = (Object) ois.readObject();

		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return basicmsg;
	}

	/**
	 * 保存文件到SD卡
	 * 
	 * @param fileName
	 *            文件完整路径
	 * @param content
	 *            文件内容
	 */
	public static void saveFileToSD(String fileName, String content) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
			File sdFile = new File(fileName);

			try {
				FileOutputStream fos = new FileOutputStream(sdFile);
				fos.write(content.getBytes());// 写入
				fos.close(); // 关闭输出流
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 读取SD卡中的文本文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static String readFileFromSD(String fileName) {
		StringBuffer sb = new StringBuffer();
		File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
		File sdFile = new File(sdCardDir, fileName);

		try {
			FileInputStream fis = new FileInputStream(sdFile); // 获得输入流
			int c;
			while ((c = fis.read()) != -1) {
				sb.append((char) c);
			}
			fis.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 保存基本信息对象到SD卡
	 * 
	 * @param basicMsg
	 * @param fileName
	 *            文件名
	 */
	public static void saveObjectToSD(Object basicMsg, String fileName) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
			File sdFile = new File(sdCardDir, fileName);

			try {
				FileOutputStream fos = new FileOutputStream(sdFile);
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(basicMsg);// 写入
				fos.close(); // 关闭输出流
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 从SD卡种读基本信息对象
	 * 
	 * @param basicMsg
	 */
	public static Object readObjectFromSD(String fileName) {
		Object basicmsg = null;
		File sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目录
		File sdFile = new File(sdCardDir, fileName);

		try {
			FileInputStream fis = new FileInputStream(sdFile); // 获得输入流
			ObjectInputStream ois = new ObjectInputStream(fis);
			basicmsg = (Object) ois.readObject();
			ois.close();
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return basicmsg;
	}

	/**
	 * 创建文件目录
	 * 
	 * @param filePath
	 */
	public static void makeRootDirectory(String filePath) {
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.mkdir();
			}
		} catch (Exception e) {

		}
	}
	
	public static void makeDirectory(String filePath){
		File file = null;
		try {
			file = new File(filePath);
			if (!file.exists()) {
				file.getParentFile().mkdirs();
//				file.createNewFile();
				file.mkdir();
			}
		} catch (Exception e) {
		}
		
	}

	/**
	 * 删除文件
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete(); // 删除文件
			} else if (file.isDirectory()) { // 否则如果它是一个目录
				File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
				for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
					deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
				}
				file.delete();
			}
		} else {
			LogUtil.d("result msg:", "文件不存在！" + "\n");
		}
	}
	
    public static File writeToSDfromInput(String path,String fileName,InputStream inputStream){
        //createSDDir(path);
    	
//        File file=createSDFile(path + fileName);
        File file = new File(path + fileName);
        OutputStream outStream=null;
        try {
            outStream=new FileOutputStream(file);
            //4k的缓冲区
            byte[] buffer=new byte[4*1024];
            while(inputStream.read(buffer)!=-1){
                outStream.write(buffer);
            }
            outStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                outStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }
    
    
    /**
     * 文件重命名
     * @param filePath
     * 					文件路径
     * @param fileName
     * 					文件新名称
     */
    public static String renameFile(String filePath,String fileName){
    	String newPathString = getFilePath(filePath) + fileName;
    	File oldFile = new File(filePath);
    	File newFile = new File(newPathString);
    	oldFile.renameTo(newFile);//执行重命名
    	return newPathString;
    }
    
    /** 
     * 复制单个文件 
     * @param oldPath String 原文件路径 如：c:/fqf.txt 
     * @param newPath String 复制后路径 如：f:/fqf.txt 
     * @return boolean 
     */ 
   public static void copyFile(String oldPath, String newPath) { 
	   File newFile = new File(newPath);
	   if (newFile.exists()) {
		newFile.delete();
	   }
       try { 
           int bytesum = 0; 
           int byteread = 0; 
           File oldfile = new File(oldPath); 
           if (oldfile.exists()) { //文件存在时 
               InputStream inStream = new FileInputStream(oldPath); //读入原文件 
               FileOutputStream fs = new FileOutputStream(newPath); 
               byte[] buffer = new byte[1444]; 
               int length; 
               while ( (byteread = inStream.read(buffer)) != -1) { 
                   bytesum += byteread; //字节数 文件大小 
                   System.out.println(bytesum); 
                   fs.write(buffer, 0, byteread); 
               } 
               inStream.close(); 
           } 
       } 
       catch (Exception e) { 
           System.out.println("复制单个文件操作出错"); 
           e.printStackTrace(); 

       } 

   } 

   /** 
     * 复制整个文件夹内容 
     * @param oldPath String 原文件路径 如：c:/fqf 
     * @param newPath String 复制后路径 如：f:/fqf/ff 
     * @return boolean 
     */ 
   public void copyFolder(String oldPath, String newPath) { 

       try { 
           (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
           File a=new File(oldPath); 
           String[] file=a.list(); 
           File temp=null; 
           for (int i = 0; i < file.length; i++) { 
               if(oldPath.endsWith(File.separator)){ 
                   temp=new File(oldPath+file[i]); 
               } 
               else{ 
                   temp=new File(oldPath+File.separator+file[i]); 
               } 

               if(temp.isFile()){ 
                   FileInputStream input = new FileInputStream(temp); 
                   FileOutputStream output = new FileOutputStream(newPath + "/" + 
                           (temp.getName()).toString()); 
                   byte[] b = new byte[1024 * 5]; 
                   int len; 
                   while ( (len = input.read(b)) != -1) { 
                       output.write(b, 0, len); 
                   } 
                   output.flush(); 
                   output.close(); 
                   input.close(); 
               } 
               if(temp.isDirectory()){//如果是子文件夹 
                   copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
               } 
           } 
       } 
       catch (Exception e) { 
           System.out.println("复制整个文件夹内容操作出错"); 
           e.printStackTrace(); 

       } 

   }
   
   /**  
    * 文件转化为字节数组  
    * 
    */   
   public static byte[] getBytesFromFile(File f) {   
       if (f == null) {   
           return null;   
       }   
       try {   
           FileInputStream stream = new FileInputStream(f);   
           ByteArrayOutputStream out = new ByteArrayOutputStream(1000);   
           byte[] b = new byte[1000];   
           int n;   
           while ((n = stream.read(b)) != -1) {  
               out.write(b, 0, n);   
              }  
           stream.close();   
           out.close();   
           return out.toByteArray();   
       } catch (IOException e) {   
       }   
       return null;   
   }  
   
   
  //------------------------------------------------------------------------------
// ------------------------------------数据的缓存目录-------------------------------------------------------
   public static String SDCARD_PAHT ;// SD卡路径
	public static String LOCAL_PATH ;// 本地路径,即/data/data/目录下的程序私有目录
	public static String CURRENT_PATH = "";// 当前的路径,如果有SD卡的时候当前路径为SD卡，如果没有的话则为程序的私有目录
	
//	static
//	{
//		init();
//	}
//
//	public static void init()
//	
//	{
//		SDCARD_PAHT = getSDPath();
//		LOCAL_PATH = DemoApp.getInstance().getApplicationContext().getFilesDir().getAbsolutePath();// 本地路径,即/data/data/目录下的程序私有目录
//		
//		if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
//		{
//			CURRENT_PATH = SDCARD_PAHT;
//		} 
//		else
//		{
//			CURRENT_PATH = LOCAL_PATH;
//		}
//	}
   
   
   
	/**
	 * 创建一个文件，创建成功返回true
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean createFile(String filePath)
	{
		try
		{
			File file = new File(filePath);
			if (!file.exists())
			{
				if (!file.getParentFile().exists())
				{
					file.getParentFile().mkdirs();
				}

				return file.createNewFile();
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filePath
	 *            要删除的文件路径名
	 * @return true if this file was deleted, false otherwise
	 */
	public static boolean deleteFile(String filePath)
	{
		try {
			File file = new File(filePath);
			if (file.exists())
			{
				return file.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	public static void writeImage(Bitmap bitmap,String destPath,int quality)
	{
		try {
			FileUtil.deleteFile(destPath);
			if (FileUtil.createFile(destPath))
			{
				FileOutputStream out = new FileOutputStream(destPath);
				if (bitmap!=null && bitmap.compress(Bitmap.CompressFormat.JPEG,quality, out))
				{
					out.flush();
					out.close();
					out = null;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Try to return the absolute file path from the given Uri
	 *
	 * @param context
	 * @param uri
	 * @return the file path or null
	 */
	public static String getRealFilePath( final Context context, final Uri uri ) {
	    if ( null == uri ) return null;
	    final String scheme = uri.getScheme();
	    String data = null;
	    if ( scheme == null )
	        data = uri.getPath();
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
	        data = uri.getPath();
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );
	        if ( null != cursor ) {
	            if ( cursor.moveToFirst() ) {
	                int index = cursor.getColumnIndex( ImageColumns.DATA );
	                if ( index > -1 ) {
	                    data = cursor.getString( index );
	                }
	            }
	            cursor.close();
	        }
	    }
	    return data;
	}
	
	/**
	 * 检查文件是否存在
	 */
	public static String checkIsExsits( String fileName){
		if (fileName == null) {
			return null;
		}
		String mFilename = fileName.replace("/", "_").replace(":", "_");
		if (!mFilename.endsWith(".aac")) {
			mFilename = mFilename +".aac";
		}
		String targetPath = FileUtil.getSDPath() +"/xiangcheng/Audio/download/";
		FileUtil.makeDirectory(targetPath);
		File file = new File(targetPath);
		File[] files = file.listFiles();
		if (files != null) {
			for (File f : files) {
				if(f.getName().equals(mFilename))
					return targetPath + f.getName();
			}
		}
		return null;
	}
	
	public static boolean checkIsExsitsfromPath( String path){
		if (path == null) {
			return false;
		}
		
		String targetPath =path;
		File file = new File(targetPath);
		if(file.exists()){
			return true;
		}
		return false;
	}
	

	
	/**
	 * 根据文件的key获取文件全名
	 * @param fileKey
	 * @return
	 */
	public static String getFilePathByFileKey(String fileKey){
		String fileName = fileKey.replaceAll("/", "_");
		fileName = fileName.replaceAll(":", "_");
		if (!fileName.endsWith(".aac")) {
			fileName = fileName +".aac";
		}
		return fileName;
	}
	
	/**
	 * 获取指定格式的睡眠信息文件
	 * @param FileExtension
	 * @return
	 */
	public static List<String> getFilePathFromSD(String FileExtension) {
		 // 文件列表
		 List<String> picList = new ArrayList<String>();
		 // the sd card path
		  String filePath = getpath();
		 // get all of the file from the path
		  if(filePath .equals("noSDcard")){
			  return picList;
		  }
		  File mfile = new File(filePath);
		  File[] files = mfile.listFiles();
		 // 将所有的文件存入ArrayList中,并过滤所有需要的格式的文件
		 for (int i = 0; i < files.length; i++) {
		  File file = files[i];
		  if (checkIsquestFile(file.getPath() , FileExtension)) {
		   picList.add(file.getPath());
		  }
		 }
		 // return thr file list
		 return picList;
		}
	
	public static List<String> getFilePathFromSD(String filePath, String FileExtension) {
		 // 文件列表
		 List<String> picList = new ArrayList<String>();
		 // the sd card path
//		  String filePath = getpath();
		 // get all of the file from the path
		  if(filePath .equals("noSDcard")){
			  return picList;
		  }
		  File mfile = new File(filePath);
		  File[] files = mfile.listFiles();
		 // 将所有的文件存入ArrayList中,并过滤所有需要的格式的文件
		 for (int i = 0; i < files.length; i++) {
		  File file = files[i];
		  if (checkIsquestFile(file.getPath() , FileExtension)) {
		   picList.add(file.getPath());
		  }
		 }
		 // return thr file list
		 return picList;
		}
	
		/**
		 *  检查扩展名，得到需要格式的文件
		 * @param fName
		 * @return
		 */
	public static boolean checkIsquestFile(String fName, String FileExtension) {
		boolean isrequestFile = false;
		// 获取扩展名
		String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
				fName.length()).toLowerCase();
		if (FileEnd.equals(FileExtension)){
			isrequestFile = true;
		} else {
			isrequestFile = false;
		}
		return isrequestFile;
	}
	
	/**
	 * 獲取水面信息目錄位置
	 * @return
	 */
	public static String getpath(){
		String sdPathString = FileUtil.getSDPath();
		if(sdPathString == null){
			return "noSDcard";
		}
	
		File file = new File(sdPathString + "/data/yzm/pillow/");
		if(!file.exists()){
			file.mkdirs();
		}
		return sdPathString + "/data/yzm/pillow/";
	}
	
	public static String getHexpath(){
			String sdPathString = FileUtil.getSDPath();
			if(sdPathString == null){
				return "noSDcard";
			}
		
			File file = new File(sdPathString + "/data/yzm/devhex/");
			if(!file.exists()){
				file.mkdirs();
			}
			return sdPathString + "/data/yzm/devhex/";
	}
	
	public static boolean isHexFileExist(String fileName){
        File file=new File( getHexpath()+fileName);
        return file.exists();
    }
	
	private static String[] icons_url={"/group/ico/1.jpg","/group/ico/2.jpg","/group/ico/3.jpg","/group/ico/4.jpg","/group/ico/5.jpg","/group/ico/6.jpg"};
	
	/**
	 * 获取小组网路地址
	 * @param index
	 * @return
	 */
	public static String getIconUrl(int index){
		if(index > -1 && index < icons_url.length){
			return icons_url[index];
		}else{
			return "";
		}
	}
	
	/**
	 *  搞笑复制文件的方法
	 * @param s
	 * @param t
	 */
	public static void copyFile(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            in = fi.getChannel();//得到对应的文件通道
            out = fo.getChannel();//得到对应的文件通道
            in.transferTo(0, in.size(), out);//连接两个通道，并且从in通道读取，然后写入out通道
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.flush();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
