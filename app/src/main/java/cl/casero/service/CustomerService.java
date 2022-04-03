package cl.casero.service;

import java.util.List;

import cl.casero.model.Customer;

public interface CustomerService {
    Customer create(Customer customer);

    List<Customer> read();

    Customer readById(Number id);

    List<Customer> readBy(String filter);

    int getLastCustomerId();

    int getDebt(int customerId);

    void updateAddress(long customerId, String newAddress);

    void delete(Number id);
}
