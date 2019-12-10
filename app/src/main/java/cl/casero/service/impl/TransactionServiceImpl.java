package cl.casero.service.impl;

import java.util.List;

import cl.casero.model.enums.SaleType;
import cl.casero.model.Transaction;
import cl.casero.model.dao.DaoTransaction;
import cl.casero.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private DaoTransaction daoTransaction;

    public TransactionServiceImpl(){
        this.daoTransaction = new DaoTransaction();
    }

    @Override
    public void create(Transaction transaction) {
        this.daoTransaction.create(transaction);
    }

    @Override
    public Transaction readById(Number id) {
        return this.daoTransaction.readById(id);
    }

    @Override
    public List<Transaction> readBy(String filter) {
        return this.daoTransaction.readBy(filter);
    }

    @Override
    public List<Transaction> readByCustomer(int customerId, boolean ascending) {
        return this.daoTransaction.readByCustomer(customerId, ascending);
    }

    @Override
    public void updateDebt(int customerId, int newDebt) {
        this.daoTransaction.updateDebt(customerId, newDebt);
    }

    @Override
    public void pay(Transaction transaction, int amount) {
        this.daoTransaction.pay(transaction, amount);
    }

    @Override
    public void refund(Transaction transaction, int amount) {
        this.daoTransaction.refund(transaction, amount);
    }

    @Override
    public void debtCondonation(Transaction transaction, int amount) {
        this.daoTransaction.debtCondonation(transaction, amount);
    }

    @Override
    public void createSale(Transaction transaction, int amount, int itemCounts, SaleType saleType) {
        this.daoTransaction.createSale(transaction, amount, itemCounts, saleType);
    }
}
