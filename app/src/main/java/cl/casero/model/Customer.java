package cl.casero.model;

import cl.casero.model.util.Util;

public class Customer {

    private int id;
    private String name;
    private String sector;
    private String address;
    private int debt;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDebt() {
        return debt;
    }

    public void setDebt(int debt) {
        this.debt = debt;
    }

    public String getFormattedDebt() {
        return Util.formatPrice(debt);
    }
}
