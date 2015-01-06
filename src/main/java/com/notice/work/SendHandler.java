package com.notice.work;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.notice.model.SysEmailSend;
import com.notice.model.SysTaskAll;
import com.notice.template.ITemplateParse;
import com.notice.template.TemplateParseFactory;
import com.notice.util.CheckUtils;
import com.notice.util.Constains;
import com.notice.util.ParseUtil;
import com.notice.util.PropertiesManager;

@Scope("prototype")
@Controller
public class SendHandler {
	@Resource
	private TaskHandler taskHandler;
	@Resource
	private TemplateParseFactory templateParseFactory;
	private static final Logger logger = Logger.getLogger(SendHandler.class);
	
	//pulic入口
	/**
	* @Title: taskExcute 
	* @Description: 
	* @return void    返回类型 
	* @throws
	 */
	public String taskExcute(Long taskId,String subject,String cc, String recipients,String errorRecipient)throws Exception {
		if (taskId == null) {
			throw new Exception("任务id不能为空");
		}
		if (recipients == null || recipients.length() == 0) {
			throw new Exception("收件人地址不能为空");
		}
		if (errorRecipient == null || errorRecipient.length() == 0) {
			throw new Exception("邮件系统错误-收件人地址不能为空");
		}
		SysTaskAll task = taskHandler.getTaskById(taskId);
		// 任务状态检测
		//step2 private 验证
		if (CheckUtils.checkStringNull(task.getNODE_STATUS()) || "0".equals(task.getNODE_STATUS())) {
			throw new Exception("新建状态的任务【" + task.getTASK_NAME() + "】不可以执行!");
		}
		if (CheckUtils.checkStringNotNull(task.getSTATUS()) && "2".equals(task.getSTATUS())) {
			throw new Exception("任务【" + task.getTASK_NAME() + "】正在执行!");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("taskId", taskId);
		map.put("subject", subject);
		map.put("cc", cc);
		map.put("recipients", recipients);
		map.put("errorRecipient", errorRecipient);
		//private
		return taskExcute(map);
	}
	
	/*‘0’ || null  未执行
    ‘1’  执行完成
 	‘2’  执行中
    ‘3’  执行失败*/
	private String taskExcute(Map<String, Object> map) throws Exception {
		Long userId=0L;
		long taskId=Long.valueOf(map.get("taskId").toString());
		
		
		// 更新状态为-任务执行中,2  TODO:需要开启
		 //taskHandler.updateTaskStatus(taskId,Constains.STATE_2);
		
		/* 任务执行 */
		String exeResult = "1";
		 try {
			Map<String, Object> retMap = exeTemplate(map);// 模板执行
			//错误处理
			if (retMap != null) {
				String isError = retMap.get("isError") != null ? retMap.get("isError").toString() : null;
				if (isError != null && "true".equals(isError)) {
					exeResult = "3";
					String errMsg = retMap.get("errorMessage") == null ? null : retMap.get("errorMessage").toString();
					taskHandler.saveEmailSendEntity(getAnErrEmail(userId, new BigDecimal(taskId), errMsg,map.get("errorRecipient").toString()));
					return errMsg;
				}
			}
		  } catch (Exception e) { 
			  exeResult = "3";
			taskHandler.saveEmailSendEntity(getAnErrEmail(userId, new BigDecimal(taskId), e.toString(),map.get("errorRecipient").toString()));
			return e.toString();
		  }
		  try {
			taskHandler.updateTaskStatus(taskId,exeResult);
		  } catch (Exception ee) { 
			  logger.error("task 状态更新失败"+ee.toString());
			  throw new Exception("task 状态更新失败！"+ee.toString());
		  }
		  return "success";
	}

	//step3：执行模板
	private Map<String, Object> exeTemplate(Map<String, Object> map) {
		logger.info("模板执行");
		Map<String, Object> resultmap = new HashMap<String, Object>();
		try {
			Long id = (Long) map.get("taskId");// id就是taskId
			Map<String, Object> templateMap = this.getTemplate(id);//ok。测试完毕
			String fileName = (String) templateMap.get("name");
			//fileName="D:\\trip_approver.html";//TODO:测试用
			String content =ParseUtil.readFileByLines(fileName);
			logger.info("文件内容"+content);
			String fileType = (String) templateMap.get("type");

			Map<String, Object> parseMap = new HashMap<String, Object>();
			parseMap.put("content", content);
			parseMap.put("type", fileType);
			parseMap.put("propertyParamMap", templateMap.get("propertyParamMap"));
			parseMap.put("id", id.toString());
			parseMap.put("sendEmail", templateMap.get("sendEmail"));

			// 根据模板类型调用接口解析模板
			 ITemplateParse templateParse = templateParseFactory.getTemplateParseImpl(fileType);
			 resultmap = templateParse.doParse(parseMap,map);
			logger.info(content);
			return resultmap;
		} catch (Exception e) {
			logger.error(e.toString());
			resultmap.put("isError", "true");
			resultmap.put("errorMessage", e.getMessage());
		}
		return resultmap;
	}

	/*
	 * step4：获得模板信息，路径 类型等
	 */
	public Map<String, Object> getTemplate(Long id) throws Exception {
		logger.info("step4：获得模板信息，路径 类型等");
		// 上传文件与系统单据关系表
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> list = taskHandler.getFileRelations(id);
		if (list.size() > 0) {
			Map<String, Object> map = list.get(0);
			// 获取模板类型和路径
			//resultMap.put("name", ParseUtil.getString(map.get("URL")) + "/" + ParseUtil.getString(map.get("NAME")));
			resultMap.put("name", ParseUtil.getString(map.get("URL")) + ParseUtil.getString(map.get("NAME")));
			resultMap.put("type", map.get("TYPE"));
			resultMap.put("sendEmail", map.get("SENDEMAIL"));
		} else {
			throw new Exception("getTemplate出错，数据源code不存在！");
		}

		Map<String, String> propertyParamMap = new HashMap<String, String>();
		// 获取任务关联参数信息供模板解析调用
		List<Map<String, Object>> paramList = taskHandler.getParamAllByTaskId(id);

		for (Map<String, Object> map : paramList) {
			propertyParamMap.put(ParseUtil.getString(map.get("key")), ParseUtil.getString(map.get("value")));
		}

		resultMap.put("propertyParamMap", propertyParamMap);
		return resultMap;
	}
	
	private SysEmailSend getAnErrEmail(Long userId, BigDecimal taskId,
			String errorMessage,String errorRecipient) {
		SysEmailSend errEmail = new SysEmailSend();
		errEmail.setBIZ_ID(taskId);
		errEmail.setMODULE_CODE("NOTICE_MAIL");
		errEmail.setTYPE_CODE("NOTICE");
		errEmail.setTITLE("错误邮件系统通知!");
		errEmail.setRECIPIENTS(errorRecipient);//设置错误的邮件接收人
		errEmail.setCONTENT(errorMessage);
		errEmail.setCREATED_BY("PS");
		errEmail.setCREATION_DATE(new Date());
		errEmail.setLAST_UPDATED_BY("PS");
		errEmail.setSH_SEND_MAIL("N");
		errEmail.setCOMMENT1(" ");
		errEmail.setLAST_UPDATE_DATE(new Date());
		return errEmail;
	}
}