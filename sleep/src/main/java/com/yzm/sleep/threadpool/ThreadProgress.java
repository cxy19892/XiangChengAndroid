package com.yzm.sleep.threadpool;

public interface ThreadProgress {

	/**
	 *  线程开始
	 * @param poolCunt 线程的数量
	 */
	public void threadStart(int poolCunt);
	
	/**
	 * 线程结束
	 * @param successCount  成功的数量
	 * @param failedCount   失败的数量
	 */
	public void threadEnd();
	
	
}
