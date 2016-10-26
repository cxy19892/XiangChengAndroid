package com.yzm.sleep;

import im.fir.sdk.FIR;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import cn.jpush.android.api.CustomPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import com.baidu.mapapi.SDKInitializer;
import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.EasyUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.yzm.sleep.activity.HomeActivity;
import com.yzm.sleep.background.SleepInfo;
import com.yzm.sleep.model.DailyRankInfo;
import com.yzm.sleep.model.FriendsNearbyInfo;
import com.yzm.sleep.model.RingtoneInfo;
import com.yzm.sleep.utils.EaseUI;
import com.yzm.sleep.utils.FileUtil;
import com.yzm.sleep.utils.InterFaceUtilsClass.WeiBoUserInfoListClass;
import com.yzm.sleep.utils.PreManager;

public class MyApplication extends Application {
	private static MyApplication instance = null;
	public ArrayList<FriendsNearbyInfo> friendsRanks = new ArrayList<FriendsNearbyInfo>();
	public ArrayList<FriendsNearbyInfo> nearbyRanks = new ArrayList<FriendsNearbyInfo>();
	public DailyRankInfo dailyrank = new DailyRankInfo();
	public RingtoneInfo currentSelectInfo = new RingtoneInfo();
	public RingtoneInfo sleepSelectInfo = new RingtoneInfo();
	public FriendsNearbyInfo currentSendFriend = new FriendsNearbyInfo();
	
	public boolean addStart = false;
	public int receiverOrder = 0;
	
	public List<String> m_usernamesList= null;
	
	
	public int g_FriendRequest=0;
	private int fragmentSelect = 0;
	
	public static String XIANGCHENG_RINGS_PATH = FileUtil.getSDPath() +"/data/yzm/Rings/download/";
	public static String XIANGCHENG_RINGS_PATH_TEMP = FileUtil.getSDPath() +"/data/yzm/Rings/temp/";
	public static String XIANGCHENG_AUDIO_PATH = FileUtil.getSDPath() +"/xiangcheng/Audio/download/";
	public static String XIANGCHENG_AUDIO_PIC_PATH = FileUtil.getSDPath() +"/xiangcheng/pic/";
	public static String XIANGCHENG_FRIEND_TOKEN_PATH = FileUtil.getSDPath() +"/xiangcheng/friend/";
	
	List<WeiBoUserInfoListClass> weiboFriendInfo = new ArrayList<WeiBoUserInfoListClass>();
	@Override
	public void onCreate() {
		super.onCreate();
		instance =this;
		//初始化环信
		if(EaseUI.getInstance().init(getApplicationContext())){
			registerEventListener(EaseUI.getInstance());
		}
		init();
		initJPush();
		initImageLoader();
		SDKInitializer.initialize(getApplicationContext());
	}
	
	private void init() {
		FIR.init(this);
		FileUtil.makeDirectory(XIANGCHENG_AUDIO_PATH);
		FileUtil.makeDirectory(XIANGCHENG_AUDIO_PIC_PATH);
		FileUtil.makeDirectory(XIANGCHENG_FRIEND_TOKEN_PATH);
		
		SharedPreferences sp = getSharedPreferences(SleepInfo.SLEEP_SETTIME, MODE_APPEND);		
		SleepInfo.SET_STARTTIME = sp.getString(SleepInfo.STARTTIME, "").equals("") ? SleepInfo.SET_STARTTIME : Long.valueOf(sp.getString(SleepInfo.STARTTIME, ""));
		SleepInfo.SET_ENDTIME = sp.getString(SleepInfo.ENDTIME, "").equals("") ? SleepInfo.SET_ENDTIME : Long.valueOf(sp.getString(SleepInfo.ENDTIME, ""));
		
	}
	public static DisplayImageOptions selectpicOption, 
									  defaultOption,    //默认图片加载模式，包括大图加载等模式
									  choicePicOptn,    //精选页面的列表icon加载模式
									  headPicOptn       //所有头像的加载模式，包括人的头像和小组的图片
									  ;
	
