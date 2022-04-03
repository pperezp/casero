package cl.casero.service.impl;

import java.util.List;

import cl.casero.model.dao.impl.CustomerDao;
import cl.casero.model.enums.SaleType;
import cl.casero.model.Transaction;
import cl.casero.model.dao.impl.TransactionDao;
import cl.casero.service.StatisticsService;
import cl.casero.service.TransactionService;

public class TransactionServiceImpl implements TransactionService {

    private final TransactionDao transactionDao;
    private final CustomerDao customerDao;
    private final StatisticsService statisticsService;

    public TransactionServiceImpl() {
        this.transactionDao = new TransactionDao();
        this.customerDao = new CustomerDao();
        this.statisticsService = new StatisticsServiceImpl();
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
    public void pay(Transaction transaction) {
        this.statisticsService.create(transaction);
    }

    @Override
    public void refund(Transaction transaction) {
        this.statisticsService.create(transaction);
    }

    @Override
    public void forgiveDebt(Transaction transaction) {
        this.statisticsService.create(transaction);
    }

    @Override
    public void createSale(Transaction transaction, int itemCounts, SaleType saleType) {
        this.statisticsService.create(transaction, itemCounts, saleType);
    }

    @Override
    public void delete(long id) {
        Transaction transaction = transactionDao.readById(id);
        customerDao.updateCustomerBalance(transaction);
        statisticsService.deleteBy(transaction);
        transactionDao.delete(id);
    }

    @Override
    public void deleteAll(Number customerId) {
        List<Transaction> transactions = transactionDao.readByCustomer(customerId.intValue(), true);

        for (Transaction transaction : transactions) {
            delete(transaction.getId());
        }
    }
}
