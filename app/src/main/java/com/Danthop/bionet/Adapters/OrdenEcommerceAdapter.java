package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import com.Danthop.bionet.model.Ecommerce_orden_Model;
import com.google.android.gms.common.api.Api;

import java.util.List;

import de.codecrafters.tableview.TableDataAdapter;
import com.Danthop.bionet.Tables.SortableOrdenEcommerceTable;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class OrdenEcommerceAdapter extends LongPressAwareTableDataAdapter<Ecommerce_orden_Model> {

    int TEXT_SIZE = 12;

    public OrdenEcommerceAdapter(final Context context, final List<Ecommerce_orden_Model> data, final SortableOrdenEcommerceTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Ecommerce_orden_Model orden = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderClienteName(orden);
                break;
            case 1:
                renderedView = renderOrdenArticulo(orden);
                break;
            case 2:
                renderedView = renderOrdenCantidad(orden);
                break;
            case 3:
                renderedView = renderOrdenEnvio(orden);
                break;
            case 4:
                renderedView = renderOrdenImporte(orden);
                break;
            case 5:
                renderedView = renderOrdenEstatus(orden);
                break;
        }

        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Ecommerce_orden_Model orden = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(orden);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final Ecommerce_orden_Model orden) {
        final EditText editText = new EditText(getContext());
        editText.setText(orden.getCliente());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new OrdenNameUpdater(orden));
        return editText;
    }

    private View renderClienteName(final Ecommerce_orden_Model orden) {
        return renderString(orden.getCliente());
    }

    private View renderOrdenArticulo(final Ecommerce_orden_Model orden) {
        return renderString(orden.getArticulo());
    }

    private View renderOrdenCantidad(final Ecommerce_orden_Model orden) {
        return renderString(orden.getCantidad());
    }

    private View renderOrdenEnvio(final Ecommerce_orden_Model orden) {
        return renderString(orden.getEnvio());
    }

    private View renderOrdenImporte(final Ecommerce_orden_Model orden) {
        return renderString(orden.getImporte());
    }

    private View renderOrdenEstatus(final Ecommerce_orden_Model orden) {
        return renderString(orden.getEstatus());
    }


    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
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
            ordenToUpdate.setCliente(s.toString());
        }
    }}

