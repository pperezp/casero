package cl.casero.model.util;

import java.util.Date;

public class K {

    private K() {
        throw new IllegalStateException("Utility class");
    }

    public static long customerId;
    public static Date date;
    public static String startDate;
    public static String endDate;
    public static String searchName;
    public static int paymentAmount;
    public static int refundAmount;
    public static String refundDetailInput;
    public static String debtForgivenessDetailInput;
}
