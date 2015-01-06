/*import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.notice.model.SysTaskAll;
import com.notice.work.SendHandler;
import com.notice.work.TaskExcute;
import com.notice.work.TaskHandler;

public class Test {


	public static void main(String[] args) {
		try {
			 ApplicationContext	applicationContext = new ClassPathXmlApplicationContext(new String[] { "spring.xml", "spring-db.xml" });
			TaskHandler handler=(TaskHandler) applicationContext.getBean("taskHandler");
			SendHandler sendHandler=(SendHandler) applicationContext.getBean("sendHandler");
			SysTaskAll task=handler.getTaskById(20L);
			System.out.println(task.getTASK_NAME());
			String message=sendHandler.taskExcute(20L,"测试题目","@126.com","@24.com","@222.com");
			System.out.println(message);
			SysTaskAll task2=handler.getTaskById(20L);
			System.out.println(task2.getTASK_NAME()+"-"+task2.getSTATUS());
			TaskExcute taskExcute=new TaskExcute();
			String tString=taskExcute.taskExcute(20L,"测试题目","@126.com","@24.com","@222.com");
			System.out.println(tString);
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
*/