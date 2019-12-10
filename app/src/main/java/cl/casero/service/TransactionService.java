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

    void pay(Transaction transaction, int amount);

    void refund(Transaction transaction, int amount);

    void debtCondonation(Transaction transaction, int amount);

    void createSale(Transaction transaction, int amount, int itemCounts, SaleType saleType);
}
