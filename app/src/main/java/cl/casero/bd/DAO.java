package cl.casero.bd;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cl.casero.bd.model.Cliente;
import cl.casero.bd.model.Estadistica;
import cl.casero.bd.model.EstadisticaMensual;
import cl.casero.bd.model.K;
import cl.casero.bd.model.Movimiento;

/**
 * Created by Patricio Pérez Pinto on 04/01/2016.
 */
public class DAO {
    private Context contexto;
    private BD conexion;
    private SQLiteDatabase db;
    private Cursor cursor;


    private final String RUTA_BD =
            Environment.getExternalStorageDirectory().
                    getPath()+"/caseroBD/casero.sqlite";

    public DAO(Context contexto) {
        this.contexto = contexto;
    }

    public int crearCliente(Cliente c){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String insert = "insert into cliente values(null, '"+c.nombre+"', '"+c.sector+"', '"+c.direccion+"', '"+c.deuda+"')";
        Log.v("INSERT CLIENTE", insert);
        db.execSQL(insert);

        db.close();

        return getIDUltimoCliente();
    }

    private int getIDUltimoCliente(){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select max(id) from cliente";

        cursor = db.rawQuery(select, null);

        int id = -1;
        if(cursor.moveToFirst()){
            do{
                id = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        db.close();

        return id;
    }

    public void crearMovimiento(Movimiento m){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = f.format(m.fecha);
        String insert = "insert into movimiento values(null, '"+fecha+"','"+m.detalle+"','"+m.saldo+"','"+m.cliente+"')";
        db.execSQL(insert);
        Log.v("INSERT MOVIMIENTO", insert);
        db.close();
    }

    public List<Cliente> getClientes(){
        List<Cliente> lista = new ArrayList<>();
        Cliente c = null;

        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select * from cliente";

        cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                c = new Cliente();

                c.id = cursor.getInt(0);
                c.nombre = cursor.getString(1);
                c.sector = cursor.getString(2);
                c.direccion = cursor.getString(3);
                c.deuda = cursor.getInt(4);

                lista.add(c);
            }while(cursor.moveToNext());
        }

        db.close();

        return lista;

    }

