package com.notice.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesManager {

	private static final Logger logger = Logger
			.getLogger(PropertiesManager.class);

	private static Properties prop;

	static {
		refresh();
	}

	public static boolean refresh() {
		prop = new Properties();
		// if (prop == null) {
		// prop = new Properties();
		// }
		try {
			/*prop.load(PropertiesManager.class
					.getResourceAsStream("/bizlines.properties"));*/
			prop.load(PropertiesManager.class
					.getResourceAsStream("/notice.properties"));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public static String getValue(String name) {
		refresh();

		String propValue = "";
		if (prop.containsKey(name)) {
			propValue = prop.get(name).toString().trim();
		}
		try {
			propValue = new String(propValue.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
		}
		return propValue;
	}

	static public ArrayList<String> getArrayValues(String name) {
		String propValue = getValue(name);
		String[] values = propValue.split(",");
		ArrayList<String> al = new ArrayList<String>();
		for (int i = 0; i < values.length; i++) {
			al.add(values[i]);
		}
		if (null == al) {
			al = new ArrayList<String>();
		}
		return al;
	}

	static public String getValue(String pName, int index) {
		ArrayList<String> al = getArrayValues(pName);
		if (index < al.size() && index >= 0) {
			return al.get(index).toString();
		} else {
			return "";
		}
	}

	static public String getValue(String pName, int index_x, int index_y) {
		ArrayList<String> al = getArrayValues(pName + "_" + index_x);
		if (index_y < al.size() && index_y >= 0) {
			return al.get(index_y).toString();
		} else {
			return "";
		}
	}

	public static void main(String[] args) {
		// String fileName=PropertiesManager.getValue("test");
		// System.out.println(fileName);
		// ArrayList<String> al =
		// PropertiesManager.getArrayValues("hot_position_cities");
		// for(int i = 0 ; i< al.size(); i++){
		// System.out.println(al.get(i));
		// }

		System.out.println(PropertiesManager.getValue("NOTICE_TEMPLATE_FILE_PATH"));

		String adaBizlineCodes = PropertiesManager
				.getValue("ad_bizlines_codes");
		List bizlineCodeList = StringUtils.getNameListfromString(adaBizlineCodes);
		System.out.println(bizlineCodeList.size());
		for (Object o : bizlineCodeList) {
			System.out.println(o.toString());
		}
	}
}
