package cl.casero;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;

import cl.casero.listener.chart.SalesChartButtonOnClickListener;

/*
* https://github.com/PhilJay/MPAndroidChart
* */
public class ChartActivity extends ActionBarActivity {
    private Button salesChartButton;

    private static ChartActivity chartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        chartActivity = this;

        loadComponents();
        loadListeners();
    }

    private void loadListeners() {
        salesChartButton.setOnClickListener(new SalesChartButtonOnClickListener());
    }

    private void loadComponents() {
        salesChartButton = (Button) findViewById(R.id.salesChartButton);
    }

    public static Activity getActivity(){
        return chartActivity;
    }
}
