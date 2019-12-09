package cl.casero.bd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.casero.R;
import cl.casero.bd.model.Customer;
import cl.casero.bd.model.K;
import cl.casero.bd.model.MonthlyStatistic;
import cl.casero.bd.model.Statistic;
import cl.casero.bd.model.Transaction;
import cl.casero.model.Resource;

/**
 * Created by Patricio Pérez Pinto on 04/01/2016.
 */

// TODO: Separar esta clase en daos mas pequeños
public class DAO {
    private Context context;
    private SQLiteOpenHelperImpl sqLiteOpenHelper;
    private SQLiteDatabase sqLiteDatabase;
    private Cursor cursor;
    private String query;
    private SimpleDateFormat dateFormat;

    private final String DATABASE_PATH = Environment
        .getExternalStorageDirectory()
        .getPath()+"/caseroBD/casero.sqlite";

    public DAO(Context context) {
        this.context = context;
        dateFormat = new SimpleDateFormat(Resource.getString(R.string.database_date_pattern));
    }

    public int createCustomer(Customer customer){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "INSERT INTO " +
            "   cliente " +
            "VALUES(" +
                "null, " +
                "'"+customer.getName() +"', " +
                "'"+customer.getSector()+"', " +
                "'"+customer.getAddress() +"', " +
                "'"+customer.getDebt() +"'" +
            ")";

        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();

        return getLastCustomerId();
    }

    private int getLastCustomerId(){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "MAX(id) " +
            "FROM " +
                "cliente";

        cursor = sqLiteDatabase.rawQuery(query, null);

        int id = -1;
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return id;
    }

    public void createTransaction(Transaction transaction){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        String transactionDate = dateFormat.format(transaction.getDate());

        query =
            "INSERT INTO " +
                "movimiento " +
            "VALUES(" +
                "NULL, " +
                "'"+transactionDate+"'," +
                "'"+transaction.getDetail() +"'," +
                "'"+transaction.getBalance() +"'," +
                "'"+transaction.getCustomerId() +"'" +
            ")";

        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }

    public List<Customer> getCustomers(){
        List<Customer> customers = new ArrayList<>();
        Customer customer;

        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "* " +
            "FROM " +
                "cliente";

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                customer = new Customer();

                customer.setId(cursor.getInt(0));
                customer.setName(cursor.getString(1));
                customer.setSector(cursor.getString(2));
                customer.setAddress(cursor.getString(3));
                customer.setDebt(cursor.getInt(4));

                customers.add(customer);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return customers;
    }

    public List<Customer> getDebtors(int limit){
        List<Customer> debtors = new ArrayList<>();
        Customer customer;

        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "* " +
            "FROM " +
                "cliente " +
            "ORDER BY deuda DESC " +
            "LIMIT "+limit;

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                customer = new Customer();

                customer.setId(cursor.getInt(0));
                customer.setName(cursor.getString(1));
                customer.setSector(cursor.getString(2));
                customer.setAddress(cursor.getString(3));
                customer.setDebt(cursor.getInt(4));

                debtors.add(customer);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return debtors;
    }

    public List<Customer> getBestCustomers(int limit){
        List<Customer> customers = new ArrayList<>();
        Customer customer;

        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "* " +
            "FROM " +
                "cliente " +
            "ORDER BY deuda ASC " +
            "LIMIT "+limit;

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                customer = new Customer();

                customer.setId(cursor.getInt(0));
                customer.setName(cursor.getString(1));
                customer.setSector(cursor.getString(2));
                customer.setAddress(cursor.getString(3));
                customer.setDebt(cursor.getInt(4));

                customers.add(customer);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return customers;
    }

    public List<Customer> getCustomers(String searchText){
        List<Customer> customers = new ArrayList<>();
        Customer customer;

        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        /*String select = "select * from customer where name like '%"+filtro+"%' " +
                "or address like '%"+filtro+"%' " +
                "or sector like '%"+filtro+"%' order by name asc";*/

        query =
            "SELECT * FROM cliente WHERE " +
            "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(nombre),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'ñ','n') LIKE '%"+searchText+"%' OR " +
            "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(direccion),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'ñ','n') LIKE '%"+searchText+"%' OR " +
            "REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(sector),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'ñ','n') LIKE '%"+searchText+"%' " +
            "ORDER BY nombre ASC";

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                customer = new Customer();

                customer.setId(cursor.getInt(0));
                customer.setName(cursor.getString(1));
                customer.setSector(cursor.getString(2));
                customer.setAddress(cursor.getString(3));
                customer.setDebt(cursor.getInt(4));

                customers.add(customer);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return customers;
    }

    public Customer getCustomer(long customerId){
        Customer customer = null;

        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "* " +
            "FROM " +
                "cliente " +
            "WHERE " +
                "id = '"+customerId+"'";

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                customer = new Customer();

                customer.setId(cursor.getInt(0));
                customer.setName(cursor.getString(1));
                customer.setSector(cursor.getString(2));
                customer.setAddress(cursor.getString(3));
                customer.setDebt(cursor.getInt(4));
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return customer;
    }

