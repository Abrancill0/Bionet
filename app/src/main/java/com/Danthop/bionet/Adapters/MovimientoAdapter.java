package com.Danthop.bionet.Adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.Danthop.bionet.Tables.SortableMovimientoTable;
import com.Danthop.bionet.Tables.SortableOrdenEspecialTable;
import com.Danthop.bionet.model.MovimientoModel;
import com.Danthop.bionet.model.OrdenEspecialModel;

import java.text.NumberFormat;
import java.util.List;

import de.codecrafters.tableview.toolkit.LongPressAwareTableDataAdapter;

public class MovimientoAdapter extends LongPressAwareTableDataAdapter<MovimientoModel> {

    int TEXT_SIZE = 16;
    private static final NumberFormat PRICE_FORMATTER = NumberFormat.getNumberInstance();


    public MovimientoAdapter(final Context context, final List<MovimientoModel> data, final SortableMovimientoTable tableView) {
        super(context, data, tableView);
    }

    @Override
    public View getDefaultCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        MovimientoModel movimiento = getRowData(rowIndex);
        View renderedView = null;

        switch (columnIndex) {
            case 0:
                renderedView = renderFecha(movimiento);
                break;
            case 1:
                renderedView = renderArticulo(movimiento);
                break;
            case 2:
                renderedView = renderSucursal(movimiento);
                break;
            case 3:
                renderedView = renderMonto(movimiento);
                break;
        }
        return renderedView;
    }

    @Override
    public View getLongPressCellView(int rowIndex, int columnIndex, ViewGroup parentView) {
        final MovimientoModel movimiento = getRowData(rowIndex);

        return renderString("");
    }


    private View renderFecha(final MovimientoModel movimiento) {
        return renderString(movimiento.getMovimiento_fecha());
    }

    private View renderArticulo(final MovimientoModel movimiento) {
        return renderString(movimiento.getMovimiento_articulo_ticket());
    }

    private View renderSucursal(final MovimientoModel movimiento) {
        return renderString(movimiento.getMovimiento_suc_nombre()+"("+movimiento.getMovimiento_suc_numero()+")");
    }

    private View renderMonto(final MovimientoModel movimiento) {
        double Monto = Double.parseDouble( movimiento.getMovimiento_monto() );
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        final TextView textView = new TextView( getContext() );
        textView.setText( formatter.format( Monto ) );
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

    private static class MovimientoNameUpdater implements TextWatcher {

        private OrdenEspecialModel movimientoToUpdate;

        public MovimientoNameUpdater(OrdenEspecialModel ordenToUpdate) {
            this.movimientoToUpdate = ordenToUpdate;
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
            movimientoToUpdate.getOrden_nombre_cliente();
        }
    }
}