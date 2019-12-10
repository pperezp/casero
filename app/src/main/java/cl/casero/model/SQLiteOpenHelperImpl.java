package cl.casero.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteOpenHelperImpl extends SQLiteOpenHelper{

    private String customerTable =
        "CREATE TABLE cliente("+
            "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "nombre TEXT," +
            "sector TEXT," +
            "direccion TEXT," +
            "deuda INTEGER"+
        ")";

    private String transactionTable =
        "CREATE TABLE movimiento("+
            "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "fecha TEXT,"+// TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
            "detalle TEXT,"+
            "saldo INTEGER,"+
            "cliente INTEGER," +
            "FOREIGN KEY(cliente) REFERENCES cliente(id)"+
        ")";

    private String statisticsTable =
        "CREATE TABLE estadistica("+
            "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "tipo INTEGER," + // venta o abono (K.SALE K.PAYMENT)
            "monto INTEGER," +
            "fecha TEXT," +
            "tipoVenta INTEGER," +  // ventaNueva o Mantencion (K.NUEVA, K.MAINTENANCE)
                                    // y -1 cuando es abono
            "cantPrendas INTEGER"+// cantidad de prendas al momento de una venta
                                  // -1 cuando es abono
        ")";

    public SQLiteOpenHelperImpl(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(customerTable);
        sqLiteDatabase.execSQL(transactionTable);
        sqLiteDatabase.execSQL(statisticsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*No implementado*/
    }

}
