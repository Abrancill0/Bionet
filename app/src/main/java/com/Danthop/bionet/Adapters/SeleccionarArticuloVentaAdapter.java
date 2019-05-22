package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.Tables.SortableSeleccionarArticuloTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.SucursalModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class SeleccionarArticuloVentaAdapter extends LongPressAwareTableDataAdapter<ArticuloModel> {

    int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public SeleccionarArticuloVentaAdapter(final Context context, final List<ArticuloModel> data, final SortableSeleccionarArticuloTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        ArticuloModel Articulo = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderNombre(Articulo);
                break;
            case 1:
                renderedView = renderSKU(Articulo);
                break;
            case 2:
                renderedView = renderDescripcion(Articulo);
                break;
            case 3:
                renderedView = renderCategoria(Articulo);
                break;

        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ArticuloModel Articulo = getRowData(rowIndex);
        View renderedView = null;

        return renderedView;
    }

    private View renderEditableClienteName(final ArticuloModel Articulo) {
        final TextView v = new TextView(getContext());
        v.setText(Articulo.getarticulo_Nombre());
        return v;
    }

    private View renderNombre(final ArticuloModel articulo) {
        return renderString(articulo.getarticulo_Nombre());
    }

    private View renderSKU(final ArticuloModel articulo) {
        return renderString(articulo.getArticulo_sku());

    }
    private View renderDescripcion(final ArticuloModel articulo) {
        return renderString(articulo.getarticulo_Descripcion());

    }

    private View renderCategoria(final ArticuloModel articulo) {
        return renderString(articulo.getArticulo_categoria());

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
