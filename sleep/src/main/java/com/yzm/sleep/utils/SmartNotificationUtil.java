package com.yzm.sleep.utils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.yzm.sleep.model.SmartRemindBean;


public class SmartNotificationUtil {
	
	/**
	 * 
	 * @param remindState 提醒模式 
	 *  0-->午间提醒;
	 *  1 -->睡前提醒
	 * @param endTime     起床时间
	 * @param startTime   睡觉时间
	 * @param suggestSleepTime  设定的睡觉时间 T10
	 * @param T1          T1
	 * @return
	 */
	public static SmartRemindBean GetSmartNotifications(int remindState, String endTime, String startTime, String suggestSleepTime){
		
		float sleepLength = getIntervalHour(endTime, startTime);
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		try {
		
		Date TIME_1 = sdf.parse(GetT1str(suggestSleepTime));//
		
		Date TIME_10= sdf.parse(suggestSleepTime);//
		Date End_time= sdf.parse(endTime);//
		Date Start_time= sdf.parse(startTime);//
		Date clock_1_time= sdf.parse("01:00");//
		Date clock_2_time= sdf.parse("02:00");//
		Date clock_12_time= sdf.parse("12:00");//
		Date clock_23_time= sdf.parse("23:00");//
		
		if(isSleepInGivenTime(TIME_1, clock_23_time, Start_time) && sleepLength >= 8){//T1<=xx<23:00  yy>8
			return getSmartRemind(remindState, 11, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}else if(isSleepInGivenTime(TIME_1, clock_23_time, Start_time) && sleepLength < 8){//T1<=xx<23:00 yy<8
			return getSmartRemind(remindState, 12, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}else if(isSleepInGivenTime(clock_23_time, clock_1_time, Start_time) && sleepLength > 8){//23<=xx<1:00  yy>8
			return getSmartRemind(remindState, 21, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_23_time, clock_1_time, Start_time)&& sleepLength > 8){//23<=xx<1:00   yy>8
			return getSmartRemind(remindState, 22, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_23_time, clock_1_time, Start_time)&& (sleepLength <= 8 && sleepLength>=6)){//23<=xx<1:00  6<=yy<8
			return getSmartRemind(remindState, 31, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_23_time, clock_1_time, Start_time) && (sleepLength <= 8 && sleepLength>=6)){//23<=xx<1:00  6<=yy<8
			return getSmartRemind(remindState, 32, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_23_time, clock_1_time, Start_time)&& (sleepLength < 6 && sleepLength>=0)){//23<=xx<1:00   0<=yy<6
			return getSmartRemind(remindState, 41, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_23_time, clock_1_time, Start_time)&& (sleepLength < 6 && sleepLength>=0)){//23<=xx<1:00   0<=yy<6
			return getSmartRemind(remindState, 42, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_1_time, clock_2_time, Start_time) && sleepLength >= 8){//1:00<=xx<2:00   yy>8
			return getSmartRemind(remindState, 51, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_1_time, clock_2_time, Start_time) && sleepLength >= 8){//1:00<=xx<2:00   yy>8
			return getSmartRemind(remindState, 52, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_1_time, clock_2_time, Start_time) && (sleepLength <= 8 && sleepLength>=6)){//1:00<=xx<2:00   6<=yy<8
			return getSmartRemind(remindState, 61, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_1_time, clock_2_time, Start_time) && (sleepLength <= 8 && sleepLength>=6)){//1:00<=xx<2:00   6<=yy<8
			return getSmartRemind(remindState, 62, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_1_time, clock_2_time, Start_time) && (sleepLength < 6 && sleepLength>=0)){//1:00<=xx<2:00   0<=yy<6
			return getSmartRemind(remindState, 71, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(clock_1_time, clock_2_time, Start_time) && (sleepLength < 6 && sleepLength>=0)){//1:00<=xx<2:00   0<=yy<6
			return getSmartRemind(remindState, 72, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(TIME_1, clock_23_time, Start_time) && (sleepLength <= 8 && sleepLength>=6)){//T1<=xx<23:00     6<=yy<8
			return getSmartRemind(remindState, 81, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(TIME_1, clock_23_time, Start_time) && (sleepLength <= 8 && sleepLength>=6)){//T1<=xx<23:00   6<=yy<8
			return getSmartRemind(remindState, 82, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		else if(isSleepInGivenTime(TIME_1, clock_23_time, Start_time) && (sleepLength < 6 && sleepLength>=0)){//T1<=xx<23:00    0<=yy<6
			return getSmartRemind(remindState, 91, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		} 
		else if(isSleepInGivenTime(TIME_1, clock_23_time, Start_time) && (sleepLength < 6 && sleepLength>=0)){//T1<=xx<23:00    0<=yy<6
			return getSmartRemind(remindState, 92, startTime, endTime, sleepLength, End_time, TIME_10, clock_12_time);
		}
		//……………………
		
		} catch (Exception e) {
			return null;
		}
		return null;
	}
	
	/**
	 * 判断睡觉时间是否在给定的时间区间内
	 * @param starttime 给定的起始时间
	 * @param StopTime  给定的终止时间
	 * @param fallsleeptime 实际睡觉时间
	 * @return
	 */
	private static boolean isSleepInGivenTime(Date starttime, Date StopTime, Date fallsleeptime){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		Date clock_12_time;
		try {
			clock_12_time = sdf.parse("12:00");
			if((StopTime.getTime()) < (clock_12_time.getTime())){										  //结束时间 0:00-11:59
				if((fallsleeptime.getTime()) < (clock_12_time.getTime())){//                睡觉时间 0:00-11:59  结束时间 0:00-11:59
					if((starttime.getTime()) < (clock_12_time.getTime())){//开始时间 0:00-11:59  睡觉时间 0:00-11:59 结束时间 0:00-11:59
						if(fallsleeptime.getTime()<0){ //睡觉时间在8点之前 
							if(starttime.getTime()<0){
								if(fallsleeptime.getTime()>starttime.getTime()){// fallsleeptime < starttime.getTime()
									return true;
								}else{
									return false;
								}
							}else{
								return false;
							}
						}else{
							if(starttime.getTime()<0 && StopTime.getTime()>fallsleeptime.getTime()){
								return true;
							}else{
								return false;
							}
						}

					}else{												//开始时间12:00-23:59  睡觉时间 0:00-11:59 结束时间 0:00-11:59
						if((fallsleeptime.getTime()) <= (StopTime.getTime())){
							return true;
						}else{
							return false;
						}
					}
				}else{//睡觉时间12:00-23:59 结束时间0:00-11:59
					if((starttime.getTime()) <= (clock_12_time.getTime())){//开始时间 0:00-11:59
						return false;
					}else{//开始时间12:00-23:59
						if((starttime.getTime()) <= (fallsleeptime.getTime())){
							return true;
						}else{
							return false;
						}
					}
				}
				
			}else{//结束时间 12:00-23:59
				if((fallsleeptime.getTime()) < (clock_12_time.getTime())){//睡觉时间 0:00-11:59  结束时间  12:00-23:59
					if((starttime.getTime()) < (clock_12_time.getTime())){//睡觉时间 0:00-11:59  结束时间  12:00-23:59 开始时间  0:00-11:59
						if(fallsleeptime.getTime()<0){ //睡觉时间在8点之前 
							if(starttime.getTime()<0){
								if(fallsleeptime.getTime()>starttime.getTime()){// fallsleeptime < starttime.getTime()
									return true;
								}else{
									return false;
								}
							}else{
								return false;
							}
						}else{
							if(starttime.getTime()<0 && StopTime.getTime()>fallsleeptime.getTime()){
								return true;
							}else{
								return false;
							}
						}
						
					}else{//睡觉时间 0:00-11:59  结束时间  12:00-23:59 开始时间  12:00-23:59
						return false;
					}
					
				}else{												  //睡觉时间  12:00-23:59  结束时间  12:00-23:59
					if((starttime.getTime()) <= (clock_12_time.getTime())){//睡觉时间   0:00-11:59  结束时间  12:00-23:59 开始时间  0:00-11:59
						if((fallsleeptime.getTime()) <= (StopTime.getTime())){
							return true;
						}else{
							return false;
						}
					}else{											  //睡觉时间  12:00-23:59  结束时间  12:00-23:59 开始时间  12:00-23:59
						if((fallsleeptime.getTime()) >= (starttime.getTime()) && (fallsleeptime.getTime()) <= (StopTime.getTime())){
							return true;
						}else{
							return false;
						}
					}
				}
			}
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}//
		
	}
	
	
	
	private static String getRemindMsg(int Remindint, String startTime, float sleepLength){
		String Remindmessage = "";
		switch(Remindint){
		case 11:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，总体睡眠质量不错，小橙子建议保持哦。";
			break;
		case 12:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，总体睡眠质量不错，小橙子建议现在就开始准备休息呐。";
			break;
		case 21:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡觉时间太晚了，小橙子建议今晚23点 前睡觉哦。";
			break;
		case 22:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡觉时间太晚了，小橙子建议现在请开始睡觉哦。";
			break;
		case 31:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡觉晚睡眠少，小橙子建议今晚22:30 点前睡觉。";
			break;
		case 32:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡觉晚睡眠少，小橙子建议马上睡觉去。";
			break;
		case 41:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡觉晚睡眠糟糕，为了拥有更好的精力，请在22点前睡觉哦。";
			break;
		case 42:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡眠时间偏少，为了拥有更好的精力， 请马上开始睡觉哦。";
			break;
		case 51:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，晚睡又晚起，小橙子建议您养成良好的 睡眠习惯，今晚请在23点前睡觉吧。";
			break;
		case 52:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，晚睡又晚起，小橙子建议您养成良好的 睡眠习惯，马上睡觉去。";
			break;
		case 61:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡的晚觉不够，为了您的健康，今夜请23:00前睡觉哦。";
			break;
		case 62:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡的晚觉不够，为了您的健康，今夜请立即睡觉去。";
			break;
		case 71:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，您可能患有轻度失眠的症状，请放松心 态，在每天22点前上床休息。";
			break;
		case 72:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，您可能患有轻度失眠的症状，请放松心 态，马上上床休息。";		
			break;
		case 81:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡眠时间偏少，建议每天23点前上床就 寝。";
			break;
		case 82:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，睡眠时间偏少，建议立即上床就寝。";
			break;
		case 91:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，您可能患有轻度失眠的症状，请放松心 态，适当运动，保持愉快的睡眠状态，如身体不适建议就医。";
			break;
		case 92:
			Remindmessage = "昨夜"+startTime+"点入睡，一觉睡了"+sleepLength+"小时，您可能患有轻度失眠的症状，请放松心 态，适当运动，立即准备就寝，如身体不适建议就医。";
			break;
			default:
				break;
		}
		return Remindmessage;
		
	}
	
	
	public static SmartRemindBean getSmartRemind(int remindState, int remindtypes, String startTime, String endTime, float sleepLength, Date End_time, Date TIME_10, Date clock_12_time) throws ParseException{
		SmartRemindBean mSmartRemind = new SmartRemindBean();
		if(remindState == 0){//午间提醒
			mSmartRemind.Remindmsgs = getRemindMsg(remindtypes, startTime, sleepLength);
			if(End_time.getTime() > clock_12_time.getTime()){
				mSmartRemind.SuggestRemindTime = endTime;
			}else{
				mSmartRemind.SuggestRemindTime = "12:00";
			}
		}else{//睡前提醒
			mSmartRemind.Remindmsgs = getRemindMsg(remindtypes, startTime, sleepLength);
			if(remindtypes == 12 || remindtypes == 22 ||remindtypes ==82){
				mSmartRemind.SuggestRemindTime = longToString(TIME_10.getTime() - (1000*60*60), "HH:mm");//(1000*60*30)
			}else{
				mSmartRemind.SuggestRemindTime = longToString(TIME_10.getTime() - (1000*60*60), "HH:mm");
			}
			
		}
		return mSmartRemind;
	}
	
	
	  /**
     *   获取间隔时长
     * @param endTime   结束时间
     * @param startTime	开始时间
     * @return
     */

	private static float getIntervalHour(String endTime, String startTime){
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
			Date d1 = sdf.parse(endTime);
			Date d2 = sdf.parse(startTime);
			float hour = (float)(d1.getTime() - d2.getTime())/(1000 * 60 * 60);
			DecimalFormat decimalFormat = new DecimalFormat(".#"); 
			hour =Float.parseFloat(decimalFormat.format(hour)) ;
			if(hour < 0)
				hour += 24;
			return hour;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return 0;
	}

	private static String GetT1str(String T10){
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			long t1 = sdf.parse(T10).getTime() + sdf.parse("7:00").getTime();
			return longToString(t1, "HH:mm");
		} catch (Exception e) {
			return "";
		}
	}
	
	public static String longToString(long currentTime, String formatType)
 			throws ParseException {
 		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
 		String strTime = dateToString(date, formatType); // date类型转成String
 		return strTime;
 	}
	
	// long转换为Date类型
	 	// currentTime要转换的long类型的时间
	 	
	 	public static Date longToDate(long currentTime, String formatType)
	 			throws ParseException {
	 		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
	 		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
	 		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
	 		return date;
	 	}
	
	 	public static String dateToString(Date data, String formatType) {
	 		return new SimpleDateFormat(formatType).format(data);
	 	}
	 	
	 	public static Date stringToDate(String strTime, String formatType)
	 			throws ParseException {
	 		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
	 		Date date = null;
	 		date = formatter.parse(strTime);
	 		return date;
	 	}
}
