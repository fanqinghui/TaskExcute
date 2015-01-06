package com.notice.work;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.notice.model.MailInfo;
import com.notice.model.SysEmailSend;
import com.notice.model.SysTaskAll;
import com.notice.util.CheckUtils;
@Service
public class MailImpl {
	@Resource
	private TaskHandler taskHandler;
	private static final Logger logger = Logger.getLogger(MailImpl.class);
	
	public void createEmail(MailInfo mailInfo,Map<String, Object> params) throws Exception {
		logger.info("保存SysEmailSend对象");
		SysEmailSend entity = new SysEmailSend();
		entity.setBIZ_ID(new BigDecimal(mailInfo.getTaskId()));
		entity.setMODULE_CODE("NOTICE_MAIL");
		entity.setTYPE_CODE("NOTICE");

		SysTaskAll task = taskHandler.getTaskById(mailInfo.getTaskId());
		if (task == null) {
			return;
		}
		if (CheckUtils.checkLongNull(task.getIS_SEND_EMAIL())
				|| task.getIS_SEND_EMAIL() ==new BigDecimal(0)) {
			return;
		}


		entity.setRECIPIENTS(params.get("recipients").toString());//右键接收人
		if(params.get("cc  ")!=null){
			entity.setCARBON_COPYS(params.get("cc").toString());
		}
		if(params.get("subject")!=null){//重新设置title
			entity.setTITLE(params.get("subject").toString());
		}else{
			entity.setTITLE(task.getEMAIL_TITLE());
		}
		StringBuilder attachFileName = new StringBuilder("");
		
		Set<String> imgFileList = mailInfo.getImgFileList();
		if (imgFileList != null && imgFileList.size() > 0) {
			for (String string : imgFileList) {
				attachFileName.append(string).append(";");
			}
			entity.setATTACH_FILE_NAME(attachFileName.substring(0,
					attachFileName.length() - 1).toString());
		}
		
		entity.setCONTENT(mailInfo.getContent());
		entity.setCREATED_BY("PS");
		entity.setCREATED_BY("PS");
		entity.setCREATION_DATE(new Date());
		entity.setLAST_UPDATE_DATE(new Date());
		entity.setLAST_UPDATED_BY("PS");
		entity.setSH_SEND_MAIL("N");
		entity.setCOMMENT1(" ");
		taskHandler.saveEmailSendEntity(entity);
	}
}
