package cl.casero.listener.statistics;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cl.casero.R;
import cl.casero.StatisticsActivity;
import cl.casero.model.MonthlyStatistic;
import cl.casero.model.Resource;
import cl.casero.model.util.K;
import cl.casero.model.util.Util;
import cl.casero.service.StatisticsService;
import cl.casero.service.impl.StatisticsServiceImpl;

public class RangeButtonOnClickListener implements View.OnClickListener {

    private TextView finishedCardsTextView;
    private TextView newCardsTextView;
    private TextView maintenanceTextView;
    private TextView itemCountsTextView;
    private TextView paymentsTextView;
    private TextView salesTextView;
    private TextView titleTextView;

    private StatisticsActivity statisticsActivity;
    private StatisticsService statisticsService;

    public RangeButtonOnClickListener(){
        this.statisticsActivity = StatisticsActivity.getActivity();
        this.statisticsService = new StatisticsServiceImpl();
        loadComponents();
    }

    private void loadComponents() {
        finishedCardsTextView = (TextView) statisticsActivity.findViewById(R.id.finishedCardsTextView);
        newCardsTextView = (TextView) statisticsActivity.findViewById(R.id.newCardsTextView);
        maintenanceTextView = (TextView) statisticsActivity.findViewById(R.id.maintenanceTextView);
        itemCountsTextView = (TextView) statisticsActivity.findViewById(R.id.itemCountsTextView);
        paymentsTextView = (TextView) statisticsActivity.findViewById(R.id.paymentsTextView);
        salesTextView = (TextView) statisticsActivity.findViewById(R.id.salesTextView);
        titleTextView = (TextView) statisticsActivity.findViewById(R.id.titleTextView);
    }

    @Override
    public void onClick(View view) {
        if(K.startDate != null && K.endDate != null){
            loadStatistics(
                K.startDate,
                K.endDate,
                true,
                Resource.getString(R.string.statistics_between_dates)
            );
        }else{
            Toast.makeText(
                statisticsActivity,
                Resource.getString(R.string.enter_both_dates),
                Toast.LENGTH_LONG
            ).show();
        }
    }

    // TODO: Sacar este método como interfaz en StatisticsActivity
    private void loadStatistics(String startDate, String endDate, boolean isDateRange, String title) {
        MonthlyStatistic monthlyStatistic = statisticsService.getMonthlyStatistic(
            startDate, 
            endDate,
            isDateRange
        );

        titleTextView.setText(title);
        finishedCardsTextView.setText(String.valueOf(monthlyStatistic.getFinishedCardsCount()));
        newCardsTextView.setText(String.valueOf(monthlyStatistic.getNewCardsCount()));
        maintenanceTextView.setText(String.valueOf(monthlyStatistic.getMaintenanceCount()));
        itemCountsTextView.setText(String.valueOf(monthlyStatistic.getTotalItemsCount()));
        paymentsTextView.setText(Util.formatPrice(monthlyStatistic.getPaymentsCount()));
        salesTextView.setText(Util.formatPrice(monthlyStatistic.getSalesCount()));
    }
}
