package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableHistoricoTable;
import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.InventarioModel;
import java.util.List;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class HistoricoAdapter extends LongPressAwareTableDataAdapter<InventarioModel> {
    int TEXT_SIZE = 12;

    public HistoricoAdapter(final Context context, final List<InventarioModel> data, final SortableHistoricoTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        InventarioModel Invetario = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderart_nombre(Invetario);
                break;
            case 1:
                renderedView = rendercat_nombre(Invetario);
                break;
            case 2:
                renderedView = renderhis_tipo(Invetario);
                break;
            case 3:
                renderedView = renderhis_cantidad(Invetario);
                break;
            case 4:
                renderedView = renderhis_observaciones(Invetario);
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
        editText.addTextChangedListener(new HistoricoAdapter.OrdenNameUpdater(Inventario));
        return editText;
    }

    private View renderart_nombre(final InventarioModel Inventario) {
        return renderString(Inventario.getArt_nombre());
    }

    private View rendercat_nombre(final InventarioModel Inventario) {
        return renderString(Inventario.getCat_nombre());
    }

    private View renderhis_tipo(final InventarioModel Inventario) {
        return renderString(Inventario.getHis_tipo());
    }

    private View renderhis_cantidad(final InventarioModel Inventario) {
        return renderString(Inventario.getHis_cantidad());
    }
    private View renderhis_observaciones(final InventarioModel Inventario) {
        return renderString(Inventario.getHis_observaciones());
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
    }}