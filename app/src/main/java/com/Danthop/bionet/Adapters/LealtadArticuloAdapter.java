package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.Tables.SortableLealtadArticulosTable;
import com.Danthop.bionet.Tables.SortablePuntosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.LealtadArticulosModel;
import com.Danthop.bionet.model.Puntos_acumulados_model;
import com.Danthop.bionet.model.SucursalModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class LealtadArticuloAdapter extends LongPressAwareTableDataAdapter<LealtadArticulosModel> {

    int TEXT_SIZE = 12;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public LealtadArticuloAdapter(final Context context, final List<LealtadArticulosModel> data, final SortableLealtadArticulosTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        LealtadArticulosModel articulo = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderProducto(articulo);
                break;
            case 1:
                renderedView = renderCategoria(articulo);
                break;
            case 2:
                renderedView = renderExistencia(articulo);
                break;
            case 3:
                renderedView = renderPrecioLista(articulo);
                break;
            case 4:
                renderedView = renderDescuentoActual(articulo);
                break;
            case 5:
                renderedView = renderPrecioTotal(articulo);
                break;

        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final LealtadArticulosModel articulo = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {

        }

        return renderedView;
    }

    private View renderProducto(final LealtadArticulosModel articulo) {
        return renderString(articulo.getProducto());
    }

    private View renderCategoria(final LealtadArticulosModel articulo) {
        return renderString(articulo.getCategoria());
    }

    private View renderExistencia(final LealtadArticulosModel articulo) {
        return renderString(articulo.getExistencia());
    }
    private View renderPrecioLista(final LealtadArticulosModel articulo) {
        return renderString(articulo.getPrecio_lista());
    }
    private View renderDescuentoActual(final LealtadArticulosModel articulo) {
        return renderString(articulo.getDescuentoActual());
    }
    private View renderPrecioTotal(final LealtadArticulosModel articulo) {
        return renderString(articulo.getPrecioTotal());
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