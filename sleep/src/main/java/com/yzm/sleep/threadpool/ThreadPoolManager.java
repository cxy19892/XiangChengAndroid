package com.yzm.sleep.threadpool;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.yzm.sleep.background.SleepResult;
import com.yzm.sleep.threadpool.SoftDataUpLoadTask.ThreadEndInterface;
import com.yzm.sleep.utils.LogUtil;


public class ThreadPoolManager implements ThreadEndInterface{

	/** 线程池并发数*/
	private final int POOL_COUNT = 10;
	
	private ExecutorService threadPool;
	private List<Runnable> rList;
	private List<Runnable> rListTemp;
	
	private ThreadProgress threadProgress;
	
	private static ThreadPoolManager tpManager;
	
	private ThreadPoolManager(){
		threadPool = Executors.newFixedThreadPool(POOL_COUNT);
	}
	
	public static ThreadPoolManager initstance(){
		if(tpManager == null)
			tpManager = new ThreadPoolManager();
		return tpManager;
	}
	
	/**
	 *  添加任务到线程池
	 * @param list
	 * @return
	 */
	public ThreadPoolManager addThreadToPool(List<Runnable> list){
		rListTemp = list;
		rList = list;
		return tpManager;
	}
	
	/**
	 *  获取线程池的线程集合
	 * @return
	 */
	public List<Runnable> getThreadCollection(){
		return rList;
	}
	
	/**
	 * 开启线程池线程
	 */
	public synchronized void start(){
		if(rList == null)
			return ;
		if(threadProgress != null)
			threadProgress.threadStart(rList.size());
		for (Runnable runable : rList) {
			if(runable instanceof SoftDataUpLoadTask){
				((SoftDataUpLoadTask)runable).setThreadEndInterface(this);
			}
			if(!threadPool.isShutdown())
				threadPool.execute(runable);
		}
	}
	
	
	
	public void setOnThreadProgress(ThreadProgress threadProgress){
		this.threadProgress = threadProgress;
	}

	public synchronized void isSuccess(SleepResult name, boolean is) {
		if(rListTemp == null)
			return;
		for (int i = 0; i < rListTemp.size(); i++) {
			Runnable runable = rListTemp.get(i);
			if(runable instanceof SoftDataUpLoadTask){
				SleepResult taskName = ((SoftDataUpLoadTask)runable).getTaskName();
				if(taskName.getDate().equals(name.getDate()))
					rListTemp.remove(runable);
			}
		} 
		if(threadProgress != null){
			if(rListTemp.size() == 0){
				threadProgress.threadEnd();
				threadPool.shutdown();
				tpManager = null;
				threadPool = null;
				rList = null;
				rListTemp = null;
			}
		}
			
	}
}
