package cl.casero.bd.model;

public class Fecha {
    private int anio;
    private int mes;
    private int dia;

    public enum metrica{
        segundos, minutos, horas, dias, meses
    }

    public Fecha(int anio, int mes, int dia) {
        this.anio = anio;
        this.mes = mes;
        this.dia = dia;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    public String toString(){
        return dia + " de "+mes + " de "+anio;
    }
}
