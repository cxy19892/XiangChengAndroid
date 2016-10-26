package com.yzm.sleep.render;

import java.io.Serializable;
import java.util.ArrayList;



public class GetSleepAvgTimeValueClass implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6418015623996415318L;
	
	public static class   GetAvgTimeParamItem{
		public int iSleepCountTime;
		public int iOutSleepTime;
		public int iInSleepTime;
		public int ibak1;
		public int ibak2;
		public GetAvgTimeParamItem get(int i) {
			// TODO Auto-generated method stub
			return null;
		}
	};

	public class GetAvgTimeHead{
		 int itemcount;
		 int ibak1;
		 int ibak2;	
		 };

	public class AvgTimeResult implements Serializable{ 
		 /**
		 * 
		 */
		private static final long serialVersionUID = -5653960185830621024L;
		
		public int iAvgSleepCountTime;   //暂时不管
		public int iAvgOutSleepTime;	  //平均醒来时间点（以秒为单位）
		public int iAvgInSleepTime;	  //平均入睡时间点（以秒为单位）
		public int ibak1;
		public int ibak2;
	};
	////////////////////	
		
		public  AvgTimeResult getAVG(ArrayList<GetAvgTimeParamItem> pdata)
		{
			

			int one_day=24*3600;
			int iSize= pdata.size();
			if(iSize == 0){
				return null;
			}
			
			GetAvgTimeParamItem mLPTimeItemfirst= pdata.get(0);
				
		
			long  Norm_InSleep = mLPTimeItemfirst.iInSleepTime % one_day;
			long  Norm_OutSleep = mLPTimeItemfirst.iOutSleepTime % one_day;
			long  Norm_Sleep = mLPTimeItemfirst.iSleepCountTime;
			long  Avg_InSleep=0,Avg_OutSleep=0,Avg_Sleep=Norm_Sleep;
			
				
				for (int i = 1; i < pdata.size(); i++)
				{
					GetAvgTimeParamItem mLPTimeItem = pdata.get(i);
					
					long  temp_InSleep,temp_OutSleep;
					long  add_InSleep,add_OutSleep;
					temp_InSleep = mLPTimeItem.iInSleepTime % one_day - Norm_InSleep;
					temp_OutSleep = mLPTimeItem.iOutSleepTime % one_day - Norm_OutSleep;
					
					if(temp_InSleep<0)
					{
						if(-temp_InSleep < temp_InSleep + one_day)
							add_InSleep = temp_InSleep;
						else 
							add_InSleep = temp_InSleep + one_day;
					}
					else
					{
						if(temp_InSleep < one_day - temp_InSleep)
							add_InSleep = temp_InSleep;
						else 
							add_InSleep = temp_InSleep - one_day;
					}

					if(temp_OutSleep<0)
					{
						if(-temp_OutSleep < temp_OutSleep + one_day)
							add_OutSleep = temp_OutSleep;
						else 
							add_OutSleep = temp_OutSleep + one_day;
					}
					else
					{
						if(temp_OutSleep < one_day - temp_OutSleep)
							add_OutSleep = temp_OutSleep;
						else 
							add_OutSleep = temp_OutSleep - one_day;
					}

					Avg_InSleep += add_InSleep;
					Avg_OutSleep += add_OutSleep;
					Avg_Sleep += mLPTimeItem.iSleepCountTime;
					
				}
				Avg_InSleep /= iSize;
				Avg_OutSleep /= iSize;
				Avg_Sleep /=iSize;
				
				AvgTimeResult mAvgResult =new AvgTimeResult();
				mAvgResult.iAvgInSleepTime = (int) (Avg_InSleep + mLPTimeItemfirst.iInSleepTime);
				mAvgResult.iAvgOutSleepTime = (int) (Avg_OutSleep + mLPTimeItemfirst.iOutSleepTime);
				mAvgResult.iAvgSleepCountTime = (int) Avg_Sleep;
				mAvgResult.ibak1=0;
				mAvgResult.ibak2=0;
				
				if(mAvgResult.iAvgInSleepTime<0)
					mAvgResult.iAvgInSleepTime+=one_day;
				if(mAvgResult.iAvgOutSleepTime<0)
					mAvgResult.iAvgOutSleepTime+=one_day;

				if(mAvgResult.iAvgInSleepTime>=one_day)
					mAvgResult.iAvgInSleepTime-=one_day;
				if(mAvgResult.iAvgOutSleepTime>=one_day)
					mAvgResult.iAvgOutSleepTime-=one_day;					
				
				
				
			return  mAvgResult;
		}


}
