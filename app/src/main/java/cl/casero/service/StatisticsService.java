package cl.casero.service;

import java.util.List;

import cl.casero.model.Customer;
import cl.casero.model.MonthlyStatistic;
import cl.casero.model.Statistic;
import cl.casero.model.Transaction;
import cl.casero.model.enums.SaleType;

public interface StatisticsService {
    void create(Transaction transaction);

    void create(Transaction transaction, int itemCounts, SaleType saleType);

    List<Statistic> read();

    Statistic readById(Number id);

    List<Statistic> readBy(String filter);

    MonthlyStatistic getMonthlyStatistic(String startDate, String endDate, boolean isDateRange);

    MonthlyStatistic getMonthlyStatistic(int month, int year);

    int getTotalDebt();

    int getAverageDebt();

    int getCustomersCount(String sector);

    List<Customer> getDebtors(int limit);

    List<Customer> getBestCustomers(int limit);

    int getCustomersCount();

    void deleteBy(Transaction transaction);
}
