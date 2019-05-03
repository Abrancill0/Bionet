package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableOrdenEspecialDetalleTable;
import com.Danthop.bionet.model.OrdenEspecialArticuloModel;
import com.Danthop.bionet.model.OrdenEspecialModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class DetalleOrdenEspecialAdapter extends LongPressAwareTableDataAdapter<OrdenEspecialArticuloModel> {

    int TEXT_SIZE = 12;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();

    public DetalleOrdenEspecialAdapter(final Context context, final List<OrdenEspecialArticuloModel> data, final SortableOrdenEspecialDetalleTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        OrdenEspecialArticuloModel orden = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderNombreArticulo(orden);
                break;
            case 1:
                renderedView = renderCantidad(orden);
                break;
            case 2:
                renderedView = renderImporteTotal(orden);
                break;
            case 3:
                renderedView = renderImporteRestante(orden);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final OrdenEspecialArticuloModel orden = getRowData(rowIndex);

        return renderString("");
    }

    private View renderNombreArticulo(final OrdenEspecialArticuloModel orden) {
        return renderString(orden.getNombre_articulo());
    }

    private View renderCantidad(final OrdenEspecialArticuloModel orden) {
        return renderString(orden.getCantidad());
    }

    private View renderImporteTotal(final OrdenEspecialArticuloModel orden) {
        double ImporteTotal = Double.parseDouble( orden.getImporte_total() );
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( ImporteTotal ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }

    private View renderImporteRestante(final OrdenEspecialArticuloModel orden) {
        double ImporteTotal = Double.parseDouble( orden.getImporte_restante());
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( ImporteTotal ) );
        textView.setPadding( 20, 10, 20, 10 );
        textView.setTextSize( TEXT_SIZE );

        return textView;
    }


    private View renderString ( final String value){
        final TextView textView = new TextView(getContext());
        textView.setText(value);
        textView.setPadding(20, 10, 20, 10);
        textView.setTextSize(TEXT_SIZE);
        return textView;
    }

    private static class OrdenNameUpdater implements TextWatcher {

        private OrdenEspecialArticuloModel ordenToUpdate;

        public OrdenNameUpdater(OrdenEspecialArticuloModel ordenToUpdate) {
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