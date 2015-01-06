package com.notice.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class StringUtils {
	public static String toSpace(String var) {
		if (var == null || var.trim().equals("")) {
			return "&nbsp;";
		}

		return var;
	}

	public static String toEmpty(String var) {
		if (var == null) {
			return "";
		}
		return var;
	}
    
    /**
     * 字符串转换成Long
     * @Title: stringToLong 字符串转换成Long
     * @Description: TODO 字符串转换成Long
     * @param str
     * @return Long
     * @throws
     */
    public static Long stringToLong(String str) {
		if (CheckUtils.checkStringNull(str)) {
			return null;
		}
		str = str.replaceAll(",", "");
		str = str.replaceAll("，", "");
		boolean isPass = CheckUtils.checkLong(str);
		if(!isPass) {
			return null;
		} else {
			Long strLong = Long.parseLong(str);
			return strLong;
		}
	}

	public static String toEmpty(Long var) {
		if (var == null) {
			return "";
		}

		return var.toString();
	}

	public static List getIdListfromString(String idString) {
		List idList = new ArrayList();
		if (CheckUtils.checkStringNull(idString))
			return idList;
		if ("".equals(idString.trim()))
			return idList;
		idString = idString.replaceAll("，", ",");
		StringTokenizer st = new StringTokenizer(idString, ",");
		Long id = null;
		String tmp = null;
		while (st.hasMoreTokens()) {
			tmp = st.nextToken();
			if (CheckUtils.checkStringNull(tmp)) {
				continue;
			}
			try {
				id = new Long(tmp);
			} catch (NumberFormatException nfe) {
				continue;
			}
			idList.add(id);
		}
		return idList;
	}

	public static List getNameListfromString(String nameString) {
		List nameList = new ArrayList();
		if (CheckUtils.checkStringNull(nameString))
			return nameList;
		StringTokenizer st = new StringTokenizer(nameString, ",");
		String tmp = null;
		while (st.hasMoreTokens()) {
			tmp = st.nextToken();
			if (CheckUtils.checkStringNull(tmp)) {
				continue;
			}
			nameList.add(tmp);
		}
		return nameList;
	}

	public static String getDisplayString(String source) {
		if (source == null)
			return "";
		else if (source.length() <= 10)
			return source;
		else
			return source.substring(0, 8) + "...";
	}

	public static String getDisplayString(String source, int length) {
		if (source == null)
			return "";
		else if (source.length() <= length)
			return source;
		else
			return source.substring(0, length - 2) + "...";
	}

	public static String createBatchNum(Long userId) throws Exception {
		Date date = new Date();
		String tmp = DateUtils.dateToString(date, "yyyy-MM-dd HH:mm:ss");

		String year = "";
		String month = "";
		String day = "";
		String hour = "";
		String minute = "";
		String second = "";

		String[] dateTmp = tmp.split(" ");
		if (dateTmp != null) {
			if (dateTmp.length != 0) {
				String[] v1 = dateTmp[0].split("-");
				String[] v2 = dateTmp[1].split(":");
				if (v1 != null) {
					if (v1.length != 0) {
						year = v1[0].substring(2, 4);// yy
						month = v1[1]; // mm
						day = v1[2]; // dd
					}
				}

				if (v2 != null) {
					if (v2.length != 0) {
						hour = v2[0]; // HH
						minute = v2[1]; // mm
						second = v2[2]; // ss
					}
				}
			}
		}

		String batchNum = "";
		if ((CheckUtils.checkStringNotNull(year))
				&& (CheckUtils.checkStringNotNull(month))
				&& (CheckUtils.checkStringNotNull(day))
				&& (CheckUtils.checkStringNotNull(hour))
				&& (CheckUtils.checkStringNotNull(minute))
				&& (CheckUtils.checkStringNotNull(second))) {
			batchNum = year + month + day + hour + minute + second
					+ userId.toString();

		} else {
			throw new Exception("获取批号失败！");
		}

		return batchNum;
	}

	/**
	 * @author humeng
	 * @date : 2012-8-29 description: 输入 搜狐其它.搜狐技术部 输出 搜狐其它
	 */
	public static String getSubString(String str, String reg) {
		int end = str.indexOf(reg);
		// 借款管理>> 借款查询(不能打印的bug) q.l
		if (end < 0) {
			return str;
		}
		return str.substring(0, end);
	}
	
	/**
	 * 获取科目的值(为空给默认，为多个0给1个0)
	 * @Title: getSegment 获取科目的值(为空给默认，为多个0给1个0)
	 * @Description: TODO 获取科目的值(为空给默认，为多个0给1个0)
	 * @param segment
	 * @param segmentNum
	 * @return String
	 * @throws
	 */
	public static String getSegment(String segment, int segmentNum) {
		if(segmentNum == 1) {
			segment = segment == null ? "119" : segment;
			segment = segment.trim().length() == 0 ? "119" :segment;
		} else if(segmentNum == 4) {
			segment = segment == null ? "10102" : segment;
			segment = segment.trim().length() == 0 ? "10102" :segment;
		} else {
			segment = segment == null ? "0" : segment;
			segment = segment.trim().length() == 0 ? "0" :segment;
		}
		Pattern p_str = Pattern.compile("^[0]*$");
		Matcher m_str = p_str.matcher(segment);
		boolean test = m_str.matches();
		if(test) {
			segment = "0";
		}
		return segment;
	}
	
	/**
	 * 获取科目的值集合
	 * @Title: getSegments 获取科目的值集合
	 * @Description: TODO 获取科目的值集合
	 * @param segment1
	 * @param segment2
	 * @param segment3
	 * @param segment4
	 * @param segment5
	 * @param segment6
	 * @param segment7
	 * @param segment8
	 * @param segment9
	 * @param segment10
	 * @return String
	 * @throws
	 */
	public static String getSegments(String segment1, String segment2,
			String segment3, String segment4, String segment5, String segment6,
			String segment7, String segment8, String segment9, String segment10) {
		StringBuffer segmentsSb = new StringBuffer();
		segmentsSb.append(segment1);
		segmentsSb.append(".").append(segment2);
		segmentsSb.append(".").append(segment3);
		segmentsSb.append(".").append(segment4);
		segmentsSb.append(".").append(segment5);
		segmentsSb.append(".").append(segment6);
		segmentsSb.append(".").append(segment7);
		segmentsSb.append(".").append(segment8);
		segmentsSb.append(".").append(segment9);
		// segmentSb.append(".").append(segment10);
		String segments = segmentsSb.toString();
		return segments;
	}
}
