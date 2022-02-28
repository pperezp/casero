package cl.casero.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cl.casero.R;
import cl.casero.model.Customer;
import cl.casero.model.util.Util;
import cl.casero.service.CustomerService;
import cl.casero.service.impl.CustomerServiceImpl;

public class CustomerAdapter extends BaseAdapter {

    private final Activity activity;
    private final List<Customer> customers;
    private final CustomerService customerService;

    public CustomerAdapter(Activity activity, List<Customer> customers) {
        this.activity = activity;
        this.customers = customers;
        this.customerService = new CustomerServiceImpl();
    }

    @Override
    public int getCount() {
        return customers.size();
    }

    @Override
    public Object getItem(int position) {
        return customers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return customers.get(position).getId();
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View view = inflate(contentView, parent);

        Customer customer = customers.get(position);

        TextView customerNameTextView = (TextView) view.findViewById(R.id.nameTextView);
        customerNameTextView.setText(customer.getName());


        TextView sectorTextView = (TextView) view.findViewById(R.id.sectorTextView);
        sectorTextView.setText(customer.getSector());

        int balance = customerService.getDebt(customer.getId());

        TextView balanceTextView = (TextView) view.findViewById(R.id.customerBalanceTextView);
        balanceTextView.setText(Util.formatPrice(balance));

        return view;
    }

    private View inflate(View view, ViewGroup parent) {
        if (view != null) {
            return view;
        }

        LayoutInflater layoutInflater = (LayoutInflater)
                activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        return layoutInflater.inflate(R.layout.activity_customer_item, parent, false);
    }
}
