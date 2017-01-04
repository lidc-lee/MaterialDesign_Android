/**
 * Copyright 2014  XCL-Charts
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 	
 * @Project XCL-Charts 
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @Copyright Copyright (c) 2014 XCL-Charts (www.xclcharts.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.ldc.materialdesign.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.event.click.PointPosition;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.plot.PlotLegend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import static android.graphics.Paint.Align.LEFT;
import static org.xclcharts.renderer.XEnum.BarCenterStyle.SPACE;

/**
 * @ClassName LineChart01View
 * @Description  折线图的例子
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 */
public class LineChart01View extends DemoView {
	
	private String TAG = "LineChart01View";
	private LineChart chart = new LineChart();
	
	//标签集合
	private LinkedList<String> labels = new LinkedList<String>();
	private LinkedList<LineData> chartData = new LinkedList<LineData>();

	private Paint mPaintTooltips = new Paint(Paint.ANTI_ALIAS_FLAG);
	private String title = "折线图(Line Chart)";
	private float max = 100;
	private float min =-5;


	public LineChart01View(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		initView();
	}
	
	public LineChart01View(Context context, AttributeSet attrs){   
        super(context, attrs);   
        initView();
	 }
	 
	 public LineChart01View(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			initView();
	 }
	 
	 private void initView()
	 {
		 	chartLabels();
			chartDataSet();
			chartRender();
			
			//綁定手势滑动事件
			this.bindTouch(this,chart);
	 }
	 

