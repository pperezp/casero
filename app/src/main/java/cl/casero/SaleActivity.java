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
import cl.casero.bd.model.Customer;
import cl.casero.bd.model.K;
import cl.casero.bd.model.Transaction;

public class SaleActivity extends ActionBarActivity {
    private TextView saleCustomerNameTextView;
    private TextView saleDateTextView;
    private EditText saleDetailEditText;
    private EditText saleItemsCountEditText;
    private EditText saleAmountEditText;
    private Button saleDateButton;
    private Button saleCreateButton;

    private Customer customer;
    private DAO dao;

    // TODO: Separar si o si esto
    /*FECHA!*/
    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int anio, int mes, int dia) {
            // el mes comienza de 0

            SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat f2 = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");
            try {
                K.date = f.parse(anio+"-"+(mes+1)+"-"+dia);
                saleDateTextView.setText(f2.format(K.date));
            }catch (ParseException ex){
            }
        }
    };
    /*FECHA!*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale);

        dao = new DAO(SaleActivity.this);

        loadComponents();
        loadCustomerName();
        loadListeners();

    }

    private void loadCustomerName() {
        customer = dao.getCustomer(K.customerId);
        saleCustomerNameTextView.setText(customer.getName());
    }

    private void loadListeners() {
        saleDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Revisar todos los showDialog deprecados
                showDialog(999);
            }
        });

        saleCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                DAO dao = new DAO(SaleActivity.this);
                                int subtotal = -1;
                                int itemsCount = -1;

                                try {
                                    itemsCount = Integer.parseInt(saleItemsCountEditText.getText().toString());
                                } catch (NumberFormatException ex) {
                                    // TODO: Hardcode
                                    Toast.makeText(
                                        SaleActivity.this,
                                        "Ingrese sólo números en prendas",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                }

                                if (itemsCount != -1) {
                                    try {
                                        subtotal = Integer.parseInt(saleAmountEditText.getText().toString());
                                    } catch (NumberFormatException ex) {
                                        // TODO: Hardcode
                                        Toast.makeText(
                                            SaleActivity.this,
                                            "Ingrese sólo números en precio TOTAL",
                                            Toast.LENGTH_SHORT
                                        ).show();
                                    }

                                    if (subtotal != -1) {
                                        Transaction transaction = new Transaction();
                                        String detail;

                                        transaction.setCustomerId((int) K.customerId);

                                        // TODO: Cambiar a append
                                        // TODO: Hardcode
                                        detail = "[Venta]: " + saleDetailEditText.getText().toString();
                                        detail += "\n[Prendas]: " + itemsCount;
                                        detail += "\n\n[Subtotal]: $" + subtotal;

                                        transaction.setDetail(detail);
                                        transaction.setDate(K.date);

                                        int currentBalance = dao.getDebt((int) K.customerId);

                                        int saleType = (currentBalance == 0 ? K.NEW_SALE: K.MAINTENANCE);

                                        currentBalance = currentBalance + subtotal;

                                        transaction.setBalance(currentBalance);

                                        dao.createSale(transaction, subtotal, itemsCount, saleType);

                                        // TODO: Hardcode
                                        Toast.makeText(
                                            SaleActivity.this,
                                            "Mantención realizada. NUEVO SALDO: $"+currentBalance,
                                            Toast.LENGTH_LONG
                                        ).show();

                                        Intent intent = new Intent(SaleActivity.this, MainActivity.class);
                                        SaleActivity.this.startActivity(intent);
                                    }
                                }

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };

                // TODO: Hardcode todo para abajo
                if (saleDetailEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(
                        SaleActivity.this,
                        "Ingrese detail de mantención",
                        Toast.LENGTH_SHORT
                    ).show();
                } else if (saleItemsCountEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(
                        SaleActivity.this,
                        "Ingrese cantidad de prendas",
                        Toast.LENGTH_SHORT
                    ).show();
                } else if (saleAmountEditText.getText().toString().trim().equals("")) {
                    Toast.makeText(
                        SaleActivity.this,
                        "Ingrese precio total",
                        Toast.LENGTH_SHORT
                    ).show();
                } else if (saleDateTextView.getText().toString().equals("[CustomDate]")) {
                    Toast.makeText(
                        SaleActivity.this,
                        "Seleccione alguna date de venta",
                        Toast.LENGTH_SHORT
                    ).show();
                } else {
                    try {
                        int itemsCount = Integer.parseInt(saleItemsCountEditText.getText().toString());

                        if (itemsCount > 0) {
                            try {
                                int subtotal = Integer.parseInt(saleAmountEditText.getText().toString());

                                if (subtotal > 0) {
                                    String saleSummary;

                                    // TODO: Hardcode
                                    saleSummary = "Cliente: " + customer.getName() + "\n";
                                    saleSummary += "Detalle: " + saleDetailEditText.getText().toString() + "\n";
                                    saleSummary += "Prendas: " + saleItemsCountEditText.getText().toString() + "\n";
                                    saleSummary += "Precio: $" + saleAmountEditText.getText().toString() + "\n";
                                    saleSummary += "Fecha venta: " + saleDateTextView.getText().toString();

                                    // TODO: Hardcode
                                    new AlertDialog
                                        .Builder(SaleActivity.this)
                                        .setMessage("¿Confirmar venta? \n\n" + saleSummary)
                                        .setPositiveButton("SI", dialogClickListener)
                                        .setNegativeButton("NO", dialogClickListener)
                                        .show();
                                } else {
                                    // TODO: Hardcode
                                    Toast.makeText(
                                        SaleActivity.this,
                                        "El precio de venta no puede ser negativo",
                                        Toast.LENGTH_SHORT
                                    ).show();
                                }
                            } catch (NumberFormatException ex) {
                                // TODO: Hardcode
                                Toast.makeText(
                                    SaleActivity.this,
                                    "Ingrese sólo números en precio TOTAL",
                                    Toast.LENGTH_SHORT
                                ).show();
                            }
                        } else {
                            // TODO: Hardcode
                            Toast.makeText(
                                SaleActivity.this,
                                "La cantidad de prendas no puede ser negativa",
                                Toast.LENGTH_SHORT
                            ).show();
                        }
                    } catch (NumberFormatException ex) {
                        // TODO: Hardcode
                        Toast.makeText(
                            SaleActivity.this,
                            "Ingrese sólo números en prendas",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });
    }
    /*FECHA!*/
    // TODO: deprecated
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == 999) {
            // el mes comienza de 0
            Calendar calendar = Calendar.getInstance();

            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(this, myDateListener, year, month, day);
        }

        return null;
    }
    /*FECHA!*/

    private void loadComponents() {
        saleCustomerNameTextView = (TextView) findViewById(R.id.saleCustomerNameTextView);
        saleDateTextView = (TextView) findViewById(R.id.saleDateTextView);
        saleDetailEditText = (EditText) findViewById(R.id.saleDetailEditText);
        saleDetailEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        saleItemsCountEditText = (EditText) findViewById(R.id.saleItemsCountEditText);
        saleAmountEditText = (EditText) findViewById(R.id.saleAmountEditText);
        saleDateButton = (Button) findViewById(R.id.saleDateButton);
        saleCreateButton = (Button) findViewById(R.id.saleCreateButton);
    }

}
