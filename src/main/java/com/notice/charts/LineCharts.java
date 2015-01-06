package com.notice.charts;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.DefaultCategoryDataset;

import com.notice.model.ImageDefData;
import com.notice.model.ImageHeadData;


public class LineCharts implements ICharts {
	public LineCharts(String title) {
		//super(title);
		// TODO Auto-generated constructor stub
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
		this.setStrok(Float.valueOf(imageHeadData.getStroke() == null || "".equals(imageHeadData.getStroke()) ? "1" : imageHeadData.getStroke()));
		this.setDisplayNumber((imageHeadData.getDisplayNumber() == null || "".equals(imageHeadData.getDisplayNumber()) ? "0" : imageHeadData.getDisplayNumber()));
		this.setValueAxisLabel(imageHeadData.getyLabel());
		this.setCategoryAxisLabel(imageHeadData.getxLabel());
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
	private DefaultCategoryDataset createDataset() {
		DefaultCategoryDataset localCategoryDataset = new DefaultCategoryDataset();
		List<DataSets> datas = this.getDatas() == null ? new ArrayList<DataSets>()
				: this.getDatas();
		for (DataSets data : datas) {
			localCategoryDataset.addValue(data.getValue(), data.getName(), data.getCategory());
		}
		return localCategoryDataset;
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
		JFreeChart localJFreeChart = ChartFactory.createLineChart(this
				.getChartTitle(), this.getCategoryAxisLabel(), this.getValueAxisLabel(), createDataset(), 
				this.isVertical() == true ? PlotOrientation.VERTICAL
						: PlotOrientation.HORIZONTAL, this.isLegend(), this
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
		// 获得线性图的Plot对象
	    CategoryPlot localCategoryPlot = (CategoryPlot)chart.getPlot();
	    localCategoryPlot.setBackgroundAlpha(this.getAlpha());
	    
	    BasicStroke stroke = new BasicStroke();
	    localCategoryPlot.setRangeGridlinesVisible(true) ;//数据轴网格是否可见
	    localCategoryPlot.setRangeGridlinePaint(Color.black)   ;//数据轴网格线条颜色
	    //localCategoryPlot.setRangeGridlineStroke(stroke) ;//数据轴网格线条笔触

	    //localCategoryPlot.setDomainGridlinesVisible(true) ;//数据轴网格是否可见
	    //localCategoryPlot.setDomainGridlinePaint(Color.black)   ;//数据轴网格线条颜色
	    //localCategoryPlot.setDomainGridlineStroke(stroke);
	    CategoryAxis localCategoryXAxis = (CategoryAxis)localCategoryPlot.getDomainAxis();
	    NumberAxis localNumberYAxis = (NumberAxis)localCategoryPlot.getRangeAxis();
	    localCategoryXAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 12));
	    localCategoryXAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
	    localNumberYAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	    localNumberYAxis.setTickLabelFont(new Font("宋体", Font.PLAIN, 12));
	    localNumberYAxis.setLabelFont(new Font("宋体", Font.PLAIN, 12));
	    
	    LineAndShapeRenderer localLineAndShapeRenderer = (LineAndShapeRenderer)localCategoryPlot.getRenderer();
	    //localLineAndShapeRenderer.setDrawLines(true);//    是否折线的数据点之间用线连
	    //localLineAndShapeRenderer.setDrawShapes(true);//   是否折线的数据点根据分类使用不同的形状
	    localLineAndShapeRenderer.setStroke(new BasicStroke(this.getStrok()));//这个是设置线条的粗细
	    
	    if("1".equals(this.getDisplayNumber())){
	    localLineAndShapeRenderer.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());//折线上显示数字
	    localLineAndShapeRenderer.setBaseItemLabelsVisible(true);
	    }

	    localLineAndShapeRenderer.setBaseShapesVisible(true);
	    localLineAndShapeRenderer.setBaseShapesFilled(true);
	    localLineAndShapeRenderer.setDrawOutlines(true);
	    localLineAndShapeRenderer.setUseFillPaint(true);
	    localLineAndShapeRenderer.setBaseFillPaint(Color.white);	
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
	
	private float strok;
	
	private String displayNumber = null;

	public float getStrok() {
		return strok;
	}

	public void setStrok(float strok) {
		this.strok = strok;
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public String getDisplayNumber() {
		return displayNumber;
	}

	public void setDisplayNumber(String displayNumber) {
		this.displayNumber = displayNumber;
	}

	public Font getTickLabelFont() {
		return tickLabelFont == null ? BarCharts.DEFAULT_TICK_LABEL_FONT
				: this.tickLabelFont;
	}

	public void setTickLabelFont(Font tickLabelFont) {
		this.tickLabelFont = tickLabelFont;
	}

	public Font getLabelFont() {
		return labelFont == null ? BarCharts.DEFAULT_LABEL_FONT
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
		ICharts chart = new LineCharts("JFreeChart: LineCharts");
		
		chart.setFilepath("D:\\chart.jpg");
		chart.setChartTitle("PR费用");
		chart.setWidth(500);
		chart.setHeight(400);
		chart.setTooltips(false);
		chart.setUrls(false);
		
		List<ImageDefData> datas = new ArrayList<ImageDefData>();
		ImageDefData data = new ImageDefData();
		data.setCategory("Jan-11");
		data.setName("马子俊");
		data.setValue("900");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Feb-11");
		data.setName("马子俊");
		data.setValue("1000");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Mar-11");
		data.setName("马子俊");
		data.setValue("1100");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Jan-11");
		data.setName("李强");
		data.setValue("1350");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Feb-11");
		data.setName("李强");
		data.setValue("2000");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Mar-11");
		data.setName("沈焕新");
		data.setValue("2200");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Jan-11");
		data.setName("沈焕新");
		data.setValue("2350");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Feb-11");
		data.setName("沈焕新");
		data.setValue("1000");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Mar-11");
		data.setName("沈焕新");
		data.setValue("2300");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Mar-11");
		data.setName("沈焕新");
		data.setValue("2200");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Jan-11");
		data.setName("靳邦杰");
		data.setValue("2650");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Feb-11");
		data.setName("靳邦杰");
		data.setValue("2000");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Mar-11");
		data.setName("靳邦杰");
		data.setValue("2300");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Mar-11");
		data.setName("郭钇江");
		data.setValue("2500");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Jan-11");
		data.setName("郭钇江");
		data.setValue("2250");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Feb-11");
		data.setName("郭钇江");
		data.setValue("2200");
		datas.add(data);
		data = new ImageDefData();
		data.setCategory("Mar-11");
		data.setName("郭钇江");
		data.setValue("2300");
		datas.add(data);
		try {
			ImageHeadData ihd = new ImageHeadData();
			ihd.setUrl("D:\\chart.jpg");
			ihd.setTitle("PR费用");
			ihd.setHeight("400");
			ihd.setWidth("500");
			ihd.setAlpha("0");
			ihd.setyLabel("金额（元）");
			ihd.setxLabel("月份");
			ihd.setStroke("1");
			ihd.setDisplayNumber("1");
			ihd.setImageDefData(datas);

			chart.drawChart(ihd);
		} catch (ChartException e) {
		}
	}

}
