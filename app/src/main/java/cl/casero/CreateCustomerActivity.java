package cl.casero;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import cl.casero.bd.DAO;
import cl.casero.bd.model.Customer;
import cl.casero.bd.model.K;

public class CreateCustomerActivity extends ActionBarActivity {
    private Button backButton;
    private Button createButton;
    private EditText nameEditText;
    private EditText addressEditText;
    private Spinner sectorSpinner;
    private TextView countTextView;
    private DAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer);

        dao = new DAO(this);

        loadComponents();
        loadListeners();

        countTextView.setText("Clientes: " + dao.getCustomersCount());

    }

    private void loadListeners() {
        // TODO: Separar los listeners
        backButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(
                        CreateCustomerActivity.this,
                        MainActivity.class
                    );

                    CreateCustomerActivity.this.startActivity(intent);
                }
            }
        );

        createButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Validaciones*/
                    // TODO: Hardcode
                    if(nameEditText.getText().toString().trim().equals("")){
                        Toast.makeText(
                            CreateCustomerActivity.this.getApplicationContext(),
                            "Ingrese el nombre del cliente",
                            Toast.LENGTH_SHORT
                        ).show();
                    }else if(addressEditText.getText().toString().trim().equals("")){
                        Toast.makeText(
                            CreateCustomerActivity.this.getApplicationContext(),
                            "Ingrese la dirección del cliente",
                            Toast.LENGTH_SHORT
                        ).show();
                    }else{
                        try {
                            DAO dao = new DAO(CreateCustomerActivity.this);

                            Customer customer = new Customer();

                            customer.setName(nameEditText.getText().toString());
                            customer.setAddress(addressEditText.getText().toString());
                            customer.setSector(sectorSpinner.getSelectedItem().toString());
                            customer.setDebt(0);

                            dao.createCustomer(customer);

                            nameEditText.setText("");
                            addressEditText.setText("");
                            sectorSpinner.setSelection(0);
                            nameEditText.requestFocus();
                            countTextView.setText("Clientes: " + dao.getCustomersCount());

                            // TODO: Hardcode
                            Toast.makeText(
                                CreateCustomerActivity.this.getApplicationContext(),
                                "Cliente ["+customer.getName() +"] creado!",
                                Toast.LENGTH_SHORT
                            ).show();

                            K.searchName = customer.getName();
                            Intent intent = new Intent(CreateCustomerActivity.this, MainActivity.class);
                            CreateCustomerActivity.this.startActivity(intent);
                        }catch (NumberFormatException ex){
                            Toast.makeText(
                                CreateCustomerActivity.this.getApplicationContext(),
                                "Ingrese sólo números en el saldo",
                                Toast.LENGTH_SHORT
                            ).show();
                        }
                    }
                }
            }
        );
    }

    private void loadComponents() {
        backButton = (Button) findViewById(R.id.backButton);
        createButton = (Button) findViewById(R.id.createButton);
        nameEditText = (EditText) findViewById(R.id.nameTextView);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        sectorSpinner = (Spinner) findViewById(R.id.sectorSpinner);
        countTextView = (TextView) findViewById(R.id.countTextView);

        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        addressEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

}
