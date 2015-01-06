package com.notice.model;

import java.util.List;

public class ImageHeadData {

	// key值关联图片内容信息
	private String key;
	// 图片发布路径
	private String url;
	// 图片显示高度
	private String height;
	// 图片显示宽度
	private String width;
	// 图片名称
	private String title;
	// 图片类型
	private String type;
	// 图片透明度
	private String alpha;
	
	private String stroke;
	
	private String guide;
	
	private String displayNumber;
	
	
	//柱状图 x轴名称
	private String xLabel;
	
	//柱状图 y轴名称
	private String yLabel;
	
	
	public String getDisplayNumber() {
		return displayNumber;
	}
	public void setDisplayNumber(String displayNumber) {
		this.displayNumber = displayNumber;
	}
	public String getStroke() {
		return stroke;
	}
	public void setStroke(String stroke) {
		this.stroke = stroke;
	}
	public String getxLabel() {
		return xLabel;
	}
	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}
	public String getyLabel() {
		return yLabel;
	}
	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}
	public String getAlpha() {
		return alpha;
	}
	public void setAlpha(String alpha) {
		this.alpha = alpha;
	}
	public String getXLabel() {
		return xLabel;
	}
	public void setXLabel(String label) {
		xLabel = label;
	}
	public String getYLabel() {
		return yLabel;
	}
	public void setYLabel(String label) {
		yLabel = label;
	}
	
	private List<ImageDefData> imageDefData = null;
	
	
	public List<ImageDefData> getImageDefData() {
		return imageDefData;
	}
	public void setImageDefData(List<ImageDefData> imageDefData) {
		this.imageDefData = imageDefData;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getGuide() {
		return guide;
	}
	public void setGuide(String guide) {
		this.guide = guide;
	}
	
	
}
