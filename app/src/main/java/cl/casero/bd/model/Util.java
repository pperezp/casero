package cl.casero.bd.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

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
}
