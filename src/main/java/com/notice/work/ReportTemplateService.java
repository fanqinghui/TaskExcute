package com.notice.work;

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import oracle.jdbc.OracleTypes;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;

import com.notice.dateSource.impl.DateSourceParseSQL;
import com.notice.model.ImageDefData;
import com.notice.model.MailInfo;
import com.notice.model.UserArgument;
import com.notice.util.ParseUtil;


@SuppressWarnings("unchecked")
@Service
public class ReportTemplateService {
	private static final Logger log = Logger.getLogger(ReportTemplateService.class);
	@Resource
	private SimpleJdbcTemplate jdbcTemplate ;

	private JdbcTemplate rtJdbcTemplate;
	
	public void setRtJdbcTemplate(SingleConnectionDataSource ds) {
		this.rtJdbcTemplate = new JdbcTemplate(ds);
	}
	
	
	
	/*
	 * 获得模板信息，路径 类型等
	 */
	public Map<String,Object> getTemplate(Long id) throws Exception{
		
		
			Map<String,Object> resultMap = new HashMap<String,Object>();
			
			String sql ="select  t1.FILE_NAME as name ,t1.SUFFIX_CODE as code ,t1.FILE_PATH  as url,t1.BILL_ATTACHMENT_CODE as type,t2.is_send_email as sendEmail " +
					  "  from sys_file_relations t1,sys_task_all t2 where  t1.BILL_ID = t2.TASK_ID and t1.disabled='0' and t1.bill_id="+id;
			
			List<Map<String,Object>> list = (List<Map<String,Object>>)this.jdbcTemplate.queryForList(sql);
			if(list.size()>0){
				Map<String,Object> map = list.get(0);
				//获取模板类型和路径
				//resultMap.put("name", ReportTemplateStatic.Local_Template_Url+"test_template.html"); 
				resultMap.put("name", ParseUtil.getString(map.get("url"))+"/"+ParseUtil.getString(map.get("name")));
				resultMap.put("type", map.get("type"));
				resultMap.put("sendEmail", map.get("sendEmail"));
			}
			else
			{
				throw new Exception("getSqlByCode出错，数据源code不存在！");
				
			}
			
			Map<String,String> propertyParamMap = new HashMap<String,String>(); 
			String paramSql ="select PARAM_NAME as key,PARAM_VALUE as value from sys_param_all t where (DISABLE_DATE is null or DISABLE_DATE>sysdate) and t.task_id="+id;
     
			//获取任务关联参数信息供模板解析调用
			List<Map<String,Object>> paramList = (List<Map<String,Object>>)this.jdbcTemplate.queryForList(paramSql);
			for (Map<String, Object> map : paramList) {
				propertyParamMap.put(ParseUtil.getString(map.get("key")), ParseUtil.getString(map.get("value")));
			}

			resultMap.put("propertyParamMap", propertyParamMap);
			return resultMap;
		
	}
	
	
	
	
	public String getSqlByCode(String code) throws Exception {
		//code="PS_REPORT_NAME";
			        if(!ParseUtil.isNotNullforString(code))
			        {
						throw new Exception("getSqlByCode出错，数据源code不存在！");
					}
					

					String sql="";
					//TODO:code改完SH_CODE<!--modify by fqh-->
					//List<Map<String,Object>> list = (List<Map<String,Object>>)jdbcTemplate.queryForList("select PS_DEFINING from SYS_DATA_SOURCE where CODE ='"+code+"'");
					List<Map<String,Object>> list = (List<Map<String,Object>>)jdbcTemplate.queryForList("select PS_DEFINING from SYS_DATA_SOURCE where SH_CODE ='"+code+"'");
					if(list.size()>0)
					{
						Map<String,Object> map = list.get(0);
						String clob =(String)map.get("PS_DEFINING");
						if(clob!=null)
						{
							sql =clob.toString();	
						}	
					}else{
						throw new Exception("getSqlByCode出错，数据源code查询出错！");
					}
				    return sql;

	}
	
	
    public String executeSql(String sql,String code) throws Exception {
    	SingleConnectionDataSource ds = getSingleConnectionDataSource(code);
    	this.setRtJdbcTemplate(ds);
    	String result;
		try {
			result = (String)this.rtJdbcTemplate.queryForObject(sql,null,java.lang.String.class);
			return result;
		} catch (Exception e) {
			log.error(this.getClass() + "\n---方法：" + new Exception().getStackTrace()[0].getMethodName() + "\n---e.getMessage():\n" + e.toString() + "\n" + e);
			throw new Exception(e.getMessage());
		}
		finally{
			ds.getConnection().close();
		}
    	
		
	}
	
