package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableLealtadInscribirTable;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.Puntos_acumulados_model;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class LealtadInscribirAdapter extends LongPressAwareTableDataAdapter<ClienteModel> implements Filterable {

    int TEXT_SIZE = 16;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();
    private FragmentTransaction fr;

    private List<ClienteModel> clientesList;
    private List<ClienteModel> clientesListFull;


    public LealtadInscribirAdapter(final Context context, final List<ClienteModel> data, final SortableLealtadInscribirTable tableView,FragmentTransaction gr) {
        super(context, data, tableView);
        fr = gr;

        clientesList = data;
        clientesListFull = new ArrayList<>(data);

    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ClienteModel cliente = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderClienteName(cliente);
                break;
            case 1:
                renderedView = renderClienteCorreo(cliente);
                break;
            case 2:
                renderedView = renderClienteTelefono(cliente);
                break;
            case 3:  renderedView = renderClienteUltimaVisita(cliente);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ClienteModel cliente = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(cliente);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final ClienteModel cliente) {
        final EditText editText = new EditText(getContext());
        editText.setText(cliente.getCliente_Nombre());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new LealtadInscribirAdapter.ClienteNameUpdater(cliente));
        return editText;
    }

    private View renderClienteName(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Nombre());
    }

    private View renderClienteCorreo(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Correo());
    }

    private View renderClienteTelefono(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Telefono());
    }

    private View renderClienteUltimaVisita(final ClienteModel cliente) {
        return renderString(cliente.getCliente_Ultima_Visita());
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }



    private static class ClienteNameUpdater implements TextWatcher {

        private ClienteModel clienteToUpdate;

        public ClienteNameUpdater(ClienteModel clienteToUpdate) {
            this.clienteToUpdate = clienteToUpdate;
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
            clienteToUpdate.setCliente_Nombre(s.toString());
        }
    }

    @Override
    public Filter getFilter() {
        return PuntosFilter;
    }

    private Filter PuntosFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<ClienteModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(clientesListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (ClienteModel item : clientesListFull) {
                    if (item.getCliente_Nombre().toLowerCase().contains(filterPattern)
                            || item.getCliente_Correo().toLowerCase().contains(filterPattern)
                            || item.getCliente_Ultima_Visita().toLowerCase().contains(filterPattern)
                            || item.getCliente_Telefono().toLowerCase().contains(filterPattern)) {
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
            clientesList.clear();
            clientesList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };



}