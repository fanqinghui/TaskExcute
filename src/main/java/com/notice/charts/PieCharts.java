package com.notice.charts;

import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;

import com.notice.model.ImageDefData;
import com.notice.model.ImageHeadData;


public class PieCharts implements ICharts {
	public PieCharts(String title) {
	}

	public String drawChart(ImageHeadData imageHeadData) throws ChartException {
		this.setPropertys(imageHeadData);
		this.chartInit();
		JFreeChart chart = this.createChart();
		//this.pack();
		this.setTitleStyle(chart);
		this.setLegendStyle(chart);
		this.setPlotStyle(chart);
		this.writeChartFile(chart);
		return null;
	}

	public void setPropertys(ImageHeadData imageHeadData ){
		this.setFilepath(imageHeadData.getUrl());
		this.setChartTitle(imageHeadData.getTitle());
		this.setWidth(Integer.valueOf(imageHeadData.getWidth()));
		this.setHeight(Integer.valueOf(imageHeadData.getHeight()));
		this.setTooltips(true);
		this.setUrls(true);
		this.setAlpha(Float.valueOf(imageHeadData.getAlpha() == null || "".equals(imageHeadData.getAlpha()) ? "9" : imageHeadData.getAlpha()));
		this.setGuide(imageHeadData.getGuide() == null || "".equals(imageHeadData.getGuide()) || "1".equals(imageHeadData.getGuide()) ? true : false);
		
		List<DataSets> datas = new ArrayList<DataSets>();
		
		List<ImageDefData> defList = imageHeadData.getImageDefData();
		for (ImageDefData imageDefData : defList) {
			DataSets data = new DataSets();
			data.setCategory(imageDefData.getCategory());
			data.setName(imageDefData.getName());
			data.setValue(Double.valueOf(imageDefData.getValue()==null||"".equals(imageDefData.getValue())?"0":imageDefData.getValue()));
			datas.add(data);
		}
		this.setDatas(datas);
	}
	
	private void chartInit() {
		// TODO Auto-generated method stub
	}

	/**
	 * 填写将图表数据
	 * 
	 * @param list
	 * @return
	 */
	private DefaultPieDataset createDataset() {
		DefaultPieDataset localDefaultPieDataset = new DefaultPieDataset();
		List<DataSets> datas = this.getDatas() == null ? new ArrayList<DataSets>()
				: this.getDatas();
		for (DataSets data : datas) {
			localDefaultPieDataset.setValue(data.getName(), data.getValue());
		}
		return localDefaultPieDataset;
	}

	/**
	 * 将生成的图表写入文件中
	 * 
	 * @param chart
	 * @throws ChartException
	 */
	private void writeChartFile(JFreeChart chart) throws ChartException {
		// 输出文件
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(this.getFilepath());
		} catch (FileNotFoundException e) {
			throw new ChartException("图片文件不存在！" + e.getMessage());
		}
		try {
			ChartUtilities.writeChartAsJPEG(fos, // 文件流
					chart, // 统计图表对象
					this.getWidth(), // 图片宽度
					this.getHeight() // 图片高度
					);
		} catch (IOException e) {
			throw new ChartException("图片文件写入发生异常！" + e.getMessage());
		}

