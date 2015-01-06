package com.notice.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对日期进行处理的工具类
 * 
 * @author luojian
 */
public class DateUtils
{
	/*
	 * pattern 日期的格式，
	 * 如：yyyy/MM/dd yyyy-MM-dd  (年/月/日)
	 * yyyy-MM-dd HH:mm:ss (年/月/日 小时:分:秒 24小时)
	 * yyyy-MM-dd hh:mm:ss (年/月/日 小时:分:秒 12小时)
	 */
	public static java.util.Date stringToDate(String strDate, String pattern){
		if (strDate == null || strDate.trim().length() <= 0)
			return null;

		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			return null;
		}

	}
	/*
	 * 不指定格式的转换函数
	 */
	public static java.util.Date stringToDate(String strDate){
		if (strDate == null) {
			return null;
		} else if(strDate.trim().length() <= 0) {
			return null;
		}
		boolean isPass = CheckUtils.checkDateFormat(strDate.trim());
		if(!isPass) {
			return null;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			return null;
		}

	}	
	public static boolean checkDate(String strDate){
		if(strDate == null || strDate.length() == 0)
			return true;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdf.parse(strDate);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	public static String dateToString(java.util.Date date, String pattern)
	{
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
		return sdf.format(date);
	}

	public static String dateToString(java.util.Date date)
	{
		if (date == null)
			return "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static Date dateAddHours(Date date, int addHours)
	{
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, addHours);
		return cal.getTime();
	}

	public static Date dateAddDays(Date date, int addDays)
	{
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, addDays);
		return cal.getTime();
	}

	public static Date dateAddMonths(Date date, int addMonths)
	{
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, addMonths);
		return cal.getTime();
	}
	public static Date dateAddWeeks(Date date, int addWeeks) {
		if(date == null) return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.WEEK_OF_YEAR, addWeeks);
		return cal.getTime();
	}

	public static Date dateAddYears(Date date, int addYears)
	{
		if (date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, addYears);
		return cal.getTime();
	}
	
	public static int getDateLength(Date beginDate, Date endDate){
		int length = 0;
		if(beginDate == null || endDate == null) return length;
		
		length = (int) (( endDate.getTime() -  beginDate.getTime()) / (1000*60*60*24)); 
		length++;
		return length;
	}
	
	public static int getMonthLength(Date beginDate, Date endDate){
	        int length = 0; 
	        if(beginDate.after(endDate)){
	        	Date tmp = (Date)beginDate.clone();
	        	beginDate = (Date) endDate.clone();
	        	endDate = (Date) tmp.clone();
	        }
	   
	        Calendar c1 = Calendar.getInstance();
	        c1.setTime(beginDate);
	        c1.clear(Calendar.MILLISECOND);    
	        c1.clear(Calendar.SECOND);    
	        c1.clear(Calendar.MINUTE);    
	        c1.clear(Calendar.HOUR_OF_DAY);    
	        c1.clear(Calendar.DATE);    
	   
	        Calendar c2 = Calendar.getInstance();
	        c2.setTime(endDate);
	        c2.clear(Calendar.MILLISECOND);    
	        c2.clear(Calendar.SECOND);    
	        c2.clear(Calendar.MINUTE);    
	        c2.clear(Calendar.HOUR_OF_DAY);    
	        c2.clear(Calendar.DATE);    
	   
	        return (12 * (c2.get(Calendar.YEAR) - c1.get(Calendar.YEAR)) + c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH));    
	}
	
	public static String getDateBegin(String strDateSegment)
	{
		if (strDateSegment == null || strDateSegment.length() <= 0)
			return null;
		return strDateSegment + " 00:00:00";
	}

	public static String getDateEnd(String strDateSegment)
	{
		if (strDateSegment == null || strDateSegment.length() <= 0)
			return null;
		return strDateSegment + " 23:59:59";
	}
	
	//检查录入的日期格式（yyyyMMdd-yyyyMMdd或yyyyMMdd）
	public static boolean checkLongDatePattern(String strDate){ 
        String ps = "^\\d{4}\\d{1,2}\\d{1,2}-\\d{4}\\d{1,2}\\d{1,2}$|^\\d{4}\\d{1,2}\\d{1,2}$";
        Pattern   p   =   Pattern.compile(ps);
        Matcher   m   =   p.matcher(strDate);   
        if(!m.matches()){
        	return false;
        }
        return true;
	}
	
	//检查录入的日期格式（yyyy-MM-dd）
	public static boolean checkDatePattern(String strDate){ 
        String ps = "^\\d{4}-\\d{1,2}-\\d{1,2}$";
        Pattern   p   =   Pattern.compile(ps);
        Matcher   m   =   p.matcher(strDate);   
        if(!m.matches()){
        	return false;
        }
        return true;
	}
	
	/* 检查和格式化日期
	 * param:strDate 如:2007-06-01--2007-12-01,2007-06-18,20070601--20071201,20070718 
	 * return:HashMap 格式化以后的date对象：beginDate和endDate 
	 * throws ParseException 输入的日期格式错误
	 */
	public static List transformStrDateToListDate(String strDate) throws ParseException{
		List dateList = new ArrayList();
		String[] tempStrDate = strDate.split(",");
		for(int i=0;i<tempStrDate.length;i++){
			//将字符串格式化为“yyyy-MM-dd--yyyy-MM-dd”
			Date tempDate = stringToDate(tempStrDate[i],"yyyy-MM-dd");
			dateList.add(tempDate);
		}
		
		return dateList;
	}
	
	/* 检查和格式化日期
	 * param:strDate 如:2007-06-01--2007-12-01,2007-06-18,20070601--20071201,20070718 
	 * return:HashMap 格式化以后String型的日期：beginDate和endDate 
	 * throws ParseException 输入的日期格式错误
	 */
	public static HashMap formatStrDateToDateMap(String strDate) throws ParseException{
		HashMap dateMap = new HashMap();
		
		Date beginDate = null;
		Date endDate = null;
		if(strDate == null || strDate.trim().length() == 0) return null;
		
		if(strDate.indexOf("--") > 0){
			String[] tempDate = strDate.split("--");
			if(tempDate.length == 2){
				if(tempDate[0].indexOf("-") > 0){
					beginDate = stringToDateThrowsExe(tempDate[0],"yyyy-MM-dd");
				}else{
					beginDate = stringToDateThrowsExe(tempDate[0],"yyyyMMdd");
				}
				if(tempDate[1].indexOf("-") > 0) {
					endDate = stringToDateThrowsExe(tempDate[1],"yyyy-MM-dd");
				}else {
					endDate = stringToDateThrowsExe(tempDate[1],"yyyyMMdd");
				}
			}else if(tempDate.length == 1){
				if(tempDate[0].indexOf("-") > 0){
					beginDate = stringToDateThrowsExe(tempDate[0],"yyyy-MM-dd");
					endDate = stringToDateThrowsExe(tempDate[0],"yyyy-MM-dd");
				}else{
					beginDate = stringToDateThrowsExe(tempDate[0],"yyyyMMdd");
					endDate = stringToDateThrowsExe(tempDate[0],"yyyyMMdd");
				}
			}
		}else{
			if(strDate.indexOf("-") > 0) {
				beginDate = stringToDateThrowsExe(strDate,"yyyy-MM-dd");
				endDate = stringToDateThrowsExe(strDate,"yyyy-MM-dd");
			}else{
				beginDate = stringToDateThrowsExe(strDate,"yyyyMMdd");
				endDate = stringToDateThrowsExe(strDate,"yyyyMMdd");
			}
		}
		dateMap.put("beginDate",dateToString(beginDate,"yyyy-MM-dd"));
		dateMap.put("endDate",dateToString(endDate,"yyyy-MM-dd"));
		return dateMap;
	}

	private static java.util.Date stringToDateThrowsExe(String strDate, String pattern) throws ParseException{
		if (strDate == null || strDate.trim().length() <= 0)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(strDate);
	}
	/* 检查日期的先后顺序
	 * param:beginDate endDate
	 * return:boolean boolean beginDate<=endDate 返回true；beginDate>endDate返回false
	 */
	public static boolean compareDate(String beginDate,String endDate){
		if(endDate == null || endDate.trim().length() == 0)
			endDate = beginDate;
		
		Date bDate = stringToDate(beginDate,"yyyy-MM-dd");
		Date eDate = stringToDate(endDate,"yyyy-MM-dd");
		
		if(bDate == null || eDate == null ) return true;
		if(!bDate.after(eDate)) return true;
		return false;
	}
	
	public static boolean comparePeriod(String beginDate,String endDate){
		if(endDate == null || endDate.trim().length() == 0)
			endDate = beginDate;
		
		Date bDate = stringToDate(beginDate,"MMM-yy");
		Date eDate = stringToDate(endDate,"MMM-yy");
		
		if(bDate == null || eDate == null ) return true;
		if(!bDate.after(eDate)) return true;
		return false;
	}
	
	public static Date getFirstDateOfMonth(Date date) {
		if (date == null)
			return null;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		return c1.getTime();
	}
	
	public static Date getLastDateOfMonth(Date date) {
		if (date == null)
			return null;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		c1.set(Calendar.MONTH, c1.get(Calendar.MONTH) + 1);
		c1.set(Calendar.DAY_OF_MONTH, 1);
		c1.set(Calendar.HOUR_OF_DAY, 0);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);
		
		c1.add(Calendar.MILLISECOND, -1);

		return c1.getTime();
	}
	
}
