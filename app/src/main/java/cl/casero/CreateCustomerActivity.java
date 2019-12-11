package cl.casero;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import cl.casero.listener.createCustomer.BackButtonOnClickListener;
import cl.casero.listener.createCustomer.CreateCustomerButtonOnClickListener;
import cl.casero.model.Resource;
import cl.casero.service.StatisticsService;
import cl.casero.service.impl.StatisticsServiceImpl;

public class CreateCustomerActivity extends ActionBarActivity {
    private Button backButton;
    private Button createButton;
    private EditText nameEditText;
    private EditText addressEditText;
    private TextView countTextView;

    private StatisticsService statisticsService;

    private static CreateCustomerActivity createCustomerActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_customer);

        createCustomerActivity = this;

        statisticsService = new StatisticsServiceImpl();

        loadComponents();
        loadListeners();

        String customers = Resource.getString(R.string.customers);

        customers = customers.replace("{0}", String.valueOf(statisticsService.getCustomersCount()));

        countTextView.setText(customers);
    }

    private void loadListeners() {
        backButton.setOnClickListener(new BackButtonOnClickListener());
        createButton.setOnClickListener(new CreateCustomerButtonOnClickListener());
    }

    private void loadComponents() {
        backButton = (Button) findViewById(R.id.backButton);
        createButton = (Button) findViewById(R.id.createButton);
        nameEditText = (EditText) findViewById(R.id.nameTextView);
        addressEditText = (EditText) findViewById(R.id.addressEditText);
        countTextView = (TextView) findViewById(R.id.countTextView);

        nameEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        addressEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
    }

    public static CreateCustomerActivity getActivity(){
        return createCustomerActivity;
    }
}
