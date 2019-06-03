package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableLealtadArticulosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.LealtadArticuloModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class LealtadArticuloAdapter extends LongPressAwareTableDataAdapter<LealtadArticuloModel> {

    int TEXT_SIZE = 16;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public LealtadArticuloAdapter(final Context context, final List<LealtadArticuloModel> data, final SortableLealtadArticulosTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        LealtadArticuloModel articulo = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderNombre(articulo);
                break;
            case 1:
                renderedView = renderDescripcion(articulo);
                break;
            case 2:
                renderedView = renderCategoria(articulo);
                break;

        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final LealtadArticuloModel articulo = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {

        }

        return renderedView;
    }

    private View renderNombre(final LealtadArticuloModel articulo) {
        return renderString(articulo.getArticuloNombre());
    }

    private View renderCategoria(final LealtadArticuloModel articulo) {
        return renderString(articulo.getArticuloCategoria());
    }

    private View renderDescripcion(final LealtadArticuloModel articulo) {
        return renderString(articulo.getArticuloDescripcion());
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
