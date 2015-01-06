package com.notice.dateSource.impl;

import java.util.HashMap;
import java.util.Map;

import com.notice.dateSource.DateSourceParseBase;
import com.notice.dateSource.IDateSourceParse;
import com.notice.util.ParseUtil;

@SuppressWarnings("unchecked")
public class DateSourceParseTd  extends DateSourceParseBase implements IDateSourceParse {
	
	
	

	/*
	 *  格式：[$TD,数据源CODE,name,参数,TD$]；
	 	说明：适用于form表单中各字段赋值，传入数据源code，参数等；                       
	 	              当解析出是[$TD]数据源时，先查询内存中是否存在该数据源数据（通过sql判断），如果有调用内存中已有数据,根据name属性替换赋值，
	 	              如果没有，解析数据源并执行数据源查询，并将其放入内存中，以sql作为唯一标示。
	 	举例：[$TD,selectOneUser,name, [&P,id=1&name=zoulei,P&],TD$]
	 * @see com.info.autorun.rt.dateSource.IDateSourceParse#doParse(java.util.Map)
	 */
	public Map<String, Object> doParse(Map<String, Object> map) throws Exception{

		
			
				Map<String,Object>  formMap = (Map<String,Object>)map.get("formMap");
				 
				String oldContent = (String) map.get("content");
				if (ParseUtil.isNotNullforString(oldContent)) 
				{
					String resultSql ="";
				    String name="";
				    
				    String content =oldContent;
				    String code =""; 
					// select * from user where id=:id  and  name=:name 
					// [$TD,selectOneUser,deptid,[&P,name={$name},P&],TD$]
					if(content.indexOf("[&P")>-1)
					{
						 //获取模板参数map信息
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
				         name=array[2];
				         System.out.println(resultSql);
				         
					}
					else
					{
						String [] array = content.split(","); 
						resultSql = reportTemplateService.getSqlByCode(array[1]);
						code=array[1];
						name = array[2];
					}
					
					
					
					Map<String,Object> keyMap = (Map<String,Object>)formMap.get(resultSql.trim());
					Map<String, Object> resultMap = new HashMap<String, Object>();
					if(keyMap==null)
					{
						resultMap = reportTemplateService.executeSqlForForm(resultSql,code);
						if(resultMap!=null){
							formMap.put(resultSql.trim(), resultMap);
				        }
					}
					else
					{
						resultMap =keyMap;
					}
					
				   
					
					Map<String,Object> returnMap = new HashMap<String,Object>();
				    returnMap.put("viewValue",resultMap.get(name));
				    returnMap.put("formMap",formMap);
				    
				    return returnMap;
				    
				} 
				else 
				{
					return null;
				}
			
		} 
		
	
	
	
	
}
