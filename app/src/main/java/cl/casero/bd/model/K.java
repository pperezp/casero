package cl.casero.bd.model;

import java.util.Date;

/**
 * Created by Patricio Pérez Pinto on 04/01/2016.
 */
public class K {
    public static long customerId;
    public static Date date;
    public static String startDate;
    public static String endDate;
    public final static int SALE = 0;
    public final static int PAYMENT = 1;
    public final static int REFUND = 2;
    public final static int DEBT_CONDONATION = 3;
    public final static int NEW_SALE = 0;
    public final static int MAINTENANCE = 1;
    public static String searchName = null;
}
