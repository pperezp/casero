package cl.casero;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cl.casero.adapter.CustomerAdapter;
import cl.casero.listener.DebtForgivenessDateListener;
import cl.casero.listener.PaymentDateListener;
import cl.casero.listener.ReturnProductDateListener;
import cl.casero.model.Customer;
import cl.casero.model.util.K;
import cl.casero.model.util.Util;
import cl.casero.model.Resource;
import cl.casero.service.CustomerService;
import cl.casero.service.StatisticsService;
import cl.casero.service.impl.CustomerServiceImpl;
import cl.casero.service.impl.StatisticsServiceImpl;

public class MainActivity extends ActionBarActivity {

    private EditText searchNameEditText;
    private ListView customersListView;
    private TextView resultTextView;

    private static MainActivity instance;

    private CustomerService customerService;
    private StatisticsService statisticsService;

    private OnDateSetListener paymentDateListener;
    private OnDateSetListener returnProductDateListener;
    private OnDateSetListener debtForgivenessDateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        customerService = new CustomerServiceImpl();
        statisticsService = new StatisticsServiceImpl();

        loadComponents();
        loadListeners();
        loadOnClickCustomerList();

        if (K.searchName != null) {
            searchNameEditText.setText(K.searchName);
            searchNameEditText.setSelection(K.searchName.length(), K.searchName.length());
        }

