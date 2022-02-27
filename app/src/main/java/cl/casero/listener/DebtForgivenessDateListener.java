package cl.casero.listener;

import android.app.DatePickerDialog.OnDateSetListener;
import android.widget.DatePicker;

import java.util.Date;

import cl.casero.R;
import cl.casero.model.Resource;
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

public class DebtForgivenessDateListener implements OnDateSetListener {

    private final CustomerService customerService;
    private final TransactionService transactionService;
    private final ViewService viewService;

    public DebtForgivenessDateListener(){
        this.customerService = new CustomerServiceImpl();
        this.transactionService = new TransactionServiceImpl();
        this.viewService = new ViewServiceImpl();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Date date = Util.getDate(year, monthOfYear, dayOfMonth);
        String debtForgivenessDetail = Resource.getString(R.string.debt_forgiveness_detail);
        debtForgivenessDetail = debtForgivenessDetail.replace("{0}", K.debtForgivenessDetailInput);
        int balance = customerService.getDebt((int) K.customerId);

        Transaction transaction = new Transaction();

        transaction.setDate(date);
        transaction.setCustomerId((int) K.customerId);
        transaction.setDetail(debtForgivenessDetail);
        transaction.setType(TransactionType.DEBT_FORGIVENESS.getId());
        transaction.setAmount(balance);
        transaction.forgiveDebt();

        transactionService.forgiveDebt(transaction);

        viewService.loadCustomerListView();
    }
}
