package com.notice.template;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import com.notice.model.MailInfo;
import com.notice.util.MD5;
import com.notice.util.ParseUtil;
import com.notice.util.ReportTemplateStatic;
import com.notice.work.MailImpl;
import com.notice.work.TaskPageImpl;

public class TemplateParseBase {
	@Resource
	private MailImpl mailImpl;
	@Resource
	private TaskPageImpl taskPageImpl;

	protected boolean ifsendmail = true;

	protected String fileName = "";

	protected String text = "";

	protected MailInfo mailInfo = new MailInfo();

	public boolean isSql = false;

	protected Map<String, Object> taskPageMap = new HashMap<String, Object>();;

	public void beforeParse() {

	}

	public void afterParse(Map<String,Object> params) throws Exception {
		String keyStr = "";
		if (!isSql) {
			if (ParseUtil.isNotNullforString(text) && ParseUtil.isNotNullforString(fileName)) {
				PrintWriter pw;

				pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(ReportTemplateStatic.File_Url + fileName)), true);
				pw.println(text);
				pw.close();

				StringBuilder key = new StringBuilder("");
				key.append(mailInfo.getTaskId()).append("@").append(String.valueOf(System.currentTimeMillis()));

				MD5 m = new MD5();
				keyStr = m.getMD5ofStr(key.toString());
				taskPageImpl.updateTaskKey(mailInfo.getTaskId(), keyStr);
				m = null;
				taskPageImpl.saveTaskPage(mailInfo.getTaskId(), fileName, "");
			}
		}

	//	if (ifsendmail) {
			/*查看历史
			// <a href='http://10.1.65.245:8080/taskBuildPageListFromEmail.do";

			// public static final String PR_Url_end = "' tager='_blank' >�鿴��ʷ</a>";
			StringBuilder sb = new StringBuilder("");
			if (!isSql) {
				sb.append("<a href='")
				// .append(MessageBox.getMessageByKey("trip.global.path.mail.root.url"))
						.append("taskBuildPageListFromEmail.do").append("?code=").append(mailInfo.getTaskId()).append("&key=").append(keyStr).append("' tager='_blank' >查看历史</a>");
			}*/
			mailInfo.setContent(mailInfo.getContent());
			mailImpl.createEmail(mailInfo, params);
		//}
	}

}