    public String executeSqlForSelectName(String sql,String name,String code)throws Exception {
    	String ret="";
    	List<Map<String,Object>> list = (List<Map<String,Object>>)this.rtJdbcTemplate.queryForList(sql);
		
    	if(list.size()>0){
			Map<String,Object> map = list.get(0);
			ret = (String)map.get(name);
		}
		
		return ret;
	}
    
    
    /*
     * sqlmap.put("resultSql",resultSql);
	   sqlmap.put("orderStr",orderStr);
	   sqlmap.put("trStyleStr",trStyleStr);
     */
    public String executeSqlForTableDs(Map<String,String> sqlmap) throws Exception{
		String resultSql = sqlmap.get("resultSql");
		
		String trStyleStr = sqlmap.get("trStyleStr");
		
		List<String>  array = ParseUtil.parseTrStyleStr(trStyleStr);
		
		StringBuilder sb = new StringBuilder();
		
		SingleConnectionDataSource ds = getSingleConnectionDataSource(sqlmap.get("code"));
    	this.setRtJdbcTemplate(ds);
    	
		try {
			List<Map<String,Object>> list = (List<Map<String,Object>>)this.rtJdbcTemplate.queryForList(resultSql);
			
			if(list.size()>0){
				for (Map<String, Object> map : list) {
					String newStr =trStyleStr;
					for (String str :array) {
						String v = map.get(str.trim())==null?"":map.get(str.trim()).toString();
						newStr = newStr.replaceAll("\\[&SV,"+str+",&]",v);
					}
					
					sb.append(newStr);
				}
			}
			return sb.toString();
			
		} catch (Exception e) {
			log.error(this.getClass() + "\n---方法：" + new Exception().getStackTrace()[0].getMethodName() + "\n---e.getMessage():\n" + e.toString() + "\n" + e);
			throw new Exception(e.getMessage());
		}
		finally{
			ds.getConnection().close();
		}
		
		
	}
    
   
    
    
    
    /*
     * sqlmap.put("resultSql",resultSql);
	   sqlmap.put("showViewStr",showViewStr);H=h&W=w&T=t
     */
    public List<ImageDefData> executeSqlForImgdefDs(Map<String,String> sqlmap)throws Exception{
        String resultSql = sqlmap.get("resultSql");
		
		String showViewStr = sqlmap.get("showViewStr");
		
		List<ImageDefData> returnList = new ArrayList<ImageDefData>();
		
		SingleConnectionDataSource ds = getSingleConnectionDataSource(sqlmap.get("code"));
    	this.setRtJdbcTemplate(ds);
    	
    	
		try {
			List<Map<String,Object>> list = (List<Map<String,Object>>)this.rtJdbcTemplate.queryForList(resultSql);
			if(list.size()>0){
				for (Map<String, Object> map : list) 
				{
					ImageDefData imageDefData = new ImageDefData();
					String [] array = showViewStr.split("&");
					for (String str : array) 
					{
						String [] arrP =str.split("=");
						if(arrP.length==1)
						{
							imageDefData = this.getImageDefData(imageDefData, arrP[0], "");
						}
						else
						{
							imageDefData = this.getImageDefData(imageDefData, arrP[0], ParseUtil.getString(map.get(arrP[1])));
								
						}
              	    }
					returnList.add(imageDefData);
				}
			}
			return returnList;
		} catch (Exception e) {
			log.error(this.getClass() + "\n---方法：" + new Exception().getStackTrace()[0].getMethodName() + "\n---e.getMessage():\n" + e.toString() + "\n" +e);
			throw new Exception(e.getMessage());
		}
		finally{
			ds.getConnection().close();
		}
		
    	
	}
    
    /*
     *  [$IMGDEF,01,,[&SV,H=200&W=300&T=111,SV&],[&SV,H=300&W=200&T=222,SV&],[&SV,H=200&W=200&T=200,SV&],$]
     */
    public List<ImageDefData> getImageDefDataList(String content)throws Exception{
    	List<ImageDefData> returnList = new ArrayList<ImageDefData>();
	    
    	while(content.indexOf("[&SV")>-1){
    		ImageDefData imageDefData= new ImageDefData();
    		String code =content.substring(content.indexOf("[&SV,")+"[&SV,".length(),content.indexOf(",SV&]",content.indexOf("[&SV,"))); 
    		String [] array = code.split("&");
			for (String str : array) 
			{
				String [] arrP =str.split("=");
				if(arrP.length==1)
				{
					imageDefData = this.getImageDefData(imageDefData, arrP[0], "");
				}
				else
				{
					imageDefData = this.getImageDefData(imageDefData, arrP[0], arrP[1]);
				}
				
			}
    		
    		content  = content.replaceFirst("\\[&SV", "");
    		returnList.add(imageDefData);
	    }
    	
    	
    	return returnList;
    }
    
