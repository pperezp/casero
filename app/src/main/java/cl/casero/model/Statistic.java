package cl.casero.model;

import java.util.Date;

import cl.casero.model.enums.SaleType;

public class Statistic {

    private int id;
    private int type;
    private int amount;
    private int saleType;
    private int itemsCount;
    private Date date;

    public Statistic(Transaction transaction) {
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.date = transaction.getDate();
        this.saleType = -1;
        this.itemsCount = -1;
    }

    public Statistic(Transaction transaction, int itemCounts, SaleType saleType) {
        this(transaction);
        this.saleType = saleType.getId();
        this.itemsCount = itemCounts;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSaleType() {
        return saleType;
    }

    public void setSaleType(int saleType) {
        this.saleType = saleType;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }
}
