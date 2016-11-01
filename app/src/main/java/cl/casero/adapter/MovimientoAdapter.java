package cl.casero.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import cl.casero.R;
import cl.casero.bd.DAO;
import cl.casero.bd.model.Movimiento;
import cl.casero.bd.model.Util;

/**
 * Created by Patricio Pérez Pinto on 04/01/2016.
 */
public class MovimientoAdapter extends BaseAdapter {
    protected List<Movimiento> movs;
    protected Activity activity;

    public MovimientoAdapter(Activity activity, List<Movimiento> movs) {
        this.activity = activity;
        this.movs = movs;
    }
    @Override
    public int getCount() {
        return movs.size();
    }

    @Override
    public Object getItem(int position) {
        return movs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return movs.get(position).id;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View vi = contentView;

		/*Acá cargo el layout del item tienda*/
        if (contentView == null) {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.activity_movimiento_item, null);
        }

		/*obtengo el cliente en la posición determinada*/
        Movimiento m = movs.get(position);

		/*Seteo los datos de la tienda de forma gráfica*/
        TextView lblFecha = (TextView) vi.findViewById(R.id.lblFecha);
        TextView lblDetalle = (TextView) vi.findViewById(R.id.lblDetalle);
        TextView lblSaldo = (TextView) vi.findViewById(R.id.lblSaldoDetalle);

        SimpleDateFormat f = new SimpleDateFormat("dd 'de' MMMM 'de' yyyy");

        lblFecha.setText("["+f.format(m.fecha)+"]");
        lblDetalle.setText(m.detalle);

        if(m.detalle.contains("Abono")){
            lblSaldo.setTextColor(Color.parseColor("#f44336"));
        }else if(m.detalle.contains("Venta")){
            lblSaldo.setTextColor(Color.parseColor("#4caf50"));
        }else{
            lblSaldo.setTextColor(Color.parseColor("#3f51b5"));
        }

        lblSaldo.setText("$"+ Util.getNumeroConPuntos(m.saldo));

        return vi;
    }
}
