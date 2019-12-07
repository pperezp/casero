package cl.casero;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.List;

import cl.casero.bd.DAO;
import cl.casero.bd.model.MonthlyStatistic;
import cl.casero.bd.model.CustomDate;
import cl.casero.bd.model.Util;

/*
* https://github.com/PhilJay/MPAndroidChart
* */
public class ChartActivity extends ActionBarActivity {

    private BarChart barChart;
    private Spinner startMonthSpinner;
    private Spinner endMonthSpinner;
    private Spinner startYearSpinner;
    private Spinner endYearSpinner;
    private Button salesChartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        loadComponents();
        loadListeners();
    }

    // TODO: Separar listeners
    private void loadListeners() {
        salesChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int startMonth = startMonthSpinner.getSelectedItemPosition();
                int startYear = Integer.parseInt(startYearSpinner.getSelectedItem().toString());
                startMonth++;

                int endMonth = endMonthSpinner.getSelectedItemPosition();
                int endYear = Integer.parseInt(endYearSpinner.getSelectedItem().toString());
                endMonth++;

                CustomDate startDate, endDate;

                startDate = new CustomDate(startYear, startMonth, 1);
                endDate = new CustomDate(endYear, endMonth, 1);

                // TODO: Validar que la primera fecha sea menor a la segunda

                List<BarEntry> sales = new ArrayList<>();
                List<BarEntry> payments = new ArrayList<>();

                MonthlyStatistic monthlyStatistic;

                int xCount = 0;

                for(int year = startDate.getYear(); year <= endDate.getYear(); year++){
                    if(startDate.getYear() == endDate.getYear()){// si es el mismo año, recorro normal
                        for(int month = startDate.getMonth(); month <= endDate.getMonth(); month++){
                            // TODO: Revisar si el método getMonthlyStatistic va aquí o no
                            monthlyStatistic = getMonthlyStatistic(month, year);

                            sales.add(new BarEntry(xCount, monthlyStatistic.getSale()));
                            payments.add(new BarEntry(xCount, monthlyStatistic.getPayment()));
                            xCount++;
                        }
                    }else{
                        // aca los años no son iguales

                        // si estoy en el primer año, debo ir desde
                        // el primer mes (f1.getMonth() hasta 12)

                        // TODO: Arreglar este if else con dos for (debiese ser un for, son similares)
                        if(year == startDate.getYear()){
                            for(int mes = startDate.getMonth(); mes <= 12; mes++) {
                                monthlyStatistic = getMonthlyStatistic(mes, year);
                                sales.add(new BarEntry(xCount, monthlyStatistic.getSale()));
                                payments.add(new BarEntry(xCount, monthlyStatistic.getPayment()));
                                xCount++;
                            }
                        }else if(year < endDate.getYear()){
                            // si aún no llego al año límite
                            // recorro el año (o sea del 1 al 12)
                            for(int mes = 1; mes <= 12; mes++) {
                                monthlyStatistic = getMonthlyStatistic(mes, year);
                                sales.add(new BarEntry(xCount, monthlyStatistic.getSale()));
                                payments.add(new BarEntry(xCount, monthlyStatistic.getPayment()));
                                xCount++;
                            }

                        }else{
                            // el año actual es el año limite, por
                            // ende tengo que llegar al f2.getMonth();
                            for(int mes = 1; mes <= endDate.getMonth(); mes++) {
                                monthlyStatistic = getMonthlyStatistic(mes, year);
                                sales.add(new BarEntry(xCount, monthlyStatistic.getSale()));
                                payments.add(new BarEntry(xCount, monthlyStatistic.getPayment()));
                                xCount++;
                            }
                        }
                    }

                }

                // TODO: Hardcode
                BarDataSet salesDataSet = new BarDataSet(sales, "Ventas");
                BarDataSet paymentsDataSet = new BarDataSet(payments, "Cobros");

                salesDataSet.setColor(Color.parseColor("#f44336"));
                paymentsDataSet.setColor(Color.parseColor("#3f51b5"));

                BarData barData = new BarData(salesDataSet, paymentsDataSet);

                float barWidth = 0.7f; // x2 dataset

                barData.setBarWidth(barWidth);
                barData.setValueTextSize(10);

                barChart.setData(barData);

                XAxis xAxis = barChart.getXAxis();
                xAxis.setCenterAxisLabels(true);
                Description description = new Description();
                description.setText("");
                barChart.setDescription(description);
                barChart.invalidate(); // refresh
            }
        });
    }

    private MonthlyStatistic getMonthlyStatistic(int mes, int anio) {
        return new DAO(ChartActivity.this).getMonthlyStatistic(mes, anio);
    }

    private void loadComponents() {
        startMonthSpinner = (Spinner) findViewById(R.id.startMonthSpinner);
        endMonthSpinner = (Spinner) findViewById(R.id.endMonthSpinner);
        startYearSpinner = (Spinner) findViewById(R.id.startYearSpinner);
        endYearSpinner = (Spinner) findViewById(R.id.endYearSpinner);
        salesChartButton = (Button) findViewById(R.id.salesChartButton);
        barChart = (BarChart)findViewById(R.id.barChart);

        Util.loadYears(ChartActivity.this, startYearSpinner);
        Util.loadYears(ChartActivity.this, endYearSpinner);
    }
}