    public ImageDefData getImageDefData(ImageDefData imageDefData,String key,String value )throws Exception{
    	key=key.trim();
    	value=value.trim();
    	Class<? extends ImageDefData> c=imageDefData.getClass(); 
    	
    	
			String aa = key.substring(0,1).toUpperCase()+key.substring(1,key.length());
			Method m = c.getMethod("set"+aa,new Class[]{Class.forName("java.lang.String")}); 
	    	m.invoke(imageDefData,new Object[]{value}); 
	  
	

    	return imageDefData;
    }
    
    
    
    public Map<String,Object> executeSqlForForm(String sql,String code) throws Exception{
    	
			
				if(!ParseUtil.isNotNullforString(sql)){
				throw new Exception("执行executeSqlForForm时sql为空！");
				}
				
				
				SingleConnectionDataSource ds = getSingleConnectionDataSource(code);
		    	this.setRtJdbcTemplate(ds);
		    	
		    	
				try {
					List<Map<String,Object>> list = (List<Map<String,Object>>)this.rtJdbcTemplate.queryForList(sql);
					if(list.size()>0){
						return list.get(0);
					}
					return null;
				} catch (Exception e) {
					log.error(this.getClass() + "\n---方法：" + new Exception().getStackTrace()[0].getMethodName() + "\n---e.getMessage():\n" + e.toString() + "\n" +e);
					throw new Exception(e.getMessage());
				}
				finally{
					ds.getConnection().close();
				}
				
			
	}
    
	
    
    
    public SingleConnectionDataSource getSingleConnectionDataSource(String code) throws Exception {
		
        //String sql ="select t2.DRIVER_CLASS as driver,t2.url ,t2.user_name ,t2.password from SYS_DATA_SOURCE t1,sys_db_links t2 where t1.link_id=t2.link_id and t1.code='"+code+"'";
    	String sql ="select t2.DRIVER_CLASS as driver,t2.url ,t2.user_name ,t2.password from SYS_DATA_SOURCE t1,sys_db_links t2 where t1.link_id=t2.link_id and t1.sh_code='"+code+"'";
		
    	List<Map<String,Object>> list = (List<Map<String,Object>>)this.jdbcTemplate.queryForList(sql);
     	Map<String,Object> map = new HashMap<String,Object>();
    	if(list.size()>0){
           map = list.get(0);
         }
        SingleConnectionDataSource ds = new SingleConnectionDataSource();
    	
        ds.setDriverClassName(ParseUtil.getString(map.get("driver")));
        ds.setUrl(ParseUtil.getString(map.get("url")));
        ds.setUsername(ParseUtil.getString(map.get("user_name")));
        ds.setPassword(ParseUtil.getString(map.get("password")));
 		
 		return ds;
 	}
    
