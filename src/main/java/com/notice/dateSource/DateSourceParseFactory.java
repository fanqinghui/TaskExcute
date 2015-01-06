package com.notice.dateSource;

import java.util.HashMap;
import java.util.Map;

public class DateSourceParseFactory {
	
	private Map<String, IDateSourceParse> dateSourceParseClass = new HashMap<String, IDateSourceParse>();
	
	public IDateSourceParse getDateSourceParseImpl(String type) {
		return dateSourceParseClass.get(type);
	}

	public Map<String, IDateSourceParse> getDateSourceParseClass() {
		return dateSourceParseClass;
	}

	public void setDateSourceParseClass(
			Map<String, IDateSourceParse> dateSourceParseClass) {
		this.dateSourceParseClass = dateSourceParseClass;
	}

	
}
