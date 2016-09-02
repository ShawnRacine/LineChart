package com.racine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.racine.linechart.R;
import com.racine.datas.DataRes;

public class MainActivity extends AppCompatActivity {
    private ListView listView;
    LineChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chart = (LineChart) findViewById(R.id.chart);
        chart.setSeries(DataRes.getSeries());
    }
}
