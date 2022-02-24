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

    public static TransactionType getTransactionType(int id) {
        switch (id){
            case 0:
                return SALE;
            case 1:
                return PAYMENT;
            case 2:
                return REFUND;
            case 3:
                return DEBT_FORGIVENESS;
            default:
                return INITIAL_BALANCE;
        }
    }

    public int getId() {
        return id;
    }
}