		try {
			fos.close();
		} catch (IOException e) {
			throw new ChartException("图片文件流无法正常关闭！" + e.getMessage());
		}
	}

	private JFreeChart createChart() {
		JFreeChart localJFreeChart = ChartFactory.createPieChart(this
				.getChartTitle(), createDataset(),
				this.isLegend(), this
						.isTooltips(), this.isUrls());
		return localJFreeChart;
	}

	/**
	 * 重新设置图表标题，改变字体样式
	 * 
	 * @param chart
	 * @throws ChartException
	 */
	private void setTitleStyle(JFreeChart chart) throws ChartException {
		if (chart == null)
			throw new ChartException("chart未正确创建，设置图表标题样式时异常！");
		chart
				.setTitle(new TextTitle(this.getChartTitle(), this
						.getTitleFont()));

	}

	/**
	 * 设置图例字体样式
	 * 
	 * @param chart
	 * @throws ChartException
	 */
	private void setLegendStyle(JFreeChart chart) throws ChartException {
		if (chart == null)
			throw new ChartException("chart未正确创建，设置图例样式时异常！");
		// 修改图例的字体
		chart.getLegend().setItemFont(this.getLegendFont());
	}
	
	private void setPlotStyle(JFreeChart chart) throws ChartException {
		if (chart == null)
			throw new ChartException("chart未正确创建，设置图表样式时异常！");
		// 获得饼图的Plot对象
		PiePlot plot = (PiePlot) chart.getPlot();
		// 设置饼图各部分的标签字体
		plot.setLabelFont(this.getLabelFont());
		// 设置透明度（0-1.0之间）
		plot.setBackgroundAlpha(this.getAlpha());
		// 忽略无值的分类
		plot.setIgnoreNullValues(true);
		// 分类标签的格式，设置成null则整个标签包括连接线都不显示
		if (this.isGuide()) {

		} else {
			plot.setLabelGenerator(null);
		}
		
	}

	private int width;

	private int height;

	private List<DataSets> datas = null;

	private String filepath = null;

	private String chartTitle = null;

	private String categoryAxisLabel = null;

	private String valueAxisLabel = null;

	private boolean isVertical = true;

	private boolean legend = true;

	private boolean tooltips = true;

	private boolean urls = true;

	private Font titleFont = null;

	private Font legendFont = null;
	
	private Font labelFont = null;
	
	private Font tickLabelFont = null;
	
	private float alpha;
	
	private boolean guide = true;

	public boolean isGuide() {
		return guide;
	}

	public void setGuide(boolean guide) {
		this.guide = guide;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public Font getTickLabelFont() {
		return tickLabelFont == null ? BarCharts.DEFAULT_TICK_LABEL_FONT
				: this.tickLabelFont;
	}

	public void setTickLabelFont(Font tickLabelFont) {
		this.tickLabelFont = tickLabelFont;
	}

	public Font getLabelFont() {
		return labelFont == null ? BarCharts.DEFAULT_LEGEND_FONT
				: this.labelFont;
	}

	public void setLabelFont(Font labelFont) {
		this.labelFont = labelFont;
	}

	public Font getLegendFont() {
		return legendFont == null ? BarCharts.DEFAULT_LEGEND_FONT
				: this.legendFont;
	}

	public void setLegendFont(Font legendFont) {
		this.legendFont = legendFont;
	}

	public Font getTitleFont() {
		return titleFont == null ? BarCharts.DEFAULT_TITLE_FONT
				: this.titleFont;
	}

	public void setTitleFont(Font titleFont) {
		this.titleFont = titleFont;
	}

	public String getCategoryAxisLabel() {
		return categoryAxisLabel;
	}

	public void setCategoryAxisLabel(String categoryAxisLabel) {
		this.categoryAxisLabel = categoryAxisLabel;
	}

	public String getValueAxisLabel() {
		return valueAxisLabel;
	}

	public void setValueAxisLabel(String valueAxisLabel) {
		this.valueAxisLabel = valueAxisLabel;
	}

	public String getChartTitle() {
		return chartTitle;
	}

	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
	}

	public boolean isVertical() {
		return isVertical;
	}

	public void setVertical(boolean isVertical) {
		this.isVertical = isVertical;
	}

	public boolean isLegend() {
		return legend;
	}

	public void setLegend(boolean legend) {
		this.legend = legend;
	}

	public boolean isTooltips() {
		return tooltips;
	}

	public void setTooltips(boolean tooltips) {
		this.tooltips = tooltips;
	}

	public boolean isUrls() {
		return urls;
	}

	public void setUrls(boolean urls) {
		this.urls = urls;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public List<DataSets> getDatas() {
		return datas;
	}

	public void setDatas(List<DataSets> datas) {
		this.datas = datas;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return this.width == 0 ? DEFAULT_WIDTH : this.width;
	}

	public int getHeight() {
		return this.height == 0 ? DEFAULT_HEIGHT : this.height;
	}

	public static void main(String[] paramArrayOfString) {
		ICharts chart = new PieCharts("JFreeChart: BarCharts");
		
		chart.setFilepath("D:\\chart.jpg");
		chart.setChartTitle("PR费用");
		chart.setWidth(900);
		chart.setHeight(900);
		chart.setTooltips(false);
		chart.setUrls(false);
		
		List<ImageDefData> datas = new ArrayList<ImageDefData>();
		ImageDefData data = new ImageDefData();
		//data.setCategory("ERP");
		data.setName("马子俊");
		data.setValue("500");
		datas.add(data);data = new ImageDefData();
		//data.setCategory("ERP");
		data.setName("邹磊");
		data.setValue("1100");
		datas.add(data);data = new ImageDefData();
		//data.setCategory("ERP");
		data.setName("靳邦杰");
		data.setValue("1200");
		datas.add(data);data = new ImageDefData();
		//data.setCategory("ES");
		data.setName("王炎");
		data.setValue("1400");
		datas.add(data);data = new ImageDefData();
		//data.setCategory("ES");
		data.setName("***");
		data.setValue("1010");
		datas.add(data);data = new ImageDefData();
		//data.setCategory("ES");
		data.setName("1212");
		data.setValue("1100");
		datas.add(data);data = new ImageDefData();
		//data.setCategory("ES");
		data.setName("1213");
		//data.setValue("0");
		datas.add(data);
		try {
			ImageHeadData ihd = new ImageHeadData();
			ihd.setUrl("D:\\chart.jpg");
			ihd.setTitle("PR费用");
			ihd.setHeight("400");
			ihd.setWidth("500");
			ihd.setAlpha("0");
			ihd.setGuide("no");
			ihd.setImageDefData(datas);
			chart.drawChart(ihd);
		} catch (ChartException e) {
		}
	}

}
