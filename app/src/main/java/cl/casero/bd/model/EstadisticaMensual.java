package cl.casero.bd.model;

/**
 * Created by Patricio Pérez Pinto on 06/01/2016.
 */
public class EstadisticaMensual {
    public int tarTerminadas;
    public int tarNuevas;
    public int mantenciones;
    public int totalPrendas;
    public int cobro;
    public int venta;

    @Override
    public String toString() {
        return "EstadisticaMensual{" +
                "tarTerminadas=" + tarTerminadas +
                ", tarNuevas=" + tarNuevas +
                ", mantenciones=" + mantenciones +
                ", totalPrendas=" + totalPrendas +
                ", cobro=" + cobro +
                ", venta=" + venta +
                '}';
    }
}
