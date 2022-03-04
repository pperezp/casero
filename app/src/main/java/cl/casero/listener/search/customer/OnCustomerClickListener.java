package cl.casero.listener.search.customer;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import cl.casero.CustomerViewActivity;
import cl.casero.MainActivity;
import cl.casero.model.util.K;

public class OnCustomerClickListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        K.customerId = id;
        MainActivity mainActivity = MainActivity.getInstance();
        Intent intent = new Intent(mainActivity, CustomerViewActivity.class);
        mainActivity.startActivity(intent);
    }
}
