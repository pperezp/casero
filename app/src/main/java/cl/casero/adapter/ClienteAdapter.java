package cl.casero.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cl.casero.R;
import cl.casero.bd.DAO;
import cl.casero.bd.model.Cliente;
import cl.casero.bd.model.Util;

/**
 * Created by Patricio Pérez Pinto on 04/01/2016.
 */
public class ClienteAdapter extends BaseAdapter {
    protected Activity activity;
    protected List<Cliente> clientes;

    public ClienteAdapter(Activity activity, List<Cliente> clientes) {
        this.activity = activity;
        this.clientes = clientes;
    }

    @Override
    public int getCount() {
        return clientes.size();
    }

    @Override
    public Object getItem(int position) {
        return clientes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return clientes.get(position).id;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View vi = contentView;

		/*Acá cargo el layout del item cliente*/
        if (contentView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.activity_cliente_item, null);
        }

		/*obtengo el cliente en la posición determinada*/
        Cliente c = clientes.get(position);

		/*Seteo los datos de la tienda de forma gráfica*/
        TextView nombre = (TextView) vi.findViewById(R.id.txtNomCli);
        nombre.setText(c.nombre);


        TextView txtSector = (TextView) vi.findViewById(R.id.txtSector);
        txtSector.setText(c.sector);

        DAO d = new DAO(ClienteAdapter.this.activity.getApplicationContext());
        int saldo = d.getDeuda(c.id);
        TextView txtSaldo = (TextView) vi.findViewById(R.id.txtSalCli);
        txtSaldo.setText("$ "+ Util.getNumeroConPuntos(saldo));

        //ImageView imagen = (ImageView)vi.findViewById(R.id.imageViewTienda);
        //imagen.setImageBitmap(tienda.getImagen());

        return vi;
    }
}
