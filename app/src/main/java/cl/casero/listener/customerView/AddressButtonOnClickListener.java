package cl.casero.listener.customerView;

import android.app.AlertDialog;
import android.view.View;

import cl.casero.CustomerViewActivity;
import cl.casero.R;
import cl.casero.model.Customer;
import cl.casero.model.Resource;
import cl.casero.model.util.K;
import cl.casero.service.CustomerService;
import cl.casero.service.impl.CustomerServiceImpl;

public class AddressButtonOnClickListener implements View.OnClickListener {

    private CustomerViewActivity customerViewActivity;

    private CustomerService customerService;

    public AddressButtonOnClickListener(){
        this.customerViewActivity = CustomerViewActivity.getActivity();
        this.customerService = new CustomerServiceImpl();
    }

    @Override
    public void onClick(View view) {
        Customer customer = customerService.readById(K.customerId);

        AlertDialog.Builder builder = new AlertDialog.Builder(customerViewActivity);

        String addressOf = Resource.getString(R.string.address_of);
        addressOf = addressOf.replace("{0}",customer.getName());
        builder.setTitle(addressOf);

        builder.setMessage(customer.getAddress() +", "+customer.getSector());
        builder.setPositiveButton(Resource.getString(R.string.ok), null);
        builder.create().show();
    }
}
