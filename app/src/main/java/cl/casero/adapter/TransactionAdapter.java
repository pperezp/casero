package cl.casero.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cl.casero.R;
import cl.casero.bd.model.Transaction;
import cl.casero.bd.model.Util;
import cl.casero.model.Resource;

/**
 * Created by Patricio Pérez Pinto on 04/01/2016.
 */
public class TransactionAdapter extends BaseAdapter {
    private List<Transaction> transactions;
    private Activity activity;

    public TransactionAdapter(Activity activity, List<Transaction> transactions) {
        this.activity = activity;
        this.transactions = transactions;
    }
    @Override
    public int getCount() {
        return transactions.size();
    }

    @Override
    public Object getItem(int position) {
        return transactions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return transactions.get(position).getId();
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View view = contentView;

        String format = Resource.getString(R.string.date_pattern);
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);

        if (contentView == null) {
            LayoutInflater inflater =
                (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(inflater != null){
                view = inflater.inflate(R.layout.activity_transaction_item, null);
            }
        }

        Transaction transaction = transactions.get(position);

        TextView dateTextView = (TextView) view.findViewById(R.id.dateTextView);
        TextView detailTextView = (TextView) view.findViewById(R.id.detailTextView);
        TextView balanceTextView = (TextView) view.findViewById(R.id.transactionBalanceTextView);

        dateTextView.setText("["+dateFormat.format(transaction.getDate())+"]");
        detailTextView.setText(transaction.getDetail());

        String payment = Resource.getString(R.string.payment);
        String sale = Resource.getString(R.string.sale);

        int paymentColor = Resource.getColor(R.color.payment);
        int saleColor = Resource.getColor(R.color.sale);
        int transactionColor = Resource.getColor(R.color.transaction);

        if(transaction.getDetail().contains(payment)){
            balanceTextView.setTextColor(paymentColor);
        }else if(transaction.getDetail().contains(sale)){
            balanceTextView.setTextColor(saleColor);
        }else{
            balanceTextView.setTextColor(transactionColor);
        }

        balanceTextView.setText("$"+ Util.formatPrice(transaction.getBalance()));

        return view;
    }
}
