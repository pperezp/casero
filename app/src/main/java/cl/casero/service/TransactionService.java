package cl.casero.service;

import java.util.List;

import cl.casero.model.enums.SaleType;
import cl.casero.model.Transaction;

public interface TransactionService {
    void create(Transaction transaction);

    Transaction readById(Number id);

    List<Transaction> readBy(String filter);

    List<Transaction> readByCustomer(int customerId, boolean ascending);

    void updateDebt(int customerId, int newDebt);

    void pay(Transaction transaction);

    void refund(Transaction transaction);

    void forgiveDebt(Transaction transaction);

    void createSale(Transaction transaction, int itemCounts, SaleType saleType);

    void delete(long id);

    void deleteAll(Number customerId);
}
