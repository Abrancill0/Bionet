package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableArticulosTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.SucursalModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class ArticuloAdapter extends LongPressAwareTableDataAdapter<ArticuloModel> {

    int TEXT_SIZE = 12;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public ArticuloAdapter(final Context context, final List<ArticuloModel> data, final SortableArticulosTable tableView) {
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
                renderedView = renderPrecio(Articulo);
                break;
            case 2:
                 renderedView = renderExistencia(Articulo);
                 break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final ArticuloModel Articulo = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 1:
                renderedView = renderEditableClienteName(Articulo);
                break;
            default:
                renderedView = getDefaultCellView(rowIndex, columnIndex, parentView);
        }

        return renderedView;
    }

    private View renderEditableClienteName(final ArticuloModel Articulo) {
        final EditText editText = new EditText(getContext());
        editText.setText(Articulo.getarticulo_Nombre());
        editText.setPadding(20, 10, 20, 10);
        editText.setTextSize(TEXT_SIZE);
        editText.setSingleLine();
        editText.addTextChangedListener(new ArticuloAdapter.OrdenNameUpdater(Articulo));
        return editText;
    }

    private View renderNombre(final ArticuloModel articulo) {
        return renderString(articulo.getarticulo_Nombre());
    }

    private View renderPrecio(final ArticuloModel articulo) {
        double Importe = Double.parseDouble(articulo.getarticulo_Precio());

        NumberFormat formatter = NumberFormat.getCurrencyInstance();

        final TextView textView = new TextView(getContext());
        textView.setText(formatter.format(Importe));
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);

        return textView;
    }

    private View renderExistencia(final ArticuloModel articulo) {
        return renderString(articulo. getArticulo_cantidad());
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
