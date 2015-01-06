package com.notice.dateSource.impl;

import java.util.HashMap;
import java.util.Map;

import com.notice.dateSource.DateSourceParseBase;
import com.notice.dateSource.IDateSourceParse;
import com.notice.util.ParseUtil;


@SuppressWarnings("unchecked")
public class DateSourceParseTable extends DateSourceParseBase implements IDateSourceParse {
	
	
	
	/*
	 * 解析table数据源方法
	 * 格式：[$TABLE,数据源CODE, tr行样式 ,参数,TABLE$] ；
	       说明：适用于列表级数据源，传入数据源code，顺序号，tr行样式，参数等；                       
	       当解析出是[$TABLE]数据源时，根据传入参数信息生成sql查询语句，获得list数据，迭代list，根据tr样式和顺序号生成tr行数据，然后替换赋值；
	       举例：[$TABLE,selectUserList, [&TR,<tr><td>[&SV,id,SV&]</td> <td width="20%"align="right">[&SV,name,SV&]</td></tr>,TR&] ,[&P,name=zoulei,P&],TABLE$]
     * @see com.info.autorun.rt.dateSource.IDateSourceParse#doParse(java.util.Map)
	 */
	public Map<String, Object> doParse(Map<String, Object> map) throws Exception {

			
				String oldContent = (String) map.get("content");
				if (ParseUtil.isNotNullforString(oldContent)) 
				{
					String resultSql ="";
				   
				    String content =oldContent;
				    
				       
				    //取出数据源中tr行样式信息。
				    String trStyleStr =ParseUtil.getTrStyleStr(content);
				    content = ParseUtil.replaceContentByTrStyle(content);
				    
				    String code="";
					// select * from user where id=:id  and  name=:name 
					// [$TD,selectOneUser,deptid,[&P,name={$name},P&],TD$]
					if(content.indexOf("[&P")>-1)
					{
						 //取出数据源中模板参数map信息
						 Map<String,String> propertyParamMap = (Map<String,String>)map.get("propertyParamMap");
						  
						 //取出属性参数信息
						 Map<String,String> propertyMap = ParseUtil.getPropertyContent(content,propertyParamMap);
						
						 //替换参数属性为空，防止解析出错
						 content = ParseUtil.replaceContentByProperty(content);
					     String [] array = content.split(","); 
				         String paramSql = reportTemplateService.getSqlByCode(array[1]); 				     
					   
				         code=array[1];
				         
				         //组织sql，根据sql数据源和参数信息
				         resultSql = ParseUtil.createSql(propertyMap,paramSql);
				        
				         System.out.println(resultSql);
				    }
					else
					{
						String [] array = content.split(","); 
						 code=array[1];
						resultSql = reportTemplateService.getSqlByCode(array[1]); 	
					}
					
					
				    Map<String,String> sqlmap = new HashMap<String,String>();
				    sqlmap.put("resultSql",resultSql);
				    sqlmap.put("trStyleStr",trStyleStr);
				    sqlmap.put("code", code);
					String  reslut = reportTemplateService.executeSqlForTableDs(sqlmap);
					
					Map<String,Object> returnMap = new HashMap<String,Object>();
				    returnMap.put("viewValue",reslut);
				       
				    return returnMap;
				} 
				else 
				{
					return null;
				}
			
		
    }




}
