package com.notice.template;

import java.util.HashMap;
import java.util.Map;

public class TemplateParseFactory {
	
	private Map<String, ITemplateParse> templateParseClass = new HashMap<String, ITemplateParse>();
	
	public ITemplateParse getTemplateParseImpl(String type) {
		return templateParseClass.get(type);
	}

	public Map<String, ITemplateParse> getTemplateParseClass() {
		return templateParseClass;
	}

	public void setTemplateParseClass(Map<String, ITemplateParse> templateParseClass) {
		this.templateParseClass = templateParseClass;
	}
	
	
}
