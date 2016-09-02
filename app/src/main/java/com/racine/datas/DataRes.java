package com.racine.datas;

import com.racine.components.Series;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunrx on 2016/8/23.
 */
public class DataRes {
    public static List<String> getXAxes() {
        ArrayList<String> xAxes = new ArrayList<>();
        xAxes.add("2008");
        xAxes.add("2009");
        xAxes.add("2010");
        xAxes.add("2011");
        xAxes.add("2012");
        xAxes.add("2013");
        xAxes.add("2014");
        xAxes.add("2015");
        xAxes.add("2016");
        xAxes.add("2017");
        return xAxes;
    }

    public static List<Float> getYAxes() {
        ArrayList<Float> yAxes = new ArrayList<>();
        yAxes.add(130.7f);
        yAxes.add(180.1f);
        yAxes.add(110.2f);
        yAxes.add(50.9f);
        yAxes.add(150.4f);
        yAxes.add(170.8f);
        yAxes.add(10.2f);
        yAxes.add(190.7f);
        yAxes.add(140.5f);
        yAxes.add(190.1f);
        return yAxes;
    }

    public static Series getSeries() {
        Series series = new Series();
        series.add("2008", 130.7f);
        series.add("2009", 180.1f);
        series.add("2010", 110.2f);
        series.add("2011", 150.9f);
        series.add("2012", 150.4f);
        series.add("2013", 170.8f);
        series.add("2014", 110.2f);
        series.add("2015", 190.7f);
        series.add("2016", 140.5f);
        series.add("2017", 190.1f);
        return series;
    }

}
