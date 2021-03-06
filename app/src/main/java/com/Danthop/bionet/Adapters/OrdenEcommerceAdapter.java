package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.InventarioModel;
import com.Danthop.bionet.model.VolleySingleton;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonArrayRequest;
import com.android.volley.request.JsonObjectRequest;
import com.google.android.gms.common.api.Api;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;

import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class OrdenEcommerceAdapter extends LongPressAwareTableDataAdapter<Ecommerce_orden_Model> implements Filterable {

    int TEXT_SIZE = 16;
    public Dialog pop_up1;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();

    private List<Ecommerce_orden_Model> OrdenList;
    private List<Ecommerce_orden_Model> OrdenListFull;

    public OrdenEcommerceAdapter(final Context context, final List<Ecommerce_orden_Model> data, final SortableOrdenEcommerceTable tableView) {
        super( context, data, tableView );
        OrdenList = data;
        OrdenListFull = new ArrayList<>(data);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Ecommerce_orden_Model orden = getRowData( rowIndex );
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderClienteName( orden );
                break;
            case 1:
                renderedView = renderOrdenArticulo( orden );
                break;
            case 2:
                renderedView = renderOrdenCantidad( orden );
                break;
            case 3:
                renderedView = renderOrdenEnvio( orden );
                break;
            case 4:
                renderedView = renderOrdenImporte( orden );
                break;
            case 5:
                renderedView = renderOrdenEstatus( orden );
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Ecommerce_orden_Model orden = getRowData( rowIndex );
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName( orden );
                break;
            default:
                renderedView = getDefaultCellView( rowIndex, columnIndex, parentView );
        }

        return renderedView;
    }

    private View renderEditableClienteName(final Ecommerce_orden_Model orden) {
        final EditText editText = new EditText( getContext() );
        editText.setText( orden.getCliente() );
        editText.setPadding( 20, 10, 20, 10 );
        editText.setTextSize( TEXT_SIZE );
        editText.setSingleLine();
        editText.addTextChangedListener( new OrdenNameUpdater( orden ) );
        return editText;
    }

    private View renderClienteName(final Ecommerce_orden_Model orden) {
        return renderString( orden.getCliente() );
    }

    private View renderOrdenArticulo(final Ecommerce_orden_Model orden) {
        return renderString( orden.getArticulo() );
    }

    private View renderOrdenCantidad(final Ecommerce_orden_Model orden) {
        return renderString( orden.getCantidad() );
    }

    private View renderOrdenEnvio(final Ecommerce_orden_Model orden) {

        double Envio = Double.parseDouble( orden.getEnvio() );

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Envio ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;

    }

    private View renderOrdenImporte(final Ecommerce_orden_Model orden) {
        double Importe = Double.parseDouble( orden.getImporte() );

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Importe ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View renderOrdenEstatus(final Ecommerce_orden_Model orden) {
        return renderString( orden.getEstatus() );
    }

    private View renderString(final String value) {
        final TextView textView = new TextView( getContext() );
        textView.setText( value );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );
        return textView;
    }

    private void VerGuia(String Token, String ship) {

        //Nombre Persona que vende
        //Nickname de persona que vende

        //Direccion solo texto
        //Cliente OK
        //Celular OK
        //Direccio OK

        //Fecha creacion OK
        //Tipo de pago
        //Estado de la orden
        //Costo de envio OK
        //importe de producto OK
        //numero de guia OK
        //servicio OK


        //date_created
        //tracking_number
        //tracking_method

        //shipping_option object
        //  cost
        //  list_cost
        //


        //receiver_address   object
        //  address_line
        //  comment
        //  zip_code
        //  receiver_name
        //  receiver_phone
        //city   object
        //  name


    }

    private static class OrdenNameUpdater implements TextWatcher {

        private Ecommerce_orden_Model ordenToUpdate;

        public OrdenNameUpdater(Ecommerce_orden_Model ordenToUpdate) {
            this.ordenToUpdate = ordenToUpdate;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // no used
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // not used
        }

        @Override
        public void afterTextChanged(Editable s) {
            ordenToUpdate.setCliente( s.toString() );
        }
    }
    @Override
    public Filter getFilter() {
        return OrdenFilter;
    }

    private Filter OrdenFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Ecommerce_orden_Model> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(OrdenListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Ecommerce_orden_Model item : OrdenListFull) {
                    if (item.getCliente().toLowerCase().contains(filterPattern)
                            || item.getArticulo().toLowerCase().contains(filterPattern)
                            || item.getCantidad().toLowerCase().contains(filterPattern)
                            || item.getEnvio().toLowerCase().contains(filterPattern)
                            || item.getImporte().toLowerCase().contains(filterPattern)
                            || item.getEstatus().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            OrdenList.clear();
            OrdenList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}