	private void initImageLoader(){
		//图片缓存路径cacheDir
	    File cacheDir = StorageUtils.getOwnCacheDirectory(this, "data/yzm/Cache"); 
		
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration
				.Builder(this)
				.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽  
			    .threadPoolSize(3)//线程池内加载的数量  
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new WeakMemoryCache()) // You can pass your own memory cache implementation
			    .memoryCacheSize(2 * 1024 * 1024)    
			    .discCacheSize(50 * 1024 * 1024)    
			    .tasksProcessingOrder(QueueProcessingType.LIFO)  
			    .discCacheFileCount(100) //缓存的文件数量  
			    .discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径  
			    .build();//开始构建  
			 // Initialize ImageLoader with configuration.  
		ImageLoader.getInstance().init(config);
		
		
		defaultOption = new DisplayImageOptions.Builder()  
		.showImageOnLoading(new ColorDrawable(getResources().getColor(R.color.cbg_color))) //设置图片在下载期间显示的图片  
		.showImageForEmptyUri(new ColorDrawable(getResources().getColor(R.color.cbg_color)))//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(new ColorDrawable(getResources().getColor(R.color.cbg_color)))  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中 
		.cacheOnDisk(true)
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示  
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		.displayer(new FadeInBitmapDisplayer(200))//是否图片加载好后渐入的动画时间
		.build();//构建完成  
		
		selectpicOption = new DisplayImageOptions.Builder()
		.showImageOnLoading(new ColorDrawable(getResources().getColor(R.color.cbg_color))) // resource or
        .showImageForEmptyUri(new ColorDrawable(getResources().getColor(R.color.cbg_color))) // resource or
        .showImageOnFail(new ColorDrawable(getResources().getColor(R.color.cbg_color))) // resource or
        .bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型//
        .resetViewBeforeLoading(false) // default
        .imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示  
        .cacheInMemory(true)
        .build();
		
