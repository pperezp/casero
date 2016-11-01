package cl.casero.bd;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class BD extends SQLiteOpenHelper{


    private String tCliente =
            "create table cliente("+
                "id INTEGER primary key AUTOINCREMENT,"+
                "nombre TEXT," +
                    "sector TEXT," +
                    "direccion TEXT," +
                    "deuda INTEGER"+
            ")";

    private String tMovimiento =
            "create table movimiento("+
                    "id INTEGER primary key AUTOINCREMENT,"+
                    "fecha TEXT,"+// TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
                    "detalle TEXT,"+
                    "saldo INTEGER,"+
                    "cliente INTEGER," +
                    "FOREIGN KEY(cliente) REFERENCES cliente(id)"+
                    ")";

    private String tEstadistica =
            "create table estadistica("+
                    "id INTEGER primary key AUTOINCREMENT,"+
                    "tipo INTEGER," + // venta o abono (K.VENTA K.ABONO)
                    "monto INTEGER," +
                    "fecha TEXT," +
                    "tipoVenta INTEGER," + // ventaNueva o Mantencion (K.NUEVA, K.MANTENCION)
                                            // y -1 cuando es abono
                    "cantPrendas INTEGER"+// cantidad de prendas al momento de una venta
                                             // -1 cuando es abono
                    ")";

    public BD(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tCliente);
        db.execSQL(tMovimiento);
        db.execSQL(tEstadistica);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/*No implementado*/
    }

}
