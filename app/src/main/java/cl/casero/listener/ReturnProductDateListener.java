package cl.casero.listener;

import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cl.casero.MainActivity;
import cl.casero.R;
import cl.casero.adapter.CustomerAdapter;
import cl.casero.model.Customer;
import cl.casero.model.Resource;
import cl.casero.model.Transaction;
import cl.casero.model.util.K;
import cl.casero.model.util.Util;
import cl.casero.service.CustomerService;
import cl.casero.service.TransactionService;
import cl.casero.service.ViewService;
import cl.casero.service.impl.CustomerServiceImpl;
import cl.casero.service.impl.TransactionServiceImpl;
import cl.casero.service.impl.ViewServiceImpl;

import static cl.casero.model.util.K.refundAmount;

public class ReturnProductDateListener implements OnDateSetListener {

    private final CustomerService customerService;
    private final TransactionService transactionService;
    private final ViewService viewService;

    public ReturnProductDateListener(){
        this.customerService = new CustomerServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.viewService = new ViewServiceImpl();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Date date = Util.getDate(year, monthOfYear, dayOfMonth);
        String refundDetail = Resource.getString(R.string.refund_detail);

        refundDetail = refundDetail.replace("{0}", Util.formatPrice(refundAmount));
        refundDetail = refundDetail.replace("{1}", K.refundDetailInput);

        Transaction transaction = new Transaction();

        transaction.setDate(date);
        transaction.setCustomerId((int) K.customerId);
        transaction.setDetail(refundDetail);

        int balance = customerService.getDebt(transaction.getCustomerId());

        balance = balance - refundAmount;
        transaction.setBalance(balance);

        transactionService.refund(transaction, refundAmount);

        viewService.loadCustomerListView();
    }
}
