package com.egorpetruchcho.tweetstream.ui;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.egorpetruchcho.tweetstream.analysis.NaiveBayes;
import com.egorpetruchcho.tweetstream.analysis.Stats;
import com.egorpetruchcho.tweetstream.core.TweetsActivity;
import com.egorpetruchcho.tweetstream.operations.R;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.List;

public class StatsActivity extends TweetsActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_stats);

        Stats stats = NaiveBayes.getInstance().stats;
        if (stats == null) {
            return;
        }
        initPlot((XYPlot) findViewById(R.id.plot1), stats.getCountOfAllTweets(), stats.getPercentages());
        initPlot((XYPlot) findViewById(R.id.plot2), stats.getCountOfLikedTweets(), stats.getPercentages());
        initPlot((XYPlot) findViewById(R.id.plot3), stats.getCountOfAllTweets(), stats.getF1());
        initPlot((XYPlot) findViewById(R.id.plot4), stats.getCountOfLikedTweets(), stats.getF1());
    }

    private void initPlot(XYPlot plot, List<? extends Number> xs, List<? extends Number> ys) {
        final Number[] domainLabels = new Number[xs.size()];
        Number[] series1Numbers = new Number[ys.size()];

        for (int i = 0; i < xs.size(); i++) {
            domainLabels[i] = xs.get(i);
            series1Numbers[i] = ys.get(i);
        }

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
            public StringBuffer format(Object obj, @NonNull StringBuffer toAppendTo, @NonNull FieldPosition pos) {
                int i = Math.round(((Number) obj).floatValue());
                return toAppendTo.append(domainLabels[i]);
            }

            @Override
            public Object parseObject(String source, @NonNull ParsePosition pos) {
                return null;
            }
        });
    }
}
