package cl.casero.model.dao;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import cl.casero.model.SQLiteOpenHelperImpl;
import cl.casero.model.Statistic;
import cl.casero.model.Transaction;
import cl.casero.model.enums.SaleType;
import cl.casero.model.enums.TransactionType;

public class DaoTransaction extends AbstractDao<Transaction> {

    private DaoStatistics daoStatistics;

    public DaoTransaction(){
        daoStatistics = new DaoStatistics();
    }

    @Override
    public void create(Transaction transaction) {
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

    @Override
    public List<Transaction> read() {
        return null;
    }

    @Override
    public void update(Transaction transaction) {

    }

    @Override
    public void delete(Number id) {

    }

    @Override
    public Transaction readById(Number id) {
        return null;
    }

    @Override
    public List<Transaction> readBy(String filter) {
        return null;
    }

    public List<Transaction> readByCustomer(int customerId, boolean ascending){
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

    public void updateDebt(int customerId, int newDebt){
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

    public void pay(Transaction transaction, int amount){
        create(transaction);
        updateDebt(transaction.getCustomerId(), transaction.getBalance());

        // TODO: esto se repite mucho en otros métodos, ver que hacer
        Statistic statistic = new Statistic();

        statistic.setAmount(amount);
        statistic.setType(TransactionType.PAYMENT.getId());
        statistic.setDate(transaction.getDate());
        statistic.setSaleType(-1);
        statistic.setItemsCount(-1);

        daoStatistics.create(statistic);
    }

    public void refund(Transaction transaction, int amount){
        create(transaction);
        updateDebt(transaction.getCustomerId(), transaction.getBalance());

        Statistic statistic = new Statistic();

        statistic.setAmount(amount);
        statistic.setType(TransactionType.REFUND.getId());
        statistic.setDate(transaction.getDate());
        statistic.setSaleType(-1);
        statistic.setItemsCount(-1);

        daoStatistics.create(statistic);
    }

    public void debtCondonation(Transaction transaction, int amount){
        create(transaction);
        updateDebt(transaction.getCustomerId(), transaction.getBalance());

        Statistic statistic = new Statistic();

        statistic.setAmount(amount);
        statistic.setType(TransactionType.DEBT_CONDONATION.getId());
        statistic.setDate(transaction.getDate());
        statistic.setSaleType(-1);
        statistic.setItemsCount(-1);

        daoStatistics.create(statistic);
    }

    // TODO: Pensar en hacer clase Sale.java
    /**
     *
     * @param transaction
     * @param amount
     * @param itemCounts
     * @param saleType puede ser mantencion o venta nueva
     */
    public void createSale(Transaction transaction, int amount, int itemCounts, SaleType saleType){
        Statistic statistic = new Statistic();
        updateDebt(transaction.getCustomerId(), transaction.getBalance());

        statistic.setAmount(amount);
        statistic.setType(TransactionType.SALE.getId());
        statistic.setDate(transaction.getDate());
        statistic.setSaleType(saleType.getId());
        statistic.setItemsCount(itemCounts);

        create(transaction);
        daoStatistics.create(statistic);
    }
}