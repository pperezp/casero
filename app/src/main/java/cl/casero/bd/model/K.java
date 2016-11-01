package cl.casero.bd.model;

import java.util.Date;

/**
 * Created by Patricio Pérez Pinto on 04/01/2016.
 */
public class K {
    public static long id;
    public static Date fecha;
    public static String fecIni;
    public static String fecFin;
    public final static int VENTA = 0;
    public final static int ABONO = 1;
    public final static int DEVOLUCION = 2;
    public final static int CONDONACION_DEUDA = 3;
    public final static int VENTA_NUEVA = 0;
    public final static int MANTENCION = 1;
    public static String nombreBusqueda = null;
}
