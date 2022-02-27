package cl.casero.listener;

import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;

import java.util.Date;

import cl.casero.model.Transaction;
import cl.casero.model.enums.TransactionType;
import cl.casero.model.util.K;
import cl.casero.model.util.Util;
import cl.casero.service.CustomerService;
import cl.casero.service.TransactionService;
import cl.casero.service.ViewService;
import cl.casero.service.impl.CustomerServiceImpl;
import cl.casero.service.impl.TransactionServiceImpl;
import cl.casero.service.impl.ViewServiceImpl;

public class PaymentDateListener implements OnDateSetListener {

    private final CustomerService customerService;
    private final TransactionService transactionService;
    private final ViewService viewService;

    public PaymentDateListener(){
        this.customerService = new CustomerServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.viewService = new ViewServiceImpl();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Date date = Util.getDate(year, monthOfYear, dayOfMonth);

        Transaction transaction = createPaymentTransaction(date);

        int balance = customerService.getDebt(transaction.getCustomerId());
        balance = balance - K.paymentAmount;
        transaction.setBalance(balance);

        transactionService.pay(transaction);
        viewService.loadCustomerListView();
    }

    private Transaction createPaymentTransaction(Date date) {
        Transaction transaction = new Transaction();

        transaction.setDate(date);
        transaction.setCustomerId((int) K.customerId);
        transaction.setDetail("[Abono]: $" + K.paymentAmount);
        transaction.setAmount(K.paymentAmount);
        transaction.setType(TransactionType.PAYMENT.getId());

        return transaction;
    }
}
