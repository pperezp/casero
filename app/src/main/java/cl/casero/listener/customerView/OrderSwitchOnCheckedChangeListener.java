package cl.casero.listener.customerView;

import android.widget.CompoundButton;
import android.widget.ListView;

import java.util.List;

import cl.casero.CustomerViewActivity;
import cl.casero.R;
import cl.casero.adapter.TransactionAdapter;
import cl.casero.model.Customer;
import cl.casero.model.Transaction;
import cl.casero.model.util.K;
import cl.casero.service.CustomerService;
import cl.casero.service.TransactionService;
import cl.casero.service.impl.CustomerServiceImpl;
import cl.casero.service.impl.TransactionServiceImpl;

public class OrderSwitchOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
    private ListView detailListView;

    private CustomerService customerService;
    private TransactionService transactionService;

    private CustomerViewActivity customerViewActivity;

    public OrderSwitchOnCheckedChangeListener(){
        this.customerViewActivity = CustomerViewActivity.getActivity();

        this.customerService = new CustomerServiceImpl();
        this.transactionService = new TransactionServiceImpl();

        loadComponents();
    }

    private void loadComponents() {
        detailListView = (ListView) customerViewActivity.findViewById(R.id.detailListView);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        Customer customer = customerService.readById(K.customerId);

        // TODO: Investigar como invertir los items de un ListView
        List<Transaction> transactions = transactionService.readByCustomer(customer.getId(), isChecked);

        detailListView.setAdapter(new TransactionAdapter(customerViewActivity, transactions));
    }
}
