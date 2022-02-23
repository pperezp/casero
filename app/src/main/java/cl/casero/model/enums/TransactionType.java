package cl.casero.model.enums;

public enum TransactionType {

    SALE(0),
    PAYMENT(1),
    REFUND(2),
    DEBT_FORGIVENESS(3),
    INITIAL_BALANCE(4);

    private final int id;

    TransactionType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
