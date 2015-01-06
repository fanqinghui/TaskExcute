package com.notice.template.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.notice.charts.ChartsFactory;
import com.notice.charts.ICharts;
import com.notice.dateSource.DateSourceParseFactory;
import com.notice.dateSource.IDateSourceParse;
import com.notice.model.ImageHeadData;
import com.notice.template.ITemplateParse;
import com.notice.template.TemplateParseBase;
import com.notice.util.ParseUtil;
import com.notice.util.ReportTemplateStatic;


public class TemplateParseHtml extends TemplateParseBase implements ITemplateParse {
	 @Resource
	  private DateSourceParseFactory dateSourceParseFactory;
	 @Resource
	  private ChartsFactory chartsFactory;
	 

	private Set<String> imgFileList = new HashSet<String>();

	private String isError = "";
	private String errorMessage = "";

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

	/*
	 * html模板解析实体
	 * 
	 * @see com.info.autorun.rt.template.ITemplateParse#doParse(java.util.Map)
	 * 
	 * @return 返回该模板内数据源集合
	 */

	public Map<String, Object> doParse(Map<String, Object> map,Map<String,Object> params) throws Exception {
		beforeParse();

		String sendEmail = ParseUtil.getString(map.get("sendEmail"));
		if ("1".equals(sendEmail)) {
			ifsendmail = true;
		} else {
			ifsendmail = false;
		}
		parse(map);
		mailInfo.setContent(text.replace(ReportTemplateStatic.ImageHeadDate_Url, ReportTemplateStatic.Email_Cid));
		mailInfo.setTaskId(Long.valueOf(ParseUtil.getString(map.get("id"))));
		mailInfo.setImgFileList(imgFileList);
		afterParse(params);

		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("isError", isError);
		resultMap.put("errorMessage", errorMessage);
		return resultMap;
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> parse(Map<String, Object> map) {
		isError = "";
		errorMessage = "";
		imgFileList = new HashSet<String>();
		String content = (String) map.get("content");
		Map<String, String> propertyParamMap = (Map<String, String>) map.get("propertyParamMap");
		Map<String, Object> formMap = new HashMap<String, Object>();

		Set<Map<String, String>> set = new HashSet<Map<String, String>>();

		Set<Map<String, String>> imgdefSet = new HashSet<Map<String, String>>();

		// 解析模板，获取模板中所有数据源
		if (ParseUtil.isNotNullforString(content)) {

			String[] array = content.split("\\[\\$");
			for (String str : array) {
				if (str.indexOf("$]") > -1) {
					// 替换中文逗号，防止解析出错
					str = str.replace("，", ",");
					Map<String, String> dataSourseMap = new HashMap<String, String>();
					String header = str.substring(0, str.indexOf(","));
					String type = header.toLowerCase();
					dataSourseMap.put("type", type);

					StringBuilder sb = new StringBuilder();
					sb.append("[$");
					sb.append(str.substring(0, str.indexOf("$]")));
					sb.append("$]");
					dataSourseMap.put("content", sb.toString());

					// 判断是否图片详细参数数据源，如果是提取出来最后解析。
					if (ReportTemplateStatic.Imgdef.equals(type)) {
						imgdefSet.add(dataSourseMap);
					} else {
						set.add(dataSourseMap);
					}

				}
			}
		}

		String newContent = content;
		Map<String, Object> imageMap = new HashMap<String, Object>();
		for (Map<String, String> dataSourseMap : set) {

			try {
				String dataSourseType = dataSourseMap.get("type");
				// 根据数据源类型获取解析借口
				IDateSourceParse dateSourceParse = dateSourceParseFactory.getDateSourceParseImpl(dataSourseType);

				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("content", dataSourseMap.get("content"));
				paramMap.put("propertyParamMap", propertyParamMap);
				paramMap.put("formMap", formMap);
				paramMap.put("id", map.get("id"));

				Map<String, Object> returnMap = dateSourceParse.doParse(paramMap);

				// 执行数据数据源解析
				if (returnMap != null && !ReportTemplateStatic.Image.equals(dataSourseType)) {
					Map<String, Object> map2 = (Map<String, Object>) returnMap.get("formMap");
					if (map2 != null) {
						// 将formMap信息存入内存中 供后面解析调用
						formMap = map2;
					}

					// 替换content
					String v = returnMap.get("viewValue") == null ? "" : returnMap.get("viewValue").toString();
					newContent = newContent.replace(dataSourseMap.get("content").toString(), v);

				}
				// 执行图片头数据源解析
				else if (ReportTemplateStatic.Image.equals(dataSourseType)) {
					ImageHeadData imageHeadData = (ImageHeadData) returnMap.get("imageHeadData");
					returnMap.remove("imageHeadData");
					imageMap.putAll(returnMap);

					// String v = "<img  border='0'  src='D:\\0_01_1303959663897.jpg'  width='300' height='300'/>";
					StringBuilder sb = new StringBuilder("");
					sb.append("<img  border='0'  src='").append(imageHeadData.getUrl()).append("' width='").append(imageHeadData.getWidth()).append("' height='").append(imageHeadData.getHeight()).append("'/>");

					// 保存图片路径供邮件发送使用
					imgFileList.add(imageHeadData.getUrl());

					newContent = newContent.replace(dataSourseMap.get("content").toString(), sb.toString());
				}
			} catch (Exception e) {
				isError = "true";
				errorMessage = e.toString();
			}

		}

		// 解析图片详细数据源
		for (Map<String, String> dataSourseMap : imgdefSet) {
			try {
				String dataSourseType = dataSourseMap.get("type");
				// 根据数据源类型获取解析借口
				IDateSourceParse dateSourceParse = dateSourceParseFactory.getDateSourceParseImpl(dataSourseType);

				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("content", dataSourseMap.get("content"));
				paramMap.put("propertyParamMap", propertyParamMap);
				paramMap.put("imageMap", imageMap);

				// 执行数据源解析
				Map<String, Object> returnMap = dateSourceParse.doParse(paramMap);
				ImageHeadData imageHeadData = (ImageHeadData) returnMap.get("imageHeadData");

				ICharts chart = chartsFactory.getChartsImpl(imageHeadData.getType());

				chart.drawChart(imageHeadData);

			} catch (Exception e) {
				isError = "true";
				errorMessage = e.getMessage();
			}

		}

		// newContent = newContent.replaceAll("\\[\\$IMGDEF[.\n\r]*\\$]", "");
		if (newContent.indexOf("[$IMGDEF") > 0) {
			newContent = newContent.replace(newContent.substring(newContent.indexOf("[$IMGDEF"), newContent.lastIndexOf("$]") + 2), "");
		}

		// System.out.println(newContent);

		text = newContent;
		fileName = map.get("id") + "_" + System.nanoTime() + ".html";

		return null;
	}
}
