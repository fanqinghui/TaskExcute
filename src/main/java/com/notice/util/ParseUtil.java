package com.notice.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class ParseUtil {
	private static final Log log = LogFactory.getLog(ParseUtil.class);
	
	public static boolean isNotNullforString(String str) {
		if (str != null) 
		{
			str = str.trim();
			if (!"".equals(str) && str.length() > 0) 
			{
				return true;
			} else 
			{
				return false;
			}
		} 
		else 
		{
			return false;
		}
	}
	
	
	
	/*
	 * [&P,name={$name},P&]   [&P,name=1&name={$name}&p='1'&id=(1,2),P&]  [&P,1&{$name}&'1'&(1,2),P&]
	         解析数据源内参数信息
	 */
	public static Map<String,String> getPropertyContent(String content,Map<String,String> propertyMap)  throws Exception {
		if(content.indexOf("[&P")>-1)
		{
			 String propertyContent = content.substring(content.indexOf("[&P,")+4,content.indexOf(",P&]"));
			 String [] array =  propertyContent.split("&");
             
			 Map<String,String> map = new HashMap<String,String>();
			 int i=1;
			 for (String p : array) 
             {
				if(p.indexOf("=")>-1)
				{
					String [] arrP =p.split("="); 
					String values =""; 
					if(arrP.length==1)
					{
						values="";
					}
					else
					{
						
						int index = arrP[1].indexOf("{$");
						if(index>-1)
						{
							String code =arrP[1].substring(index+2,arrP[1].indexOf("}")); 
							values = propertyMap.get(code);
						}
						else
						{
							values = arrP[1];
						}
						
					}
					map.put(arrP[0],values);
				
				}
				else
				{
					
					int index = p.indexOf("{$");
					if(index>-1)
					{
						String code =p.substring(index+2,p.indexOf("}")); 
						p = propertyMap.get(code);
					}
					map.put("P"+i,p );
					i++;
				}
				
			 }
			 
			 
			 return   map;
		}
		
		return null;
	}
	
	/*
	 * 替换属性信息，防止解析出错
	 */
	public static String replaceContentByProperty(String content)  throws Exception {
		content = content.replaceAll("\\[&P.*P&]", "");
		return content;
	}
	
	public static String getString(Object obj) {
		return obj==null?"":obj.toString();
	}
	
	/*
	 * 解析table数据源中顺序号信息
	 */
	public static String getOrderStr(String content) throws Exception  {
		String orderStr = content.substring(content.indexOf("[&ORDER,")+"[&ORDER,".length(),content.indexOf(",ORDER&]"));
		return orderStr;
	}
	
	/*
	 * 替换顺序号信息，防止解析出错
	 */
	public static String replaceContentByOrder(String content) throws Exception  {
		content = content.replaceAll("\\[&ORDER.*ORDER&]", "");
		return content;
	}
	
	

	/*
	 * 解析table数据源中tr行样式信息
	 */
	public static String getTrStyleStr(String content) throws Exception  {
		String trStyleStr = content.substring(content.indexOf("[&TR,")+"[&TR,".length(),content.indexOf(",TR&]"));
		return trStyleStr;
	}
	
	/*
	 * 解析imgdef数据源中显示参数信息
	 */
	public static String getshowViewStr(String content) throws Exception  {
		
		String svStr = content.substring(content.indexOf("[&SV,")+"[&SV,".length(),content.indexOf(",SV&]"));
		return svStr;
	}
	
	
	
	
	
	/*
	 * 替换样式信息，防止解析出错
	 */
	public static String replaceContentByTrStyle(String content) throws Exception  {
		content = content.replaceAll("\\[&TR.*TR&]", "");
		return content;
	}
	
	
	
	/*
	 * 解析result，如果result有需赋值变量信息，则替换赋值。
	 */
	public static String parseResult(String result, Map<String,String> propertyMap)  throws Exception {
		int index = result.indexOf("{$");
		String value = "";
		while(result.indexOf("{$")>-1)
		{
			String code =result.substring(index+2,result.indexOf("}")); 
			value = (String)propertyMap.get(code);
			result = result.replaceFirst(result.substring(index,result.indexOf("}")+1), value);
		}
		
		
		return result;
	}
	
	
	
	/*
	 *  [&TR,<tr><td>[&SV,id,&]</td> <td width="20%"align="right">[&SV,name,&]</td></tr>,TR&]
	 */
	public static List<String> parseTrStyleStr(String trStyleStr) throws Exception  {
		List<String> list = new ArrayList<String>();
		
		
		while(trStyleStr.indexOf("[&SV")>-1){
			String code = trStyleStr.substring(trStyleStr.indexOf("[&SV,")+5,trStyleStr.indexOf("&]",trStyleStr.indexOf("[&SV"))-1);
			trStyleStr  = trStyleStr.replaceFirst("\\[&SV", "");
			list.add(code);
            
		}
		
		return list;
	}
	
	
	
	/*
	 * 组织sql
	 */
	public static String createSql(Map<String,String> map,String sql) throws Exception  {
		 Set<Map.Entry<String,String>> set = map.entrySet();
			
		 for (Entry<String, String> entry : set) {
			 sql = sql.replaceAll(":"+entry.getKey(), entry.getValue());
		 }
		
		 return sql;
	}
	
	 /**
     * 以行为单位读取文件，常用于读面向行的格式化文件
	 * @throws Exception 
     */
	 public static String readFileByLines(String fileName) throws Exception {
	        File file = new File(fileName);
	        StringBuilder sb = new StringBuilder("");
	        BufferedReader reader = null;
	      
	            log.info("\n---以行为单位读取文件内容，一次读一整行：");
	            reader = new BufferedReader(new FileReader(file));
	            String tempString = null;
	            int line = 1;
	            // 一次读入一行，直到读入null为文件结束
	            while ((tempString = reader.readLine()) != null) {
	                // 显示行号
	            	sb.append(tempString);
	            	sb.append("\n");
	                line++;
	            }
	           
	            reader.close();
	            return sb.toString();
	    }
}
