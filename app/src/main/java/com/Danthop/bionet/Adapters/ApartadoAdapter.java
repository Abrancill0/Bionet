package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableApartadoTable;
import com.Danthop.bionet.model.ApartadoModel;
import com.Danthop.bionet.model.ArticuloApartadoModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class ApartadoAdapter extends LongPressAwareTableDataAdapter<ApartadoModel> {

    int TEXT_SIZE = 12;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public ApartadoAdapter(final Context context, final List<ApartadoModel> data, final SortableApartadoTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ApartadoModel apartado = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderCliente(apartado);
                break;
            case 1:
                renderedView = renderSucursal(apartado);
                break;
            case 2:
                renderedView = renderFechaCreacion(apartado);
                break;
            case 3:
                renderedView = renderFechaVencimiento(apartado);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ApartadoModel apartado = getRowData(rowIndex);

        return renderString("");
    }


    private View renderCliente(final ApartadoModel apartado) {
        return renderString(apartado.getApartado_nombre_cliente());
    }

    private View renderSucursal(final ApartadoModel apartado) {
        return renderString(apartado.getApartado_nombre_sucursal());
    }

    private View renderFechaCreacion(final ApartadoModel apartado) {
        return renderString(apartado.getFecha_creacion());
    }

    private View renderFechaVencimiento(final ApartadoModel apartado) {
        return renderString(apartado.getFecha_vencimiento());
    }

    private View renderString ( final String value){
            final TextView textView = new TextView(getContext());
            textView.setText(value);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextSize(TEXT_SIZE);
            return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

            private ArticuloApartadoModel ordenToUpdate;

            public OrdenNameUpdater(ArticuloApartadoModel ordenToUpdate) {
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
                ordenToUpdate.getNombre_articulo();
            }
    }
}