	/**
	 * added by JBJ 执行SQL或者存储过程
	 * 
	 * @param sql
	 * @param code
	 * @return
	 * @throws Exception
	 */
	public String execute(final Long taskId, final String sql,
			final String sqlType, final Map<Long, UserArgument> procParamTypes,
			final Map<Long, String> procParamValues) throws Exception {
		SingleConnectionDataSource ds = getSingleConnectionDataSource(taskId);
		this.setRtJdbcTemplate(ds);
		String result;
		try {
			// result =
			// (String)this.rtJdbcTemplate.queryForObject(sql,null,java.lang.String.class);
			if (DateSourceParseSQL.SQL_SQL.equals(sqlType)) {

				String msg = "";
				if (sql.trim().toUpperCase().indexOf("SELECT") == 0) {
					List<Map<String, Object>> list = (List<Map<String, Object>>) this.jdbcTemplate
							.queryForList(sql);

					for (Map<String, Object> dataMap : list) {
						Iterator iter = dataMap.entrySet().iterator();
						while (iter.hasNext()) {
							Map.Entry entry = (Map.Entry) iter.next();
							Object key = entry.getKey();
							Object val = entry.getValue();
							msg += "," + key + "=" + val;
						}
						msg += "\r\n<br/>";
					}

					return "SQL(查询)语句：【" + sql + "】执行完成！\r\n<br/>" + msg;

				} else if (sql.trim().toUpperCase().indexOf("UPDATE") == 0) {

					this.rtJdbcTemplate.execute(sql);

					return "SQL(更新)语句：【" + sql + "】执行完成！";
				} else if (sql.trim().toUpperCase().indexOf("INSERT") == 0) {
					this.rtJdbcTemplate.execute(sql);
					return "SQL(插入)语句：【" + sql + "】执行完成！";
				}

				return "SQL语句：" + sql + "执行完成！";

			} else if (DateSourceParseSQL.SQL_PROCEDURE.equals(sqlType)) {
				Object obj = rtJdbcTemplate.execute(new ConnectionCallback() {
					public Object doInConnection(Connection arg0)
							throws SQLException, DataAccessException {
						CallableStatement cs = arg0.prepareCall(sql);

						for (int i = 1; i <= procParamTypes.size(); i++) {
							UserArgument ua = procParamTypes.get(new Long(i));
							String dataType = ua.getDataType();
							String inOut = ua.getInOut();
							String value = procParamValues.get(new Long(i - 1));

							if (inOut.toUpperCase().equals("IN")) {
								if ("VARCHAR2".equals(dataType.toUpperCase())
										|| "VARCHAR".equals(dataType
												.toUpperCase())) {
									if (value == null || value == "") {
										cs.setNull(i, OracleTypes.VARCHAR);
									} else {
										cs.setString(i, value);
									}
								} else if ("NUMBER".equals(dataType
										.toUpperCase())) {
									if (value == null || value == "") {
										cs.setNull(i, OracleTypes.NUMBER);
									} else {
										cs.setLong(i, Long.parseLong(value));
									}
								}
							} else if (inOut.toUpperCase().equals("OUT")) {
								if ("VARCHAR2".equals(dataType.toUpperCase())
										|| "VARCHAR".equals(dataType
												.toUpperCase())) {
									cs.registerOutParameter(i,
											OracleTypes.VARCHAR);
								} else if ("NUMBER".equals(dataType
										.toUpperCase())) {
									cs.registerOutParameter(i,
											OracleTypes.NUMBER);
								}
							}
						}
						ResultSet rs = cs.executeQuery();
						String msg = "";
						msg = "存储过程：" + sql + "执行完成！";
						return msg;
					}
				});

				if (obj instanceof java.lang.String) {
					return obj.toString();
				}
			}
			return null;
		} catch (Exception e) {
			log.error(this.getClass() + "\n---方法：" + new Exception().getStackTrace()[0].getMethodName() + "\n---e.getMessage():\n" + e.toString() + "\n" +e);
			throw new Exception(e.getMessage());
		} finally {
			ds.getConnection().close();
		}
	}
    
	/**
	 * added by JBJ 根据任务参数设置寻找数据库连接信息
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public SingleConnectionDataSource getSingleConnectionDataSource(Long taskId)
			throws Exception {

		String sql = "SELECT t.param_value linkName FROM SYS_PARAM_ALL T WHERE SYSDATE BETWEEN T.ENABLE_DATE AND NVL(T.DISABLE_DATE, SYSDATE) AND t.param_name = 'DB_LINK' AND T.TASK_ID = "
				+ taskId;

		List<Map<String, Object>> list = (List<Map<String, Object>>) this.jdbcTemplate
				.queryForList(sql);
		Map<String, Object> map = new HashMap<String, Object>();
		if (list.size() > 0) {
			map = list.get(0);
		}

		String linkCode = ParseUtil.getString(map.get("linkName"));

		sql = "SELECT dl.dRIVER_CLASS as driver,dl.url ,dl.user_name ,dl.password FROM sys_db_links dl WHERE SYSDATE BETWEEN DL.ENABLE_DATE AND NVL(DL.DISABLE_DATE, SYSDATE) AND DL.LINK_CODE = '"
				+ linkCode + "'";

		list = (List<Map<String, Object>>) this.jdbcTemplate.queryForList(sql);
		map = new HashMap<String, Object>();
		if (list.size() > 0) {
			map = list.get(0);
		}

		SingleConnectionDataSource ds = new SingleConnectionDataSource();

		ds.setDriverClassName(ParseUtil.getString(map.get("driver")));
		ds.setUrl(ParseUtil.getString(map.get("url")));
		ds.setUsername(ParseUtil.getString(map.get("user_name")));
		ds.setPassword(ParseUtil.getString(map.get("password")));

		return ds;
	}
    
	/**
	 * added by JBJ 查找有效的任务参数
	 * 
	 * @param taskId
	 * @return
	 */
	public Map<String, String> findParamListByTaskId(Long taskId) {

		Map<String, String> paramList = new HashMap<String, String>();

		String sql = "SELECT t.param_name pname,t.param_value pvalue FROM SYS_PARAM_ALL T WHERE SYSDATE BETWEEN T.ENABLE_DATE AND NVL(T.DISABLE_DATE, SYSDATE)  AND T.TASK_ID = "
				+ taskId;
		List<Map<String, Object>> list = (List<Map<String, Object>>) this.jdbcTemplate
				.queryForList(sql);

		for (Map<String, Object> dataMap : list) {
			paramList.put((String) dataMap.get("pname"), (String) dataMap
					.get("pvalue"));
		}

		return paramList;
	}
	
