package cl.casero;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.*;

import org.w3c.dom.Text;

import java.util.List;

import cl.casero.adapter.MovimientoAdapter;
import cl.casero.bd.DAO;
import cl.casero.bd.model.Cliente;
import cl.casero.bd.model.K;
import cl.casero.bd.model.Movimiento;

public class VisualizarClienteActivity extends ActionBarActivity {

    private TextView lblNombre;
    private ListView lvDetalle;
    private Switch swOrden;
    private Button btnDireccion;

    private DAO d;
    /*private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int anio, int mes, int dia) {
            // el mes comienza de 0s
            lblFecha.setText(anio+"-"+(mes+1)+"-"+dia);
        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_cliente);

        d = new DAO(this);

        cargarComponentes();
        programarBotones();

        cargarCliente();
    }

    private void cargarCliente() {
        Cliente c = d.getCliente(K.id);

        lblNombre.setText(c.nombre);
        List<Movimiento> lista = d.getMovimientos(c.id, false);

        lvDetalle.setAdapter(new MovimientoAdapter(VisualizarClienteActivity.this, lista));
    }

    private void programarBotones() {
        swOrden.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Cliente c = d.getCliente(K.id);

                lblNombre.setText(c.nombre);

                List<Movimiento> lista = d.getMovimientos(c.id, isChecked);

                lvDetalle.setAdapter(new MovimientoAdapter(VisualizarClienteActivity.this, lista));
            }
        });

        btnDireccion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cliente c = d.getCliente(K.id);

                AlertDialog.Builder b = new AlertDialog.Builder(VisualizarClienteActivity.this);
                b.setTitle("Dirección de " + c.nombre);
                b.setMessage(c.direccion+", "+c.sector);

                b.setPositiveButton("Ok", null);

                b.create().show();
            }
        });


        /*btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });*/
    }

   /* @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            // el mes comienza de 0
            return new DatePickerDialog(this, myDateListener, 2016, 0, 1);
        }
        return null;
    }*/

    private void cargarComponentes() {
        lblNombre = (TextView) findViewById(R.id.lblNombre);
        lvDetalle = (ListView) findViewById(R.id.lvDetalle);
        swOrden = (Switch) findViewById(R.id.swOrden);
        btnDireccion = (Button) findViewById(R.id.btnDireccion);
    }
}
