package com.Danthop.bionet.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.Danthop.bionet.R;
import com.Danthop.bionet.Tables.SortableSincronizarTable;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.Danthop.bionet.model.SincronizarModel;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class SincronizarAdapter extends LongPressAwareTableDataAdapter<SincronizarModel> implements Filterable {

    int TEXT_SIZE = 16;
    //private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private List<SincronizarModel> SincronizarList;
    private List<SincronizarModel> SincronizarListFull;

    public SincronizarAdapter(final Context context, final List<SincronizarModel> data, final SortableSincronizarTable tableView) {
        super(context, data, tableView);
        SincronizarList = data;
        SincronizarListFull = new ArrayList<>(data);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        SincronizarModel sincronizar = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderArticulo(sincronizar);
                break;
            case 1:
                renderedView = renderDisponible(sincronizar);
                break;
            case 2:
                renderedView = renderEnvioGratis(sincronizar);
                break;
            case 3:
                renderedView = renderPrecio(sincronizar);
                break;
            case 5:
                renderedView = renderVerFicha(sincronizar);
                break;
            case 6:
                renderedView = renderPrecio(sincronizar);
                break;
    }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final SincronizarModel sincronizar = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(sincronizar);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final SincronizarModel sincronizar) {
        final EditText editText = new EditText(getContext());
        editText.setText(sincronizar.getArticulo());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new OrdenNameUpdater(sincronizar));
        return editText;
    }

    private View renderArticulo(final SincronizarModel sincronizar) {
        return renderString(sincronizar.getArticulo());
    }

    private View renderDisponible(final SincronizarModel sincronizar) {
        return renderString(sincronizar.getDisponible());
    }

    private View renderEnvioGratis(final SincronizarModel sincronizar) {
        return renderString(sincronizar.getEnvio_gratis());
    }

    private View renderPrecio(final SincronizarModel sincronizar) {
        double Importe = Double.parseDouble(sincronizar.getPrecio());

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView(getContext());
        textView.setText(formatter.format(Importe));
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);

        return textView;

    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private View renderVerFicha(final SincronizarModel Sincronizar) {
        return ButtonVerFicha(Sincronizar);
    }

    private View ButtonVerFicha(final SincronizarModel Sincronizar){
        final Button ver = new Button(getContext());
        ver.setText("Ver");
        ver.setPadding(10, 10, 10, 10);
        ver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                Dialog ver_cliente_dialog;
                ver_cliente_dialog=new Dialog(getContext());
                ver_cliente_dialog.setContentView(R.layout.pop_up_ecommerce_contestar_pregunta );
                ver_cliente_dialog.show();

                //Articulo;
                // Disponible;
                // Envio_gratis;
                // Precio;

                //ecommerse_cliente
                //ecommerse_tipo_pago
                //ecommerse_costo
                //ecommerse_importe
                //ecommerse_estado
                //ecommerse_fecha

                TextView NameCliente = ver_cliente_dialog.findViewById(R.id.cliente_nombre);
                TextView CorreoCliente = ver_cliente_dialog.findViewById(R.id.email_cliente);
                TextView TelefonoCliente = ver_cliente_dialog.findViewById(R.id.telefono_cliente);

            }
        });
        return ver;
    }



    private static class OrdenNameUpdater implements TextWatcher {

        private SincronizarModel ordenToUpdate;

        public OrdenNameUpdater(SincronizarModel ordenToUpdate) {
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
            ordenToUpdate.setArticulo(s.toString());
        }
    }
    @Override
    public Filter getFilter() {
        return OrdenFilter;
    }

    private Filter OrdenFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<SincronizarModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(SincronizarListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (SincronizarModel item : SincronizarListFull) {
                    if (item.getArticulo().toLowerCase().contains(filterPattern)
                            || item.getDisponible().toLowerCase().contains(filterPattern)
                            || item.getEnvio_gratis().toLowerCase().contains(filterPattern)
                            || item.getPrecio().toLowerCase().contains(filterPattern)){
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
            SincronizarList.clear();
            SincronizarList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

}


