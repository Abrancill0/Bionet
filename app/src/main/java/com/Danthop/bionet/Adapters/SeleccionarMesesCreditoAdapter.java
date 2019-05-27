package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.Danthop.bionet.Tables.SortableSeleccionarArticuloTable;
import com.Danthop.bionet.Tables.SortableSeleccionarPromocionTable;
import com.Danthop.bionet.model.ArticuloModel;
import com.Danthop.bionet.model.PromocionesModel;
import java.text.NumberFormat;
import java.util.List;
import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class SeleccionarMesesCreditoAdapter extends LongPressAwareTableDataAdapter<PromocionesModel> {

    int TEXT_SIZE = 14;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public SeleccionarMesesCreditoAdapter(final Context context, final List<PromocionesModel> data, final SortableSeleccionarPromocionTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        PromocionesModel Articulo = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderMes(Articulo);
                break;


        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final PromocionesModel Articulo = getRowData(rowIndex);
        View renderedView = null;

        return renderedView;
    }

   /* private View renderEditableClienteName(final PromocionesModel Articulo) {
        final TextView v = new TextView(getContext());
        v.setText(Articulo.getarticulo_Nombre());
        return v;
    }*/

    private View renderMes(final PromocionesModel articulo) {
        return renderString(articulo.getNomPromoCredito());
    }



    private View renderString(final String value) {
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private PromocionesModel ordenToUpdate;

        public OrdenNameUpdater(PromocionesModel ordenToUpdate) {
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
            //ordenToUpdate.getarticulo_Nombre();
        }
    }}
