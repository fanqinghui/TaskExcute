package com.notice.charts;

import java.util.HashMap;
import java.util.Map;


public class ChartsFactory {
	

	private Map<String, ICharts> chartsClass = new HashMap<String, ICharts>();
	
	public ICharts getChartsImpl(String type) {
		return chartsClass.get(type);
	}

	public Map<String, ICharts> getChartsClass() {
		return chartsClass;
	}

	public void setChartsClass(Map<String, ICharts> chartsClass) {
		this.chartsClass = chartsClass;
	}

	
	
}
