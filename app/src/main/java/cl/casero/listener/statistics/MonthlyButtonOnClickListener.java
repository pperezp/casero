package cl.casero.listener.statistics;

import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import cl.casero.R;
import cl.casero.StatisticsActivity;
import cl.casero.model.MonthlyStatistic;
import cl.casero.model.Resource;
import cl.casero.model.util.Util;
import cl.casero.service.StatisticsService;
import cl.casero.service.impl.StatisticsServiceImpl;

public class MonthlyButtonOnClickListener implements View.OnClickListener {

    private Spinner monthSpinner;
    private Spinner yearSpinner;
    private TextView finishedCardsTextView;
    private TextView newCardsTextView;
    private TextView maintenanceTextView;
    private TextView itemCountsTextView;
    private TextView paymentsTextView;
    private TextView salesTextView;
    private TextView titleTextView;

    private StatisticsActivity statisticsActivity;
    private StatisticsService statisticsService;

    public MonthlyButtonOnClickListener(){
        this.statisticsActivity = StatisticsActivity.getActivity();
        this.statisticsService = new StatisticsServiceImpl();
        loadComponents();
    }

    private void loadComponents() {
        monthSpinner = (Spinner) statisticsActivity.findViewById(R.id.monthSpinner);
        yearSpinner = (Spinner) statisticsActivity.findViewById(R.id.yearSpinner);
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
        String monthString = monthSpinner.getSelectedItem().toString();

        int month = monthSpinner.getSelectedItemPosition();
        int year = Integer.parseInt(yearSpinner.getSelectedItem().toString());

        month++;

        String startDate, endDate;

        startDate = year + "-" + (month < 10 ? "0" + month : month) + "-01";

        if (month == 12) {
            endDate = (year + 1) + "-01-01";
        } else {
            endDate = year + "-" + ((month + 1) < 10 ? "0" + (month + 1) : (month + 1)) + "-01";
        }

        String statisticsTitle = Resource.getString(R.string.statistics_by_year_month);

        statisticsTitle = statisticsTitle.replace("{0}", monthString);
        statisticsTitle = statisticsTitle.replace("{1}", String.valueOf(year));

        loadStatistics(startDate, endDate, false, statisticsTitle);
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
