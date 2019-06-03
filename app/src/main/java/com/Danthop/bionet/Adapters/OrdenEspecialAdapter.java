package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableOrdenEspecialTable;
import com.Danthop.bionet.model.OrdenEspecialModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class OrdenEspecialAdapter extends LongPressAwareTableDataAdapter<OrdenEspecialModel> {

    int TEXT_SIZE = 16;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public OrdenEspecialAdapter(final Context context, final List<OrdenEspecialModel> data, final SortableOrdenEspecialTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        OrdenEspecialModel orden = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderCliente(orden);
                break;
            case 1:
                renderedView = renderSucursal(orden);
                break;
            case 2:
                renderedView = renderFechaCreacion(orden);
                break;
            case 3:
                renderedView = renderFechaVencimiento(orden);
                break;
            case 4:
                renderedView = renderEstatus(orden);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final OrdenEspecialModel orden = getRowData(rowIndex);

        return renderString("");
    }


    private View renderCliente(final OrdenEspecialModel orden) {
        return renderString(orden.getOrden_nombre_cliente());
    }

    private View renderSucursal(final OrdenEspecialModel orden) {
        return renderString(orden.getOrden_nombre_sucursal());
    }

    private View renderFechaCreacion(final OrdenEspecialModel orden) {
        return renderString(orden.getFecha_creacion());
    }

    private View renderFechaVencimiento(final OrdenEspecialModel orden) {
        return renderString(orden.getFecha_vencimiento());
    }

    private View renderEstatus(final OrdenEspecialModel orden) {
        return renderString(orden.getOrden_estatus());
    }

    private View renderString ( final String value){
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private OrdenEspecialModel ordenToUpdate;

        public OrdenNameUpdater(OrdenEspecialModel ordenToUpdate) {
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
            ordenToUpdate.getOrden_nombre_cliente();
        }
    }
}