    public int getCustomersCount(){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "COUNT(0) " +
            "FROM " +
                "cliente";

        cursor = sqLiteDatabase.rawQuery(query, null);

        int customersCount = 0;

        if(cursor.moveToFirst()){
            do{
                customersCount = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return customersCount;
    }

    public List<Transaction> getTransactions(int customerId, boolean ascending){
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction;

        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "* " +
            "FROM " +
                "movimiento " +
            "WHERE " +
                "cliente = '"+customerId+"' " +
            "ORDER BY fecha " + (ascending ? "ASC" : "DESC");

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                transaction = new Transaction();

                transaction.setId(cursor.getInt(0));
                try{
                    transaction.setDate(dateFormat.parse(cursor.getString(1)));
                }catch(ParseException ex){}

                transaction.setDetail(cursor.getString(2));
                transaction.setBalance(cursor.getInt(3));
                transaction.setCustomerId(cursor.getInt(4));

                transactions.add(transaction);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return transactions;
    }

    public int getDebt(int customerId){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "deuda " +
            "FROM " +
                "cliente " +
            "WHERE " +
                "id = '"+customerId+"'";

        cursor = sqLiteDatabase.rawQuery(query, null);

        int debt = 0;

        if(cursor.moveToFirst()){
            do{
                debt = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return debt;
    }

    public void pay(Transaction transaction, int amount){
        createTransaction(transaction);
        updateDebt(transaction.getCustomerId(), transaction.getBalance());

        // TODO: esto se repite mucho en otros métodos, ver que hacer
        Statistic statistic = new Statistic();

        statistic.setAmount(amount);
        statistic.setType(K.PAYMENT);
        statistic.setDate(transaction.getDate());
        statistic.setSaleType(-1);
        statistic.setItemsCount(-1);

        createStatistic(statistic);
    }

    public void refund(Transaction transaction, int amount){
        createTransaction(transaction);
        updateDebt(transaction.getCustomerId(), transaction.getBalance());

        Statistic statistic = new Statistic();

        statistic.setAmount(amount);
        statistic.setType(K.REFUND);
        statistic.setDate(transaction.getDate());
        statistic.setSaleType(-1);
        statistic.setItemsCount(-1);

        createStatistic(statistic);
    }

    public void debtCondonation(Transaction transaction, int amount){
        createTransaction(transaction);
        updateDebt(transaction.getCustomerId(), transaction.getBalance());

        Statistic statistic = new Statistic();

        statistic.setAmount(amount);
        statistic.setType(K.DEBT_CONDONATION);
        statistic.setDate(transaction.getDate());
        statistic.setSaleType(-1);
        statistic.setItemsCount(-1);

        createStatistic(statistic);
    }

    /**
     *
     * @param transaction
     * @param amount
     * @param itemCounts
     * @param saleType puede ser mantencion o venta nueva
     */

    // TODO: Pensar en hacer clase Sale.java
    public void createSale(Transaction transaction, int amount, int itemCounts, int saleType){
        Statistic statistic = new Statistic();
        updateDebt(transaction.getCustomerId(), transaction.getBalance());

        statistic.setAmount(amount);
        statistic.setType(K.SALE);
        statistic.setDate(transaction.getDate());
        statistic.setSaleType(saleType);
        statistic.setItemsCount(itemCounts);

        createTransaction(transaction);
        createStatistic(statistic);
    }

    private void updateDebt(int customerId, int newDebt){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "UPDATE " +
                "cliente " +
            "SET " +
                "deuda = '"+newDebt+"' " +
            "WHERE " +
                "id = '"+customerId+"'";

        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }

    public void updateAddress(long customerId, String newAddress){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "UPDATE " +
                "cliente " +
            "SET " +
                "direccion = '"+newAddress+"' " +
            "WHERE " +
                "id = '"+customerId+"'";

        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }

    private void createStatistic(Statistic statistic){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        String date = dateFormat.format(statistic.getDate());

        query =
            "INSERT INTO " +
                "estadistica " +
            "VALUES(" +
                "null, " +
                "'"+statistic.getType() +"'," +
                "'"+statistic.getAmount() +"'," +
                "'"+date+"'," +
                "'"+statistic.getSaleType() +"'," +
                "'"+statistic.getItemsCount() +"'" +
            ")";

        sqLiteDatabase.execSQL(query);
        sqLiteDatabase.close();
    }

    public MonthlyStatistic getMonthlyStatistic(String startDate, String endDate, boolean isDateRange){
        // si isDateRange es verdadero, debo incluir ambos DAYS (>= <=) si no no
        // si isDateRange es falso, es porque quiere ver un mes completo
        // y en ese caso no debo incluir el último día del rango (que es el 1ero del próx. mes)

        MonthlyStatistic monthlyStatistic = new MonthlyStatistic();

        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        // TODO: separar estas consultas (son 6)

        // 1.- select tarjetas terminadas
        query =
            "SELECT " +
                "COUNT(0) " +
            "FROM " +
                "movimiento " +
            "WHERE " +
                "saldo = 0 AND " +
                "fecha >= '"+startDate+"' AND " +
                "fecha <"+(isDateRange?"=":"")+" '"+endDate+"'";

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setFinishedCardsCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }





        // 2.- select tarjetas nuevas
        query =
            "SELECT " +
                "COUNT(0) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.SALE +"' AND " +
                "tipoVenta = '"+K.NEW_SALE +"' AND " +
                "fecha >= '"+startDate+"' AND " +
                "fecha <"+(isDateRange?"=":"")+" '"+endDate+"'";

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setNewCardsCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }





        // 3.- select Mantenciones
        query =
            "SELECT " +
                "COUNT(0) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.SALE +"' AND " +
                "tipoVenta = '"+K.MAINTENANCE +"' AND " +
                "fecha >= '"+startDate+"' AND " +
                "fecha <"+(isDateRange?"=":"")+" '"+endDate+"'";

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setMaintenanceCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }




        // 4.- select total prendas
        query =
            "SELECT " +
                "SUM(cantPrendas) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.SALE +"' AND " +
                "fecha >= '"+startDate+"' AND " +
                "fecha <"+(isDateRange?"=":"")+" '"+endDate+"'";

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setTotalItemsCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }





        // 5.- select cobro (payment)
        query =
            "SELECT " +
                "sum(monto) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.PAYMENT +"' AND " +
                "fecha >= '"+startDate+"' AND " +
                "fecha <"+(isDateRange?"=":"")+" '"+endDate+"'";

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setPaymentsCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }






