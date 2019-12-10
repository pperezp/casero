package cl.casero.model.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cl.casero.R;
import cl.casero.model.CustomDate;
import cl.casero.model.Resource;

/**
 * Created by Patricio Pérez Pinto on 09/01/2016.
 */
public class Util {
    public static String formatPrice(int price){
        String languageTag = Resource.getString(R.string.language_tag);
        String languagePriceSymbol = Resource.getString(R.string.language_price_symbol);
        Locale locale = Locale.forLanguageTag(languageTag);

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);
        String formatPrice = numberFormat.format(price);
        formatPrice = formatPrice.replace(languagePriceSymbol, "");

        return formatPrice;
    }

    public static void message(Activity activity, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);
        builder.setMessage(message);

        builder.setPositiveButton(Resource.getString(R.string.ok), null);

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
