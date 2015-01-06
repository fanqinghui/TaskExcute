package com.notice.charts;

import java.awt.Font;
import java.io.Serializable;
import java.util.List;

public class ChartParam implements Serializable {

	private static final long serialVersionUID = 3663373361311966132L;

	private int width = 0;

	private int height = 0;

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

}
