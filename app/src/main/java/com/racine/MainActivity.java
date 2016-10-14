package com.racine;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.racine.linechart.R;
import com.racine.datas.DataRes;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    GraphView chart;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chart = (GraphView) findViewById(R.id.chart);
        chart.addSeries(DataRes.getSeries());

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                chart.addSeries(DataRes.getSeries1());
//                chart.invalidate();
//                chart.start(3000);
//            }
//        }, 3000);
    }
}
