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
import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.Tables.SortableClientesTable;
import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.Tables.SortableSucursalTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.ClienteModel;
import com.Danthop.bionet.model.InventarioModel;
import com.Danthop.bionet.model.SucursalModel;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class InventarioAdapter extends LongPressAwareTableDataAdapter<InventarioModel> implements Filterable {
    int TEXT_SIZE = 16;

    private List<InventarioModel> inventarioList;
    private List<InventarioModel> inventarioListFull;

    public InventarioAdapter(final Context context, final List<InventarioModel> data, final SortableInventariosTable tableView) {
        super(context, data, tableView);
        inventarioList=data;
        inventarioListFull = new ArrayList<>(data);

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
                renderedView = renderarticulo(Invetario);
                break;
            case 2:
                renderedView = rendercodigoBarras(Invetario);
                break;
            case 3:
                renderedView = rendercategoria(Invetario);
                break;
            case 4:
                renderedView = renderexistencia(Invetario);
                break;
            case 5:
                renderedView = rendersucursal(Invetario);
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
        editText.addTextChangedListener(new InventarioAdapter.OrdenNameUpdater(Inventario));
        return editText;
    }

    private View renderSKU(final InventarioModel Inventario) {
        return renderString(Inventario.getSku());
    }

    private View renderarticulo(final InventarioModel Inventario) {
        return renderString(Inventario.getProducto());
    }

    private View renderexistencia(final InventarioModel Inventario) {
        final TextView textView = new TextView(getContext());
        textView.setText(Inventario.getExistencia());
        textView.setTextSize(TEXT_SIZE);
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        return textView;
    }

    private View rendercategoria(final InventarioModel Inventario) {
        return renderString(Inventario.getCategoria());
    }

    private View rendercodigoBarras(final InventarioModel Inventario) {
        return renderString(Inventario.getcodigoBarras());
    }

    private View rendersucursal(final InventarioModel Inventario) {
        return renderString(Inventario.getNombre_sucursal());
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setTextSize(TEXT_SIZE);
        textView.setPadding(5,2,5,2);;
        textView.setGravity(View.TEXT_ALIGNMENT_TEXT_END);
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
                filteredList.addAll(inventarioListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (InventarioModel item : inventarioList) {
                    if (item.getProducto().toLowerCase().contains(filterPattern)
                            ||item.getSku().toLowerCase().contains(filterPattern)
                            ||item.getcodigoBarras().toLowerCase().contains(filterPattern)
                            ||item.getCategoria().toLowerCase().contains(filterPattern)
                            ||item.getExistencia().toLowerCase().contains(filterPattern)
                            ||item.getNombre_sucursal().toLowerCase().contains(filterPattern)) {
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
            inventarioList.clear();
            inventarioList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };


}
