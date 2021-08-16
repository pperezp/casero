package cl.casero.service.impl;

import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cl.casero.MainActivity;
import cl.casero.R;
import cl.casero.adapter.CustomerAdapter;
import cl.casero.model.Customer;
import cl.casero.model.util.K;
import cl.casero.service.CustomerService;
import cl.casero.service.ViewService;

public class ViewServiceImpl implements ViewService {

    private final ListView customersListView;
    private final EditText searchNameEditText;
    private final CustomerService customerService;
    private final MainActivity mainActivity;

    public ViewServiceImpl(){
        this.mainActivity = MainActivity.getInstance();
        this.customersListView = (ListView) mainActivity.findViewById(R.id.customersListView);
        this.searchNameEditText = (EditText) mainActivity.findViewById(R.id.searchNameEditText);
        this.customerService = new CustomerServiceImpl();
    }

    @Override
    public void loadCustomerListView() {
        K.searchName = searchNameEditText.getText().toString();
        List<Customer> customers = customerService.readBy(K.searchName);

        if (!customers.isEmpty()) {
            customersListView.setAdapter(new CustomerAdapter(mainActivity, customers));
        } else {
            customersListView.setAdapter(new CustomerAdapter(mainActivity, new ArrayList<>()));
        }
    }
}
