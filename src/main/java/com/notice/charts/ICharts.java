package com.notice.charts;

import java.awt.Font;
import java.util.List;

import com.notice.model.ImageHeadData;

public interface ICharts {

	public static final int DEFAULT_WIDTH = 500;

	public static final int DEFAULT_HEIGHT = 700;

	public static final Font DEFAULT_TITLE_FONT = new Font("黑体", Font.ITALIC,
			22);

	public static final Font DEFAULT_LEGEND_FONT = new Font("宋体", Font.BOLD,
			12);

	public static final Font DEFAULT_LABEL_FONT = new Font("宋体", Font.BOLD, 12);

	public static final Font DEFAULT_TICK_LABEL_FONT = new Font("宋体", Font.BOLD, 12);

	public Font getTickLabelFont();
	public void setTickLabelFont(Font labelFont);
	
	public Font getLabelFont();
	public void setLabelFont(Font labelFont);

	public Font getLegendFont();
	public void setLegendFont(Font legendFont);

	public Font getTitleFont();
	public void setTitleFont(Font titleFont);

	public String getCategoryAxisLabel();
	public void setCategoryAxisLabel(String categoryAxisLabel);

	public String getValueAxisLabel();
	public void setValueAxisLabel(String valueAxisLabel);

	public String getChartTitle();
	public void setChartTitle(String chartTitle);

	public boolean isVertical();

	public void setVertical(boolean isVertical);

	public boolean isLegend();
	public void setLegend(boolean legend);

	public boolean isTooltips();
	public void setTooltips(boolean tooltips);

	public boolean isUrls();
	public void setUrls(boolean urls);

	public String getFilepath();
	public void setFilepath(String filepath);

	public List<DataSets> getDatas();

	public void setDatas(List<DataSets> datas);

	public void setWidth(int width);
	public void setHeight(int height);
	public int getWidth();
	public int getHeight();

	public String drawChart(ImageHeadData imageHeadData ) throws ChartException;
}
