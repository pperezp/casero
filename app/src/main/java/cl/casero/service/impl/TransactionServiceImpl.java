package cl.casero.service.impl;

import java.util.List;

import cl.casero.model.dao.impl.CustomerDao;
import cl.casero.model.dao.impl.StatisticsDao;
import cl.casero.model.enums.SaleType;
import cl.casero.model.Transaction;
import cl.casero.model.dao.impl.TransactionDao;
import cl.casero.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private TransactionDao transactionDao;
    private CustomerDao customerDao;
    private StatisticsDao statisticsDao;

    public TransactionServiceImpl() {
        this.transactionDao = new TransactionDao();
        this.customerDao = new CustomerDao();
        this.statisticsDao = new StatisticsDao();
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

    @Override
    public void delete(long id) {
        Transaction transaction = transactionDao.readById(id);

        customerDao.updateCustomerBalance(transaction);
        statisticsDao.deleteBy(transaction);
        transactionDao.delete(id);
    }
}
