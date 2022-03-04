package cl.casero.listener.search.customer;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cl.casero.MainActivity;
import cl.casero.R;
import cl.casero.adapter.CustomerAdapter;
import cl.casero.model.Customer;
import cl.casero.model.Resource;
import cl.casero.model.util.Util;
import cl.casero.service.CustomerService;
import cl.casero.service.StatisticsService;
import cl.casero.service.impl.CustomerServiceImpl;
import cl.casero.service.impl.StatisticsServiceImpl;

public class SearchTextChangedListener implements TextWatcher {

    private final EditText searchNameEditText;
    private final TextView resultTextView;
    private final ListView customersListView;
    private final StatisticsService statisticsService;
    private final CustomerService customerService;

    public SearchTextChangedListener() {
        MainActivity mainActivity = MainActivity.getInstance();

        searchNameEditText = (EditText) mainActivity.findViewById(R.id.searchNameEditText);
        resultTextView = (TextView) mainActivity.findViewById(R.id.resultTextView);
        customersListView = (ListView) mainActivity.findViewById(R.id.customersListView);

        statisticsService = new StatisticsServiceImpl();
        customerService = new CustomerServiceImpl();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        MainActivity mainActivity = MainActivity.getInstance();
        try {
            if (searchNameEditText.getText().toString().equals("")) {
                resultTextView.setText(Resource.getString(R.string.no_result));
                customersListView.setAdapter(new CustomerAdapter(mainActivity, new ArrayList<>()));
            } else if (searchNameEditText.getText().toString().charAt(0) == '@') {
                // Quiere ingresar un comando
                String command = searchNameEditText.getText().toString();

                if (command.charAt(1) == 'h') { //comando de ayuda
                    String helpText = "";

                    // TODO: Arreglar esto
                    helpText += "@m numero: Ver el top de morosos según el número\n";
                    helpText += "@c comando: Ejecutar comandos\n";
                    helpText += "Comandos posibles:\n";
                    helpText += "\tpd: Promedio total de deudas\n";
                    helpText += "\tcps: Cantidad de customers por sector\n";

                    Util.message(mainActivity, Resource.getString(R.string.help), helpText);
                } else if (command.charAt(1) == 'm') {// Comando morosos
                    String limitStr = command.split(" ")[1];

                    try {
                        int limit = Integer.parseInt(limitStr);

                        List<Customer> debtors = statisticsService.getDebtors(limit);

                        int debtorsSize = debtors.size();

                        resultTextView.setText(
                                (debtorsSize == 1 ?
                                        Resource.getString(R.string.result).replace("{0}", String.valueOf(debtorsSize)) :
                                        Resource.getString(R.string.results).replace("{0}", String.valueOf(debtorsSize)))
                        );

                        customersListView.setAdapter(new CustomerAdapter(mainActivity, debtors));

                        String topDebtors = Resource.getString(R.string.top_debtors);

                        topDebtors = topDebtors.replace("{0}", String.valueOf(limit));

                        Toast.makeText(
                                mainActivity.getApplicationContext(),
                                topDebtors,
                                Toast.LENGTH_SHORT
                        ).show();
                    } catch (NumberFormatException ex) {
                    }
                } else if (command.charAt(1) == 'c') { // comando generico
                    String parameter = command.split(" ")[1];

                    switch (parameter) {
                        case "pd":// TODO: Arreglar
                            // promedio deuda

                            Util.message(
                                    mainActivity,
                                    Resource.getString(R.string.debt_average),
                                    Util.formatPrice(statisticsService.getAverageDebt())
                            );

                            break;

                        case "cps":
                            // Cantidad de customers por sector
                            String summary = "";

                            // TODO: Arreglar
                            summary += "Santa Cruz: " + statisticsService.getCustomersCount("Santa Cruz") + "\n";
                            summary += "Los Boldos: " + statisticsService.getCustomersCount("Los Boldos") + "\n";
                            summary += "Barreales: " + statisticsService.getCustomersCount("Barreales") + "\n";
                            summary += "Palmilla: " + statisticsService.getCustomersCount("Palmilla") + "\n";
                            summary += "Quinahue: " + statisticsService.getCustomersCount("Quinahue") + "\n";
                            summary += "Chépica: " + statisticsService.getCustomersCount("Chépica");

                            Util.message(
                                    mainActivity,
                                    Resource.getString(R.string.customers_by_sector),
                                    summary
                            );

                            break;
                    }
                }
            } else if (searchNameEditText.getText().toString().toLowerCase().contains("#bue")) {
                String searchName = searchNameEditText.getText().toString().toLowerCase();
                String limitStr = searchName.split(" ")[1];

                try {
                    int limit = Integer.parseInt(limitStr);

                    List<Customer> bestCustomers = statisticsService.getBestCustomers(limit);

                    int bestCustomersSize = bestCustomers.size();

                    resultTextView.setText(
                            (bestCustomersSize == 1 ?
                                    Resource.getString(R.string.result).replace("{0}", String.valueOf(bestCustomersSize)) :
                                    Resource.getString(R.string.results).replace("{0}", String.valueOf(bestCustomersSize)))
                    );

                    customersListView.setAdapter(new CustomerAdapter(mainActivity, bestCustomers));

                    String topBestCustomers = Resource.getString(R.string.top_best_customers);

                    topBestCustomers = topBestCustomers.replace("{0}", String.valueOf(limit));

                    Toast.makeText(
                            mainActivity.getApplicationContext(),
                            topBestCustomers,
                            Toast.LENGTH_SHORT
                    ).show();
                } catch (NumberFormatException ex) {
                }
            } else if (searchNameEditText.getText().toString().toLowerCase().contains("#mor")) {
                // TODO: esto se repite para arriba, revisar
                String searchName = searchNameEditText.getText().toString().toLowerCase();
                String limitStr = searchName.split(" ")[1];

                try {
                    int limit = Integer.parseInt(limitStr);

                    List<Customer> debtors = statisticsService.getDebtors(limit);

                    int debtorsSize = debtors.size();

                    resultTextView.setText(
                            (debtorsSize == 1 ?
                                    Resource.getString(R.string.result).replace("{0}", String.valueOf(debtorsSize)) :
                                    Resource.getString(R.string.results).replace("{0}", String.valueOf(debtorsSize)))
                    );

                    customersListView.setAdapter(new CustomerAdapter(mainActivity, debtors));

                    String topDebtors = Resource.getString(R.string.top_debtors);

                    topDebtors = topDebtors.replace("{0}", String.valueOf(limit));

                    Toast.makeText(
                            mainActivity.getApplicationContext(),
                            topDebtors,
                            Toast.LENGTH_SHORT
                    ).show();
                } catch (NumberFormatException ex) {
                }
            } else {
                String searchName = searchNameEditText.getText().toString();

                List<Customer> customers = customerService.readBy(searchName);

                resultTextView.setText(
                        customers.size() + (
                                customers.size() == 1 ?
                                        " resultado" :
                                        " resultados"
                        )
                );

                if (!customers.isEmpty()) {
                    customersListView.setAdapter(new CustomerAdapter(mainActivity, customers));
                } else {
                    customersListView.setAdapter(new CustomerAdapter(mainActivity, new ArrayList<>()));
                }
            }
        } catch (StringIndexOutOfBoundsException ex) {
        } catch (ArrayIndexOutOfBoundsException ex) {
        }
    }
}
