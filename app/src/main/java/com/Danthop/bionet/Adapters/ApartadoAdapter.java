package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableApartadoTable;
import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.model.ApartadoModel;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.SucursalModel;

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
                renderedView = renderArticulo(apartado);
                break;
            case 1:
                renderedView = renderCantidad(apartado);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ApartadoModel apartado = getRowData(rowIndex);

        return renderString("");
    }


    private View renderArticulo(final ApartadoModel apartado) {
        return renderString(apartado.getArticulo());
    }

    private View renderCantidad(final ApartadoModel apartado) {
        return renderString(apartado.getCantidad());
    }

    private View renderString ( final String value){
            final TextView textView = new TextView(getContext());
            textView.setText(value);
            textView.setPadding(20, 10, 20, 10);
            textView.setTextSize(TEXT_SIZE);
            return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

            private ApartadoModel ordenToUpdate;

            public OrdenNameUpdater(ApartadoModel ordenToUpdate) {
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
                ordenToUpdate.getArticulo();
            }
    }
}