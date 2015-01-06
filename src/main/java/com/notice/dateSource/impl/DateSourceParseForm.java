package com.notice.dateSource.impl;

import java.util.HashMap;
import java.util.Map;

import com.notice.dateSource.DateSourceParseBase;
import com.notice.dateSource.IDateSourceParse;
import com.notice.util.ParseUtil;


@SuppressWarnings("unchecked")
public class DateSourceParseForm  extends DateSourceParseBase implements IDateSourceParse  {
	
	/*
	 *  格式：[$FORM,数据源CODE,key,name,参数,FORM$]；
	 	说明：适用于form表单中各字段赋值，传入数据源code，key值，参数等；                       
	 	              当解析出是[$FORM]数据源时，先查询内存中是否存在该数据源数据（通过key值判断），如果有调用内存中已有数据,根据name属性替换赋值，
	 	              如果没有，解析数据源并执行数据源查询，并将其放入内存中，以key作为唯一标示。如key为空则不取内存信息，直接解析并执行数据库查询。
	 	举例：[$FORM,selectOneUser,1,name, [&P,id=1&name=zoulei,P&],FORM$]；
	          又如： [$FORM,selectOneUser,2,id, [&P,name=邦杰,P&],FORM$]。
	 * @see com.info.autorun.rt.dateSource.IDateSourceParse#doParse(java.util.Map)
	 */
	public Map<String, Object> doParse(Map<String, Object> map)throws Exception{

		if (map != null) 
		{
			String oldContent = (String) map.get("content");
			
			if (ParseUtil.isNotNullforString(oldContent)) 
			{
				
                String content =oldContent;
				
                String [] contentArray =content.split(",");
                
                //获取内存中formmap历史记录
                Map<String,Object>  formMap = (Map<String,Object>)map.get("formMap");
                
                Map<String,Object> resultMap = new HashMap<String,Object>();
                if(ParseUtil.isNotNullforString(contentArray[2])){
                
                	String formKey = contentArray[1]+contentArray[2];
                	Map<String,Object> keyMap = (Map<String,Object>)formMap.get(formKey);
                	
                	if(keyMap==null)
                	{
                		resultMap = this.getParseMap(map);
                		formMap.put(formKey, resultMap);
                	}
                	else
                	{
                		resultMap =keyMap;
                	}
                	
                }
                else{
                	 resultMap =this.getParseMap(map);
                }
                
                
                
                String viewValue =(String)resultMap.get(contentArray[3]); 
                
                
                
                Map<String,Object> returnMap = new HashMap<String,Object>();
                returnMap.put("formMap",formMap);
                returnMap.put("viewValue",viewValue);
               
                return returnMap;
			} 
			else 
			{
				return null;
			}
		} 
		else 
		{
			return null; 
		}
    }
	
	
	
	
	
	public Map<String, Object> getParseMap(Map<String, Object> map)throws Exception{
		String content = (String) map.get("content");
		String resultSql ="";
		String code="";
		
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
            
             System.out.println(resultSql);
             
		}
		else
		{
			String [] array = content.split(","); 
			resultSql = reportTemplateService.getSqlByCode(array[1]); 	
			code=array[1];
		}
		Map<String, Object>  result = reportTemplateService.executeSqlForForm(resultSql,code);
		
		return result;
	}

}
