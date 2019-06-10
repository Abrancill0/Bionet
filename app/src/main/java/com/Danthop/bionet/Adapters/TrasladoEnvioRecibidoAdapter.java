package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableTrasladosTable;
import com.Danthop.bionet.model.InventarioModel;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class TrasladoEnvioRecibidoAdapter extends LongPressAwareTableDataAdapter<InventarioModel> implements Filterable {
    int TEXT_SIZE = 16;
    private List<InventarioModel> InventarioList;
    private List<InventarioModel> InventarioListFull;

    public TrasladoEnvioRecibidoAdapter(final Context context, final List<InventarioModel> data, final SortableTrasladosTable tableView) {
        super(context, data, tableView);

        InventarioList = data;
        InventarioListFull = new ArrayList<>(data);

    }
    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        InventarioModel Invetario = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderOrigen(Invetario);
                break;
            case 1:
                renderedView = renderDestino(Invetario);
                break;
            case 2:
                renderedView = renderEstatus(Invetario);
                break;
            case 3:
                renderedView = renderMotivo(Invetario);
                break;
            case 4:
                renderedView = renderFecha(Invetario);
                break;

        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final InventarioModel Invetario = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableInventarioName(Invetario);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableInventarioName(final InventarioModel Inventario) {
        final EditText editText = new EditText(getContext());
        editText.setText(Inventario.getNombre_sucursal());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new TrasladoEnvioRecibidoAdapter.OrdenNameUpdater(Inventario));
        return editText;
    }


    private View renderOrigen(final InventarioModel Inventario) {
        return renderString(Inventario.getRecibidasOrigen());
    }

    private View renderDestino(final InventarioModel Inventario) {
        return renderString(Inventario.getRecibidasDestino());
    }

    private View renderEstatus(final InventarioModel Inventario) {
        return renderString(Inventario.gettra_nombre_estatus());
    }

    private View renderMotivo(final InventarioModel Inventario) {
        return renderString(Inventario.gettra_motivo());
    }

    private View renderFecha(final InventarioModel Inventario) {
        return renderString( String.valueOf( Inventario.getfechaSolicitud() ) );
    }



    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private InventarioModel ordenToUpdate;

        public OrdenNameUpdater(InventarioModel ordenToUpdate) {
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
            ordenToUpdate.setNombre_sucursal(s.toString());
        }
    }

    @Override
    public Filter getFilter() {
        return InventarioFilter;
    }

    private Filter InventarioFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<InventarioModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(InventarioListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (InventarioModel item : InventarioListFull) {
                    if (item.getRecibidasOrigen().toLowerCase().contains(filterPattern)
                            || item.getRecibidasDestino().toLowerCase().contains(filterPattern)
                            || item.gettra_nombre_estatus().toLowerCase().contains(filterPattern)
                            || item.gettra_motivo().toLowerCase().contains(filterPattern)){
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
            InventarioList.clear();
            InventarioList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
