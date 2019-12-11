package cl.casero.listener.createCustomer;

import android.content.Intent;
import android.view.View;

import cl.casero.CreateCustomerActivity;
import cl.casero.MainActivity;

public class BackButtonOnClickListener implements View.OnClickListener {

    private CreateCustomerActivity createCustomerActivity;

    public BackButtonOnClickListener(){
        createCustomerActivity = CreateCustomerActivity.getActivity();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(
            createCustomerActivity,
            MainActivity.class
        );

        createCustomerActivity.startActivity(intent);
    }
}
