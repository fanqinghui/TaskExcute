package com.notice.dateSource.impl;

import java.util.HashMap;
import java.util.Map;

import com.notice.dateSource.DateSourceParseBase;
import com.notice.dateSource.IDateSourceParse;
import com.notice.model.ImageHeadData;
import com.notice.util.ParseUtil;
import com.notice.util.ReportTemplateStatic;

/*
 * 解析table数据源方法
 * 格式：[$IMG,默认数据源CODE,title=PR单费用&font=SONG&orientation=true&showtip=true,[$SV,name,value,[$P,数据源CODE]] IMG$] ；
       说明：适用于列表级数据源，默认数据源code，显示参数，；                       
       当解析出是[$TABLE]数据源时，根据传入参数信息生成sql查询语句，获得list数据，迭代list，根据tr样式和顺序号生成tr行数据，然后替换赋值；
       举例：[$TABLE,selectUserList,[&ORDER,1,3,4,ORDER&], [&TR,<tr><td>[&SV,id,SV&]</td> <td width="20%"align="right">[&SV,name,SV&]</td></tr>,TR&] ,[&P,name=zoulei,P&],TABLE$]
 * @see com.info.autorun.rt.dateSource.IDateSourceParse#doParse(java.util.Map)
 */
@SuppressWarnings("unchecked")
public class DateSourceParseImage  extends DateSourceParseBase implements IDateSourceParse {
	
	/*
	 * 格式：[$IMG,key,图片类型code,参数,$]；
	       说明： 适用于图片数据源基本信息；                       
	 	              当解析出是[$IMG]数据源时，解析参数信息，同时生产发布路径，再根据key值去寻找图片详细信息
	 	举例：[$IMG,01,01,[&P,h=300&w=200&t=PR费用信息,P&],$]
	 * @see com.info.autorun.rt.dateSource.IDateSourceParse#doParse(java.util.Map)
	 */
	public Map<String, Object> doParse(Map<String, Object> map) throws Exception{
		
			String oldContent = (String) map.get("content");
			if (ParseUtil.isNotNullforString(oldContent)) 
			{   
				String content =oldContent;
				ImageHeadData imageHeadData = new ImageHeadData();
				if(content.indexOf("[&P")>-1)
				{
					 //获取模板参数map信息
					 Map<String,String> propertyParamMap = (Map<String,String>)map.get("propertyParamMap");
					  
					 //取出属性参数信息
					 Map<String,String> propertyMap = ParseUtil.getPropertyContent(content,propertyParamMap);
					 content = ParseUtil.replaceContentByProperty(content);
					
					 imageHeadData.setHeight(ParseUtil.getString(propertyMap.get(ReportTemplateStatic.ImageHeadDate_Height)));
					 imageHeadData.setWidth(ParseUtil.getString(propertyMap.get(ReportTemplateStatic.ImageHeadDate_Width)));
					 imageHeadData.setTitle(ParseUtil.getString(propertyMap.get(ReportTemplateStatic.ImageHeadDate_Title)));
					 imageHeadData.setXLabel(ParseUtil.getString(propertyMap.get(ReportTemplateStatic.ImageHeadDate_XLabel)));
					 imageHeadData.setYLabel(ParseUtil.getString(propertyMap.get(ReportTemplateStatic.ImageHeadDate_YLabel)));
					 if(propertyMap.get(ReportTemplateStatic.ImageHeadDate_alpha) != null)
						 imageHeadData.setAlpha(ParseUtil.getString(propertyMap.get(ReportTemplateStatic.ImageHeadDate_alpha)));
					 if(propertyMap.get(ReportTemplateStatic.ImageHeadDate_Strok) != null)
						 imageHeadData.setStroke(ParseUtil.getString(propertyMap.get(ReportTemplateStatic.ImageHeadDate_Strok)));
					 if(propertyMap.get(ReportTemplateStatic.ImageHeadDate_DisplayNumber) != null)
						 imageHeadData.setDisplayNumber(ParseUtil.getString(propertyMap.get(ReportTemplateStatic.ImageHeadDate_DisplayNumber)));
					 
				}	
				
				 String [] array = content.split(","); 
				 imageHeadData.setKey(array[1]);
				 imageHeadData.setType(array[2]);
				 
				 StringBuilder sb = new StringBuilder("");
				 sb.append(ReportTemplateStatic.ImageHeadDate_Url)
				   .append(ParseUtil.getString(map.get("id")))
				   .append("_")
				   .append(array[1])
				   .append("_")
				   .append(System.nanoTime())
				   .append(".jpg");
				   
				 
				 imageHeadData.setUrl(sb.toString());
				 Map<String,Object> resultMap = new HashMap<String,Object>();
				 resultMap.put(array[1], imageHeadData);
				 resultMap.put("imageHeadData", imageHeadData);
				 return resultMap;
			}
			return null;
		
	}
}
