package cl.casero.service.impl;

import java.util.List;

import cl.casero.model.Customer;
import cl.casero.model.dao.impl.CustomerDao;
import cl.casero.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

    private final CustomerDao customerDao;

    public CustomerServiceImpl() {
        this.customerDao = new CustomerDao();
    }

    @Override
    public Customer create(Customer customer) {
        this.customerDao.create(customer);
        return customer;
    }

    @Override
    public List<Customer> read() {
        return this.customerDao.read();
    }

    @Override
    public Customer readById(Number id) {
        return this.customerDao.readById(id);
    }

    @Override
    public List<Customer> readBy(String filter) {
        return this.customerDao.readBy(filter);
    }

    @Override
    public int getLastCustomerId() {
        return this.customerDao.getLastCustomerId();
    }


    @Override
    public int getDebt(int customerId) {
        return this.customerDao.getDebt(customerId);
    }

    @Override
    public void updateAddress(long customerId, String newAddress) {
        this.customerDao.updateAddress(customerId, newAddress);
    }

    @Override
    public void delete(Number id) {
        this.customerDao.delete(id);
    }
}
