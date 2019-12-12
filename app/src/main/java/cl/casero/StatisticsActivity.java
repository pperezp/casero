package cl.casero;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cl.casero.listener.statistics.MonthlyButtonOnClickListener;
import cl.casero.listener.statistics.RangeButtonOnClickListener;
import cl.casero.model.Resource;
import cl.casero.model.util.K;
import cl.casero.model.util.Util;
import cl.casero.service.StatisticsService;
import cl.casero.service.impl.StatisticsServiceImpl;

public class StatisticsActivity extends ActionBarActivity {
    private Spinner yearSpinner;

    private Button startDateButton;
    private Button statisticsRangeButton;
    private Button endDateButton;
    private Button monthlyStatisticsButton;
    private TextView startDateTextView;
    private TextView endDateTextView;


    private StatisticsService statisticsService;

    private static StatisticsActivity statisticsActivity;

    // TODO: Separar si o si esto
    // TODO: Arreglar algoritmo
    /*FECHA!*/
    private DatePickerDialog.OnDateSetListener startDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // el mes comienza de 0

            SimpleDateFormat f = new SimpleDateFormat(Resource.getString(R.string.database_date_pattern));
            SimpleDateFormat f2 = new SimpleDateFormat(Resource.getString(R.string.statistics_date_pattern));
            try {
                Date fIni = f.parse(year+"-"+(month+1)+"-"+day);
                K.startDate = year+"-"+((month+1)<10?"0"+(month+1):(month+1))+"-"+(day<10?"0"+day:day);
                startDateTextView.setText(f2.format(fIni));
            }catch (ParseException ex){}
        }
    };

    // TODO: Arreglar algoritmo
    private DatePickerDialog.OnDateSetListener endDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // el mes comienza de 0

            SimpleDateFormat f = new SimpleDateFormat(Resource.getString(R.string.database_date_pattern));
            SimpleDateFormat f2 = new SimpleDateFormat(Resource.getString(R.string.statistics_date_pattern));
            try {
                Date fFin = f.parse(year+"-"+(month+1)+"-"+day);
                K.endDate = year+"-"+((month+1)<10?"0"+(month+1):(month+1))+"-"+(day<10?"0"+day:day);
                endDateTextView.setText(f2.format(fFin));
            }catch (ParseException ex){}
        }
    };
    /*FECHA!*/

    /*FECHA!*/
    // TODO: Investigar el deprecated
    @Override
    protected Dialog onCreateDialog(int id) {
        // el mes comienza de 0
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (id == 1) {
            return new DatePickerDialog(this, startDateListener, year, month, day);
        }else if(id == 2){
            return new DatePickerDialog(this, endDateListener, year, month, day);
        }

        return null;
    }
    /*FECHA!*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        statisticsActivity = this;

        statisticsService = new StatisticsServiceImpl();

        loadComponents();
        loadListeners();

        K.startDate = null;
        K.endDate = null;
    }

    private void loadListeners() {
        Util.loadYears(StatisticsActivity.this, yearSpinner);

        // TODO: Arreglar este listener
        startDateButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(1);
                }
            }
        );

        // TODO: Arreglar este listener
        endDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(2);
            }
        });

        monthlyStatisticsButton.setOnClickListener(new MonthlyButtonOnClickListener());
        statisticsRangeButton.setOnClickListener(new RangeButtonOnClickListener());
    }



    private void loadComponents() {
        yearSpinner = (Spinner) findViewById(R.id.yearSpinner);
        startDateButton = (Button) findViewById(R.id.startDateButton);
        statisticsRangeButton = (Button) findViewById(R.id.statisticsRangeButton);
        endDateButton = (Button) findViewById(R.id.endDateButton);
        monthlyStatisticsButton = (Button) findViewById(R.id.monthlyStatisticsButton);
        startDateTextView = (TextView) findViewById(R.id.startDateTextView);
        endDateTextView = (TextView) findViewById(R.id.endDateTextView);

    }

    public static StatisticsActivity getActivity() {
        return statisticsActivity;
    }
}
