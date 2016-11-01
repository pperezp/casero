package cl.casero;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import java.util.Date;

import cl.casero.bd.DAO;
import cl.casero.bd.model.Cliente;
import cl.casero.bd.model.K;
import cl.casero.bd.model.Movimiento;

public class CrearClienteActivity extends ActionBarActivity {
    private Button btnVolver;
    private Button btnCrear;
    private EditText txtNombre;
    private EditText txtDireccion;
    private Spinner spiSector;
    private TextView lblCantidad;
    private DAO d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_cliente);

        d = new DAO(this);

        cargarComponentes();
        programarBotones();

        lblCantidad.setText("Clientes: " + d.getCantidadClientes());

    }

    private void programarBotones() {
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CrearClienteActivity.this, MainActivity.class);
                CrearClienteActivity.this.startActivity(i);
            }
        });

        btnCrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Validaciones*/
                if(txtNombre.getText().toString().trim().equals("")){
                    Toast.makeText(
                            CrearClienteActivity.this.getApplicationContext(),
                            "Ingrese el nombre del cliente",
                            Toast.LENGTH_SHORT).show();
                }else if(txtDireccion.getText().toString().trim().equals("")){
                    Toast.makeText(
                            CrearClienteActivity.this.getApplicationContext(),
                            "Ingrese la dirección del cliente",
                            Toast.LENGTH_SHORT).show();
                }else{
                    try {
                        DAO d = new DAO(CrearClienteActivity.this);

                        Cliente c = new Cliente();
                        //Movimiento m = new Movimiento();

                        //m.saldo = Integer.parseInt(txtSaldo.getText().toString());

                        //if(m.saldo >= 0){
                        //    m.detalle = txtDetalle.getText().toString();
                        //    m.fecha = new Date();

                            c.nombre = txtNombre.getText().toString();
                            c.direccion = txtDireccion.getText().toString();
                            c.sector = spiSector.getSelectedItem().toString();
                            //c.deuda = m.saldo;
                            c.deuda = 0;

                            d.crearCliente(c);
                        //    m.cliente = d.crearCliente(c);

                        //    d.crearMovimiento(m);

                        //    txtDetalle.setText("");
                            txtNombre.setText("");
                        //    txtSaldo.setText("");
                            txtDireccion.setText("");
                            spiSector.setSelection(0);
                            txtNombre.requestFocus();
                            lblCantidad.setText("Clientes: " + d.getCantidadClientes());
                            Toast.makeText(CrearClienteActivity.this.getApplicationContext(), "Cliente ["+c.nombre+"] creado!", Toast.LENGTH_SHORT).show();

                            K.nombreBusqueda = c.nombre;
                            Intent i = new Intent(CrearClienteActivity.this, MainActivity.class);
                            CrearClienteActivity.this.startActivity(i);


                        //}else{
                        //  Toast.makeText(
                        //            CrearClienteActivity.this.getApplicationContext(),
                        //            "No puede ingresar saldos negativos",
                        //            Toast.LENGTH_SHORT).show();
                        //}
                    }catch (NumberFormatException ex){
                        Toast.makeText(
                                CrearClienteActivity.this.getApplicationContext(),
                                "Ingrese sólo números en el saldo",
                                Toast.LENGTH_SHORT).show();
                    }
                }




            }
        });
    }

    private void cargarComponentes() {
        btnVolver = (Button) findViewById(R.id.btnVolVis);
        btnCrear = (Button) findViewById(R.id.btnCrear);
        //txtDetalle = (EditText) findViewById(R.id.txtDetalle);
        //txtDetalle.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        txtNombre = (EditText) findViewById(R.id.txtNomCli);
        txtNombre.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        //txtSaldo = (EditText) findViewById(R.id.txtSaldo);
        txtDireccion = (EditText) findViewById(R.id.txtDire);
        txtDireccion.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        spiSector = (Spinner) findViewById(R.id.spiSector);
        lblCantidad = (TextView) findViewById(R.id.lblCantidad);
    }

}
