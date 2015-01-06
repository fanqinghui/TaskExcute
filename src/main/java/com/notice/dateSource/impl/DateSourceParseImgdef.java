package com.notice.dateSource.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.notice.dateSource.DateSourceParseBase;
import com.notice.dateSource.IDateSourceParse;
import com.notice.model.ImageDefData;
import com.notice.model.ImageHeadData;
import com.notice.util.ParseUtil;

@SuppressWarnings("unchecked")
public class DateSourceParseImgdef  extends DateSourceParseBase implements IDateSourceParse {
	
	/*
	 * 格式：[$IMGDEF,key,数据源code,数据源参数,显示参数,$]；
	       说明： 适用于图片数据源详细显示信息；                       
	 	              当解析出是[$IMGDEF]数据源时，判断是否数据源code是否为空，不为空根据sql查询显示参数，否则根据传入的显示参数生成图片
	 	举例：[$IMGDEF,01,selectimg,[&P,h=300&w=200&t=PR费用信息,P&],[&SV,H=h&W=w&T=t,SV&],$]
	 	      [$IMGDEF,01,,[&SV,H=200&W=300&T=111,SV&],[&SV,H=300&W=200&T=222,SV&],[&SV,H=200&W=200&T=200,SV&],$]
	 	
	 * @see com.info.autorun.rt.dateSource.IDateSourceParse#doParse(java.util.Map)
	 */
	public Map<String, Object> doParse(Map<String, Object> map) throws Exception {
		
			String oldContent = (String) map.get("content");
			Map<String,Object> imageMap =  (Map<String,Object> )map.get("imageMap");
			
			if (ParseUtil.isNotNullforString(oldContent)) 
			{   
				 String content =oldContent;
				 
				 String [] array = content.split(","); 
				 
				 ImageHeadData imageHeadData =(ImageHeadData)imageMap.get(array[1]);
				 List<ImageDefData> list = new ArrayList<ImageDefData>();
				 if(ParseUtil.isNotNullforString(array[2]))
				 {
					 String resultSql ="";
					 String paramSql = reportTemplateService.getSqlByCode(array[2]); 	
					 if(content.indexOf("[&P")>-1)
					 {
						 //取出数据源中模板参数map信息
						 Map<String,String> propertyParamMap = (Map<String,String>)map.get("propertyParamMap");
						 //取出属性参数信息
						 Map<String,String> propertyMap = ParseUtil.getPropertyContent(content,propertyParamMap);
						 
						 //组织sql，根据sql数据源和参数信息
			             resultSql = ParseUtil.createSql(propertyMap,paramSql);
					 }
					 else
					 {
						 resultSql = paramSql;
					 }
					
				     //取出数据源中显示参数信息。
			         String showViewStr =ParseUtil.getshowViewStr(content);
			         
			         Map<String,String> sqlmap= new HashMap<String,String>();
			         sqlmap.put("resultSql", resultSql);
			         sqlmap.put("showViewStr",showViewStr);
			         sqlmap.put("code", array[2]);
			         
			         list = reportTemplateService.executeSqlForImgdefDs(sqlmap);
    
				 }
				 else
				 {
					 list = reportTemplateService.getImageDefDataList(content);
				 }
				 
				 imageHeadData.setImageDefData(list);
				 
				 
				 Map<String,Object> resultMap = new HashMap<String,Object>();
				 resultMap.put("imageHeadData", imageHeadData);
				 return resultMap;
			}
			return null;
		
	}
	
	
	
}
