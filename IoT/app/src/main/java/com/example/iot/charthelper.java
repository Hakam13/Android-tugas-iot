package com.example.iot;

import android.graphics.Color;

import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


public class charthelper implements OnChartValueSelectedListener{
    private LineChart mChart;

    public charthelper(LineChart mChart) {
        this.mChart = mChart;
        mChart.setOnChartValueSelectedListener(this);
        mChart.setTouchEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setPinchZoom(true);
        mChart.setBackgroundColor(Color.WHITE);
        LineData data =new LineData();
        mChart.setData(data);
    }

    public void setmchart(LineChart mChart){
        this.mChart = mChart;
    }

    public void addEntry(float value){
        LineData data =mChart.getData();
        if (data != null){
            ILineDataSet set = data.getDataSetByIndex(0);
            if(set == null){
                set = createset();
                data.addDataSet(set);
            }
            data.addEntry(new Entry(set.getEntryCount(),value),0);
            data.notifyDataChanged();
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRangeMaximum(15);
            mChart.moveViewTo(set.getEntryCount()-1,data.getYMax(),YAxis.AxisDependency.LEFT);
        }

    }

    private LineDataSet createset() {
        LineDataSet set =new LineDataSet(null,"data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor((Color.RED));
        set.setLineWidth(2f);
        return set;

    }




    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}