        // 6.- select ventas
        query =
            "SELECT " +
                "sum(monto) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.SALE +"' AND " +
                "fecha >= '"+startDate+"' AND " +
                "fecha <"+(isDateRange?"=":"")+" '"+endDate+"'";

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setSalesCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return monthlyStatistic;
    }

    // usado para el gráfico
    public MonthlyStatistic getMonthlyStatistic(int month, int year){
        MonthlyStatistic monthlyStatistic = new MonthlyStatistic();

        int endYear = year;
        int endMonth = month + 1;

        if (month == 12) {
            endYear = (year + 1);
            endMonth = 1;
        }

        String dateQuery =
                "fecha >= '"+year+"-"+(month < 10?"0":"")+month+"-01' AND " +
                "fecha < '"+endYear+"-"+(endMonth < 10?"0":"")+endMonth+"-01'";

        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        // 1.- select tarjetas terminadas
        query =
            "SELECT " +
                "COUNT(0) " +
            "FROM " +
                "movimiento " +
            "WHERE " +
                "saldo = 0 AND " + dateQuery;

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setFinishedCardsCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }





        // 2.- select tarjetas nuevas
        query =
            "SELECT " +
                "COUNT(0) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.SALE +"' AND " +
                "tipoVenta = '"+K.NEW_SALE +"' AND " + dateQuery;

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setNewCardsCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }





        // 3.- select Mantenciones
        query =
            "SELECT " +
                "COUNT(0) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.SALE +"' AND " +
                "tipoVenta = '"+K.MAINTENANCE +"' AND " + dateQuery;

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setMaintenanceCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }




        // 4.- select total prendas
        query =
            "SELECT " +
                "sum(cantPrendas) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.SALE +"' AND " + dateQuery;

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setTotalItemsCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }





        // 5.- select cobro
        query =
            "SELECT " +
                "sum(monto) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.PAYMENT +"' AND " + dateQuery;

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setPaymentsCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }






        // 6.- select ventas
        query =
            "SELECT " +
                "sum(monto) " +
            "FROM " +
                "estadistica " +
            "WHERE " +
                "tipo = '"+K.SALE +"' AND " + dateQuery;

        cursor = sqLiteDatabase.rawQuery(query, null);

        if(cursor.moveToFirst()){
            do{
                monthlyStatistic.setSalesCount(cursor.getInt(0));
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return monthlyStatistic;
    }

    public int getTotalDebt(){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "SUM(deuda) " +
            "FROM " +
                "cliente";

        cursor = sqLiteDatabase.rawQuery(query, null);

        int totalDebt = 0;

        if(cursor.moveToFirst()){
            do{
                totalDebt = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return totalDebt;
    }

    public int getAverageDebt(){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "AVG(deuda) " +
            "FROM " +
                "cliente";

        cursor = sqLiteDatabase.rawQuery(query, null);

        int averageDebt = 0;

        if(cursor.moveToFirst()){
            do{
                averageDebt = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return averageDebt;
    }

    public int getCustomersCount(String sector){
        sqLiteOpenHelper = new SQLiteOpenHelperImpl(context, DATABASE_PATH, null, 1);
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

        query =
            "SELECT " +
                "COUNT(0) " +
            "FROM " +
                "cliente " +
            "WHERE " +
                "sector = '"+sector+"'";

        cursor = sqLiteDatabase.rawQuery(query, null);

        int customersCount = 0;

        if(cursor.moveToFirst()){
            do{
                customersCount = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        sqLiteDatabase.close();

        return customersCount;
    }
}
