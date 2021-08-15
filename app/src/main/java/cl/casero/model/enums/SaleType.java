package cl.casero.model.enums;

public enum SaleType {

    NEW_SALE(0),
    MAINTENANCE(1);

    private final int id;

    SaleType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
