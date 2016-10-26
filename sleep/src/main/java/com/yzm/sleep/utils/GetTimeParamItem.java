package com.yzm.sleep.utils;

import java.io.Serializable;
import java.util.ArrayList;

public class GetTimeParamItem {
	public long iBelongDate; //所属日期  年月日 的时间戳  单位毫秒
	public long iInSleepDate;//入睡日期   年月日 的 时间戳 单位毫秒
	public long iInSleepTime; //入睡日期   时分秒的   时间戳  单位毫秒
	public long iOutSleepDate;  //起床日期   年月日 的 时间戳  单位毫秒
	public long iOutSleepTime;  //起床日期   时分秒的   时间戳  单位毫秒
	public long ibak1;// 备份字段1
	public long ibak2;// 备份字段2
	

	public static class   GetTimeResult implements Serializable{
		public long iInSleepTime_early;//最早起床  时分秒的   时间戳  单位毫秒
		public long iInSleepTime_last; //入睡日期   时分秒的   时间戳  单位毫秒
		public long iOutSleepTime_early;  //起床日期   年月日 的 时间戳  单位毫秒
		public long iOutSleepTime_last;  //起床日期   时分秒的   时间戳 单位毫秒
		public int ibak1;// 备份字段1
		public int ibak2;// 备份字段2
	};
	
	public static  GetTimeResult ProcTimeData(ArrayList<GetTimeParamItem> pdata)
	{
		int iSize =  pdata.size();
		
		int TotalTime = 24*3600*1000 ; 
		
		long iInSleepTime_early=86400000;		
		 
		long iInSleepTime_last=0; 	
		 
		long iOutSleepTime_early=86400000;  		
		 
		long iOutSleepTime_last=0;  
			
		if(iSize==1)
		{
			
			GetTimeParamItem dataGetTimeParamItem =  pdata.get(0);	
			
			iInSleepTime_early = dataGetTimeParamItem.iInSleepTime  ;// iInSleepTime_early<=dataGetTimeParamItem.iInSleepTime?iInSleepTime_early:dataGetTimeParamItem.iInSleepTime;  //取最小的
			
			iInSleepTime_last =dataGetTimeParamItem.iInSleepTime ;//iInSleepTime_last>=dataGetTimeParamItem.iInSleepTime?iInSleepTime_last:dataGetTimeParamItem.iInSleepTime;  //取最大的		
			
			 iOutSleepTime_early=dataGetTimeParamItem.iOutSleepTime ;//iOutSleepTime_early<=dataGetTimeParamItem.iOutSleepTime?iOutSleepTime_early:dataGetTimeParamItem.iOutSleepTime;  //取最小的		 
			
			 iOutSleepTime_last=dataGetTimeParamItem.iOutSleepTime ;;// iOutSleepTime_last>=dataGetTimeParamItem.iOutSleepTime?iOutSleepTime_last:dataGetTimeParamItem.iOutSleepTime;  //取最小的
						
		}else if(iSize>1) {
			
			
			for (int i = 0; i < iSize; i++) 
			{
				
				GetTimeParamItem dataGetTimeParamItem =  pdata.get(i);	
				
				
				if(dataGetTimeParamItem.iBelongDate != dataGetTimeParamItem.iInSleepDate)
					dataGetTimeParamItem.iInSleepTime+=TotalTime;
				
				if(dataGetTimeParamItem.iBelongDate == dataGetTimeParamItem.iOutSleepDate)
					dataGetTimeParamItem.iOutSleepTime-=TotalTime;
				
				iInSleepTime_early = iInSleepTime_early<=dataGetTimeParamItem.iInSleepTime?iInSleepTime_early:dataGetTimeParamItem.iInSleepTime;  //取最小的
				
				iInSleepTime_last = iInSleepTime_last>=dataGetTimeParamItem.iInSleepTime?iInSleepTime_last:dataGetTimeParamItem.iInSleepTime;  //取最大的		
				
				 iOutSleepTime_early= iOutSleepTime_early<=dataGetTimeParamItem.iOutSleepTime?iOutSleepTime_early:dataGetTimeParamItem.iOutSleepTime;  //取最小的		 
				
				 iOutSleepTime_last= iOutSleepTime_last>=dataGetTimeParamItem.iOutSleepTime?iOutSleepTime_last:dataGetTimeParamItem.iOutSleepTime;  //取最小的
				
			}
		}else {
			
			 iInSleepTime_early=0;		
			 
			 iInSleepTime_last=0; 	
			 
			 iOutSleepTime_early=0;  		
			 
			 iOutSleepTime_last=0;  
		}
		
	
		
		//把修改的时间还原回去
		if(iInSleepTime_last>=TotalTime)			
				iInSleepTime_last-=TotalTime;
		
		if(iInSleepTime_early>=TotalTime)
			iInSleepTime_early-=TotalTime;
	
		if(iOutSleepTime_last<0)			
			iOutSleepTime_last+=TotalTime;
	
		
		if(iOutSleepTime_early<0)
			iOutSleepTime_early+=TotalTime;
		
		
		GetTimeResult dataGetTimeResult= new GetTimeResult();
//		long t=0;
//		try {
//			 t=new SimpleDateFormat("yyyy-MM-dd").parse("2015-01-01").getTime();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		dataGetTimeResult.iInSleepTime_early=iInSleepTime_early;
		dataGetTimeResult.iInSleepTime_last=iInSleepTime_last;
		dataGetTimeResult.iOutSleepTime_early=iOutSleepTime_early;
		dataGetTimeResult.iOutSleepTime_last=iOutSleepTime_last;
		
		return dataGetTimeResult;
	}
}
