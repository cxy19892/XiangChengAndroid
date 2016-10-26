package com.yzm.sleep.utils;

import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.yzm.sleep.AppManager;
import com.yzm.sleep.bean.EaseUser;
import com.yzm.sleep.model.EaseNotifier;

public final class EaseUI {
    public static final String TAG = EaseUI.class.getSimpleName();
    private static EaseUI instance = null;
    private EaseUserProfileProvider userProvider;
    private EaseSettingsProvider settingsProvider;
    private Context appContext = null;
    private boolean sdkInited = false;
    private EaseNotifier notifier = null;
    
    private EaseUI(){}
    
    /**
     * 获取EaseUI单实例对象
     * @return
     */
    public synchronized static EaseUI getInstance(){
        if(instance == null){
            instance = new EaseUI();
        }
        return instance;
    }
    
    /**
     *this function will initialize the HuanXin SDK
     * 
     * @return boolean true if caller can continue to call HuanXin related APIs after calling onInit, otherwise false.
     * 
     * 初始化环信sdk及easeui库
     * 返回true如果正确初始化，否则false，如果返回为false，请在后续的调用中不要调用任何和环信相关的代码
     * @param context
     * @return
     */
    public synchronized boolean init(Context context){
        if(sdkInited){
            return true;
        }
        appContext = context;
        
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        if (processAppName == null || !processAppName.equalsIgnoreCase(appContext.getPackageName())) {
            return false;
        }
        EMChat.getInstance().init(context);
        
        initChatOptions();
        if(settingsProvider == null){
            settingsProvider = new DefaultSettingsProvider();
        }
        sdkInited = true;
        return true;
    }
    
    protected void initChatOptions(){
        EMChatOptions options = EMChatManager.getInstance().getChatOptions();
        options.setAcceptInvitationAlways(false);
        options.setUseRoster(false);
        options.setRequireAck(false);
        options.setRequireDeliveryAck(false);
        options.setNumberOfMessagesLoaded(20);
        options.setNotifyBySoundAndVibrate(PreManager.instance().getIsOpenChatInform(appContext)==1);
        notifier = createNotifier();
        notifier.init(appContext);
    }
    
    protected EaseNotifier createNotifier(){
        return new EaseNotifier();
    }
    
    public EaseNotifier getNotifier(){
        return notifier;
    }
    
    public boolean hasForegroundActivies(){
        return AppManager.getAppManager().getActivitySize() != 0;
    }
    
    /**
     * 设置用户属性提供者
     * @param provider
     */
    public void setUserProfileProvider(EaseUserProfileProvider userProvider){
        this.userProvider = userProvider;
    }
    
    /**
     * 获取用户属性提供者
     * @return
     */
    public EaseUserProfileProvider getUserProfileProvider(){
        return userProvider;
    }
    
    public void setSettingsProvider(EaseSettingsProvider settingsProvider){
        this.settingsProvider = settingsProvider;
    }
    
    public EaseSettingsProvider getSettingsProvider(){
        return settingsProvider;
    }
    
    /**
     * check the application process name if process name is not qualified, then we think it is a service process and we will not init SDK
     * @param pID
     * @return
     */
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) appContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
           RunningAppProcessInfo info = (RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
            }
        }
        return processName;
    }
    
    /**
     * 用户属性提供者
     * @author wei
     *
     */
    public static interface EaseUserProfileProvider {
        EaseUser getUser(String username);
    }
    
    /**
     * 新消息提示设置的提供者
     *
     */
    public static interface EaseSettingsProvider {
        boolean isMsgNotifyAllowed(EMMessage message);
        boolean isMsgSoundAllowed(EMMessage message);
        boolean isMsgVibrateAllowed(EMMessage message);
        boolean isSpeakerOpened();
    }
    
    /**
     * default settings provider
     */
    protected class DefaultSettingsProvider implements EaseSettingsProvider{

        @Override
        public boolean isMsgNotifyAllowed(EMMessage message) {
         	return PreManager.instance().getIsOpenChatInform(appContext)==1;
        }

        @Override
        public boolean isMsgSoundAllowed(EMMessage message) {
            return PreManager.instance().getIsOpenChatInform(appContext)==1;
        }

        @Override
        public boolean isMsgVibrateAllowed(EMMessage message) {
            return PreManager.instance().getIsOpenChatInform(appContext)==1;
        }

        @Override
        public boolean isSpeakerOpened() {
            return PreManager.instance().getIsOpenChatInform(appContext)==1;
        }
    }
}
