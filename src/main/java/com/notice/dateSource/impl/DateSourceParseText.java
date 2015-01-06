package com.notice.dateSource.impl;

import java.util.HashMap;
import java.util.Map;

import com.notice.dateSource.DateSourceParseBase;
import com.notice.dateSource.IDateSourceParse;
import com.notice.util.ParseUtil;


@SuppressWarnings("unchecked")
public class DateSourceParseText  extends DateSourceParseBase implements IDateSourceParse {
	
	/*
	 * 格式：[$TEXT,数据源CODE, [&P,id=1&name=zoulei,P&],TD$]TEXT$] ；
	        说明：适用于固定变量信息,若数据源返回信息中有需赋值的变量根据传入的属性值替换，变量格式为{&name}
	        
	 * @see com.info.autorun.rt.dateSource.IDateSourceParse#doParse(java.util.Map)
	 */
	public Map<String, Object> doParse(Map<String, Object> map) throws Exception{

		
			
				String oldContent = (String) map.get("content");
				if (ParseUtil.isNotNullforString(oldContent)) 
				{
					String resultSql ="";
					String content =oldContent;
					String [] array = content.split(","); 
					resultSql = reportTemplateService.getSqlByCode(array[1]); 
				
					String  result = reportTemplateService.executeSql(resultSql,array[1]);
					
					//解析result，如果result有需赋值变量信息，则替换赋值。
					if(content.indexOf("[&P")>-1)
					{
						 //取出数据源中模板参数map信息
						 Map<String,String> propertyParamMap = (Map<String,String>)map.get("propertyParamMap");
						  
						 //取出属性参数信息
						 Map<String,String> propertyMap = ParseUtil.getPropertyContent(content,propertyParamMap);
						 
						 result = ParseUtil.parseResult(result, propertyMap);
						 
						 
					}
					
					
					Map<String,Object> returnMap = new HashMap<String,Object>();
				    returnMap.put("viewValue",result);
				       
				    return returnMap;
				}
				else 
				{
					return null;
				}
		
		
	}
	
}
