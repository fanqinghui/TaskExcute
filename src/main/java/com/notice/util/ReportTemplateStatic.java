package com.notice.util;


public class ReportTemplateStatic {

	//模板类型，对应bean工厂key
	public static final String Html = "HTML";
	
	//数据源类型，对应bean工厂key
	public static final String Form = "form";
	
	public static final String Image = "img";
	
	public static final String Imgdef = "imgdef";
	
	
	public static final String Table = "table";
	
	public static final String Td= "td";
	
	public static final String Text = "text";
	
	
	
	
	
	public static final String ImageHeadDate_Height = "h";
	
	public static final String ImageHeadDate_Width = "w";
	
	public static final String ImageHeadDate_Title = "t";
	
	public static final String ImageHeadDate_XLabel= "x";
	
	public static final String ImageHeadDate_YLabel= "y";
	
	public static final String ImageHeadDate_alpha= "a";
	
	public static final String ImageHeadDate_Strok= "s";
	
	public static final String ImageHeadDate_Guide= "g";
	
	public static final String ImageHeadDate_DisplayNumber= "dn";
	
	//图片数据源解析后生成图片保存路径
	public static final String ImageHeadDate_Url =PropertiesManager.getValue("NOTICE_TEMPLATE_ImageHeadDate_Url") ;
	
	//解析后网页文件保存路径
	public static final String File_Url = PropertiesManager.getValue("NOTICE_TEMPLATE_HtmlFile_Url");

	public static final String Email_Cid = "cid:";

	
	
	
	public static final String Local_Template_Url = "E:/ipmsgv2.09/ipmsg/PR/src/mailTemplate/";
	
	
	
	//public static final String PR_Url = "<a href='http://pr.sohu-inc.com/taskBuildPageListFromEmail.do?'";
	

	//JANUARY, FEBRUARY, MARCH, APRIL, MAY, JUNE, JULY, AUGUST, SEPTEMBER, OCTOBER, NOVEMBER, DECEMBER
	public static final String [] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
	
}
