package cl.casero.service.impl;

import java.util.List;

import cl.casero.model.enums.SaleType;
import cl.casero.model.Transaction;
import cl.casero.model.dao.impl.TransactionDao;
import cl.casero.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private TransactionDao transactionDao;

    public TransactionServiceImpl() {
        this.transactionDao = new TransactionDao();
    }

    @Override
    public void create(Transaction transaction) {
        this.transactionDao.create(transaction);
    }

    @Override
    public Transaction readById(Number id) {
        return this.transactionDao.readById(id);
    }

    @Override
    public List<Transaction> readBy(String filter) {
        return this.transactionDao.readBy(filter);
    }

    @Override
    public List<Transaction> readByCustomer(int customerId, boolean ascending) {
        return this.transactionDao.readByCustomer(customerId, ascending);
    }

    @Override
    public void updateDebt(int customerId, int newDebt) {
        this.transactionDao.updateDebt(customerId, newDebt);
    }

    @Override
    public void pay(Transaction transaction, int amount) {
        this.transactionDao.pay(transaction, amount);
    }

    @Override
    public void refund(Transaction transaction, int amount) {
        this.transactionDao.refund(transaction, amount);
    }

    @Override
    public void forgiveDebt(Transaction transaction, int amount) {
        this.transactionDao.debtForgiveness(transaction, amount);
    }

    @Override
    public void createSale(Transaction transaction, int amount, int itemCounts, SaleType saleType) {
        this.transactionDao.createSale(transaction, amount, itemCounts, saleType);
    }
}
