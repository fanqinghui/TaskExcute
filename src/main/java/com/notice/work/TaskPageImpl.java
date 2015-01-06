package com.notice.work;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.notice.model.SysTaskAll;
import com.notice.model.SysTaskPageAll;
import com.notice.util.CheckUtils;

@Service
public class TaskPageImpl {
	@Resource
	private TaskHandler taskHandler;
	
	public void updateTaskKey(Long taskId, String key)
	       throws Exception {
		//SysTaskAll task = taskHandler.getTaskById(taskId);
		//task.setATTRIBUTE1(key);
		taskHandler.updateTaskKey(taskId,key);
	}

	
	@SuppressWarnings("unused")
	public void saveTaskPage(Long taskId, String pageName, String desc)
			throws Exception {

		SysTaskPageAll page = new SysTaskPageAll();
		if (taskId == null || CheckUtils.checkStringNull(pageName)) {
			throw new Exception("保存任务页面对象失败");
		}
		page.setTASK_ID(new BigDecimal(taskId));
		page.setPAGE_NAME(pageName);
		page.setDISABLED("0");
		page.setDESCRIPTION(desc);
		page.setCREATED_BY(new BigDecimal(-1));
		page.setCREATION_DATE(new Date());
		page.setLAST_UPDATE_DATE(new Date());
		page.setLAST_UPDATED_BY(new BigDecimal(-1));
		taskHandler.saveTaskPageAll(page);
	}

}