	@Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
       //图所占范围大小
        chart.setChartRange(w,h);
    }  
	
	private void chartRender()
	{
		try {				
			
			//设置绘图区默认缩进px值,留置空间显示Axis,Axistitle....		
			int [] ltrb = getBarLnDefaultSpadding();
			float top = DensityUtil.dip2px(getContext(), 100);
			chart.setPadding(ltrb[0], top, ltrb[2], ltrb[3]);
			
			//限制Tickmarks可滑动偏移范围
			chart.setXTickMarksOffsetMargin(ltrb[2] - 20.f);
			chart.setYTickMarksOffsetMargin(ltrb[3] - 20.f);
			
			//显示边框
//			chart.showRoundBorder();
			
			
			//设定数据源
			chart.setCategories(labels);								
			chart.setDataSource(chartData);
			
//			//数据轴最大值
			chart.getDataAxis().setAxisMax(max);
			chart.getDataAxis().setAxisMin(min);
			//数据轴刻度间隔
			chart.getDataAxis().setAxisSteps(10);

			//背景网格
			chart.getPlotGrid().showHorizontalLines();
			chart.getPlotGrid().setHorizontalLineStyle(XEnum.LineStyle.DOT);

			chart.getPlotGrid().getHorizontalLinePaint().setColor(Color.GRAY);
			chart.getPlotGrid().getVerticalLinePaint().setColor(Color.BLUE);

			chart.getPlotTitle().getTitlePaint().setTextSize(20);
			chart.setTitle(this.title);
			chart.setTitleVerticalAlign(XEnum.VerticalAlign.TOP);
			
//			chart.getAxisTitle().setLowerTitle("(年份)");
			
			//激活点击监听
			chart.ActiveListenItemClick();
			//为了让触发更灵敏，可以扩大5px的点击监听范围
			chart.extPointClickRange(10);
			chart.showClikedFocus();


			//想隐藏轴的可以下面的函数来隐藏
//			chart.getDataAxis().hide();
//			chart.getCategoryAxis().hide();
			//想设置刻度线属性的可用下面函数
			chart.getDataAxis().getTickMarksPaint();
			chart.getCategoryAxis().getTickMarksPaint();
			chart.getDataAxis().hideAxisLine();
			chart.getDataAxis().hideTickMarks();
			chart.getDataAxis().setTickLabelMargin(5);

			//想设置刻度线标签属性的可用下面函数 
//			chart.getDataAxis().getAxisTickLabelPaint()	;

			chart.getPlotArea().extWidth(100.f);
			
			//调整轴显示位置
			chart.setDataAxisLocation(XEnum.AxisLocation.LEFT);
			chart.setCategoryAxisLocation(XEnum.AxisLocation.BOTTOM);
			
			//收缩绘图区右边分割的范围，让绘图区的线不显示出来
			chart.getClipExt().setExtRight(0.f);
			chart.disablePanMode();
			chart.disableScale();

			//test x坐标从刻度线而不是轴开始
			chart.setXCoordFirstTickmarksBegin(true);
			chart.getCategoryAxis().showTickMarks();
			chart.getCategoryAxis().setVerticalTickPosition(XEnum.VerticalAlign.BOTTOM);
			chart.getCategoryAxis().getAxisPaint().setColor(Color.GRAY);
			chart.getCategoryAxis().getTickLabelPaint().setColor(Color.GRAY);
			chart.getCategoryAxis().getTickMarksPaint().setColor(Color.GRAY);

			chart.setBarCenterStyle(SPACE);

			//
			chart.getDataAxis().getTickLabelPaint().setTextSize(15);


		} catch (Exception e) {
			// TODO Auto-generated catch block
			Log.e(TAG, e.toString());
		}
	}
	private void chartDataSet()
	{
		
		//Line 1
		LinkedList<Double> dataSeries1= new LinkedList<Double>();	
		dataSeries1.add(20d); 
		dataSeries1.add(10d); 
		dataSeries1.add(31d); 
		dataSeries1.add(40d);
		dataSeries1.add(0d);

		//Line 2
		LinkedList<Double> dataSeries2= new LinkedList<Double>();	
		dataSeries2.add((double)30); 
		dataSeries2.add((double)42); 
		dataSeries2.add((double)0); 	
		dataSeries2.add((double)60); 
		dataSeries2.add((double)40);
						
		//Line 3
		LinkedList<Double> dataSeries3= new LinkedList<Double>();	
		dataSeries3.add(65d);
		dataSeries3.add(75d);
		dataSeries3.add(55d);
		dataSeries3.add(65d);
		dataSeries3.add(95d);

		//Line 4
		LinkedList<Double> dataSeries4= new LinkedList<Double>();	
		dataSeries4.add(50d);
		dataSeries4.add(60d);
		dataSeries4.add(80d);
		dataSeries4.add(84d);
		dataSeries4.add(90d);

		//Line 5
		LinkedList<Double> valuesE= new LinkedList<Double>();	
		valuesE.add(0d);
		valuesE.add(80d);
		valuesE.add(85d);
		valuesE.add(90d);

		//Line 2
		LinkedList<Double> dataSeries6= new LinkedList<Double>();	
		dataSeries6.add((double)50); 
		dataSeries6.add((double)52); 
		dataSeries6.add((double)53); 	
		dataSeries6.add((double)55); 
		dataSeries6.add((double)40); 

		Map<String,LinkedList<Double>> map = new HashMap<>();
		map.put("浩迪员工宿舍2",dataSeries1);
		map.put("301",dataSeries2);
		map.put("401",dataSeries3);
		map.put("501",dataSeries4);

		setChartData(map);
	}
	
	private void chartLabels()
	{
		labels.add("1月");
		labels.add("2月");
		labels.add("3月");
		labels.add("4月");
		labels.add("5月");
//		labels.add("6月");
//		labels.add("7月");
//		labels.add("8月");
//		labels.add("9月");
//		labels.add("10月");
	}
	
	@Override
    public void render(Canvas canvas) {
        try{
            chart.render(canvas);

        } catch (Exception e){
        	Log.e(TAG, e.toString());
        }
    }

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub		
				
		if(event.getAction() == MotionEvent.ACTION_UP) 
		{			
			 triggerClick(event.getX(),event.getY());
		}
		super.onTouchEvent(event);
		return true;
	}
	
	
	//触发监听
	private void triggerClick(float x,float y)
	{
		//交叉线
		if(chart.getDyLineVisible())chart.getDyLine().setCurrentXY(x,y);		
		if(!chart.getListenItemClickStatus())
		{
			//交叉线
			if(chart.getDyLineVisible())this.invalidate();
		}else{			
			PointPosition record = chart.getPositionRecord(x,y);
			if( null == record)
			{
				if(chart.getDyLineVisible())this.invalidate();
				return;
			}
			LineData lData = chartData.get(record.getDataID());
			Double lValue = lData.getLinePoint().get(record.getDataChildID());
			float r = record.getRadius();
			chart.showFocusPointF(record.getPosition(),r + r*0.5f);		
			chart.getFocusPaint().setStyle(Style.STROKE);
			chart.getFocusPaint().setStrokeWidth(3);		
			if(record.getDataID() >= 3)
			{
				chart.getFocusPaint().setColor(Color.BLUE);
			}else{
				chart.getFocusPaint().setColor(Color.RED);
			}		
			
			//在点击处显示tooltip
			mPaintTooltips.setColor(lData.getLineColor());

			chart.getToolTip().getBackgroundPaint().setColor(Color.WHITE);
			chart.getToolTip().setAlign(LEFT);
			chart.getToolTip().setCurrentXY(record.getPosition().x,record.getPosition().y);

			chart.getToolTip().addToolTip(lData.getLabel()+":" +Double.toString(lValue),mPaintTooltips);
				
			
			//当前标签对应的其它点的值
//			int cid = record.getDataChildID();
//			String xLabels = "";
//			for(LineData data : chartData)
//			{
//				if(cid < data.getLinePoint().size())
//				{
//					xLabels = Double.toString(data.getLinePoint().get(cid));
//					chart.getToolTip().addToolTip("Line:"+data.getLabel()+","+ xLabels,mPaintTooltips);
//				}
//			}
			chart.getPlotLegend().getType();
			this.invalidate();
		}
		
		
	}

	////////////////////////////////////////////////////////////////////

	public void setLabels(LinkedList<String> labels){
		this.labels = labels;
		chart.setCategories(labels);
	}
	public void setChartData(Map<String,LinkedList<Double>> map){
		chartData.clear();
		Iterator data = map.keySet().iterator();
		int i =0;
		while (data.hasNext()){
			i += 1;
			String key = (String) data.next();
			LinkedList<Double> dataSeries = map.get(key);

			LineData lineData = new LineData(key,dataSeries,getColor(i));
			lineData.setDotStyle(getStyle(i));
			//把线弄细点
			lineData.getLinePaint().setStrokeWidth(3);
			if (getStyle(i).equals(XEnum.DotStyle.RING)){
				lineData.getPlotLine().getPlotDot().setRingInnerColor(Color.YELLOW);
			}
			chartData.add(lineData);
		}
		getMaxAndMin(map);
	}

	private int getColor(int i){
		switch (i%9){
			case 1:
				return Color.rgb(234, 142, 43);
			case 2:
				return Color.rgb(234, 83, 71);
			case 3:
				return Color.rgb(123, 89, 168);
			case 4:
				return Color.rgb(84, 206, 231);
			case 5:
				return Color.rgb(75, 166, 51);
			case 6:
				return Color.rgb(75, 166, 51);
			default:
				Random random = new Random();
				int r = random.nextInt(255);
				int g = random.nextInt(255);
				int b = random.nextInt(255);
				return Color.rgb(r,g,b);
		}
	}
	private XEnum.DotStyle getStyle(int i){
		switch (i%9){
			case 1:
				return XEnum.DotStyle.TRIANGLE;
			case 2:
				return XEnum.DotStyle.RECT;
			case 3:
				return XEnum.DotStyle.DOT;
			case 4:
				return XEnum.DotStyle.PRISMATIC;
			case 5:
				return XEnum.DotStyle.RING2;
			case 6:
				return XEnum.DotStyle.RING;
		}
		return XEnum.DotStyle.TRIANGLE;
	}

	public void setTitle(String title){
		this.title = title;
		chart.setTitle(title);
	}

	public void setLeftTitle(String title){
		//左标题
		chart.getAxisTitle().setLeftTitle(title);
		chart.getAxisTitle().setLeftAxisTitleOffsetX(50);
	}

	private void getMaxAndMin(Map<String,LinkedList<Double>> map){
		Iterator data = map.keySet().iterator();
		float max = 0;
		float min = 0;
		while (data.hasNext()){
			String key = (String) data.next();
			LinkedList<Double> dataSeries = map.get(key);
			for (int j =0;j<dataSeries.size();j++){
				if (dataSeries.get(j)>max){
					max = dataSeries.get(j).floatValue();
				}
				if (dataSeries.get(j)<min){
					min = dataSeries.get(j).floatValue();
				}
			}
		}
		this.max = max+10;
		this.min = min-10;
		//数据轴最大值
		chart.getDataAxis().setAxisMax(this.max);
		chart.getDataAxis().setAxisMin(this.min);
	}
}
