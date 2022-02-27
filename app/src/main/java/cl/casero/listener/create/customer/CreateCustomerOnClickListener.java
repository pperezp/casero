package cl.casero.listener.create.customer;

import android.content.Intent;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import cl.casero.CreateCustomerActivity;
import cl.casero.MainActivity;
import cl.casero.R;
import cl.casero.model.Customer;
import cl.casero.model.Resource;
import cl.casero.model.exceptions.EmptyTextException;
import cl.casero.model.util.K;
import cl.casero.model.util.Util;
import cl.casero.service.CustomerService;
import cl.casero.service.impl.CustomerServiceImpl;

public class CreateCustomerOnClickListener implements View.OnClickListener {

    private EditText nameEditText;
    private EditText addressEditText;
    private Spinner sectorSpinner;

    private final CustomerService customerService;
    private final CreateCustomerActivity createCustomerActivity;

    public CreateCustomerOnClickListener(CreateCustomerActivity createCustomerActivity) {
        this.createCustomerActivity = createCustomerActivity;
        this.customerService = new CustomerServiceImpl();

        loadComponents();
    }

    private void loadComponents() {
        nameEditText = (EditText) createCustomerActivity.findViewById(R.id.nameTextView);
        addressEditText = (EditText) createCustomerActivity.findViewById(R.id.addressEditText);
        sectorSpinner = (Spinner) createCustomerActivity.findViewById(R.id.sectorSpinner);

        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        addressEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    @Override
    public void onClick(View view) {
        try {
            validations();

            Customer customer = create();

            cleanForm();
            showMessage(customer);
            startActivity(customer);
        } catch (EmptyTextException emptyTextException) {
            Util.message(createCustomerActivity, emptyTextException.getMessage());
        } catch (NumberFormatException numberFormatException) {
            String errorMessage = Resource.getString(R.string.only_numbers_in_balance);
            Util.message(createCustomerActivity, errorMessage);
        }
    }

    private Customer create() {
        Customer customer = buildCustomer();
        return customerService.create(customer);
    }

    private void startActivity(Customer customer) {
        K.searchName = customer.getName();
        Intent intent = new Intent(createCustomerActivity, MainActivity.class);
        createCustomerActivity.startActivity(intent);
    }

    private void showMessage(Customer customer) {
        String createdCustomerMessage = Resource.getString(R.string.created_customer);
        createdCustomerMessage = createdCustomerMessage.replace("{0}", customer.getName());
        Util.message(createCustomerActivity, createdCustomerMessage);
    }

    private void cleanForm() {
        nameEditText.setText("");
        addressEditText.setText("");
        sectorSpinner.setSelection(0);
    }

    private Customer buildCustomer() {
        Customer customer = new Customer();

        customer.setName(nameEditText.getText().toString());
        customer.setAddress(addressEditText.getText().toString());
        customer.setSector(sectorSpinner.getSelectedItem().toString());
        customer.setDebt(0);

        return customer;
    }

    private void validations() throws EmptyTextException {
        validate(nameEditText, R.string.customer_name_required);
        validate(addressEditText, R.string.address_required);
    }

    private void validate(EditText editText, int errorMessageResourceId) throws EmptyTextException {
        Editable editable = editText.getText();
        String string = editable.toString();
        string = string.trim();

        if (string.isEmpty()) {
            String errorMessage = Resource.getString(errorMessageResourceId);
            throw new EmptyTextException(errorMessage);
        }
    }
}
