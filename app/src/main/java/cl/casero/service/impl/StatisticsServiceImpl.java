package cl.casero.service.impl;

import java.util.List;

import cl.casero.model.Customer;
import cl.casero.model.MonthlyStatistic;
import cl.casero.model.Statistic;
import cl.casero.model.Transaction;
import cl.casero.model.dao.impl.StatisticsDao;
import cl.casero.model.dao.impl.TransactionDao;
import cl.casero.model.enums.SaleType;
import cl.casero.service.StatisticsService;

public class StatisticsServiceImpl implements StatisticsService {

    private final StatisticsDao statisticsDao;
    private final TransactionDao transactionDao;

    public StatisticsServiceImpl() {
        this.statisticsDao = new StatisticsDao();
        this.transactionDao = new TransactionDao();
    }

    @Override
    public void create(Transaction transaction) {
        transactionDao.create(transaction);
        transactionDao.updateDebt(transaction);
        statisticsDao.create(new Statistic(transaction));
    }

    @Override
    public void create(Transaction transaction, int itemCounts, SaleType saleType) {
        transactionDao.create(transaction);
        transactionDao.updateDebt(transaction);
        statisticsDao.create(new Statistic(transaction, itemCounts, saleType));
    }

    @Override
    public List<Statistic> read() {
        return this.statisticsDao.read();
    }

    @Override
    public Statistic readById(Number id) {
        return this.statisticsDao.readById(id);
    }

    @Override
    public List<Statistic> readBy(String filter) {
        return this.statisticsDao.readBy(filter);
    }

    @Override
    public MonthlyStatistic getMonthlyStatistic(String startDate, String endDate, boolean isDateRange) {
        return this.statisticsDao.getMonthlyStatistic(startDate, endDate, isDateRange);
    }

    @Override
    public MonthlyStatistic getMonthlyStatistic(int month, int year) {
        return this.statisticsDao.getMonthlyStatistic(month, year);
    }

    @Override
    public int getTotalDebt() {
        return this.statisticsDao.getTotalDebt();
    }

    @Override
    public int getAverageDebt() {
        return this.statisticsDao.getAverageDebt();
    }

    @Override
    public int getCustomersCount(String sector) {
        return this.statisticsDao.getCustomersCount(sector);
    }

    @Override
    public List<Customer> getDebtors(int limit) {
        return this.statisticsDao.getDebtors(limit);
    }

    @Override
    public List<Customer> getBestCustomers(int limit) {
        return this.statisticsDao.getBestCustomers(limit);
    }

    @Override
    public int getCustomersCount() {
        return this.statisticsDao.getCustomersCount();
    }

    @Override
    public void deleteBy(Transaction transaction) {
        this.statisticsDao.deleteBy(transaction);
    }
}