    public List<Cliente> getTopMorosos(int limite){
        List<Cliente> lista = new ArrayList<>();
        Cliente c = null;

        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select * from cliente order by deuda desc limit "+limite;

        cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                c = new Cliente();

                c.id = cursor.getInt(0);
                c.nombre = cursor.getString(1);
                c.sector = cursor.getString(2);
                c.direccion = cursor.getString(3);
                c.deuda = cursor.getInt(4);

                lista.add(c);
            }while(cursor.moveToNext());
        }

        db.close();

        return lista;

    }

    public List<Cliente> getTopClientesBuenos(int limite){
        List<Cliente> lista = new ArrayList<>();
        Cliente c = null;

        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select * from cliente order by deuda asc limit "+limite;

        cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                c = new Cliente();

                c.id = cursor.getInt(0);
                c.nombre = cursor.getString(1);
                c.sector = cursor.getString(2);
                c.direccion = cursor.getString(3);
                c.deuda = cursor.getInt(4);

                lista.add(c);
            }while(cursor.moveToNext());
        }

        db.close();

        return lista;

    }

    public List<Cliente> getClientes(String filtro){


        List<Cliente> lista = new ArrayList<>();
        Cliente c = null;

        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        /*String select = "select * from cliente where nombre like '%"+filtro+"%' " +
                "or direccion like '%"+filtro+"%' " +
                "or sector like '%"+filtro+"%' order by nombre asc";*/

        String select = "select * from cliente where REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(nombre),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'ñ','n') like '%"+filtro+"%' " +
                "or REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(direccion),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'ñ','n') like '%"+filtro+"%' " +
                "or REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(LOWER(sector),'á','a'), 'é','e'),'í','i'),'ó','o'),'ú','u'),'ñ','n') like '%"+filtro+"%' order by nombre asc";

        cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                c = new Cliente();

                c.id = cursor.getInt(0);
                c.nombre = cursor.getString(1);
                c.sector = cursor.getString(2);
                c.direccion = cursor.getString(3);
                c.deuda = cursor.getInt(4);

                lista.add(c);
            }while(cursor.moveToNext());
        }

        db.close();

        return lista;

    }

    public Cliente getCliente(long id){
        Cliente c = null;

        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select * from cliente where id = '"+id+"'";

        cursor = db.rawQuery(select, null);

        if(cursor.moveToFirst()){
            do{
                c = new Cliente();

                c.id = cursor.getInt(0);
                c.nombre = cursor.getString(1);
                c.sector = cursor.getString(2);
                c.direccion = cursor.getString(3);
                c.deuda = cursor.getInt(4);

            }while(cursor.moveToNext());
        }

        db.close();

        return c;

    }

    public int getCantidadClientes(){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select count(0) from cliente";

        cursor = db.rawQuery(select, null);

        int cant = 0;

        if(cursor.moveToFirst()){
            do{
                cant = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        db.close();

        return cant;

    }

    public List<Movimiento> getMovimientos(int idCliente, boolean ascendente){
        List<Movimiento> lista = new ArrayList<>();
        Movimiento m = null;

        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select * from movimiento where cliente = '"+idCliente+"' order by fecha "+(ascendente?"asc":"desc");

        cursor = db.rawQuery(select, null);

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");

        if(cursor.moveToFirst()){
            do{
                m = new Movimiento();
            /*"id INTEGER primary key AUTOINCREMENT,"+
                    "fecha TEXT,"+// TEXT as ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS").
                    "detalle TEXT,"+
                    "saldo INTEGER,"+
                    "cliente INTEGER," +*/
                m.id = cursor.getInt(0);
                try{
                    m.fecha = f.parse(cursor.getString(1));
                }catch(ParseException ex){
                    Log.v("PARSE EXCEPTION", ex.getMessage());
                }

                m.detalle = cursor.getString(2);
                m.saldo = cursor.getInt(3);
                m.cliente = cursor.getInt(4);

                lista.add(m);
            }while(cursor.moveToNext());
        }

        db.close();

        return lista;
    }

    public int getDeuda(int idCliente){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        //String select = "select saldo from movimiento where cliente = '"+idCliente+"' order by id desc limit 1";
        String select = "select deuda from cliente where id = '"+idCliente+"'";
        cursor = db.rawQuery(select, null);

        int deuda = 0;

        if(cursor.moveToFirst()){
            do{
                deuda = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        db.close();

        return deuda;
    }

    public void abonar(Movimiento m, int monto){
        crearMovimiento(m);
        actualizarDeuda(m.cliente, m.saldo);

        Estadistica e = new Estadistica();

        e.monto = monto;
        e.tipo = K.ABONO;
        e.fecha = m.fecha;
        e.tipoVenta = -1;
        e.cantPrendas = -1;

        crearEstadistica(e);
    }

    /**
     *
     * @param m
     * @param monto
     * @param cantPrendas
     * @param tipoVenta puede ser mantencion o venta nueva
     */
    public void crearVenta(Movimiento m, int monto, int cantPrendas, int tipoVenta){
        Estadistica e = new Estadistica();
        actualizarDeuda(m.cliente, m.saldo);

        e.monto = monto;
        e.tipo = K.VENTA;
        e.fecha = m.fecha;
        e.cantPrendas = cantPrendas;
        e.tipoVenta = tipoVenta;

        /*
        esto lo hago antes y se lo entrego como parametro, porque aca estaba mal

        int saldo = getDeuda(m.cliente);

        if(saldo == 0){
            e.tipoVenta = K.VENTA_NUEVA;
        }else{
            e.tipoVenta = K.MANTENCION;
        }*/

        crearMovimiento(m);
        crearEstadistica(e);
    }

    private void actualizarDeuda(int idCliente, int deudaNueva){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String update = "update cliente set deuda = '"+deudaNueva+"' where id = '"+idCliente+"'";
        db.execSQL(update);
        db.close();
    }

    public void actualizarDireccion(long idCliente, String direccionNueva){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String update = "update cliente set direccion = '"+direccionNueva+"' where id = '"+idCliente+"'";
        db.execSQL(update);
        db.close();
    }

    private void crearEstadistica(Estadistica e){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String fecha = f.format(e.fecha);
        String insert = "insert into estadistica values(null, '"+e.tipo+"','"+e.monto+"','"+fecha+"', '"+e.tipoVenta+"','"+e.cantPrendas+"')";
        db.execSQL(insert);
        Log.v("INSERT ESTADISTICA", insert);
        db.close();
    }

    public EstadisticaMensual getEstadistica(String fecIni, String fecFin, boolean isRango){
        // si isRango es verdadero, debo incluir ambos dias (>= <=) si no no
        // si isRango es falso, es porque quiere ver un mes completo
        // y en ese caso no debo incluir el ultimo dia del rango (que es el 1ero del prox mes)

        EstadisticaMensual em = new EstadisticaMensual();
        String sel;

        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        // 1.- select tarjetas terminadas
        sel = "select count(0) from movimiento " +
                "where saldo = 0 and " +
                "fecha >= '"+fecIni+"' and " +
                "fecha <"+(isRango?"=":"")+" '"+fecFin+"'";

        cursor = db.rawQuery(sel, null);

        if(cursor.moveToFirst()){
            do{
                em.tarTerminadas = cursor.getInt(0);
            }while(cursor.moveToNext());
        }





        // 2.- select tarjetas nuevas
        sel = "select count(0) from estadistica " +
                "where tipo = '"+K.VENTA+"' and " +
                "tipoVenta = '"+K.VENTA_NUEVA+"' and " +
                "fecha >= '"+fecIni+"' and " +
                "fecha <"+(isRango?"=":"")+" '"+fecFin+"'";

        cursor = db.rawQuery(sel, null);

        if(cursor.moveToFirst()){
            do{
                em.tarNuevas = cursor.getInt(0);
            }while(cursor.moveToNext());
        }





        // 3.- select Mantenciones
        sel = "select count(0) from estadistica " +
                "where tipo = '"+K.VENTA+"' and " +
                "tipoVenta = '"+K.MANTENCION+"' and " +
                "fecha >= '"+fecIni+"' and " +
                "fecha <"+(isRango?"=":"")+" '"+fecFin+"'";

        cursor = db.rawQuery(sel, null);

        if(cursor.moveToFirst()){
            do{
                em.mantenciones = cursor.getInt(0);
            }while(cursor.moveToNext());
        }




        // 4.- select total prendas
        sel = "select sum(cantPrendas) " +
                "from estadistica " +
                "where tipo = '"+K.VENTA+"' and " +
                "fecha >= '"+fecIni+"' and " +
                "fecha <"+(isRango?"=":"")+" '"+fecFin+"'";

        cursor = db.rawQuery(sel, null);

        if(cursor.moveToFirst()){
            do{
                em.totalPrendas = cursor.getInt(0);
            }while(cursor.moveToNext());
        }





        // 5.- select cobro
        sel = "select sum(monto) " +
                "from estadistica " +
                "where tipo = '"+K.ABONO+"' and " +
                "fecha >= '"+fecIni+"' and " +
                "fecha <"+(isRango?"=":"")+" '"+fecFin+"'";

        cursor = db.rawQuery(sel, null);

        if(cursor.moveToFirst()){
            do{
                em.cobro = cursor.getInt(0);
            }while(cursor.moveToNext());
        }






        // 6.- select ventas
        sel = "select sum(monto) " +
                "from estadistica " +
                "where tipo = '"+K.VENTA+"' and " +
                "fecha >= '"+fecIni+"' and " +
                "fecha <"+(isRango?"=":"")+" '"+fecFin+"'";

        cursor = db.rawQuery(sel, null);

        if(cursor.moveToFirst()){
            do{
                em.venta = cursor.getInt(0);
            }while(cursor.moveToNext());
        }



        db.close();

        return em;
    }

    public int getDeudaTotal(){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select sum(deuda) from cliente";

        cursor = db.rawQuery(select, null);

        int suma = 0;

        if(cursor.moveToFirst()){
            do{
                suma = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        db.close();

        return suma;
    }

    public int getPromedioDeuda(){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select avg(deuda) from cliente";

        cursor = db.rawQuery(select, null);

        int prom = 0;

        if(cursor.moveToFirst()){
            do{
                prom = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        db.close();

        return prom;
    }

    public int getCantClientes(String sector){
        conexion = new BD(contexto, RUTA_BD, null, 1);
        db = conexion.getWritableDatabase();

        String select = "select count(0) from cliente where sector = '"+sector+"'";

        cursor = db.rawQuery(select, null);

        int cont = 0;

        if(cursor.moveToFirst()){
            do{
                cont = cursor.getInt(0);
            }while(cursor.moveToNext());
        }

        db.close();

        return cont;
    }
}
