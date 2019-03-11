package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.Tables.SortablePuntosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.Puntos_acumulados_model;
import com.Danthop.bionet.model.SucursalModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class PuntosAcumuladosAdapter extends LongPressAwareTableDataAdapter<Puntos_acumulados_model> {

    int TEXT_SIZE = 12;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public PuntosAcumuladosAdapter(final Context context, final List<Puntos_acumulados_model> data, final SortablePuntosTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        Puntos_acumulados_model Puntos = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderId(Puntos);
                break;
            case 1:
                renderedView = renderNombre(Puntos);
                break;
            case 2:
                renderedView = renderAcumulado(Puntos);
                break;

        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final Puntos_acumulados_model Puntos = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {

        }

        return renderedView;
    }

    private View renderId(final Puntos_acumulados_model puntos) {
        return renderString(puntos.getId_cliente());
    }

    private View renderNombre(final Puntos_acumulados_model puntos) {
        return renderString(puntos.getNombre());
    }

    private View renderAcumulado(final Puntos_acumulados_model puntos) {
        return renderString(puntos.getAcumulado());
    }

    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private ArticuloModel ordenToUpdate;

        public OrdenNameUpdater(ArticuloModel ordenToUpdate) {
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
            ordenToUpdate.getarticulo_Nombre();
        }
    }}