        paymentDateListener = new PaymentDateListener();
        returnProductDateListener = new ReturnProductDateListener();
        debtForgivenessDateListener = new DebtForgivenessDateListener();
    }

    private void loadOnClickCustomerList() {
        customersListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                K.customerId = id;

                CharSequence options[] = Resource.getStringArray(R.array.customer_options);

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setTitle(Resource.getString(R.string.choose_an_option));
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            // TODO: Cambiar numeros por constantes enum
                            case 0: // pay
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(Resource.getString(R.string.pay));
                                final EditText paymentEditText = new EditText(MainActivity.this);

                                paymentEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                paymentEditText.setHint(Resource.getString(R.string.pay_amount));
                                paymentEditText.requestFocus();
                                builder.setView(paymentEditText);

                                builder.setPositiveButton(Resource.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String paymentString = paymentEditText.getText().toString();
                                        try {
                                            K.paymentAmount = Integer.parseInt(paymentString);

                                            // TODO deprecated
                                            /*FECHA!*/
                                            showDialog(999);
                                            /*FECHA!*/
                                        } catch (NumberFormatException ex) {
                                            Toast.makeText(
                                                    MainActivity.this.getApplicationContext(),
                                                    Resource.getString(R.string.only_numbers),
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                                });

                                builder.setNegativeButton(Resource.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();

                                break;

                            case 1:// Mantención
                                K.searchName = searchNameEditText.getText().toString();
                                Intent intent = new Intent(MainActivity.this, SaleActivity.class);
                                MainActivity.this.startActivity(intent);

                                break;

                            case 2: // Devolución
                                int currentBalance = customerService.getDebt((int) K.customerId);

                                AlertDialog.Builder bui = new AlertDialog.Builder(MainActivity.this);

                                String title = Resource.getString(R.string.refund_current_balance);
                                title = title.replace("{0}", Util.formatPrice(currentBalance));

                                bui.setTitle(title);

                                final AlertDialog.Builder bui2 = new AlertDialog.Builder(MainActivity.this);
                                bui2.setTitle(title);

                                // Set up the input
                                final EditText detailEditText = new EditText(MainActivity.this);
                                final EditText amountEditText = new EditText(MainActivity.this);

                                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                amountEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                                amountEditText.setHint(Resource.getString(R.string.refund_amount));
                                amountEditText.requestFocus();
                                bui.setView(amountEditText);

                                detailEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                                detailEditText.setHint(Resource.getString(R.string.refund_detail_hint));
                                detailEditText.requestFocus();
                                bui2.setView(detailEditText);

                                bui.setPositiveButton(Resource.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        bui2.show();
                                    }
                                }).setNegativeButton(Resource.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                bui2.setPositiveButton(Resource.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String amountString = amountEditText.getText().toString();
                                        K.refundDetailInput = detailEditText.getText().toString();

                                        try {
                                            K.refundAmount = Integer.parseInt(amountString);

                                            // TODO Deprecated
                                            /*FECHA!*/
                                            showDialog(1);
                                            /*FECHA!*/
                                        } catch (NumberFormatException ex) {
                                            Toast.makeText(
                                                    MainActivity.this.getApplicationContext(),
                                                    Resource.getString(R.string.only_numbers),
                                                    Toast.LENGTH_SHORT
                                            ).show();
                                        }
                                    }
                                }).setNegativeButton(Resource.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                bui.show();

                                break;

                            case 3: // CONDONAR DEUDA
                                currentBalance = customerService.getDebt((int) K.customerId);

                                bui = new AlertDialog.Builder(MainActivity.this);

                                String condonationTitle = Resource.getString(R.string.debt_forgiveness_current_balance);
                                condonationTitle = condonationTitle.replace("{0}", Util.formatPrice(currentBalance));

                                bui.setTitle(condonationTitle);

                                final EditText condonationDetailEditText = new EditText(MainActivity.this);

                                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                condonationDetailEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                                condonationDetailEditText.setHint(Resource.getString(R.string.debt_forgiveness_reason));
                                condonationDetailEditText.requestFocus();

                                bui.setView(condonationDetailEditText);


                                bui.setPositiveButton(Resource.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        K.debtForgivenessDetailInput = condonationDetailEditText.getText().toString();
                                        /*FECHA!*/
                                        // TODO Deprecated
                                        showDialog(2);
                                        /*FECHA!*/
                                    }
                                }).setNegativeButton(Resource.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                bui.show();
                                break;

                            case 4:// ver dirección
                                Customer customer = customerService.readById(K.customerId);

                                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);

                                String addressOf = Resource.getString(R.string.address_of);
                                addressOf = addressOf.replace("{0}", customer.getName());

                                b.setTitle(addressOf);
                                b.setMessage(customer.getAddress() + ", " + customer.getSector());
                                b.setPositiveButton(Resource.getString(R.string.ok), null);
                                b.create().show();
                                break;

                            case 5:// cambiar dirección
                                // TODO: Ojo con los nombres, no se puede poner customer
                                Customer customer2 = customerService.readById(id);

                                builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle(Resource.getString(R.string.address_change));

                                final EditText addressEditText = new EditText(MainActivity.this);

                                addressEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                                addressEditText.setHint(Resource.getString(R.string.new_address));
                                addressEditText.setText(customer2.getAddress());
                                addressEditText.requestFocus();

                                builder.setView(addressEditText);

                                builder.setPositiveButton(Resource.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String newAddress = addressEditText.getText().toString();

                                        customerService.updateAddress(K.customerId, newAddress);

                                        Toast.makeText(
                                                MainActivity.this.getApplicationContext(),
                                                Resource.getString(R.string.address_updated),
                                                Toast.LENGTH_SHORT
                                        ).show();
                                    }
                                }).setNegativeButton(Resource.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });

                                builder.show();

                                break;

                            case 6: // ver detalles
                                // TODO: lo mismo de los nombres
                                Intent i2 = new Intent(MainActivity.this, CustomerViewActivity.class);
                                MainActivity.this.startActivity(i2);

                                break;
                        }
                    }
                });

                builder.show();

                return false;
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // el mes comienza de 0
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (id == 999) {
            // formulario de date de payment
            return new DatePickerDialog(this, paymentDateListener, year, month, day);
        } else if (id == 1) {
            // formulario de date de devolución
            return new DatePickerDialog(this, returnProductDateListener, year, month, day);
        } else if (id == 2) {
            // formulario de date de condonacion de debt
            return new DatePickerDialog(this, debtForgivenessDateListener, year, month, day);
        }
        return null;
    }

    private void loadListeners() {
        searchNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (searchNameEditText.getText().toString().equals("")) {
                        resultTextView.setText(Resource.getString(R.string.no_result));
                        customersListView.setAdapter(
                                new CustomerAdapter(
                                        MainActivity.this,
                                        new ArrayList<Customer>()
                                )
                        );
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

                            Util.message(MainActivity.this, Resource.getString(R.string.help), helpText);
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

                                customersListView.setAdapter(
                                        new CustomerAdapter(
                                                MainActivity.this,
                                                debtors
                                        )
                                );

                                String topDebtors = Resource.getString(R.string.top_debtors);

                                topDebtors = topDebtors.replace("{0}", String.valueOf(limit));

                                Toast.makeText(
                                        MainActivity.this.getApplicationContext(),
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
                                            MainActivity.this,
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
                                            MainActivity.this,
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

                            customersListView.setAdapter(
                                    new CustomerAdapter(
                                            MainActivity.this,
                                            bestCustomers
                                    )
                            );

                            String topBestCustomers = Resource.getString(R.string.top_best_customers);

                            topBestCustomers = topBestCustomers.replace("{0}", String.valueOf(limit));

                            Toast.makeText(
                                    MainActivity.this.getApplicationContext(),
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

                            customersListView.setAdapter(
                                    new CustomerAdapter(
                                            MainActivity.this,
                                            debtors
                                    )
                            );

                            String topDebtors = Resource.getString(R.string.top_debtors);

                            topDebtors = topDebtors.replace("{0}", String.valueOf(limit));

                            Toast.makeText(
                                    MainActivity.this.getApplicationContext(),
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
                            customersListView.setAdapter(
                                    new CustomerAdapter(
                                            MainActivity.this,
                                            customers
                                    )
                            );
                        } else {
                            customersListView.setAdapter(
                                    new CustomerAdapter(
                                            MainActivity.this,
                                            new ArrayList<Customer>()
                                    )
                            );
                        }
                    }
                } catch (StringIndexOutOfBoundsException ex) {
                } catch (ArrayIndexOutOfBoundsException ex) {
                }
            }
        });
    }

    private void loadComponents() {
        searchNameEditText = (EditText) findViewById(R.id.searchNameEditText);
        searchNameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        customersListView = (ListView) findViewById(R.id.customersListView);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.create_customer_action) {
            Intent intent = new Intent(MainActivity.this, CreateCustomerActivity.class);
            MainActivity.this.startActivity(intent);
        } else if (id == R.id.view_statistics_action) {
            //MonthlyStatistic em = dao.getMonthlyStatistic("2016-01-01","2016-02-01");
            //Toast.makeText(MainActivity.this.getApplicationContext(), em.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            MainActivity.this.startActivity(intent);
        } else if (id == R.id.see_total_debt_action) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(Resource.getString(R.string.total_debt));

            int totalDebt = statisticsService.getTotalDebt();

            builder.setMessage(Util.formatPrice(totalDebt));

            builder.setPositiveButton(Resource.getString(R.string.ok), null);

            builder.create().show();
        } else if (id == R.id.see_chart_action) {
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            MainActivity.this.startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public static MainActivity getInstance() {
        return instance;
    }
}