	/**
	 * added by JBJ 获取存储过程的参数类型
	 * 
	 * @param PACKAGE_NAME
	 * @param OBJECT_NAME
	 * @return
	 */
	public Map<Long, UserArgument> getProcParamTypes(String PACKAGE_NAME,
			String OBJECT_NAME) {

		Map<Long, UserArgument> paramList = new HashMap<Long, UserArgument>();

		String sql = "SELECT t.POSITION,t.DATA_TYPE,t.IN_OUT FROM SYS.USER_ARGUMENTS t WHERE t.PACKAGE_NAME = '"
				+ PACKAGE_NAME.toUpperCase() + "' AND t.OBJECT_NAME = '" + OBJECT_NAME.toUpperCase() + "' ";

		List<Map<String, Object>> list = (List<Map<String, Object>>) this.jdbcTemplate
				.queryForList(sql);

		for (Map<String, Object> dataMap : list) {

			UserArgument arg = new UserArgument();
			arg.setPosition(Long.parseLong(dataMap.get("POSITION").toString()));
			arg.setDataType((String) dataMap.get("DATA_TYPE"));
			arg.setInOut((String) dataMap.get("IN_OUT"));

			paramList.put(Long.parseLong(dataMap.get("POSITION").toString()),
					arg);
		}

		return paramList;
	}
	
	
	
	/**
     * 设置Sql 指令参数
     * 
     * @param p_stmt
     *            PreparedStatement
     * @param pramets
     *            HashMap
     */
    private PreparedStatement setParamet(PreparedStatement p_stmt,
            HashMap<Integer, Object> pramets) throws ClassNotFoundException,
            SQLException {
        // 如果参数为空
        if (null != pramets) {
            // 如果参数个数为0
            if (0 <= pramets.size()) {
                for (int i = 1; i <= pramets.size(); i++) {
                    try {
                        // 字符类型 String
                        if (pramets.get(i).getClass() == Class
                                .forName("java.lang.String")) {
                            p_stmt.setString(i, pramets.get(i).toString());
                        }
                        // 日期类型 Date
                        if (pramets.get(i).getClass() == Class
                                .forName("java.sql.Date")) {
                            p_stmt.setDate(i, java.sql.Date.valueOf(pramets
                                    .get(i).toString()));
                        }
                        // 布尔类型 Boolean
                        if (pramets.get(i).getClass() == Class
                                .forName("java.lang.Boolean")) {
                            p_stmt.setBoolean(i, (Boolean) (pramets.get(i)));
                        }
                        // 整型 int
                        if (pramets.get(i).getClass() == Class
                                .forName("java.lang.Integer")) {
                            p_stmt.setInt(i, (Integer) pramets.get(i));
                        }
                        // 浮点 float
                        if (pramets.get(i).getClass() == Class
                                .forName("java.lang.Float")) {
                            p_stmt.setFloat(i, (Float) pramets.get(i));
                        }
                        // 双精度型 double
                        if (pramets.get(i).getClass() == Class
                                .forName("java.lang.Double")) {
                            p_stmt.setDouble(i, (Double) pramets.get(i));
                        }

                    } catch (ClassNotFoundException ex) {
                        throw ex;
                    } catch (SQLException ex) {
                        throw ex;
                    }
                }
            }
        }
        return p_stmt;
    }
    
    
	/**
	 * 得到调用存储过程的全名
	 * 
	 * @param procName
	 *            存储过程名称
	 * @return 调用存储过程的全名
	 * @throws Exception
	 */
	public String getProcedureCallName(String procName, int prametCount)
			throws Exception {
		String procedureCallName = "{call " + procName;
		for (int i = 0; i < prametCount; i++) {
			if (0 == i) {
				procedureCallName = procedureCallName + "(?";
			}
			if (0 != i) {
				procedureCallName = procedureCallName + ",?";
			}
		}
		procedureCallName = procedureCallName + ")}";
		return procedureCallName;
	}
    
    
    public void createEmail(MailInfo mailInfo){
    	
    //	this.jdbcTemplate.getJdbcOperations().execute("");
    	
    }
    
	public SimpleJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(SimpleJdbcTemplate jdbcTemplate) {
		
		this.jdbcTemplate = jdbcTemplate;
	}
}
