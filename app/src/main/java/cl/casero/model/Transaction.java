package cl.casero.model;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cl.casero.R;

public class Transaction {

    private int id;
    private int customerId;
    private int amount;
    private int balance;
    private int type;
    private Date date;
    private String detail;
    private String rawDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void forgiveDebt() {
        this.balance = 0;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getRawDate() {
        return rawDate;
    }

    public void setRawDate(String rawDate) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(Resource.getString(R.string.database_date_pattern));
            this.rawDate = rawDate;
            this.date = dateFormat.parse(rawDate);
        } catch (ParseException ex) {
            Log.i("ParseException", "ParseException in setRawDate method");
        }
    }
}
