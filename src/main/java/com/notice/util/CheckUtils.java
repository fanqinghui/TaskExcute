package com.notice.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {
	
	/**
	 * 判断传入的String是否为空
	 * @param param
	 * @return
	 */
	public static boolean checkStringNotNull(final String param) {
		boolean check = false;
		if(param != null && !"".equals(param.trim())) {
			check = true;
		}
		return check;
	}

	public static boolean checkStringNull(final String param) {
		boolean check = false;
		if(param == null) {
			check = true;
		} else if("".equals(param.trim())) {
			check = true;
		}
		return check;
	}
	
	public static boolean checkStringArrayNotNull(final String[] param){
		return (param == null || (param.length <= 0)) ? false : true;
	}

	public static boolean checkStringArrayNull(final String[] param){
		return (param == null || (param.length <= 0)) ? true : false;
	}

	/**
	 * 判断传入的Long是否为空
	 * @param param
	 * @return
	 */
	public static boolean checkLongNotNull(final Long param){
		return (param == null || param.longValue() == 0) ? false : true;
	}

	public static boolean checkLongNull(final BigDecimal bigDecimal){
		return (bigDecimal == null || bigDecimal.longValue() == 0) ? true : false;
	}

	/**
	 * 判断传入的Long是否为空
	 * @param param
	 * @return
	 */
	public static boolean checkDoubleNotNull(final Double param){
		return (param == null || param.doubleValue() == 0) ? false : true;
	}

	public static boolean checkDoubleNull(final Double param){
		return (param == null || param.doubleValue() == 0) ? true : false;
	}

	/**
	 * 判断传入的Integer是否为空
	 * @param param
	 * @return
	 */
	public static boolean checkIntegerNotNull(Integer param) {
		return (param == null || param.intValue() == 0) ? false : true;
	}
	public static boolean checkIntegerNull(Integer param) {
		return (param == null || param.intValue() == 0) ? true : false ;
	}

	/**
	 * 判断传入的Date是否为空
	 * @param param
	 * @return
	 */
	public static boolean checkDateNotNull(Date param) {
		return (param == null) ? false : true;
	}
	public static boolean checkDateNull(Date param) {
		return (param == null) ? true : false ;
	}

	public static boolean checkSegmentsValueString(String source){
		boolean checkPass = false;
		
		if(CheckUtils.checkStringNull(source))
			return false;
		else if (source.indexOf("*")>0)
			return false;
		else {
			StringTokenizer segmentsValue = new StringTokenizer(".");
			String segment = null;
			int index = 0;
			while(segmentsValue.hasMoreTokens()){
				segment = segmentsValue.nextToken();
				index++;
			}
			if(index<0 || index > 9)
				return false;
		}
			
		checkPass = true;
		return checkPass;
	}
	
	/**
	 * 判断传入的Email地址是否为空或是否有效
	 * @Title: checkEmailAddress 判断传入的Email地址是否为空或是否有效
	 * @Description: TODO 判断传入的Email地址是否为空或是否有效
	 * @param param
	 * @return boolean
	 * @throws
	 */
	public static boolean checkEmailAddress(String param) {

		// 判断是否为空
		if (CheckUtils.checkStringNull(param))
			return false;
		// 判断是否合法
		String check = "^([a-z0-9A-Z]+[-|\\._]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		//String check = "\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*";
		//String check = "^([0-9a-zA-Z]([-.\w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-\w]*[0-9a-zA-Z]\.)+[a-zA-Z]{2,9})$";
		Pattern regex = Pattern.compile(check);
		Matcher matcher = regex.matcher(param);
		boolean isMatched = matcher.matches();
		if (!isMatched)
			return false;

		// 判断是否有效
//		try {
//			String[] mail = param.split("@");
//			InetAddress IP = InetAddress.getByName(mail[1]);
//		} catch (UnknownHostException e) {
//			return false;
//		}

		return true;
	}
	
	/**
	 * 判断传入的日期、时间是否为空或是否有效
	 * @Title: checkDateFormat 判断传入的日期、时间是否为空或是否有效
	 * @Description: TODO 判断传入的日期、时间是否为空或是否有效
	 * @param param
	 * @return
	 * @return boolean
	 * @throws
	 */
	public static boolean checkDateFormat(String param) {
		// 判断是否为空
		if (CheckUtils.checkStringNull(param)) {
			return false;
		}
		// 判断是否合法
		Pattern pattern = Pattern.compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?(\\.\\d{1,6})?$");
		Matcher matcher = pattern.matcher(param);// "2000-02-29 23:59:59");
		boolean isMatched = matcher.matches();
		if (!isMatched) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查手机是否符合
	 * @Title: checkPhone 检查手机是否符合
	 * @Description: TODO 检查手机是否符合
	 * @param param
	 * @return boolean
	 * @throws
	 */
	public static boolean checkPhone(String param) {
		// 判断是否为空
		if (CheckUtils.checkStringNull(param)) {
			return false;
		}
		// 判断是否合法
		Pattern pattern = Pattern.compile("^((\\+86)|(86))?(((13[0-9])|147|(15[^4,\\D])|(18[0,2,5-9]))\\d{8})$");
		Matcher matcher = pattern.matcher(param);
		boolean isMatched = matcher.matches();
		if (!isMatched) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查电话号码是否符合
	 * @Title: checkTel 检查电话号码是否符合
	 * @Description: TODO 检查电话号码是否符合
	 * @param param
	 * @return boolean
	 * @throws
	 */
	public static boolean checkTel(String param) {
		// 判断是否为空
		if (CheckUtils.checkStringNull(param)) {
			return false;
		}
		// 判断是否合法
		Pattern pattern = Pattern.compile("^((\\d{3}[-_ ]{0,1}\\d{8})||(\\d{8})||(\\d{4}[-_ ]{0,1}\\d{7}))$");
		Matcher matcher = pattern.matcher(param);
		boolean isMatched = matcher.matches();
		if (!isMatched) {
			return false;
		}
		return true;
	}
	
	/**
	 * 检查电话号码是否符合
	 * @Title: checkDouble 检查电话号码是否符合
	 * @Description: TODO 检查电话号码是否符合
	 * @param param
	 * @return boolean
	 * @throws
	 */
	public static boolean checkDouble(String param) {
		// 判断是否为空
		if (CheckUtils.checkStringNull(param)) {
			return false;
		}
		// 判断是否合法
		Pattern pattern = Pattern.compile("^(-?\\d+)(\\.\\d+)?$");
		Matcher matcher = pattern.matcher(param);
		boolean isMatched = matcher.matches();
		if (!isMatched) {
			return false;
		}
		return true;
	}
	
	public static boolean checkLong(String param) {
		// 判断是否为空
		if (CheckUtils.checkStringNull(param)) {
			return false;
		}
		// 判断是否合法
		Pattern pattern = Pattern.compile("^\\d+$");
		Matcher matcher = pattern.matcher(param);
		boolean isMatched = matcher.matches();
		if (!isMatched) {
			return false;
		}
		return true;
	}
	
	public static boolean checkObjectNotNull(final Object param){
		return (param == null ) ? false : true;
	}

	public static boolean checkObjectNull(final Object param){
		return (param == null ) ? true : false;
	}
	
	public static void main(String[] args){
		//boolean isPass = CheckUtils.checkEmailAddress("aimee.chen@sohu-inc.com");
		//System.out.println(isPass);
		
		boolean isPass = CheckUtils.checkDateFormat("2012-10-28 12:00:00.0");
		
		//boolean isPass = CheckUtils.checkPhone("8618601121437");
		
		//boolean isPass = CheckUtils.checkTel(" 12345678");
		
		//boolean isPass = CheckUtils.checkDouble("-1234.5678");
		
		//boolean isPass = CheckUtils.checkLong("000012345678");
		System.out.println(isPass);
	}

}
