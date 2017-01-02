package cl.casero;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cl.casero.bd.DAO;
import cl.casero.bd.model.Estadistica;
import cl.casero.bd.model.EstadisticaMensual;
import cl.casero.bd.model.Fecha;
import cl.casero.bd.model.Util;

/*
* https://github.com/PhilJay/MPAndroidChart
* */
public class GraficoActivity extends ActionBarActivity {

    private BarChart chart;
    private Spinner spiMes1;
    private Spinner spiMes2;
    private Spinner spiAnio1;
    private Spinner spiAnio2;
    private Button btnGraVentas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        cargarComponentes();

        programacionBotones();
       // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    private void programacionBotones() {
        btnGraVentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMes1 = spiMes1.getSelectedItem().toString();
                int mes1 = spiMes1.getSelectedItemPosition();
                int anio1 = Integer.parseInt(spiAnio1.getSelectedItem().toString());
                mes1++;

                String strMes2 = spiMes2.getSelectedItem().toString();
                int mes2 = spiMes2.getSelectedItemPosition();
                int anio2 = Integer.parseInt(spiAnio2.getSelectedItem().toString());
                mes2++;

                Fecha f1, f2;

                f1 = new Fecha(anio1, mes1, 1);
                f2 = new Fecha(anio2, mes2, 1);



                /*ACÁ FALTA VALIDAR LAS FECHAS, O SEA
                QUE LA PRIMERA SEA MENOR A LA SEGUNDA*/




                List<BarEntry> ventas = new ArrayList<>();
                List<BarEntry> cobros = new ArrayList<>();


                EstadisticaMensual em;

                int cont = 0;

                for(int anio=f1.getAnio(); anio<=f2.getAnio(); anio++){
                    if(f1.getAnio() == f2.getAnio()){// si es el mismo año, recorro normal
                        for(int mes = f1.getMes(); mes <= f2.getMes(); mes++){
                            em = getEstadisticaMensual(mes, anio);
                            ventas.add(new BarEntry(cont, em.venta));
                            cobros.add(new BarEntry(cont, em.cobro));
                            cont++;
                        }
                    }else{
                        // aca los años no son iguales

                        // si estoy en el primer año, debo ir desde
                        // el primer mes (f1.getMes() hasta 12)
                        if(anio == f1.getAnio()){
                            for(int mes = f1.getMes(); mes <= 12; mes++) {
                                em = getEstadisticaMensual(mes, anio);
                                ventas.add(new BarEntry(cont, em.venta));
                                cobros.add(new BarEntry(cont, em.cobro));
                                cont++;
                            }
                        }else if(anio < f2.getAnio()){
                            // si aun no llego al año limite
                            // recorro todo el año (o sea del 1 al 12)
                            for(int mes = 1; mes <= 12; mes++) {
                                em = getEstadisticaMensual(mes, anio);
                                ventas.add(new BarEntry(cont, em.venta));
                                cobros.add(new BarEntry(cont, em.cobro));
                                cont++;
                            }

                        }else{
                            // el año actual es el año limite, por
                            // ende tengo que llegar al f2.getMes();
                            for(int mes = 1; mes <= f2.getMes(); mes++) {
                                em = getEstadisticaMensual(mes, anio);
                                ventas.add(new BarEntry(cont, em.venta));
                                cobros.add(new BarEntry(cont, em.cobro));
                                cont++;
                            }
                        }
                    }

                }

                BarDataSet set1 = new BarDataSet(ventas, "Ventas");
                BarDataSet set2 = new BarDataSet(cobros, "Cobros");

                set1.setColor(Color.parseColor("#f44336"));
                set2.setColor(Color.parseColor("#3f51b5"));

                //BarDataSet set = new BarDataSet(ventas, "Ventas");
                BarData data = new BarData(set1, set2);


                // Añadirle el $ al precio
                /*data.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                        Util.getNumeroConPuntos((int)value);
                        return "$\n"+Util.getNumeroConPuntos((int)value);
                    }
                });*/


                // poner los $ en las barras
                /*YAxis yAxis = chart.getAxisLeft();
                yAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        Util.getNumeroConPuntos((int)value);
                        return "$"+Util.getNumeroConPuntos((int)value);
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                });*/


                // poner los meses en las barras
                /*final String[] values = valoresMeses.toArray(new String[valoresMeses.size()]);

                XAxis xAxis = chart.getXAxis();
                xAxis.setTextSize(5);

                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return values[(int)value];
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                });*/




                float groupSpace = 0.06f;
                float barSpace = 0.01f; // x2 dataset
                float barWidth = 0.7f; // x2 dataset


                //data.setBarWidth(0.9f); // set custom bar width
                data.setBarWidth(barWidth);
                data.setValueTextSize(10);



                chart.setData(data);
                //chart.groupBars(-2f, groupSpace, barSpace);
                XAxis xAxis = chart.getXAxis();
                xAxis.setCenterAxisLabels(true);
                Description d = new Description();
                d.setText("");
                chart.setDescription(d);
                //chart.setFitBars(true); // make the x-axis fit exactly all bars
                chart.invalidate(); // refresh



                //Util.mensaje(GraficoActivity.this, "Info", "Entries: "+entries.size());
                //Util.mensaje(GraficoActivity.this, "Info", "Meses: "+Util.getDiff(f1, f2, Fecha.metrica.meses));





























                /*
                EstadisticaMensual em;
                int cont = 0;
                for(int i=2015; i<=2016; i++){
                    for(int j=1; j<=10; j++){
                        em = getEstadisticaMensual(j, i);
                        entries.add(new BarEntry(cont, em.venta));

                        cont++;
                    }
                }

                BarDataSet set = new BarDataSet(entries, "Ventas");
                BarData data = new BarData(set);

                // Añadirle el $ al precio
                data.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

                        Util.getNumeroConPuntos((int)value);
                        return "$\n"+Util.getNumeroConPuntos((int)value);
                    }
                });



                // poner los $ en las barras
                YAxis yAxis = chart.getAxisLeft();
                yAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        Util.getNumeroConPuntos((int)value);
                        return "$"+Util.getNumeroConPuntos((int)value);
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                });


                // poner los meses en las barras
                final String[] values = new String[] {"ene","feb","mar","abr","may","jun", "jul", "Ago", "Sep", "Oct"};

                XAxis xAxis = chart.getXAxis();
                xAxis.setTextSize(15);

                xAxis.setValueFormatter(new IAxisValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return values[(int)value];
                    }

                    @Override
                    public int getDecimalDigits() {
                        return 0;
                    }
                });



                data.setBarWidth(0.9f); // set custom bar width
                data.setValueTextSize(10);


                chart.setData(data);
                chart.setFitBars(true); // make the x-axis fit exactly all bars
                chart.invalidate(); // refresh
                */
            }
        });
    }

    private EstadisticaMensual getEstadisticaMensual(int mes, int anio) {
        return new DAO(GraficoActivity.this).getEstadistica(mes, anio);
    }

    private void cargarComponentes() {
        spiMes1 = (Spinner) findViewById(R.id.spiMes1);
        spiMes2 = (Spinner) findViewById(R.id.spiMes2);
        spiAnio1 = (Spinner) findViewById(R.id.spiAnio1);
        spiAnio2 = (Spinner) findViewById(R.id.spiAnio2);
        btnGraVentas = (Button) findViewById(R.id.btnGraVentas);
        chart = (BarChart)findViewById(R.id.graficoBarras);

        Util.cargarAnios(GraficoActivity.this, spiAnio1);
        Util.cargarAnios(GraficoActivity.this, spiAnio2);
    }
}
