package com.yzm.sleep.webimage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片下载线程管理
 * 
 * @author zhaomeng
 *
 */
public class ThreadPoolFactory {
	private static ThreadPoolFactory ourInstance = new ThreadPoolFactory();

	public static ThreadPoolFactory getInstance() {
		return ourInstance;
	}

	private ExecutorService threadPool;

	private ThreadPoolFactory() {
		threadPool = Executors.newCachedThreadPool();
	}

	/**
	 * 提交给线程池执行
	 * 
	 * @param run
	 */
	public void execute(Runnable run) {
		threadPool.submit(run);
	}
}