		choicePicOptn = new DisplayImageOptions.Builder()  
		.showImageOnLoading(R.drawable.default_choice_icon) //设置图片在下载期间显示的图片  
		.showImageForEmptyUri(R.drawable.default_choice_icon)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.default_choice_icon)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中 
		.cacheOnDisk(true)
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示  
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		.displayer(new RoundedBitmapDisplayer(6))//设置图片的圆角
		.build();//构建完成  
		
		headPicOptn = new DisplayImageOptions.Builder()  
		.showImageOnLoading(R.drawable.default_head_icon) //设置图片在下载期间显示的图片  
		.showImageForEmptyUri(R.drawable.default_head_icon)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.default_head_icon)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中 
		.cacheOnDisk(true)
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY)//设置图片以如何的编码方式显示  
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		.build();//构建完成  
	}
	
	
	/**
     * 全局事件监听
     * 因为可能会有UI页面先处理到这个消息，所以一般如果UI页面已经处理，这里就不需要再次处理
     * activityStack.size()<= 0 意味着所有页面都已经在后台运行，或者已经离开Activity Stack
     */
    private void registerEventListener(final EaseUI easeUI) {
    	EMEventListener eventListener = new EMEventListener() {
            @SuppressWarnings("unchecked")
			@Override
            public void onEvent(EMNotifierEvent event) {
                EMMessage message = null;
                if(event.getData() instanceof EMMessage){
                    message = (EMMessage)event.getData();
                }
                
                switch (event.getEvent()) {
	                case EventNewMessage:
	                    //应用在后台，不需要刷新UI,通知栏提示新消息 //AppManager.getAppManager().getActivitySize() <= 0
	                    if( !EasyUtils.isAppRunningForeground(getApplicationContext()))
	                    	easeUI.getNotifier().onNewMsg(message);
	                    break;
	                case EventOfflineMessage:
	                    if(!EasyUtils.isAppRunningForeground(getApplicationContext())){
	                        List<EMMessage> messages = (List<EMMessage>)event.getData();
	                        easeUI.getNotifier().onNewMesg(messages);
	                    }
	                    break;
	                case EventNewCMDMessage:
	                	 //收到透传消息
	                	 break;
	                case EventDeliveryAck:
	                    message.setDelivered(true);
	                    break;
	                case EventReadAck:
	                    message.setAcked(true);
	                    break;
	                default:
	                    break;
                }
            }
        };
        EMChatManager.getInstance().registerEventListener(eventListener);
    }
    
	public void initJPush(){
		JPushInterface.init(this);
		JPushInterface.setDebugMode(true);
		JPushInterface.setAlias(this, PreManager.instance().getUserId(this), new TagAliasCallback() {
			
			@Override
			public void gotResult(int arg0, String arg1, Set<String> arg2) {
			}
		});
	    CustomPushNotificationBuilder builder = new 
	    	    CustomPushNotificationBuilder(this,
	    	                              R.layout.customer_notitfication_layout, 
	    	                              R.id.icon, 
	    	                              R.id.notifier_title, 
	    	                              R.id.notifier_content); 
	    	                             // 指定定制的 Notification Layout
	    	    builder.statusBarDrawable = R.drawable.logo;      
	    	    // 指定最顶层状态栏小图标
	    	    builder.layoutIconDrawable = R.drawable.logo;   
	    	    // 指定下拉状态栏时显示的通知图标
	    	    JPushInterface.setPushNotificationBuilder(2, builder);

	}

	public void setWeiBoFriend (List<WeiBoUserInfoListClass> data){
		this.weiboFriendInfo = data;
	}
	public List<WeiBoUserInfoListClass> getWeiBoFriend (){
		return this.weiboFriendInfo;
	}
    public void setFragmentSelect(int i){
    	fragmentSelect = i;
    }
    public int getFragmentSelect() {
		return fragmentSelect;
	}
	public synchronized static MyApplication instance() {
		if (instance == null) {
			instance = new MyApplication();
		}
		return instance;
	}

	public ArrayList<FriendsNearbyInfo> getFriendsRanks(){
		return this.friendsRanks;
		
	}
	public void setFriendsRanks(ArrayList<FriendsNearbyInfo> friendsRanks){
		this.friendsRanks = friendsRanks;
	}
	public ArrayList<FriendsNearbyInfo> getNearbyRanks(){
		return this.nearbyRanks;
		
	}
	public void setNearbyRanks(ArrayList<FriendsNearbyInfo> nearbyRanks){
		this.nearbyRanks = nearbyRanks;
	}
	public DailyRankInfo getDailyRank(){
		return this.dailyrank;
		
	}
	public void setDailyRank(DailyRankInfo ringInfo){
		this.dailyrank = ringInfo;
	}
	
	/**
	 * 获取当前选择的铃声
	 * @return
	 */
	public RingtoneInfo getCurrentSelectRingInfo(){
		return this.currentSelectInfo;
		
	}
	/**
	 * 设置当前选择的铃声
	 * @param ringInfo
	 */
	public void setCurrentSelectRingInfo(RingtoneInfo ringInfo){
		this.currentSelectInfo = ringInfo;
	}
	public void setSleepSelectRingInfo(RingtoneInfo ringInfo){
		this.sleepSelectInfo = ringInfo;
	}
	public RingtoneInfo getSleepSelectRingInfo(){
		return this.sleepSelectInfo;
	}
	/**
	 * 获取当前发送铃音的对象
	 * @return
	 */
	public FriendsNearbyInfo getCurrentSendFriend(){
		return this.currentSendFriend;
		
	}
	/**
	 * 设置当前发送铃音的对象
	 * @param ringInfo
	 */
	public void setCurrentSendFriend(FriendsNearbyInfo friendInfo){
		this.currentSendFriend = friendInfo;
	}
	
	/**
	 * 获取当前选择的铃声
	 * @return
	 */
	public boolean getAddStart(){
		return this.addStart;
		
	}
	/**
	 * 设置当前选择的铃声
	 * @param ringInfo
	 */
	public void setAddStart(boolean addStart){
		this.addStart = addStart;
	}
	
	/**
	 */
	public int getHomePageReceiverOrder(){
		return this.receiverOrder;
		
	}
	/**
	 */
	public void saveHomePageReceiverOrder(int receiverOrder){
		this.receiverOrder = receiverOrder;
	}
	
	public boolean CheckFriend(String strID)
	{
		boolean m_return=false;
		
		if(m_usernamesList!=null)
		{
			String StrList="";
			for (int i = 0; i < m_usernamesList.size(); i++) {				
				StrList=m_usernamesList.get(i);
				
				if(strID.equals(StrList))
				{					
					m_return=true;
					break;
				}
				
			}
			
			
		}
		
		
		return m_return;
	}

	private HomeActivity g_HomeActivity;
	
	public HomeActivity getG_HomeActivity() {
		return g_HomeActivity;
	}
	
	public void setG_HomeActivity(HomeActivity g_HomeActivity) {
		this.g_HomeActivity = g_HomeActivity;
	}
	
	
	public int g_TackPhoto=-1;
	
	public String g_PhotoPathString;
	
	public long displayWidth = 100;
	
//	public PillowHelper pillowhelper = null;

}
