package cl.casero;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cl.casero.bd.DAO;
import cl.casero.bd.model.EstadisticaMensual;
import cl.casero.bd.model.K;
import cl.casero.bd.model.Util;

public class EstadisticaActivity extends ActionBarActivity {
    private Spinner spiAnio;
    private Spinner spiMes;
    private Button btnFecIni;
    private Button btnVerEstRango;
    private Button btnFecFin;
    private Button btnVerEstPorMes;
    private TextView lblFecIni;
    private TextView lblFecFin;
    private TextView lblTT;
    private TextView lblTN;
    private TextView lblMant;
    private TextView lblTP;
    private TextView lblCobros;
    private TextView lblVentas;
    private TextView lblTituloEst;
    private DAO d;

    /*FECHA!*/
    private DatePickerDialog.OnDateSetListener listFecIni = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int anio, int mes, int dia) {
            // el mes comienza de 0

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat f2 = new SimpleDateFormat("dd 'de' MMM 'de' yyyy");
            try {
                Date fIni = f.parse(anio+"-"+(mes+1)+"-"+dia);
                K.fecIni = anio+"-"+((mes+1)<10?"0"+(mes+1):(mes+1))+"-"+(dia<10?"0"+dia:dia);
                lblFecIni.setText(f2.format(fIni));
            }catch (ParseException ex){
            }
        }
    };

    private DatePickerDialog.OnDateSetListener listFecFin = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int anio, int mes, int dia) {
            // el mes comienza de 0

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat f2 = new SimpleDateFormat("dd 'de' MMM 'de' yyyy");
            try {
                Date fFin = f.parse(anio+"-"+(mes+1)+"-"+dia);
                K.fecFin = anio+"-"+((mes+1)<10?"0"+(mes+1):(mes+1))+"-"+(dia<10?"0"+dia:dia);
                lblFecFin.setText(f2.format(fFin));
            }catch (ParseException ex){
            }
        }
    };
    /*FECHA!*/

    /*FECHA!*/
    @Override
     protected Dialog onCreateDialog(int id) {
        // el mes comienza de 0
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        if (id == 1) {
            return new DatePickerDialog(this, listFecIni, mYear, mMonth, mDay);
        }else if(id == 2){
            return new DatePickerDialog(this, listFecFin, mYear, mMonth, mDay);
        }
        return null;
    }
    /*FECHA!*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estadistica);

        d = new DAO(EstadisticaActivity.this);

        cargarComponentes();
        programarBotones();

        K.fecIni = null;
        K.fecFin = null;
    }

    private void programarBotones() {
        /*Setiando los años en el spinner*/
        List<String> list = new ArrayList<>();
        for(int i=2016; i<=2100; i++){
            list.add(String.valueOf(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiAnio.setAdapter(dataAdapter);
        /*Setiando los años en el spinner*/



        btnVerEstPorMes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strMes = spiMes.getSelectedItem().toString();
                int mes = spiMes.getSelectedItemPosition();
                int anio = Integer.parseInt(spiAnio.getSelectedItem().toString());
                mes++;

                String fecIni, fecFin;

                fecIni = anio + "-" + (mes < 10 ? "0" + mes : mes) + "-01";

                if (mes == 12) {
                    fecFin = (anio + 1) + "-01-01";
                } else {
                    fecFin = anio + "-" + ((mes + 1) < 10 ? "0" + (mes + 1) : (mes + 1)) + "-01";
                }

                cargarEstadistica(fecIni, fecFin, false, "Estadísticas de "+strMes +" "+anio);

                //Toast.makeText(EstadisticaActivity.this, fecIni+" "+fecFin, Toast.LENGTH_LONG).show();

            }
        });

        btnFecIni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(1);
            }
        });

        btnFecFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });

        btnVerEstRango.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(EstadisticaActivity.this, K.fecIni+" "+K.fecFin, Toast.LENGTH_LONG).show();
                if(K.fecIni != null && K.fecFin != null){
                    cargarEstadistica(K.fecIni, K.fecFin, true, "Estadísticas entre fechas");
                }else{
                    Toast.makeText(EstadisticaActivity.this, "Seleccione ambas fechas", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void cargarEstadistica(String fecIni, String fecFin, boolean isRango, String titulo) {
        EstadisticaMensual em = d.getEstadistica(fecIni, fecFin, isRango);

        lblTituloEst.setText(titulo);
        lblTT.setText(String.valueOf(em.tarTerminadas));
        lblTN.setText(String.valueOf(em.tarNuevas));
        lblMant.setText(String.valueOf(em.mantenciones));
        lblTP.setText(String.valueOf(em.totalPrendas));
        lblCobros.setText("$ "+ Util.getNumeroConPuntos(em.cobro));
        lblVentas.setText("$ "+ Util.getNumeroConPuntos(em.venta));
    }

    private void cargarComponentes() {
        spiAnio = (Spinner) findViewById(R.id.spiAnio);
        spiMes = (Spinner) findViewById(R.id.spiMes);
        btnFecIni = (Button) findViewById(R.id.btnFecIni);
        btnVerEstRango = (Button) findViewById(R.id.btnVerEstRango);
        btnFecFin = (Button) findViewById(R.id.btnFecFin);
        btnVerEstPorMes = (Button) findViewById(R.id.btnVerEstPorMes);
        lblFecIni = (TextView) findViewById(R.id.lblFecIni);
        lblFecFin = (TextView) findViewById(R.id.lblFecFin);
        lblTT = (TextView) findViewById(R.id.lblTT);
        lblTN = (TextView) findViewById(R.id.lblTN);
        lblMant = (TextView) findViewById(R.id.lblMant);
        lblTP = (TextView) findViewById(R.id.lblTP);
        lblCobros = (TextView) findViewById(R.id.lblCobros);
        lblVentas = (TextView) findViewById(R.id.lblVentas);
        lblTituloEst = (TextView) findViewById(R.id.lblTituloEst);
    }

}
