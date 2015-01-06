package com.notice.model;

import java.util.HashSet;
import java.util.Set;

public class MailInfo {
	
	private Long taskId ;
	
	// 模板图表中图片文件本地全路径
	private Set<String> imgFileList = new HashSet<String>();
	
	private String content ;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}


	public Set<String> getImgFileList() {
		return imgFileList;
	}

	public void setImgFileList(Set<String> imgFileList) {
		this.imgFileList = imgFileList;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
