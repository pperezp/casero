package cl.casero.service.impl;

import java.util.List;

import cl.casero.model.Customer;
import cl.casero.model.MonthlyStatistic;
import cl.casero.model.Statistic;
import cl.casero.model.dao.DaoStatistics;
import cl.casero.service.StatisticsService;

public class StatisticsServiceImpl implements StatisticsService {

    private DaoStatistics daoStatistics;

    public StatisticsServiceImpl(){
        this.daoStatistics = new DaoStatistics();
    }

    @Override
    public void create(Statistic statistic) {
        this.daoStatistics.create(statistic);
    }

    @Override
    public List<Statistic> read() {
        return this.daoStatistics.read();
    }

    @Override
    public Statistic readById(Number id) {
        return this.daoStatistics.readById(id);
    }

    @Override
    public List<Statistic> readBy(String filter) {
        return this.daoStatistics.readBy(filter);
    }

    @Override
    public MonthlyStatistic getMonthlyStatistic(String startDate, String endDate, boolean isDateRange) {
        return this.daoStatistics.getMonthlyStatistic(startDate, endDate, isDateRange);
    }

    @Override
    public MonthlyStatistic getMonthlyStatistic(int month, int year) {
        return this.daoStatistics.getMonthlyStatistic(month, year);
    }

    @Override
    public int getTotalDebt() {
        return this.daoStatistics.getTotalDebt();
    }

    @Override
    public int getAverageDebt() {
        return this.daoStatistics.getAverageDebt();
    }

    @Override
    public int getCustomersCount(String sector) {
        return this.daoStatistics.getCustomersCount(sector);
    }

    @Override
    public List<Customer> getDebtors(int limit) {
        return this.daoStatistics.getDebtors(limit);
    }

    @Override
    public List<Customer> getBestCustomers(int limit) {
        return this.daoStatistics.getBestCustomers(limit);
    }

    @Override
    public int getCustomersCount() {
        return this.daoStatistics.getCustomersCount();
    }
}
