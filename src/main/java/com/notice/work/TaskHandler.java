package com.notice.work;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;
import org.springframework.stereotype.Service;

import com.notice.model.SysDataSource;
import com.notice.model.SysEmailSend;
import com.notice.model.SysTaskAll;
import com.notice.model.SysTaskPageAll;

/**
 * @author fanqinghui100@126.com
 *
 */
@SuppressWarnings("deprecation")
@Service
public class TaskHandler extends SqlMapClientDaoSupport  {
	/**
	 * 测试
	* @Title: getCount 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @return Object    返回类型 
	* @throws
	 */
	public Object getCount(){
		return getSqlMapClientTemplate().queryForObject("SYS_TASK_ALL.getCount");
	}
	/**
	 * 
	* @Title: getTaskById 
	* @Description: 根据任务id获取任务实体
	* @return SysTaskAll    返回类型 
	* @throws
	 */
	public SysTaskAll getTaskById(Long TASK_ID) throws Exception{
		return (SysTaskAll) getSqlMapClientTemplate().queryForObject("SYS_TASK_ALL.selectByPrimaryKey",TASK_ID);
	}
	/**
	 * 
	* @Title: updateTaskStatus 
	* @Description: 根据任务id，更新任务状态 
	* @return void    返回类型 
	* @throws
	 */
	public void updateTaskStatus(Long taskId, String state) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("taskId", taskId);
		map.put("state", state);
		getSqlMapClientTemplate().update("SYS_TASK_ALL.updateTaskStatus", map);
	}
	/**
	 * 
	* @Title: getFileRelations 
	* @Description: 获取任务相关 模板信息，路径 类型等
	* @return List<Map<String,Object>>    返回类型 
	* @throws
	 */
	public List<Map<String, Object>> getFileRelations(Long taskId) {
		return getSqlMapClientTemplate().queryForList("SYS_FILE_RELATIONS.getFileRelations",taskId);
	}
	/**
	 * 
	* @Title: getParamAllByTaskId 
	* @Description: 根据任务id获取参数
	* @return List<Map<String,Object>>    返回类型 
	* @throws
	 */
	
	public List<Map<String, Object>> getParamAllByTaskId(Long taskId) {
		return getSqlMapClientTemplate().queryForList("SYS_PARAM_ALL.getParamAll", taskId);
	}
	
	/**
	 * 
	* @Title: updateTaskKey 
	* @Description: 更新任务key-attribute1
	* @return void    返回类型 
	* @throws
	 */
	public void updateTaskKey(Long taskId, String key) {
		Map<String,Object> map=new HashMap<String, Object>();
		map.put("taskId", taskId);
		map.put("key", key);
		getSqlMapClientTemplate().update("SYS_TASK_ALL.updateTaskKey", map);
	}
	/**
	* @Title: saveTaskPageAll 
	* @Description:保存taskpageall
	* @return void    返回类型 
	* @throws
	 */
	public void saveTaskPageAll(SysTaskPageAll page) {
		getSqlMapClientTemplate().insert("SYS_TASK_PAGE_ALL.saveTaskPageAll", page);

	}
	/**
	* @Title: EmailSendEntity 
	* @Description: 保存emailSend实体
	* @return void    返回类型 
	* @throws
	 */
	public void saveEmailSendEntity(SysEmailSend entity) {
		getSqlMapClientTemplate().insert("SYS_EMAIL_SEND.emailSend_insert",entity);
		
	}
	/**
	 * 
	* @Title: getSqlByCode 
	* @Description: 查询sql code
	* @return List<SysDataSource>    返回类型 
	* @throws
	 */
	public List<SysDataSource> getSqlByCode(String code) {
		return getSqlMapClientTemplate().queryForList("SYS_DATA_SOURCE.getSqlByCode",code);
	}
	/**
	 * 
	* @Title: executeSql 
	* @Description: 执行 动态sql
	* @return String    返回类型 
	* @throws
	 */
	public String executeSql(String sql) {
		return (String) getSqlMapClientTemplate().queryForObject(sql);
	}
}
