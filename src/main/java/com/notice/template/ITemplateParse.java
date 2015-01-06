package com.notice.template;

import java.util.Map;


public interface ITemplateParse {
	/*
	 * 模板解析借口
	 */
	public Map<String,Object> doParse(Map<String,Object> map,Map<String,Object> params)throws Exception;
}
