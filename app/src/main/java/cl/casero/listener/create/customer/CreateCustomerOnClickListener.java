package cl.casero.listener.create.customer;

import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import cl.casero.CreateCustomerActivity;
import cl.casero.MainActivity;
import cl.casero.R;
import cl.casero.model.Customer;
import cl.casero.model.Resource;
import cl.casero.model.util.K;
import cl.casero.service.CustomerService;
import cl.casero.service.StatisticsService;
import cl.casero.service.impl.CustomerServiceImpl;
import cl.casero.service.impl.StatisticsServiceImpl;

public class CreateCustomerOnClickListener implements View.OnClickListener {

    private EditText nameEditText;
    private EditText addressEditText;
    private Spinner sectorSpinner;
    private TextView countTextView;

    private final CustomerService customerService;
    private final StatisticsService statisticsService;
    private final CreateCustomerActivity createCustomerActivity;

    public CreateCustomerOnClickListener(CreateCustomerActivity createCustomerActivity){
        this.createCustomerActivity = createCustomerActivity;
        this.customerService = new CustomerServiceImpl();
        this.statisticsService = new StatisticsServiceImpl();

        loadComponents();
    }

    private void loadComponents() {
        nameEditText = (EditText) createCustomerActivity.findViewById(R.id.nameTextView);
        addressEditText = (EditText) createCustomerActivity.findViewById(R.id.addressEditText);
        sectorSpinner = (Spinner) createCustomerActivity.findViewById(R.id.sectorSpinner);
        countTextView = (TextView) createCustomerActivity.findViewById(R.id.countTextView);

        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        addressEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    @Override
    public void onClick(View v) {
        /*Validaciones*/
        if (nameEditText.getText().toString().trim().equals("")) {
            Toast.makeText(
                    createCustomerActivity.getApplicationContext(),
                    Resource.getString(R.string.customer_name_required),
                    Toast.LENGTH_SHORT
            ).show();
        } else if (addressEditText.getText().toString().trim().equals("")) {
            Toast.makeText(
                    createCustomerActivity.getApplicationContext(),
                    Resource.getString(R.string.address_required),
                    Toast.LENGTH_SHORT
            ).show();
        } else {
            try {
                Customer customer = new Customer();

                customer.setName(nameEditText.getText().toString());
                customer.setAddress(addressEditText.getText().toString());
                customer.setSector(sectorSpinner.getSelectedItem().toString());
                customer.setDebt(0);

                customerService.create(customer);

                nameEditText.setText("");
                addressEditText.setText("");
                sectorSpinner.setSelection(0);
                nameEditText.requestFocus();

                String customers = Resource.getString(R.string.customers);
                customers = customers.replace("{0}", String.valueOf(statisticsService.getCustomersCount()));
                countTextView.setText(customers);

                String createdCustomerMessage = Resource.getString(R.string.created_customer);
                createdCustomerMessage = createdCustomerMessage.replace("{0}", customer.getName());

                Toast.makeText(
                        createCustomerActivity.getApplicationContext(),
                        createdCustomerMessage,
                        Toast.LENGTH_SHORT
                ).show();

                K.searchName = customer.getName();
                Intent intent = new Intent(createCustomerActivity, MainActivity.class);
                createCustomerActivity.startActivity(intent);
            } catch (NumberFormatException ex) {
                Toast.makeText(
                        createCustomerActivity.getApplicationContext(),
                        Resource.getString(R.string.only_numbers_in_balance),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }
    }
}
