package com.ldc.materialdesign;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ZoomControls;

import com.ldc.materialdesign.views.DemoView;
import com.ldc.materialdesign.views.LineChart01View;
import com.ldc.materialdesign.views.PieChart01View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by AA on 2016/12/28.
 */

public class ChartsActivity extends Activity{

    private DemoView[] mCharts;
    private int mSelected = 0;
    private FrameLayout frameLayout;
    private FrameLayout frameLayout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE); //设置没标题
        setContentView(R.layout.activity_charts);

        Bundle bunde = this.getIntent().getExtras();
        mSelected = bunde.getInt("selected");
        String title = bunde.getString("title");
        this.setTitle(title);
        Log.e("mSelected",mSelected+"");
        getData();
        initView();
    }

    private void getData(){
        Map<String,Double> map = new HashMap<>();
        map.put("快钱",23.8);
        map.put("支付宝",60.0);
        map.put("微信",6.2);
        map.put("其他",10.0);
        PieChart01View pieChart01View = new PieChart01View(this);
        pieChart01View.setChartData(map);
        pieChart01View.setTitle("12月份的总充值情况");

        LineChart01View lineChart01View = new LineChart01View(this);
        LinkedList<Double> dataSeries1= new LinkedList<Double>();
        dataSeries1.add(20d);
        dataSeries1.add(10d);
        dataSeries1.add(31d);
        dataSeries1.add(40d);
//        dataSeries1.add(0d);

        //Line 2
        LinkedList<Double> dataSeries2= new LinkedList<Double>();
        dataSeries2.add((double)30);
        dataSeries2.add((double)42);
        dataSeries2.add((double)0);
        dataSeries2.add((double)60);
//        dataSeries2.add((double)40);

        //Line 3
        LinkedList<Double> dataSeries3= new LinkedList<Double>();
        dataSeries3.add(65d);
        dataSeries3.add(75d);
        dataSeries3.add(55d);
        dataSeries3.add(65d);
//        dataSeries3.add(95d);

        //Line 4
        LinkedList<Double> dataSeries4= new LinkedList<Double>();
        dataSeries4.add(50d);
        dataSeries4.add(60d);
        dataSeries4.add(80d);
        dataSeries4.add(84d);
//        dataSeries4.add(90d);

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

        Map<String,LinkedList<Double>> mapLine = new HashMap<>();
        mapLine.put("浩迪员工宿舍2",dataSeries1);
        mapLine.put("301",dataSeries2);
        mapLine.put("401",dataSeries3);
//        mapLine.put("501",dataSeries4);

        LinkedList<String> labels = new LinkedList<>();
        labels.add("1月");
        labels.add("2月");
        labels.add("3月");
        labels.add("4月");
//        labels.add("5月");
        lineChart01View.setChartData(mapLine);
        lineChart01View.setTitle("2016年度消费情况");
        lineChart01View.setLabels(labels);

        mCharts = new DemoView[]{
                lineChart01View,
                pieChart01View
        };

    }
    private void initView(){

        frameLayout = (FrameLayout) findViewById(R.id.fl_chart);
        //图表显示范围在占屏幕大小的90%的区域内
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scrWidth = (int) (dm.widthPixels * 0.9);
        int scrHeight = (int) (dm.heightPixels * 0.9);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                scrWidth, scrHeight);

        //居中显示
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        final RelativeLayout chartLayout = new RelativeLayout(this);

        chartLayout.addView( mCharts[mSelected], layoutParams);
        frameLayout.addView(chartLayout);

        frameLayout2 = (FrameLayout) findViewById(R.id.fl_chart2);
        //图表显示范围在占屏幕大小的90%的区域内
        DisplayMetrics dm2 = getResources().getDisplayMetrics();
        int scrWidth2 = (int) (dm2.widthPixels * 0.95);
        int scrHeight2 = (int) (dm2.heightPixels * 0.95);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(
                scrWidth2, scrHeight2);

        //居中显示
        layoutParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        final RelativeLayout chartLayout2 = new RelativeLayout(this);
        chartLayout2.addView( mCharts[mSelected+1], layoutParams2);
        frameLayout2.addView(chartLayout2);
    }

    private void initActivity()
    {
        //图表的使用方法:
        //使用方式一:
        // 1.新增一个Activity
        // 2.新增一个View,继承Demo中的GraphicalView或DemoView都可，依Demo中View目录下例子绘制图表.
        // 3.将自定义的图表View放置入Activity对应的XML中，将指明其layout_width与layout_height大小.
        // 运行即可看到效果. 可参考非ChartsActivity的那几个图的例子，都是这种方式。

        //使用方式二:
        //代码调用 方式有下面二种方法:
        //方法一:
        //在xml中的FrameLayout下增加图表和ZoomControls,这是利用了现有的xml文件.
        // 1. 新增一个View，绘制图表.
        // 2. 通过下面的代码得到控件，addview即可
        //LayoutInflater factory = LayoutInflater.from(this);
        //View content = (View) factory.inflate(R.layout.activity_multi_touch, null);


        //方法二:
        //完全动态创建,无须XML文件.
        FrameLayout content = new FrameLayout(this);

        //缩放控件放置在FrameLayout的上层，用于放大缩小图表
        FrameLayout.LayoutParams frameParm = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        frameParm.gravity = Gravity.BOTTOM|Gravity.RIGHT;


		  //缩放控件放置在FrameLayout的上层，用于放大缩小图表
//	       mZoomControls = new ZoomControls(this);
//	       mZoomControls.setIsZoomInEnabled(true);
//	       mZoomControls.setIsZoomOutEnabled(true);
//		   mZoomControls.setLayoutParams(frameParm);


        //图表显示范围在占屏幕大小的90%的区域内
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scrWidth = (int) (dm.widthPixels * 0.9);
        int scrHeight = (int) (dm.heightPixels * 0.9);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                scrWidth, scrHeight);

        //居中显示
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        final RelativeLayout chartLayout = new RelativeLayout(this);

        chartLayout.addView( mCharts[mSelected], layoutParams);

        //增加控件
        ((ViewGroup) content).addView(chartLayout);
        //((ViewGroup) content).addView(mZoomControls);
        setContentView(content);
        //放大监听
//          mZoomControls.setOnZoomInClickListener(new View.OnClickListener() {
//              @Override
//              public void onClick(View view) {
//
//              }
//          });
        //缩小监听
        //  mZoomControls.setOnZoomOutClickListener(new OnZoomOutClickListenerImpl());
    }
}
