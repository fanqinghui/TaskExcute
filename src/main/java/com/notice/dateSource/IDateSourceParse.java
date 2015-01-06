package com.notice.dateSource;

import java.util.Map;



public interface IDateSourceParse {
	
	/*
	 * 数据源解析借口
	 */
	public Map<String,Object> doParse(Map<String,Object> map) throws Exception;
}
