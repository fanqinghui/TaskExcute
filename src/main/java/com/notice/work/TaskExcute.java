package com.notice.work;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TaskExcute {
	private ApplicationContext applicationContext;
		//main
		public String taskExcute(Long taskId,String subject,String cc, String recipients,String errorRecipient)throws Exception {
			    applicationContext = new ClassPathXmlApplicationContext(new String[] { "spring.xml", "spring-db.xml" });
				SendHandler sendHandler=(SendHandler) applicationContext.getBean("sendHandler");
			return sendHandler.taskExcute(taskId, subject, cc, recipients, errorRecipient); 
		}
}
