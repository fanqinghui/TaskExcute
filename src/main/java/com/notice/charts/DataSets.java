package com.notice.charts;

import java.io.Serializable;

public class DataSets implements Serializable{
	
	private static final long serialVersionUID = -627097050089801346L;

	private String name;
	
	private String category;
	
	private Double value;

	public String getName() {
		return name == null || "".equals(name.trim()) ? "" : name.trim();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category == null || "".equals(category.trim()) ? "" : category.trim();
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getValue() {
		return value == null ? 0d : value;
	}

	public void setValue(Double value) {
		this.value = value;
	}
	
	

}
