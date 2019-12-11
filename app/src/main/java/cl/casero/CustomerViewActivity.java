package cl.casero;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import cl.casero.adapter.TransactionAdapter;
import cl.casero.listener.customerView.AddressButtonOnClickListener;
import cl.casero.listener.customerView.OrderSwitchOnCheckedChangeListener;
import cl.casero.model.Customer;
import cl.casero.model.Transaction;
import cl.casero.model.util.K;
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

    private static CustomerViewActivity customerViewActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_view);

        customerViewActivity = this;

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
        orderSwitch.setOnCheckedChangeListener(new OrderSwitchOnCheckedChangeListener());
        addressButton.setOnClickListener(new AddressButtonOnClickListener());
    }

    private void loadComponents() {
        nameTextView = (TextView) findViewById(R.id.nameCustomerView);
        detailListView = (ListView) findViewById(R.id.detailListView);
        orderSwitch = (Switch) findViewById(R.id.orderSwitch);
        addressButton = (Button) findViewById(R.id.addressButton);
    }

    public static CustomerViewActivity getActivity(){
        return customerViewActivity;
    }
}
