package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.HistoricoModel;
import com.Danthop.bionet.model.InventarioModel;

import java.util.ArrayList;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class InventarioExistenciasAdapter extends LongPressAwareTableDataAdapter<InventarioModel> {
    int TEXT_SIZE = 16;
    private List<InventarioModel> historicoList;
    private List<InventarioModel> historicoListFull;

    public InventarioExistenciasAdapter(final Context context, final List<InventarioModel> data, final SortableInventariosTable tableView) {
        super(context, data, tableView);
        historicoList=data;
        historicoListFull = new ArrayList<>(data);
    }
    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        InventarioModel Invetario = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {

            case 0:
                renderedView = renderSKU(Invetario);
                break;
            case 1:
                renderedView = renderProducto(Invetario);
                break;
            case 2:
                renderedView = renderSucursal(Invetario);
                break;
            case 3:
                renderedView = renderExistencias(Invetario);
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
        editText.addTextChangedListener(new InventarioExistenciasAdapter.OrdenNameUpdater(Inventario));
        return editText;
    }

    private View renderSKU(final InventarioModel Inventario) {
        return renderString(Inventario.getSku());
    }

    private View renderProducto(final InventarioModel Inventario) {
        return renderString(Inventario.getProducto());
    }

    private View renderSucursal(final InventarioModel Inventario) {
        return renderString(Inventario.getNombre_sucursal());
    }

    private View renderExistencias(final InventarioModel Inventario) {
        return renderString(Inventario.getExistencia());
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
        return HistoricoFilter;
    }

    private Filter HistoricoFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<InventarioModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(historicoListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (InventarioModel item : historicoListFull) {
                    if (item.getArt_nombre().toLowerCase().contains(filterPattern)
                            || item.getSku().toLowerCase().contains(filterPattern)
                            || item.getArt_nombre().toLowerCase().contains(filterPattern)) {
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
            historicoList.clear();
            historicoList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
