package cl.casero;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.*;

import java.util.List;

import cl.casero.adapter.TransactionAdapter;
import cl.casero.model.Customer;
import cl.casero.model.util.K;
import cl.casero.model.Transaction;
import cl.casero.model.Resource;
import cl.casero.service.CustomerService;
import cl.casero.service.TransactionService;
import cl.casero.service.impl.CustomerServiceImpl;
import cl.casero.service.impl.TransactionServiceImpl;

public class CustomerViewActivity extends ActionBarActivity {

    private TextView nameTextView;
    private ListView detailListView;
    private Switch orderSwitch;
    private Button addressButton;

    private CustomerService customerService;
    private TransactionService transactionService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);

        customerService = new CustomerServiceImpl();
        transactionService = new TransactionServiceImpl();

        loadComponents();
        loadListeners();
        loadCustomers();
    }

    private void loadCustomers() {
        // TODO: Investigar como pasar el id del cliente de otra forma, no como atributo estático
        Customer customer = customerService.readById(K.customerId);

        nameTextView.setText(customer.getName());
        List<Transaction> transactions = transactionService.readByCustomer(customer.getId(), false);

        detailListView.setAdapter(new TransactionAdapter(CustomerViewActivity.this, transactions));
    }

    private void loadListeners() {
        // TODO: Separar listeners
        orderSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Customer customer = customerService.readById(K.customerId);

                nameTextView.setText(customer.getName());

                List<Transaction> transactions = transactionService.readByCustomer(customer.getId(), isChecked);

                detailListView.setAdapter(new TransactionAdapter(CustomerViewActivity.this, transactions));
            }
        });

        addressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Customer customer = customerService.readById(K.customerId);

                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerViewActivity.this);

                String addressOf = Resource.getString(R.string.address_of);
                addressOf = addressOf.replace("{0}",customer.getName());
                builder.setTitle(addressOf);

                builder.setMessage(customer.getAddress() +", "+customer.getSector());
                builder.setPositiveButton(Resource.getString(R.string.ok), null);
                builder.create().show();
            }
        });
    }

    private void loadComponents() {
        nameTextView = (TextView) findViewById(R.id.nameCustomerView);
        detailListView = (ListView) findViewById(R.id.detailListView);
        orderSwitch = (Switch) findViewById(R.id.orderSwitch);
        addressButton = (Button) findViewById(R.id.addressButton);
    }
}
