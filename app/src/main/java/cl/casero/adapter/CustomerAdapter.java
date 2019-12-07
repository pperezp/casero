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
import cl.casero.bd.DAO;
import cl.casero.bd.model.Customer;
import cl.casero.bd.model.Util;

/**
 * Created by Patricio Pérez Pinto on 04/01/2016.
 */
public class CustomerAdapter extends BaseAdapter {
    private Activity activity;
    private List<Customer> customers;

    public CustomerAdapter(Activity activity, List<Customer> customers) {
        this.activity = activity;
        this.customers = customers;
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
        View view = contentView;

		/*Acá cargo el layout del item customer*/
        if (contentView == null) {
            LayoutInflater inflater =
                (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(inflater != null){
                view = inflater.inflate(R.layout.activity_customer_item, null);
            }
        }

		/*obtengo el customer en la posición determinada*/
        Customer customer = customers.get(position);

        TextView customerNameTextView = (TextView) view.findViewById(R.id.nameTextView);
        customerNameTextView.setText(customer.getName());


        TextView sectorTextView = (TextView) view.findViewById(R.id.sectorTextView);
        sectorTextView.setText(customer.getSector());

        DAO dao = new DAO(this.activity.getApplicationContext());

        int balance = dao.getDebt(customer.getId());

        TextView balanceTextView = (TextView) view.findViewById(R.id.customerBalanceTextView);
        balanceTextView.setText("$ "+ Util.formatPrice(balance));

        return view;
    }
}
