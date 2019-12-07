package cl.casero.bd.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
    public static String formatPrice(int price){
        String priceStr = String.valueOf(price);
        String aux = "";
        String formatPrice = "";

        int count = 0;

        /*Con este ciclo pongo los puntos, pero queda el número al revés*/
        for(int i = (priceStr.length() - 1); i >= 0; i--){
            count++;
            aux += priceStr.charAt(i);

            if(count == 3){
                count = 0;
                aux += ".";
            }
        }
        /*Con este ciclo pongo los puntos, pero queda el número al revés*/

        /*Con este ciclo doy vuelta el número al revés, poniéndolo bien*/
        for(int i=(aux.length()-1); i>=0; i--){
            formatPrice += aux.charAt(i);
        }

        if(formatPrice.charAt(0) == '.'){
            formatPrice = formatPrice.substring(1);
        }

        return formatPrice;
    }

    public static void message(Activity activity, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);
        builder.setMessage(message);

        // TODO: Hardcode
        builder.setPositiveButton("Ok", null);

        builder.create().show();
    }

    public static void loadYears(Context context, Spinner spinner){
        List<String> list = new ArrayList<>();

        for(int i = 2016; i <= 2100; i++){
            list.add(String.valueOf(i));
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(
            context,
            android.R.layout.simple_spinner_item,
            list
        );

        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }


    /**
     * La date inicial debe ser menor a la date final
     */
    public static long getDiff(CustomDate startDate, CustomDate endDate, CustomDate.Metric metric) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        calendar1.set(Calendar.YEAR, startDate.getYear());
        calendar1.set(Calendar.MONTH, startDate.getMonth() - 1);
        calendar1.set(Calendar.DAY_OF_MONTH, startDate.getDay());

        calendar2.set(Calendar.YEAR, endDate.getYear());
        calendar2.set(Calendar.MONTH, endDate.getMonth() - 1);
        calendar2.set(Calendar.DAY_OF_MONTH, endDate.getDay());

        Date date1 = calendar1.getTime();
        Date date2 = calendar2.getTime();

        long startMilis = date1.getTime();
        long endMilis = date2.getTime();

        long diff = -1;

        switch (metric) {
            case SECONDS:
                diff = (endMilis - startMilis) / 1000;
                break;

            case MINUTES:
                diff = (endMilis - startMilis) / 1000 / 60;
                break;

            case HOURS:
                diff = (endMilis - startMilis) / 1000 / 60 / 60;
                break;

            case DAYS:
                diff = (endMilis - startMilis) / 1000 / 60 / 60 / 24;
                break;

            case MONTHS:
                diff = (endMilis - startMilis) / 1000 / 60 / 60 / 24 / 30;
                diff++;//  se le suma uno, porque por ejemplo, si quiero la
                // diferencias entre el 1 de ene de 2016 y 1 de enero de 2015 son 13
                // MONTHS, no 12
                break;
        }

        return diff;
    }
}
