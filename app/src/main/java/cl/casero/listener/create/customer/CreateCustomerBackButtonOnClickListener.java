package cl.casero.listener.create.customer;

import android.content.Intent;
import android.view.View;

import cl.casero.CreateCustomerActivity;
import cl.casero.MainActivity;

public class CreateCustomerBackButtonOnClickListener implements View.OnClickListener {

    private final CreateCustomerActivity createCustomerActivity;

    public CreateCustomerBackButtonOnClickListener(CreateCustomerActivity createCustomerActivity) {
        this.createCustomerActivity = createCustomerActivity;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(createCustomerActivity, MainActivity.class);
        createCustomerActivity.startActivity(intent);
    }
}
