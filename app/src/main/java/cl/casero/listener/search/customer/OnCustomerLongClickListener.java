package cl.casero.listener.search.customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import cl.casero.CustomerViewActivity;
import cl.casero.MainActivity;
import cl.casero.R;
import cl.casero.SaleActivity;
import cl.casero.model.Customer;
import cl.casero.model.Resource;
import cl.casero.model.util.K;
import cl.casero.model.util.Util;
import cl.casero.service.CustomerService;
import cl.casero.service.impl.CustomerServiceImpl;

public class OnCustomerLongClickListener implements AdapterView.OnItemLongClickListener {

    private EditText searchNameEditText;
    private CustomerService customerService;

    public OnCustomerLongClickListener() {
        MainActivity mainActivity = MainActivity.getInstance();

        customerService = new CustomerServiceImpl();
        searchNameEditText = (EditText) mainActivity.findViewById(R.id.searchNameEditText);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        K.customerId = id;
        MainActivity mainActivity = MainActivity.getInstance();
        CharSequence options[] = Resource.getStringArray(R.array.customer_options);

        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);

        builder.setTitle(Resource.getString(R.string.choose_an_option));
        builder.setItems(options, (dialog, which) -> {
            switch (which) {
                // TODO: Cambiar numeros por constantes enum
                case 0: // pay
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(mainActivity);
                    builder1.setTitle(Resource.getString(R.string.pay));
                    final EditText paymentEditText = new EditText(mainActivity);

                    paymentEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                    paymentEditText.setHint(Resource.getString(R.string.pay_amount));
                    paymentEditText.requestFocus();
                    builder1.setView(paymentEditText);

                    builder1.setPositiveButton(Resource.getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String paymentString = paymentEditText.getText().toString();
                            try {
                                K.paymentAmount = Integer.parseInt(paymentString);

                                // TODO deprecated
                                /*FECHA!*/
                                mainActivity.showDialog(999);
                                /*FECHA!*/
                            } catch (NumberFormatException ex) {
                                Toast.makeText(
                                        mainActivity.getApplicationContext(),
                                        Resource.getString(R.string.only_numbers),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    });

                    builder1.setNegativeButton(Resource.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder1.show();

                    break;
                case 1:// Mantención
                    K.searchName = searchNameEditText.getText().toString();
                    Intent intent = new Intent(mainActivity, SaleActivity.class);
                    mainActivity.startActivity(intent);

                    break;
                case 2: // Devolución
                    int currentBalance = customerService.getDebt((int) K.customerId);

                    AlertDialog.Builder bui = new AlertDialog.Builder(mainActivity);

                    String title = Resource.getString(R.string.refund_current_balance);
                    title = title.replace("{0}", Util.formatPrice(currentBalance));

                    bui.setTitle(title);

                    final AlertDialog.Builder bui2 = new AlertDialog.Builder(mainActivity);
                    bui2.setTitle(title);

                    // Set up the input
                    final EditText detailEditText = new EditText(mainActivity);
                    final EditText amountEditText = new EditText(mainActivity);

                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    amountEditText.setRawInputType(InputType.TYPE_CLASS_NUMBER);
                    amountEditText.setHint(Resource.getString(R.string.refund_amount));
                    amountEditText.requestFocus();
                    bui.setView(amountEditText);

                    detailEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    detailEditText.setHint(Resource.getString(R.string.refund_detail_hint));
                    detailEditText.requestFocus();
                    bui2.setView(detailEditText);

                    bui.setPositiveButton(
                            Resource.getString(R.string.ok),
                            (dialog1, which1) -> bui2.show()
                    );

                    bui.setNegativeButton(
                            Resource.getString(R.string.cancel),
                            (dialog12, which12) -> dialog12.cancel()
                    );

                    bui2.setPositiveButton(Resource.getString(R.string.ok), (dialog13, which13) -> {
                        String amountString = amountEditText.getText().toString();
                        K.refundDetailInput = detailEditText.getText().toString();

                        try {
                            K.refundAmount = Integer.parseInt(amountString);

                            // TODO Deprecated
                            /*FECHA!*/
                            mainActivity.showDialog(1);
                            /*FECHA!*/
                        } catch (NumberFormatException ex) {
                            Toast.makeText(
                                    mainActivity.getApplicationContext(),
                                    Resource.getString(R.string.only_numbers),
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    });

                    bui2.setNegativeButton(
                            Resource.getString(R.string.cancel),
                            (dialog14, which14) -> dialog14.cancel()
                    );

                    bui.show();
                    break;
                case 3: // CONDONAR DEUDA
                    currentBalance = customerService.getDebt((int) K.customerId);

                    bui = new AlertDialog.Builder(mainActivity);

                    String condonationTitle = Resource.getString(R.string.debt_forgiveness_current_balance);
                    condonationTitle = condonationTitle.replace("{0}", Util.formatPrice(currentBalance));

                    bui.setTitle(condonationTitle);

                    final EditText condonationDetailEditText = new EditText(mainActivity);

                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    condonationDetailEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    condonationDetailEditText.setHint(Resource.getString(R.string.debt_forgiveness_reason));
                    condonationDetailEditText.requestFocus();

                    bui.setView(condonationDetailEditText);


                    bui.setPositiveButton(Resource.getString(R.string.ok), (dialog15, which15) -> {
                        K.debtForgivenessDetailInput = condonationDetailEditText.getText().toString();
                        /*FECHA!*/
                        // TODO Deprecated
                        mainActivity.showDialog(2);
                        /*FECHA!*/
                    });

                    bui.setNegativeButton(
                            Resource.getString(R.string.cancel),
                            (dialog16, which16) -> dialog16.cancel()
                    );

                    bui.show();
                    break;

                case 4:// ver dirección
                    Customer customer = customerService.readById(K.customerId);

                    AlertDialog.Builder b = new AlertDialog.Builder(mainActivity);

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

                    builder1 = new AlertDialog.Builder(mainActivity);
                    builder1.setTitle(Resource.getString(R.string.address_change));

                    final EditText addressEditText = new EditText(mainActivity);

                    addressEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);
                    addressEditText.setHint(Resource.getString(R.string.new_address));
                    addressEditText.setText(customer2.getAddress());
                    addressEditText.requestFocus();

                    builder1.setView(addressEditText);

                    builder1.setPositiveButton(
                            Resource.getString(R.string.ok),
                            (dialog17, which17) -> {
                                String newAddress = addressEditText.getText().toString();

                                customerService.updateAddress(K.customerId, newAddress);

                                Toast.makeText(
                                        mainActivity.getApplicationContext(),
                                        Resource.getString(R.string.address_updated),
                                        Toast.LENGTH_SHORT
                                ).show();
                            });

                    builder1.setNegativeButton(
                            Resource.getString(R.string.cancel),
                            (dialog18, which18) -> dialog18.cancel()
                    );

                    builder1.show();

                    break;

                case 6: // Eliminar cliente
                    Customer customerToDelete = customerService.readById(id);
                    String deleteCustomerConfig = Resource.getString(R.string.delete_customer_confirm);
                    deleteCustomerConfig = deleteCustomerConfig.replace("{0}", customerToDelete.getName());

                    new AlertDialog
                            .Builder(MainActivity.getInstance())
                            .setMessage(deleteCustomerConfig)
                            .setNegativeButton(Resource.getString(R.string.no), null)
                            .setPositiveButton(Resource.getString(R.string.yes), (dialogOption, whichOption) -> {
                                customerService.delete(id);
                                mainActivity.reloadCustomerList();
                                Util.message("Cliente eliminado");
                            })
                            .show();
                    break;

                case 7: // Transacciones
                    // TODO: lo mismo de los nombres
                    Intent i2 = new Intent(mainActivity, CustomerViewActivity.class);
                    mainActivity.startActivity(i2);

                    break;
            }
        });

        builder.show();
        return true;
    }
}
