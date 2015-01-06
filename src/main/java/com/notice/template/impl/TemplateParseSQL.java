package com.notice.template.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.notice.dateSource.DateSourceParseFactory;
import com.notice.dateSource.IDateSourceParse;
import com.notice.template.ITemplateParse;
import com.notice.template.TemplateParseBase;
import com.notice.util.ParseUtil;

public class TemplateParseSQL extends TemplateParseBase implements
		ITemplateParse {

	private String isError = "";
	private String errorMessage = "";
	@Resource
	private DateSourceParseFactory dateSourceParseFactory;

	
	public String getIsError() {
		return isError;
	}
	public void setIsError(String isError) {
		this.isError = isError;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Map<String, Object> doParse(Map<String, Object> map,Map<String,Object> params)
			throws Exception {
		
		isSql = true ;
		
		beforeParse();

		String sendEmail = ParseUtil.getString(map.get("sendEmail"));
		if ("1".equals(sendEmail)) {
			ifsendmail = true;
		} else {
			ifsendmail = false;
		}
		parse(map);
		mailInfo.setContent(text);
		mailInfo.setTaskId(Long.valueOf(ParseUtil.getString(map.get("id"))));
		afterParse(params);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("isError", isError);
		resultMap.put("errorMessage", errorMessage);

		return resultMap;
	}

	public Map<String, Object> parse(Map<String, Object> map) throws Exception {

		isError = "";
		errorMessage = "";

		IDateSourceParse dateSourceParse = dateSourceParseFactory.getDateSourceParseImpl("sql");

		Map<String, Object> returnMap = dateSourceParse.doParse(map);

		text = returnMap.get("msg").toString();

		return null;

	}

}
