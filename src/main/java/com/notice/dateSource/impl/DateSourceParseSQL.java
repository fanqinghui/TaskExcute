package com.notice.dateSource.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.notice.dateSource.DateSourceParseBase;
import com.notice.dateSource.IDateSourceParse;
import com.notice.model.UserArgument;
import com.notice.util.ParseUtil;


public class DateSourceParseSQL extends DateSourceParseBase implements
		IDateSourceParse {

	private static final Log log = LogFactory.getLog(DateSourceParseSQL.class);
	
	public static final String SQL_SQL = "S";

	public static final String SQL_FUNCTION = "F";

	public static final String SQL_PROCEDURE = "P";

	public static final String[] replaceArr = { "$SQL:", "$SQL：", "$P:", "$P：",
			"$F:", "$F：" };

	public Map<String, Object> doParse(Map<String, Object> map)
			throws Exception {
		// TODO Auto-generated method stub

		Map<String, Object> rets = new HashMap<String, Object>();

		Long taskId = null;

		String sql = "";
		if (map != null) {

			taskId = Long.parseLong(map.get("id").toString());

			String content = (String) map.get("content");

			if (ParseUtil.isNotNullforString(content)) {
				sql = content;
			}
		}

		// SQL模板类型，SQL语句或存储过程
		String sqlType = SQL_SQL;

		// 存储过程参数类型
		Map<Long, UserArgument> procParamTypes = null;

		// 存储过程参数值
		Map<Long, String> procParamValues = null;

		if (ParseUtil.isNotNullforString(sql)) {
			if (sql.toLowerCase().indexOf("$sql:") >= 0) {
				sqlType = SQL_SQL;
			} else if (sql.toLowerCase().indexOf("$p:") >= 0) {
				sqlType = SQL_PROCEDURE;
			} else if (sql.toLowerCase().indexOf("$f:") >= 0) {
				sqlType = SQL_FUNCTION;
			}

			for (String s : replaceArr) {
				sql = sql.replace(s, "");
			}

			sql = sql.toUpperCase();

			// 去掉回车符
			sql = this.delEnterKey(sql);

			// 获取任务参数
			Map<String, String> paramList = this.reportTemplateService
					.findParamListByTaskId(taskId);

			// 存储过程参数

			// 拼装sql
			if (paramList != null && paramList.size() > 0) {

				if (sqlType.equals(SQL_SQL)) {
					// sql替换方式
					Iterator iter = paramList.entrySet().iterator();
					while (iter.hasNext()) {
						Map.Entry entry = (Map.Entry) iter.next();
						Object key = entry.getKey();
						Object val = entry.getValue();
						sql = sql.replace(":" + key.toString().toUpperCase(),
								val.toString());
					}

				} else if (sqlType.equals(SQL_PROCEDURE)) {

					// 获取存储过程的类型
					procParamTypes = new HashMap<Long, UserArgument>();
					String s_1 = "";
					int beginIndex = sql.toUpperCase().indexOf("CALL")
							+ "CALL".length();
					int endIndex = sql.indexOf("(", beginIndex);
					s_1 = sql.substring(beginIndex, endIndex).toString().trim();

					String PACKAGE_NAME = s_1.split("[.]")[0];
					String OBJECT_NAME = s_1.split("[.]")[1];

					procParamTypes = this.reportTemplateService
							.getProcParamTypes(PACKAGE_NAME, OBJECT_NAME);

					// 存储过程的值
					procParamValues = new HashMap<Long, String>();
					beginIndex = sql.indexOf("(") + "(".length();
					endIndex = sql.indexOf(")", beginIndex);
					String s_2 = sql.substring(beginIndex, endIndex).toString()
							.trim().replace(":", "");
					String[] s_arr_2 = s_2.split(",");

					for (int i = 0; i < s_arr_2.length; i++) {
						if (paramList.get(s_arr_2[i]) == null) {
							procParamValues.put(Long.parseLong(i + ""), null);
						} else {
							procParamValues.put(Long.parseLong(i + ""),
									paramList.get(s_arr_2[i].trim()));
						}
					}

					sql = this.reportTemplateService.getProcedureCallName(
							PACKAGE_NAME + "." + OBJECT_NAME, procParamTypes
									.size());

				}

			}

		}

		if (ParseUtil.isNotNullforString(sql)) {
			log.info("\n---exe sql : " + sql);
			String retMsg = this.reportTemplateService.execute(taskId, sql,
					sqlType, procParamTypes, procParamValues);

			rets.put("msg", retMsg);
		}

		return rets;
	}

	/**
	 * 去掉回车符
	 * 
	 * @param s
	 * @return
	 */
	public String delEnterKey(String s) {
		String result = "";
		for (int i = 0; i < s.length(); i++) {
			if (s.codePointAt(i) != 10 && s.codePointAt(i) != 13) {
				result += s.charAt(i);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		String s = "{call PS_CPR_AUTORUN_EXEC_PKG.PR_BATCH_ADJUST(:ERRBUF,:RETCODE,:USER_ID,:DEPT_IDS,ADJUST_MONTH,:ADJUST_MODULE,:ADJUST_WAY)}";
		int beginIndex = s.toUpperCase().indexOf("(") + "(".length();
		int endIndex = s.indexOf(")", beginIndex);
		System.out.println(s.substring(beginIndex, endIndex).toString().trim()
				.replace(":", ""));

	}

}
