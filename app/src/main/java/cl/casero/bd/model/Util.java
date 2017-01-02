package cl.casero.bd.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Patricio Pérez Pinto on 09/01/2016.
 */
public class Util {
    public static String getNumeroConPuntos(int numero){
        String strNumero = String.valueOf(numero);
        String strAux = "";
        String strAux2 = "";

        int c = 0;
        /*Con este ciclo pongo los puntos, pero queda el numero al reves*/
        for(int i=(strNumero.length()-1); i>=0; i--){
            c++;
            strAux += strNumero.charAt(i);
            if(c == 3){
                c = 0;
                strAux += ".";
            }
        }
        /*Con este ciclo pongo los puntos, pero queda el numero al reves*/

        /*Con este ciclo doy vuelta el numero al reves, poniendolo bien*/
        for(int i=(strAux.length()-1); i>=0; i--){
            strAux2 += strAux.charAt(i);
        }

        if(strAux2.charAt(0) == '.'){
            strAux2 = strAux2.substring(1);
        }

        return strAux2;
    }

    public static void mensaje(Activity a, String titulo, String mensaje){
        AlertDialog.Builder b = new AlertDialog.Builder(a);
        b.setTitle(titulo);
        b.setMessage(mensaje);

        b.setPositiveButton("Ok", null);

        b.create().show();
    }

    public static void cargarAnios(Context context, Spinner spinner){
        List<String> list = new ArrayList<>();
        for(int i=2016; i<=2100; i++){
            list.add(String.valueOf(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


    /**
     * La fecha inicial debe ser menor a la fecha final
     *
     * @param inicial
     * @param fin
     * @param met
     * @return
     */
    public static long getDiff(Fecha inicial, Fecha fin, Fecha.metrica met) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        c1.set(Calendar.YEAR, inicial.getAnio());
        c1.set(Calendar.MONTH, inicial.getMes() - 1);
        c1.set(Calendar.DAY_OF_MONTH, inicial.getDia());

        c2.set(Calendar.YEAR, fin.getAnio());
        c2.set(Calendar.MONTH, fin.getMes() - 1);
        c2.set(Calendar.DAY_OF_MONTH, fin.getDia());

        Date d1 = c1.getTime();
        Date d2 = c2.getTime();

        long milisInicial = d1.getTime();
        long milisFinal = d2.getTime();

        long diff = -1;

        switch (met) {
            case segundos:
                diff = (milisFinal - milisInicial) / 1000;
                break;

            case minutos:
                diff = (milisFinal - milisInicial) / 1000 / 60;
                break;

            case horas:
                diff = (milisFinal - milisInicial) / 1000 / 60 / 60;
                break;

            case dias:
                diff = (milisFinal - milisInicial) / 1000 / 60 / 60 / 24;
                break;

            case meses:
                diff = (milisFinal - milisInicial) / 1000 / 60 / 60 / 24 / 30;
                diff++;//  se le suma uno, porque por ejemplo, si quiero la
                // diferencias entre el 1 de ene de 2016 y 1 de enero de 2015 son 13
                // meses, no 12
                break;
        }

        return diff;
    }
}
