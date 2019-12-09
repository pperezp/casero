package cl.casero;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cl.casero.adapter.CustomerAdapter;
import cl.casero.bd.DAO;
import cl.casero.bd.model.Customer;
import cl.casero.bd.model.K;
import cl.casero.bd.model.Transaction;
import cl.casero.bd.model.Util;
import cl.casero.model.Resource;
import cl.casero.model.TestMail;

public class MainActivity extends ActionBarActivity {
    private DAO dao;

    private EditText searchNameEditText;
    private ListView customersListView;
    private TextView resultTextView;

    private String refundDetailInput;
    private String condonationDetailInput;

    private int payment;
    private int refundAmount;

    private static MainActivity instance;

    // TODO: Separar
    /*FECHA!*/
    private DatePickerDialog.OnDateSetListener fechaAbonoListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // el mes comienza de 0
            // esto se llama cuando el usuario presiona OK en la date del payment

            DAO dao = new DAO(MainActivity.this);

            Transaction transaction = new Transaction();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                transaction.setDate(dateFormat.parse(year+"-"+(month+1)+"-"+day));
            }catch (ParseException ex){}

            transaction.setCustomerId((int) K.customerId);
            transaction.setDetail("[Abono]: $"+ payment);

            int balance = dao.getDebt(transaction.getCustomerId());

            balance = balance - payment;
            transaction.setBalance(balance);

            dao.pay(transaction, payment);


            /*cargo la lista de customers de nuevo*/
            String searchName = searchNameEditText.getText().toString();
            K.searchName = searchNameEditText.getText().toString();

            List<Customer> customers = dao.getCustomers(searchName);

            // TODO: Ver si funciona solo con customers y no if else
            if(!customers.isEmpty()){
                customersListView.setAdapter(new CustomerAdapter(MainActivity.this, customers));
            }else{
                customersListView.setAdapter(new CustomerAdapter(MainActivity.this, new ArrayList<Customer>()));
            }
            /*cargo la lista de customers de nuevo*/

        }
    };
    /*FECHA!*/


    /*LISTENER FECHA DEVOLUCIÓN*/
    private DatePickerDialog.OnDateSetListener fechaDevolucionListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int year, int month, int day) {
            // el mes comienza de 0
            // esto se llama cuando el usuario presiona OK en la date del payment

            DAO dao = new DAO(MainActivity.this);

            Transaction transaction = new Transaction();

            String datePattern = Resource.getString(R.string.database_date_pattern);
            SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
            try {
                transaction.setDate(dateFormat.parse(year+"-"+(month+1)+"-"+day));
            }catch (ParseException ex){}

            transaction.setCustomerId((int) K.customerId);

            String refundDetail = Resource.getString(R.string.refund_detail);
            refundDetail = refundDetail.replace("{0}", Util.formatPrice(refundAmount));
            refundDetail = refundDetail.replace("{1}", refundDetailInput);

            transaction.setDetail(refundDetail);
            int balance = dao.getDebt(transaction.getCustomerId());

            balance = balance - refundAmount;
            transaction.setBalance(balance);

            dao.refund(transaction, refundAmount);


            /*cargo la lista de customers de nuevo*/
            String searchName = searchNameEditText.getText().toString();
            K.searchName = searchNameEditText.getText().toString();

            List<Customer> customers = dao.getCustomers(searchName);

            // TODO: Ver si funciona solo con customers y no if else
            if(!customers.isEmpty()){
                customersListView.setAdapter(new CustomerAdapter(MainActivity.this, customers));
            }else{
                customersListView.setAdapter(new CustomerAdapter(MainActivity.this, new ArrayList<Customer>()));
            }
            /*cargo la lista de customers de nuevo*/

        }
    };
    /*LISTENER FECHA DEVOLUCIÓN*/


    /*LISTENER FECHA CONDONACIÓN*/
    private DatePickerDialog.OnDateSetListener fechaCondonacion = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int anio, int mes, int dia) {
            // el mes comienza de 0
            //Toast.makeText(MainActivity.this.getApplicationContext(),"["+payment+"]["+anio+" - "+(mes+1)+" - "+dia+"]", Toast.LENGTH_SHORT).show();
            // esto se llama cuando el usuario presiona OK en la date del payment

            DAO dao = new DAO(MainActivity.this);

            Transaction transaction = new Transaction();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                transaction.setDate(dateFormat.parse(anio+"-"+(mes+1)+"-"+dia));
            }catch (ParseException ex){
            }

            transaction.setCustomerId((int)K.customerId);

            String condonationDetail = Resource.getString(R.string.condonation_detail);
            condonationDetail = condonationDetail.replace("{0}", condonationDetailInput);

            transaction.setDetail(condonationDetail);
            int balance = dao.getDebt(transaction.getCustomerId());

            // TODO: Crear un método que deje el saldo en 0
            transaction.setBalance(0);

            dao.debtCondonation(transaction, balance);

            /*cargo la lista de customers de nuevo*/
            String searchName = searchNameEditText.getText().toString();
            K.searchName = searchNameEditText.getText().toString();

            List<Customer> customers = dao.getCustomers(searchName);

            // TODO: Ver si funciona solo con customers y no if else
            if(!customers.isEmpty()){
                customersListView.setAdapter(new CustomerAdapter(MainActivity.this, customers));
            }else{
                customersListView.setAdapter(new CustomerAdapter(MainActivity.this, new ArrayList<Customer>()));
            }
            /*cargo la lista de customers de nuevo*/
        }

        // TODO: Vi muchas veces repetido todo el código que esta arriba, analizar
    };
    /*LISTENER FECHA CONDONACIÓN*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;

        loadComponents();
        loadListeners();
        loadOnClickCustomerList();

        dao = new DAO(this);

        if(K.searchName != null){
            searchNameEditText.setText(K.searchName);
            searchNameEditText.setSelection(K.searchName.length(), K.searchName.length());
        }
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
                        switch (which){
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
                                            payment = Integer.parseInt(paymentString);

                                            // TODO deprecated
                                            /*FECHA!*/
                                            showDialog(999);
                                            /*FECHA!*/
                                        }catch (NumberFormatException ex){
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
                                int currentBalance = dao.getDebt((int)K.customerId);

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
                                        refundDetailInput = detailEditText.getText().toString();

                                        try {
                                            refundAmount = Integer.parseInt(amountString);

                                            // TODO Deprecated
                                            /*FECHA!*/
                                            showDialog(1);
                                            /*FECHA!*/
                                        }catch (NumberFormatException ex){
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
                                currentBalance = dao.getDebt((int)K.customerId);

                                bui = new AlertDialog.Builder(MainActivity.this);

                                String condonationTitle = Resource.getString(R.string.condonation_current_balance);
                                condonationTitle = condonationTitle.replace("{0}", Util.formatPrice(currentBalance));

                                bui.setTitle(condonationTitle);

                                final EditText condonationDetailEditText = new EditText(MainActivity.this);

                                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                                condonationDetailEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                                condonationDetailEditText.setHint(Resource.getString(R.string.condonation_reason));
                                condonationDetailEditText.requestFocus();

                                bui.setView(condonationDetailEditText);


                                bui.setPositiveButton(Resource.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        condonationDetailInput = condonationDetailEditText.getText().toString();
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
                                Customer customer = dao.getCustomer(K.customerId);

                                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);

                                String addressOf = Resource.getString(R.string.address_of);
                                addressOf = addressOf.replace("{0}",customer.getName());

                                b.setTitle(addressOf);
                                b.setMessage(customer.getAddress() +", "+customer.getSector());
                                b.setPositiveButton(Resource.getString(R.string.ok), null);
                                b.create().show();
                                break;

                            case 5:// cambiar dirección
                                // TODO: Ojo con los nombres, no se puede poner customer
                                Customer customer2 = dao.getCustomer(id);

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

                                        dao.updateAddress(K.customerId, newAddress);

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
    /*FECHA!*/

    @Override
    protected Dialog onCreateDialog(int id) {
        // el mes comienza de 0
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if (id == 999) {
            // formulario de date de payment
            return new DatePickerDialog(this, fechaAbonoListener, year, month, day);
        }else if(id == 1){
            // formulario de date de devolución
            return new DatePickerDialog(this, fechaDevolucionListener, year, month, day);
        }else if(id == 2){
            // formulario de date de condonacion de debt
            return new DatePickerDialog(this, fechaCondonacion, year, month, day);
        }
        return null;
    }
    /*FECHA!*/


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

                        if(command.charAt(1) == 'h'){ //comando de ayuda
                            String helpText = "";

                            // TODO: Arreglar esto
                            helpText += "@m numero: Ver el top de morosos según el número\n";
                            helpText += "@c comando: Ejecutar comandos\n";
                            helpText += "Comandos posibles:\n";
                            helpText += "\tpd: Promedio total de deudas\n";
                            helpText += "\tcps: Cantidad de customers por sector\n";

                            Util.message(MainActivity.this, Resource.getString(R.string.help), helpText);
                        }else if (command.charAt(1) == 'm') {// Comando morosos
                            String limitStr = command.split(" ")[1];

                            try {
                                int limit = Integer.parseInt(limitStr);

                                DAO dao = new DAO(MainActivity.this);
                                List<Customer> debtors = dao.getDebtors(limit);

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
                            } catch (NumberFormatException ex) {}
                        }else if(command.charAt(1) == 'c'){ // comando generico
                            String parameter = command.split(" ")[1];

                            DAO dao = new DAO(MainActivity.this);

                            switch (parameter){
                                case "pd":// TODO: Arreglar
                                    // promedio deuda

                                    Util.message(
                                        MainActivity.this,
                                        Resource.getString(R.string.debt_average),
                                        Util.formatPrice(dao.getAverageDebt())
                                    );

                                    break;

                                case "cps":
                                    // Cantidad de customers por sector
                                    String summary = "";

                                    // TODO: Arreglar
                                    summary += "Santa Cruz: "+dao.getCustomersCount("Santa Cruz")+"\n";
                                    summary += "Los Boldos: "+dao.getCustomersCount("Los Boldos")+"\n";
                                    summary += "Barreales: "+dao.getCustomersCount("Barreales")+"\n";
                                    summary += "Palmilla: "+dao.getCustomersCount("Palmilla")+"\n";
                                    summary += "Quinahue: "+dao.getCustomersCount("Quinahue")+"\n";
                                    summary += "Chépica: "+dao.getCustomersCount("Chépica");

                                    Util.message(
                                        MainActivity.this,
                                        Resource.getString(R.string.customers_by_sector),
                                        summary
                                    );

                                    break;
                            }
                        }
                    } else if(searchNameEditText.getText().toString().toLowerCase().contains("#bue")){
                        String searchName = searchNameEditText.getText().toString().toLowerCase();
                        String limitStr = searchName.split(" ")[1];

                        try {
                            int limit = Integer.parseInt(limitStr);

                            DAO dao = new DAO(MainActivity.this);
                            List<Customer> bestCustomers = dao.getBestCustomers(limit);

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
                        } catch (NumberFormatException ex) {}
                    }else if(searchNameEditText.getText().toString().toLowerCase().contains("#mor")){
                        // TODO: esto se repite para arriba, revisar
                        String searchName = searchNameEditText.getText().toString().toLowerCase();
                        String limitStr = searchName.split(" ")[1];

                        try {
                            int limit = Integer.parseInt(limitStr);

                            DAO dao = new DAO(MainActivity.this);
                            List<Customer> debtors = dao.getDebtors(limit);

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
                        } catch (NumberFormatException ex) {}
                    }else{
                        String searchName = searchNameEditText.getText().toString();

                        DAO dao = new DAO(MainActivity.this);
                        List<Customer> customers = dao.getCustomers(searchName);

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
                }catch(StringIndexOutOfBoundsException ex){
                }catch(ArrayIndexOutOfBoundsException ex){ }
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
        if (id == R.id.action_crearCliente) {
            Intent intent = new Intent(MainActivity.this, CreateCustomerActivity.class);
            MainActivity.this.startActivity(intent);
        }else if(id == R.id.action_verEstadisticas){
            //MonthlyStatistic em = dao.getMonthlyStatistic("2016-01-01","2016-02-01");
            //Toast.makeText(MainActivity.this.getApplicationContext(), em.toString(), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, StatisticsActivity.class);
            MainActivity.this.startActivity(intent);
        }else if(id == R.id.action_verDeudaTotal){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle(Resource.getString(R.string.total_debt));

            int totalDebt = dao.getTotalDebt();

            builder.setMessage(Util.formatPrice(totalDebt));

            builder.setPositiveButton(Resource.getString(R.string.ok), null);

            builder.create().show();
        }else if(id == R.id.action_verGrafico){
            Intent intent = new Intent(MainActivity.this, ChartActivity.class);
            MainActivity.this.startActivity(intent);
        }else if(id == R.id.action_enviarCorreo){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                try {
                    TestMail.testSmtp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
            });
        }

        return super.onOptionsItemSelected(item);
    }

    public static MainActivity getInstance(){
        return instance;
    }
}
