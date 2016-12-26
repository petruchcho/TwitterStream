package com.egorpetruchcho.tweetstream.ui;


import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.egorpetruchcho.tweetstream.analysis.FileUtils;
import com.egorpetruchcho.tweetstream.analysis.StatsPoint;
import com.egorpetruchcho.tweetstream.core.TweetsActivity;
import com.egorpetruchcho.tweetstream.operations.R;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;

public class StatsActivity extends TweetsActivity {

    private XYPlot plot;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_stats);

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot1);

        StatsPoint[] statsPoints = FileUtils.getInstance().readStats();
        final Number[] domainLabels = new Number[statsPoints.length];
        Number[] series1Numbers = new Number[statsPoints.length];

        for (int i = 0; i < statsPoints.length; i++) {
            domainLabels[i] = statsPoints[i].getCountOfLikedWords();
            series1Numbers[i] = statsPoints[i].getPercentage();
        }

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers), SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Series1");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format =
                new LineAndPointFormatter(Color.RED, Color.GREEN, Color.BLUE, null);

        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
            @Override
            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }
            @Override
            public Object parseObject(String source, ParsePosition pos) {
                return null;
            }
        });

    }
}
