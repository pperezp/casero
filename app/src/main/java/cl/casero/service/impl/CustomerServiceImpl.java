package cl.casero.service.impl;

import java.util.List;

import cl.casero.model.Customer;
import cl.casero.model.dao.DaoCustomer;
import cl.casero.service.CustomerService;

public class CustomerServiceImpl implements CustomerService {

    private DaoCustomer daoCustomer;

    public CustomerServiceImpl(){
        this.daoCustomer = new DaoCustomer();
    }

    @Override
    public void create(Customer customer) {
        this.daoCustomer.create(customer);
    }

    @Override
    public List<Customer> read() {
        return this.daoCustomer.read();
    }

    @Override
    public Customer readById(Number id) {
        return this.daoCustomer.readById(id);
    }

    @Override
    public List<Customer> readBy(String filter) {
        return this.daoCustomer.readBy(filter);
    }

    @Override
    public int getLastCustomerId() {
        return this.daoCustomer.getLastCustomerId();
    }


    @Override
    public int getDebt(int customerId) {
        return this.daoCustomer.getDebt(customerId);
    }

    @Override
    public void updateAddress(long customerId, String newAddress) {
        this.daoCustomer.updateAddress(customerId, newAddress);
    }
}
