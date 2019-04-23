package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableInventariosTable;
import com.Danthop.bionet.model.InventarioModel;

import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class TrasladoAdapter extends LongPressAwareTableDataAdapter<InventarioModel> {
    int TEXT_SIZE = 12;

    public TrasladoAdapter(final Context context, final List<InventarioModel> data, final SortableInventariosTable tableView) {
        super(context, data, tableView);
    }
    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        InventarioModel Invetario = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderarticulo(Invetario);
                break;
            case 1:
                renderedView = rendercategoria(Invetario);
                break;
            case 2:
                renderedView = rendersucursal(Invetario);
                break;
            case 3:
                renderedView = renderexistencia(Invetario);
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
        editText.addTextChangedListener(new TrasladoAdapter.OrdenNameUpdater(Inventario));
        return editText;
    }


    private View renderarticulo(final InventarioModel Inventario) {
        return renderString(Inventario.getProducto());
    }

    private View renderexistencia(final InventarioModel Inventario) {
        return renderString(Inventario.getExistencia());
    }

    private View rendercategoria(final InventarioModel Inventario) {
        return renderString(Inventario.getCategoria());
    }

    private View rendersucursal(final InventarioModel Inventario) {
        return renderString(Inventario.getNombre_sucursal());
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
