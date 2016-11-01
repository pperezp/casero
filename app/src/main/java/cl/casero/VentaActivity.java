package cl.casero;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cl.casero.bd.DAO;
import cl.casero.bd.model.Cliente;
import cl.casero.bd.model.K;
import cl.casero.bd.model.Movimiento;

public class VentaActivity extends ActionBarActivity {
    private TextView lblNomVenta;
    private TextView lblFechaVenta;
    private EditText txtDetVenta;
    private EditText txtCantPrendas;
    private EditText txtPrecioVenta;
    private Button btnFechaVenta;
    private Button btnCrearVenta;
    private Cliente c;
    private DAO d;
    /*FECHA!*/
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int anio, int mes, int dia) {
            // el mes comienza de 0

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat f2 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
            try {
                K.fecha = f.parse(anio+"-"+(mes+1)+"-"+dia);
                lblFechaVenta.setText(f2.format(K.fecha));
            }catch (ParseException ex){
            }
        }
    };
    /*FECHA!*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venta);

        d = new DAO(VentaActivity.this);

        cargarComponentes();
        cargarNombreCliente();
        programacionBotones();

    }

    private void cargarNombreCliente() {
        c = d.getCliente(K.id);
        lblNomVenta.setText(c.nombre);
    }

    private void programacionBotones() {
        btnFechaVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);
            }
        });

        btnCrearVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                DAO d = new DAO(VentaActivity.this);
                                int subTotal = -1;
                                int cantPrendas = -1;

                                try {
                                    cantPrendas = Integer.parseInt(txtCantPrendas.getText().toString());
                                } catch (NumberFormatException ex) {
                                    Toast.makeText(
                                            VentaActivity.this,
                                            "Ingrese sólo números en prendas",
                                            Toast.LENGTH_SHORT).
                                            show();
                                }

                                if (cantPrendas != -1) {
                                    try {
                                        subTotal = Integer.parseInt(txtPrecioVenta.getText().toString());
                                    } catch (NumberFormatException ex) {
                                        Toast.makeText(
                                                VentaActivity.this,
                                                "Ingrese sólo números en precio TOTAL",
                                                Toast.LENGTH_SHORT).
                                                show();
                                    }

                                    if (subTotal != -1) {
                                        Movimiento m = new Movimiento();
                                        m.cliente = (int) K.id;
                                        m.detalle = "[Venta]: " + txtDetVenta.getText().toString();
                                        m.detalle += "\n[Prendas]: " + cantPrendas;
                                        m.detalle += "\n\n[Subtotal]: $" + subTotal;
                                        m.fecha = K.fecha;

                                        int saldoActual = d.getDeuda((int) K.id);

                                        int tipoVenta;

                                        if (saldoActual == 0) {
                                            tipoVenta = K.VENTA_NUEVA;
                                        } else {
                                            tipoVenta = K.MANTENCION;
                                        }

                                        saldoActual = saldoActual + subTotal;

                                        m.saldo = saldoActual;

                                        d.crearVenta(m, subTotal, cantPrendas, tipoVenta);

                                        Toast.makeText(VentaActivity.this, "Mantención realizada. NUEVO SALDO: $"+saldoActual, Toast.LENGTH_LONG).show();

                                        Intent i = new Intent(VentaActivity.this, MainActivity.class);
                                        VentaActivity.this.startActivity(i);
                                    }
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                if (txtDetVenta.getText().toString().trim().equals("")) {
                    Toast.makeText(
                            VentaActivity.this,
                            "Ingrese detalle de mantención",
                            Toast.LENGTH_SHORT).
                            show();
                } else if (txtCantPrendas.getText().toString().trim().equals("")) {
                    Toast.makeText(
                            VentaActivity.this,
                            "Ingrese cantidad de prendas",
                            Toast.LENGTH_SHORT).
                            show();
                } else if (txtPrecioVenta.getText().toString().trim().equals("")) {
                    Toast.makeText(
                            VentaActivity.this,
                            "Ingrese precio total",
                            Toast.LENGTH_SHORT).
                            show();
                } else if (lblFechaVenta.getText().toString().equals("[Fecha]")) {
                    Toast.makeText(
                            VentaActivity.this,
                            "Seleccione alguna fecha de venta",
                            Toast.LENGTH_SHORT).
                            show();
                } else {
                    try {
                        int cantPrendas = Integer.parseInt(txtCantPrendas.getText().toString());

                        if (cantPrendas > 0) {
                            try {
                                int subTotal = Integer.parseInt(txtPrecioVenta.getText().toString());

                                if (subTotal > 0) {
                                    String resumenVenta;

                                    resumenVenta = "Cliente: " + c.nombre + "\n";
                                    resumenVenta += "Detalle: " + txtDetVenta.getText().toString() + "\n";
                                    resumenVenta += "Prendas: " + txtCantPrendas.getText().toString() + "\n";
                                    resumenVenta += "Precio: $" + txtPrecioVenta.getText().toString() + "\n";
                                    resumenVenta += "Fecha: " + lblFechaVenta.getText().toString();

                                    AlertDialog.Builder builder = new AlertDialog.Builder(VentaActivity.this);
                                    builder.setMessage("¿Confirmar venta? \n\n" + resumenVenta).setPositiveButton("SI", dialogClickListener)
                                            .setNegativeButton("NO", dialogClickListener).show();
                                } else {
                                    Toast.makeText(
                                            VentaActivity.this,
                                            "El precio de venta no puede ser negativo",
                                            Toast.LENGTH_SHORT).
                                            show();
                                }
                            } catch (NumberFormatException ex) {
                                Toast.makeText(
                                        VentaActivity.this,
                                        "Ingrese sólo números en precio TOTAL",
                                        Toast.LENGTH_SHORT).
                                        show();
                            }
                        } else {
                            Toast.makeText(
                                    VentaActivity.this,
                                    "La cantidad de prendas no puede ser negativa",
                                    Toast.LENGTH_SHORT).
                                    show();
                        }


                    } catch (NumberFormatException ex) {
                        Toast.makeText(
                                VentaActivity.this,
                                "Ingrese sólo números en prendas",
                                Toast.LENGTH_SHORT).
                                show();
                    }
                }
            }
        });
    }

    /*FECHA!*/
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            // el mes comienza de 0
            Calendar c = Calendar.getInstance();
            int mYear = c.get(Calendar.YEAR);
            int mMonth = c.get(Calendar.MONTH);
            int mDay = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, myDateListener, mYear, mMonth, mDay);
        }
        return null;
    }
    /*FECHA!*/

    private void cargarComponentes() {
        lblNomVenta = (TextView) findViewById(R.id.lblNomVenta);
        lblFechaVenta = (TextView) findViewById(R.id.lblFechaVenta);
        txtDetVenta = (EditText) findViewById(R.id.txtDetVenta);
        txtDetVenta.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        txtCantPrendas = (EditText) findViewById(R.id.txtCantPrendas);
        txtPrecioVenta = (EditText) findViewById(R.id.txtPrecioVenta);
        btnFechaVenta = (Button) findViewById(R.id.btnFechaVenta);
        btnCrearVenta = (Button) findViewById(R.id.btnCrearVenta);
    }

}
